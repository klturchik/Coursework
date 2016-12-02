import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.interfaces.*;
import java.security.spec.*;
import java.util.zip.CRC32;
import java.util.Scanner;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.xml.bind.DatatypeConverter;

public class FileTransfer {

    public static void main(String[] args) throws Exception {
        switch (args.length) {
        //no additional arguments
            case 1:
                makeKeys();
                break;
       //filename of private key, port number for server to listen
            case 3:
                server(args[1], args[2]);
                break;
       //filename of public key, host to connect to, port number for server to listen
            case 4:
                client(args[1], args[2], args[3]);
                break;
            default:
                break;
        }
    }

    public static void makeKeys() {
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(2048);
            KeyPair keyPair = gen.generateKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(new File("public.bin")))) {
                oos.writeObject(publicKey);
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(new File("private.bin")))) {
                oos.writeObject(privateKey);
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace(System.err);
        }

    }

    public static void server(String pk, String port) throws IOException, NoSuchAlgorithmException {
        (new Thread() {
            @Override
            public void run() {
                int portNum = DatatypeConverter.parseInt(port);
                ServerSocket socket = null;
                
                //TODO: BUG, not receiving ACK somewhere around this point
                
                while (true) {
                    try {
                        socket = new ServerSocket(portNum);
                        Socket clientSocket = socket.accept();
                        String address = clientSocket.getInetAddress().getHostAddress();
                        System.out.printf("Client connected: %s%n", address);
                        ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());

                        AES aes = new AES();
                        byte[] encryptedContent = null;
                        byte[] secretKey = null;
                        Message input = (Message) ois.readObject();
                        int seq = 0;
                        if (input instanceof StartMessage) {
                            StartMessage sm = (StartMessage) input;
                            FileInputStream fis = new FileInputStream(pk);
                            ObjectInputStream ofis = new ObjectInputStream(fis);
                            
                            RSAPublicKey publicKey = (RSAPublicKey) ofis.readObject();
                            String privKey = "private.bin";
                            fis = new FileInputStream(privKey);
                            ofis = new ObjectInputStream(fis);
                            RSAPrivateKey privateKey = (RSAPrivateKey) ofis.readObject();
                            ofis.close();
                            
                            String mod = publicKey.getModulus().toString(16);
                            String exp = privateKey.getPrivateExponent().toString(16);

                            Decrypter decryptor = new Decrypter(mod, exp);

                            String decoded = new String(sm.getEncryptedKey(), "UTF-8");
                            String secretHexKey = decryptor.decryptString(decoded);
                            secretKey = DatatypeConverter.parseHexBinary(secretHexKey);
                            encryptedContent = sm.getFile().getBytes();
                            aes.setSecretKey(secretKey);

                            oos.writeObject(new AckMessage(seq));
                        } else if (input instanceof Chunk) {
                            Chunk c = (Chunk) input;
                            if (c.getSeq() == seq) {
                                seq++;
                                byte[] originalContent = aes.decrypt(encryptedContent);
                                System.out.println("Original content: " + new String(originalContent));
                                
                                CRC32 crc = new CRC32();
                                crc.update(originalContent);
                                int crcValue = (int) crc.getValue();
                                if (crcValue == c.getCrc()) {
                                    oos.writeObject(new AckMessage(seq++));
                                }
                            }
                        }

                        //oos.close();
                        //ois.close();
                    } catch (IOException | ClassNotFoundException |
                            NoSuchAlgorithmException | InvalidKeySpecException |
                            NoSuchPaddingException | InvalidKeyException |
                            IllegalBlockSizeException | BadPaddingException e) {
                    }
                }
            }
        }).start();
    }

    public static void client(String pk, String host, String port) throws NoSuchAlgorithmException, IOException {
        (new Thread() {
            @Override
            public void run() {
                int portNum = DatatypeConverter.parseInt(port);
                while (true) {
                    try {
                        Socket socket = new Socket(host, portNum);
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        Scanner in = new Scanner(System.in);
                        FileInputStream fileInStream = new FileInputStream(pk);
                        ObjectInputStream ofis = new ObjectInputStream(fileInStream);
                        
                        RSAPublicKey publicKey = (RSAPublicKey) ofis.readObject();
                        AES aes = new AES();
                        String symKey = aes.getSecretKey();
                        Encrypter encrypter = new Encrypter(publicKey);
                        String encryptedSymKey = encrypter.encryptString(symKey);
                        
                        System.out.println("Connected to server.");
                        System.out.println("Enter path: ");
                        String path = in.nextLine();
                        File pathFile = new File(path);
                        
                        int chunkSize = 0;
                        if (pathFile.isFile() == true) {
                            System.out.println("Enter chunk size [1024] : ");
                            chunkSize = in.nextInt();
                        }
                        
                        String content = new Scanner(pathFile).useDelimiter("\\Z").next();
                        oos.writeObject(new StartMessage(content, encryptedSymKey.getBytes(), chunkSize));
                        int split = (int) (pathFile.length() / chunkSize);
                        System.out.println("Sending: " + path + ".  File size: " + pathFile.length());
                        System.out.println("Sending " + split + " chunks.");
                        AckMessage ack = (AckMessage) ois.readObject();
                        byte[] store = content.getBytes();

                        for (int x = 0; x < split; x++) {
                            int start = 0;
                            byte[] chunk = new byte[chunkSize];
                            System.arraycopy(store, start, chunk, start, chunkSize - start);
                            CRC32 crc = new CRC32();
                            crc.update(chunk);
                            String strChunk = new String(chunk, "UTF-8");
                            byte[] encryptedChunk = aes.encrypt(strChunk);
                            int seq = ack.getSeq();
                            oos.writeObject(new Chunk(seq, encryptedChunk, (int) crc.getValue()));
                            System.out.println();
                            ack = (AckMessage) ois.readObject();
                            
                            if (ack.getSeq() == seq++) {
                                System.out.println("Chunks completed [" + seq + "/" + split + "]");
                                start += chunkSize;
                                seq++;
                                chunkSize += chunkSize;
                                break;
                            }
                        }
                    } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {

                    }
                }
            }
        }).start();
    }
}



