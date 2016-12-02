/**********************************************************************
 * file: Chunks.java
 * author: Kyle Turchik, Vu Dao, Marco Roman
 * class: CS 445 - Computer Graphics
 *
 * assignment: Quarter Project CP#3
 * date last modified: 11/29/2016
 *
 * purpose: This class randomly generates terrain and applies textures 
 *          to the various blocks.
 *
 **********************************************************************/

import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Chunk {

    static final int CHUNK_SIZE = 30;
    static final int CUBE_LENGTH = 2;
    static final float persistanceMin = 0.03f;
    static final float persistanceMax = 0.06f;
    
    private Block[][][] Blocks;
    private int VBOVertexHandle;
    private int VBOColorHandle;
    private int VBOTextureHandle;
    private int StartX, StartY, StartZ;
    private Random r;
    private Texture texture;
    private String[] texFile = {"terrain.png","terrain_MortalCombat.png",
        "terrain_Alien.png","terrain_Mayan.png"};

    public void render() {
        glPushMatrix();
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glVertexPointer(3, GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glColorPointer(3, GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBindTexture(GL_TEXTURE_2D, 1);
        glTexCoordPointer(2, GL_FLOAT, 0, 0L);
        glDrawArrays(GL_QUADS, 0, CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE*24);
        glPopMatrix();

    }
    public void deleteTextures(int tex) {;
        glDeleteTextures(1);
        try {
            texture = TextureLoader.getTexture("PNG",
                ResourceLoader.getResourceAsStream(texFile[tex]));
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void rebuildMesh(float startX, float startY, float startZ) {
        //Generate random seed
        Random random = new Random();
        
        float persistance = 0;
        while (persistance < persistanceMin) {
            persistance = (persistanceMax)*random.nextFloat();
        }
        System.out.println(persistance);
        int seed = (int)(50 * random.nextFloat());
        System.out.println("Seed:" + seed);
        
        SimplexNoise noise = new SimplexNoise(CHUNK_SIZE, persistance, seed);
               
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers(); // placed among the other VBOs
        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer(
            (CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer(
            (CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexTextureData = BufferUtils.createFloatBuffer(
            (CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        
        //int count = 0;
        float height = 0;
        for (float x = 0; x < CHUNK_SIZE; x++) {
            for (float z = 0; z < CHUNK_SIZE; z++) {
                int i = (int) (startX + x * ((300 - startX) / 640));
                int j = (int) (startZ + z * ((300 - startZ) / 480));
                //generate height                
                height = 1+Math.abs(
                    (startY + (int) (100 * noise.getNoise(i, j))* CUBE_LENGTH));
                
                for (float y = 0; y <= height; y++) {
                    persistance = 0;
                    while (persistance < persistanceMin) {
                        persistance = (persistanceMax) * random.nextFloat();
                    }
                    
                    VertexPositionData.put(
                        createCube(
                            (float) (startX + x * CUBE_LENGTH),
                            (float) (y * CUBE_LENGTH + (int) (CHUNK_SIZE * -1.0)),
                            (float) (startZ + z * CUBE_LENGTH) + (int) (CHUNK_SIZE * 1.5)));
                    VertexColorData.put(
                        createCubeVertexCol(
                            getCubeColor(
                                Blocks[(int) x][(int) y][(int) z])));
                    if (y == height) {
                        float rand = r.nextFloat();
                        if (rand < 0.33f) {
                            VertexTextureData.put(createTexCube(
                                (float) 0, (float) 0, new Block(
                                    Block.BlockType.BlockType_Grass)));
                        } else if (rand > 0.66f) {
                            VertexTextureData.put(createTexCube(
                                (float) 0, (float) 0, new Block(
                                    Block.BlockType.BlockType_Sand)));
                        } else {
                            VertexTextureData.put(createTexCube(
                                (float) 0, (float) 0, new Block(
                                    Block.BlockType.BlockType_Water)));
                        }

                    } else if (y == 0) {
                        VertexTextureData.put(createTexCube(
                                (float) 0, (float) 0, new Block(
                                    Block.BlockType.BlockType_Bedrock)));
                    } else {
                        float rand = r.nextFloat();
                        if (rand < .50) {
                            VertexTextureData.put(createTexCube(
                                (float) 0, (float) 0, new Block(
                                    Block.BlockType.BlockType_Stone)));
                        } else {
                            VertexTextureData.put(createTexCube(
                                (float) 0, (float) 0, new Block(
                                    Block.BlockType.BlockType_Dirt)));
                        }

                    }
                }
            }
        }
        VertexTextureData.flip();
        VertexColorData.flip();
        VertexPositionData.flip();
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexPositionData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexColorData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexTextureData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private float[] createCubeVertexCol(float[] CubeColorArray) {
        float[] cubeColors = new float[CubeColorArray.length * 4 * 6];
        for (int i = 0; i < cubeColors.length; i++) {
            cubeColors[i] = CubeColorArray[i % CubeColorArray.length];
        }
        return cubeColors;
    }
 
    public static float[] createCube(float x, float y, float z) {
        int offset = CUBE_LENGTH / 2;
        return new float[]{
            // TOP QUAD
            x + offset, y + offset, z,
            x - offset, y + offset, z,
            x - offset, y + offset, z - CUBE_LENGTH,
            x + offset, y + offset, z - CUBE_LENGTH,
            // BOTTOM QUAD
            x + offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z,
            x + offset, y - offset, z,
            // FRONT QUAD
            x + offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            // BACK QUAD
            x + offset, y - offset, z,
            x - offset, y - offset, z,
            x - offset, y + offset, z,
            x + offset, y + offset, z,
            // LEFT QUAD
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z,
            x - offset, y - offset, z,
            x - offset, y - offset, z - CUBE_LENGTH,
            // RIGHT QUAD
            x + offset, y + offset, z,
            x + offset, y + offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z};

    }

    private float[] getCubeColor(Block block) {
        return new float[]{1, 1, 1};

    }

    public Chunk(int startX, int startY, int startZ, int tex) {
        try {
            texture = TextureLoader.getTexture("PNG",
                ResourceLoader.getResourceAsStream(texFile[tex]));
        } catch (Exception e) {
            System.out.print(e);
        }
        r = new Random();
        Blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    if (r.nextFloat() > 0.830f) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Grass);
                    } else if (r.nextFloat() > 0.664f) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Sand);
                    } else if (r.nextFloat() > 0.498f) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Water);
                    } else if (r.nextFloat() > 0.332f) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
                    } else if (r.nextFloat() > 0.166f) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Stone);
                    } else {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Bedrock);  // WHAT TO DO WITH THIS????!!!!
                    }
                }
            }
        }
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers(); //along with our other VBOs
        StartX = startX;
        StartY = startY;
        StartZ = startZ;
        rebuildMesh(startX, startY, startZ);
    }
    
    //Assign cube textures
    public static float[] createTexCube(float x, float y, Block block) {
        float offset = (1024f/16)/1024f;
        switch (block.getID()) {
            case 0: //Grass block type
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset*3, y + offset*10,
                    x + offset*2, y + offset*10,
                    x + offset*2, y + offset*9,
                    x + offset*3, y + offset*9,
                    // TOP!
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                    // FRONT QUAD
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,
                    // BACK QUAD
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    // LEFT QUAD
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,
                    // RIGHT QUAD
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1};
            case 1: //sand block type
                return new float[] {
                     // BOTTOM QUAD(DOWN=+Y)
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1,
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                    // TOP!
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1,
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                    // FRONT QUAD
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1,
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                    // BACK QUAD
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1,
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                    // LEFT QUAD
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1,
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                    // RIGHT QUAD
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1,
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2};
            case 2: //water block type
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset*2, y + offset*11,
                    x + offset*3, y + offset*11,
                    x + offset*3, y + offset*12,
                    x + offset*2, y + offset*12,
                    // TOP!
                    x + offset*2, y + offset*11,
                    x + offset*3, y + offset*11,
                    x + offset*3, y + offset*12,
                    x + offset*2, y + offset*12,
                    // FRONT QUAD
                    x + offset*2, y + offset*11,
                    x + offset*3, y + offset*11,
                    x + offset*3, y + offset*12,
                    x + offset*2, y + offset*12,
                    // BACK QUAD
                    x + offset*2, y + offset*11,
                    x + offset*3, y + offset*11,
                    x + offset*3, y + offset*12,
                    x + offset*2, y + offset*12,
                    // LEFT QUAD
                    x + offset*2, y + offset*11,
                    x + offset*3, y + offset*11,
                    x + offset*3, y + offset*12,
                    x + offset*2, y + offset*12,
                    // RIGHT QUAD
                    x + offset*2, y + offset*11,
                    x + offset*3, y + offset*11,
                    x + offset*3, y + offset*12,
                    x + offset*2, y + offset*12};
            case 3: //dirt block type
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset*1, y + offset*10,
                    x + offset*2, y + offset*10,
                    x + offset*2, y + offset*11,
                    x + offset*1, y + offset*11,
                    // TOP!
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                    // FRONT QUAD
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                    // BACK QUAD
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                    // LEFT QUAD
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                    // RIGHT QUAD
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0};
            case 4: //stone block type
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset*5, y + offset*6,
                    x + offset*6, y + offset*6,
                    x + offset*6, y + offset*7,
                    x + offset*5, y + offset*7,
                    // TOP!
                    x + offset*5, y + offset*6,
                    x + offset*6, y + offset*6,
                    x + offset*6, y + offset*7,
                    x + offset*5, y + offset*7,
                    // FRONT QUAD
                    x + offset*5, y + offset*6,
                    x + offset*6, y + offset*6,
                    x + offset*6, y + offset*7,
                    x + offset*5, y + offset*7,
                    // BACK QUAD
                    x + offset*5, y + offset*6,
                    x + offset*6, y + offset*6,
                    x + offset*6, y + offset*7,
                    x + offset*5, y + offset*7,
                    // LEFT QUAD
                    x + offset*5, y + offset*6,
                    x + offset*6, y + offset*6,
                    x + offset*6, y + offset*7,
                    x + offset*5, y + offset*7,
                    // RIGHT QUAD
                    x + offset*5, y + offset*6,
                    x + offset*6, y + offset*6,
                    x + offset*6, y + offset*7,
                    x + offset*5, y + offset*7};
            default: //bedrock block type
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                    // TOP!
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                    // FRONT QUAD
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                    // BACK QUAD
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                    // LEFT QUAD
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                    // RIGHT QUAD
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2};
        }
    }
}
