//
//	Name: Turchik, Kyle
//	Homework: 1
//	Due: 1/27/2015
//	Course: cs-240-02-w15
//
//	Description:
//	Add two methods to the Matrix class.  One which performs Matrix multiplication 
//	and another which performs Matrix addition.  Also, provide a class main to 
//	verify the two methods work as intended.
//

public class Matrix {

	public static void main(String[] args) {
		int[][] matrixB;
		int[][] matrixC;
		int scalar;
		
		//Test Case 1 to verify 1, 2, and 3
		System.out.println("Test Case 1: Given");
		
		matrixB = new int[][] { { 1, 3, 5, 7 }, { 2, 4, 6, 8 } };
		printMatrix("Matrix B:", matrixB);
		matrixC = new int[][] { { 1, 8, 9 }, { 2, 7, 10 }, { 3, 6, 11 }, { 4, 5, 12 } };
		printMatrix("Matrix C:", matrixC);
		scalar = 1 + (int)(Math.random()*9);
		System.out.println("Scalar c = " + scalar);
		
		System.out.println("");
		printMatrix("A = B * C:", multiplication(matrixB, matrixC));
		printMatrix("A = c * B:", multiplication(scalar, matrixB));
		printMatrix("A = B + C:", addition(matrixB, matrixC));
		
		//Test Case 2 to verify 1, 2, and 3
		System.out.println("");
		System.out.println("--------------------");
		System.out.println("Test Case 2:");
		
		matrixB = new int[][] { { 7, 3 }, { 2, 5 }, { 6, 8 }, { 9, 0 } };
		printMatrix("Matrix B:", matrixB);
		matrixC = new int[][] { { 7, 4, 9 }, { 8, 1, 5 } };
		printMatrix("Matrix C:", matrixC);
		scalar = 1 + (int)(Math.random()*9);
		System.out.println("Scalar c = " + scalar);

		System.out.println("");
		printMatrix("A = B * C:", multiplication(matrixB, matrixC));
		printMatrix("A = c * B:", multiplication(scalar, matrixB));
		printMatrix("A = B + C:", addition(matrixB, matrixC));
		
		//Test Case 3 to verify 1, 2, and 3
		System.out.println("");
		System.out.println("--------------------");
		System.out.println("Test Case 3:");
		
		matrixB = new int[][] { { 2, 1}, { 3, 2 }, { -2, 2} };
		printMatrix("Matrix B:", matrixB);
		matrixC = new int[][] { { 1, 1}, { 4, 2 }, { -2, 1} };
		printMatrix("Matrix C:", matrixC);
		scalar = 1 + (int)(Math.random()*9);
		System.out.println("Scalar c = " + scalar);

		System.out.println("");
		printMatrix("A = B * C:", multiplication(matrixB, matrixC));
		printMatrix("A = c * B:", multiplication(scalar, matrixB));
		printMatrix("A = B + C:", addition(matrixB, matrixC));
		
		//Original method main
		System.out.println("");
		System.out.println("--------------------");
		System.out.println("Original method main:");
		
		int[][] matrixa = new int[][]{ { 1 , 2, 3 }, { 4, 5, 6 } };
		printMatrix("matrix A:", matrixa);
		int[][] matrixb = transpose(matrixa);
		printMatrix("transpose A:", matrixb);
        
		int[][] ident = identity(5);
		printMatrix("identity:", ident);       
	}
    
    /**
     * matrix multiplication
     * <p>
     * @param B		1st factor consisting of a 2-dimensional matrix
     * @param C		2nd factor consisting of a 2-dimensional matrix
     * @return A	new matrix resulting from the product of B * C
     */
	public static int[][] multiplication(int[][] B, int[][] C) {
		try {
			if (B[0].length != C.length) {
				throw new IllegalArgumentException();
			}
		}
		catch (IllegalArgumentException illArgExc){
			System.out.println("Error IllegalArgumentException:");
			System.out.println("Number of columns in Matrix B does not equal number of rows in Matrix C");
			int[][] A = new int[0][0];
			
			return A;
		}
		
		int[][] A = new int[B.length][C[0].length];

		for (int i = 0; i < B.length; i++) {			//i = Row of Matrix B / Row of Matrix A
			for (int j = 0; j < C[0].length; j++) {		//j = Column of Matrix C / Column of Matrix A
				for (int k = 0; k < B[0].length; k++) {	//k = Column of Matrix B / Row of Matrix C
					A[i][j] += B[i][k] * C[k][j];
				}
			}
		}
		return A;
	}
    
    /**
     * matrix multiplication
     * <p>
     * @param c		1st factor consisting of a scalar integer
     * @param B		2nd factor consisting of a 2-dimensional matrix
     * @return A	new matrix resulting from the product of c * B
     */
	public static int[][] multiplication(int c, int[][] B) {
		int[][] A = new int[B.length][B[0].length];

		for (int i = 0; i < B.length; i++) {
			for (int j = 0; j < B[0].length; j++) {
				A[i][j] = c * B[i][j];
			}
		}
		return A;
	}
    
    /**
     * matrix addition
     * <p>
     * @param B		1st addend consisting of a 2-dimensional matrix
     * @param C		2nd addend consisting of a 2-dimensional matrix
     * @return A	new matrix resulting from the sum of B + C
     */
	public static int[][] addition(int[][] B, int[][] C) {
		try {
			if ((B.length != C.length) || (B[0].length != C[0].length)) {
				throw new IllegalArgumentException();
			}
		}
		catch (IllegalArgumentException illArgExc){
			System.out.println("Error IllegalArgumentException:");
			System.out.println("Matrix B and Matrix C do not have the same dimensions");
			int[][] A = new int[0][0];
			
			return A;
		}

		int[][] A = new int[B.length][B[0].length];

		for (int i = 0; i < B.length; i++) {
			for (int j = 0; j < B[0].length; j++) {
				A[i][j] = B[i][j] + C[i][j];
			}
		}
		return A;
	}
    
    /**
     * matrix transpose
     * <p>
     * @param matrix 	matrix to be transposed
     * @return 			the transpose of matrix
     */
    public static int[][] transpose (int[][] matrix) {
        int[][] transposeMatrix = new int[matrix[0].length][matrix.length];

        for (int i = 0; i < transposeMatrix.length; i++) {
            for (int j = 0; j < transposeMatrix[i].length; j++) {
                transposeMatrix[i][j] = matrix[j][i];
            }
        }

        return transposeMatrix;
    }
    
    /**
     * matrix identity
     * <p>
     * @param n		size of the identity matrix
     * @return 		the identity matrix of size n
     */
    public static int[][] identity (int n) {
        int[][] ident = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ident[i][i] = 1;
            }
        }
        
        return ident;
    }
    
    /**
     * print matrix
     * <p>
     * @param matrix 	matrix to be printed
     */
    public static void printMatrix (String tag, int[][] matrix) {
        System.out.println(tag);
        for (int[] row : matrix) {
            for (int element : row) {
                System.out.printf("%4d ", element);
            }
            System.out.println("");
        }
    }
}