class Decrypter {
    private RSAPrivateKey privKey;

    public Decrypter(RSAPrivateKey key) {
        this.privKey = key;
    }

    public Decrypter(String modulus, String exponent) throws
            NoSuchAlgorithmException, InvalidKeySpecException {

        BigInteger mod = new BigInteger(modulus, 16);
        BigInteger exp = new BigInteger(exponent, 16);
        RSAPrivateKey rsaPrivKey;

        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(mod, exp);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        rsaPrivKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        this.privKey = rsaPrivKey;
    }

    public String decryptString(String encryptedText) throws
            NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        BigInteger encryptedBI = new BigInteger(encryptedText, 16);
        byte[] encryptedBytes = encryptedBI.toByteArray();
        Cipher server = Cipher.getInstance("RSA");
        server.init(Cipher.DECRYPT_MODE, this.privKey);
        byte[] clearBytes = server.doFinal(encryptedBytes);
        String clearText = new String(clearBytes);

        return clearText;
    }
}



class Encrypter {
    private final RSAPublicKey pubKey;
    private byte[] encryptedBytes;

    public Encrypter(RSAPublicKey key) {
        this.pubKey = key;
    }

    public Encrypter(String modulus) throws NoSuchAlgorithmException,
            InvalidKeySpecException {

        BigInteger mod = new BigInteger(modulus, 16);
        BigInteger exp = new BigInteger("10001", 16);
        RSAPublicKey rsaPubKey;

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(mod, exp);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        rsaPubKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);

        this.pubKey = rsaPubKey;
    }

    public String encryptString(String target) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        String encryptedStr;
        Cipher client = Cipher.getInstance("RSA");
        client.init(Cipher.ENCRYPT_MODE, this.pubKey);
        this.encryptedBytes = client.doFinal(target.getBytes());
        BigInteger encryptedBI = new BigInteger(this.encryptedBytes);
        encryptedStr = encryptedBI.toString(16);

        return encryptedStr;
    }
}



class AES {
    private byte[] secretKey;
    public byte[] generateSecretKey() throws NoSuchAlgorithmException {

        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        SecretKey skey = kgen.generateKey();
        this.secretKey = skey.getEncoded();

        return this.secretKey;
    }

    public byte[] encrypt(String target) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        SecretKeySpec spec = new SecretKeySpec(this.secretKey, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, spec);

        return cipher.doFinal(target.getBytes());
    }

    public byte[] decrypt(byte[] encrypted) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        SecretKeySpec spec = new SecretKeySpec(this.secretKey, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, spec);

        return cipher.doFinal(encrypted);
    }

    public String getSecretKey() {
        StringBuilder str = new StringBuilder(this.secretKey.length * 2);
        for (byte i : this.secretKey) {
            str.append(String.format("%02X", i));
        }

        return str.toString();
    }

    public void setSecretKey(byte[] secretKey) {
        this.secretKey = secretKey;
    }
}


/*
DEV ROADMAP:
-Client Mode
In client mode, the program should perform the following actions:

1. Generate an AES session key.																				// Working: AES.generateSecretKey() 
2. Serialize the session key and store it in a byte array.													// Easy, implement Serializable
3. Encrypt the serialized session key using the server's public key.										// Working: RSAEncryptor encryptor.encryptString(secretKey);
4. Prompt the user to enter the path for a file to transfer.												// User input / interface
5. If the path is valid, ask the user to enter the desired chunk size in bytes (default of 1024 bytes).		// Chunk class
6. After accepting the path and chunk size, send the server a StartMessage that contains the file name,	
length of the file in bytes, chunk size, and encrypted session key.
The server should respond with an AckMessage with sequence number 0 if the transfer can proceed,
otherwise the sequence number will be -1.
7. The client should then send each chunk of the file in order.  After each chunk, wait for the server to
respond with the appropriate AckMessage.  The sequence number in the ACK should be the number
for the next expected chunk.
For each chunk, the client must first read the data from the file and store in an array based on the		// Need to look up CRC32
chunk size.  It should then calculate the CRC32 value for the chunk.  Finally, encrypt the chunk data
using the session key.  Note that the CRC32 value is for the plaintext of the chunk, not the ciphertext.
8. After sending all chunks and receiving the final ACK, the transfer has completed and the client can
either begin a new file transfer or disconnect.
*/

/*
// ENCRYPTION CODE HOW TO USE:
// Server code:
  int keySize = 1024;
  KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
  keyGen.initialize(keySize);
  KeyPair pair = keyGen.generateKeyPair();
  RSAPublicKey publicKey = (RSAPublicKey) pair.getPublic();
  RSAPrivateKey privateKey = (RSAPrivateKey) pair.getPrivate();
  String modulus = publicKey.getModulus().toString(16);
  String exponent = privateKey.getPrivateExponent().toString(16);
  */

  /*
// Pass modulus (server public key) to client, Client code:
  String sample = "Encryption Test";	// Replace with chunks
  AES aes = new AES();
  byte[] secretKey = aes.generateSecretKey();
  String hexSecretKey = aes.getSecretKey();
  String encryptedHexContent = DatatypeConverter.printHexBinary(aes.encrypt(sample));
  System.out.println("Encrypted content in hex: " + encryptedHexContent);
  RSAEncryptor encryptor = new RSAEncryptor(modulus);
  String encryptedHexSecretKey = encryptor.encryptString(hexSecretKey);
  */

  /*
// Server again:
  RSADecryptor decryptor = new RSADecryptor(modulus, exponent);
  hexSecretKey = decryptor.decryptText(encryptedHexSecretKey, "RSA");
  secretKey = DatatypeConverter.parseHexBinary(hexSecretKey);
  byte[] encryptedContent = DatatypeConverter.parseHexBinary(encryptedHexContent);
  aes.setSecretKey(secretKey);
  byte[] originalContent = aes.decrypt(encryptedContent);
  System.out.println("Original content: " + new String(originalContent));
  */
