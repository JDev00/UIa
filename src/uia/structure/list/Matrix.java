package uia.structure.list;

import java.util.Objects;

public class Matrix {
    private double[][] array;

    // m -> rows
    // n -> cols
    public Matrix(int m, int n) {
        array = new double[m][n];
    }

    /**
     * Fills a matrix cell
     *
     * @param i     the row
     * @param j     the col
     * @param value the value to insert
     */

    public void put(int i, int j, double value) {
        array[i][j] = value;
    }

    /**
     * Fills a matrix row
     *
     * @param i      the row
     * @param vector the vector to insert
     */

    public void putRow(int i, double[] vector) {
        if (vector != null && vector.length == array[0].length) {

            for (int j = 0; j < vector.length; j++) {
                array[i][j] = vector[j];
            }
        }
    }

    /**
     * Fills a matrix col
     *
     * @param j      the col
     * @param vector the vector to insert
     */

    public void putCol(int j, double[] vector) {
        if (vector != null && vector.length == array.length) {

            for (int i = 0; i < array.length; i++) {
                array[i][j] = vector[i];
            }
        }
    }

    /**
     * Sums the given matrix to this one
     *
     * @param matrix the given matrix
     * @throws NullPointerException if {@code matrix == null}
     */

    public void sum(double[][] matrix) {
        Objects.requireNonNull(matrix);

        //Validates the given matrix
        if (matrix.length != array.length || matrix[0].length != array[0].length) return;

        for (int i = 0; i < array.length; i++) {

            for (int j = 0; j < array[0].length; j++) {
                array[i][j] += matrix[i][j];
            }
        }
    }

    /**
     * Sums the given matrix to this one
     *
     * @param matrix the given matrix
     * @throws NullPointerException if {@code matrix == null}
     */

    public void sum(Matrix matrix) {
        sum(Objects.requireNonNull(matrix).array);
    }

    /**
     * Multiplies the given scalar with this matrix
     *
     * @param scalar the given number
     * @throws NullPointerException if {@code matrix == null}
     */

    public void multiply(double scalar) {
        for (int i = 0; i < array.length; i++) {

            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = scalar * array[i][j];
            }
        }
    }

    /**
     * Multiplies the given matrix with this one
     *
     * @param matrix the given matrix
     * @throws NullPointerException if {@code matrix == null}
     */

    public void multiply(double[][] matrix) {
        Objects.requireNonNull(matrix);

        if (getCols() != matrix.length) return;

        double[][] newMatrix = new double[getRows()][matrix[0].length];


        for (int k = 0; k < matrix[0].length; k++) {

            for (int i = 0; i < array.length; i++) {

                for (int j = 0; j < array[0].length; j++) {
                    newMatrix[i][k] += array[i][j] * matrix[j][k];
                }
            }
        }

        array = null;
        array = newMatrix;
    }

    /**
     * Multiplies the given matrix with this one
     *
     * @param matrix the given matrix
     * @throws NullPointerException if {@code matrix == null}
     */

    public void multiply(Matrix matrix) {
        multiply(Objects.requireNonNull(matrix).array);
    }

    /**
     * Transpose this matrix
     */

    public void transpose() {
        double[][] newMatrix = new double[getCols()][getRows()];

        for (int i = 0; i < array.length; i++) {

            for (int j = 0; j < array[0].length; j++) {
                newMatrix[j][i] = array[i][j];
            }
        }

        array = null;
        array = newMatrix;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getRows() * getCols() + (getCols() - 1) + (getRows() - 1) + 100);


        for (int i = 0; i < array.length; i++) {

            for (int j = 0; j < array[0].length; j++) {
                builder.append(array[i][j]).append("  ");
            }

            builder.append("\n");
        }

        return builder.toString();
    }

    /**
     * @return the rows of this matrix
     */

    public final int getRows() {
        return array.length;
    }

    /**
     * @return the cols of this matrix
     */

    public final int getCols() {
        return array[0].length;
    }

    /*
     * @return if any, the determinant of this matrix
     *

    public final double getDeterminant() {
        if (getRows() == getCols()) {
            int row = 0;
            int zeros;
            int maxZeros = 0;

            // Checks for zeros
            for (int i = 0; i < array.length; i++) {

                zeros = 0;
                for (int j = 0; j < array[0].length; j++) {

                    if (array[i][j] == 0) zeros++;
                }

                // Updates the rows with more zeros
                if (zeros > maxZeros) {
                    row = i;
                    maxZeros = zeros;
                }
            }

            //Calculates the determinant
            for (int j = 0; j < array[0].length; j++) {

            }
        }

        return 0.0d;
    }

    // @Test
    private double getDeterminant(int i, int j) {
        if (getRows() - i == 2 && getCols() - j == 2) {
            return array[i][j] * array[i + 1][j + 1] - (array[i + 1][j] * array[i][j + 1]);
        } else {
            if (array[i][j] == 0) {
                return 0.0d;
            } else {
                return array[i][j] * getDeterminant(i + 1, j + 1);
            }
        }
    }*/

    /**
     * Returns an array filled, in order, with all the matrix's rows
     *
     * @param r the array in which store the matrix
     * @return this matrix as an array
     */

    /*
     * 2 2 2
     * 3 3 3
     * */
    public final double[] toRowsArray(double[] r) {
        int length = getRows() * getCols();

        if (r == null)
            r = new double[length];

        if (r.length < length)
            r = new double[length];

        int index = 0;
        for (double[] rows : array) {

            for (int j = 0; j < array[0].length; j++) {
                r[index++] = rows[j];
            }
        }

        return r;
    }

    /**
     * Returns an array filled, in order, with all the matrix's cols
     *
     * @param r the array in which store the matrix
     * @return this matrix as an array
     */

    public final double[] toColsArray(double[] r) {
        int length = getRows() * getCols();

        if (r == null)
            r = new double[length];

        if (r.length < length)
            r = new double[length];

        int index = 0;
        for (int j = 0; j < array[0].length; j++) {

            for (double[] rows : array) {
                r[index++] = rows[j];
            }
        }

        return r;
    }

    /**
     * Creates a new matrix
     *
     * @param matrix the matrix used to create the new one
     * @return a new matrix if the given one was a correct matrix otherwise null
     */

    public static Matrix create(double[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0] == null || matrix[0].length == 0) return null;


        //Validates the matrix
        int lastRow = matrix[0].length;
        for (int i = 1; i < matrix.length; i++) {

            if (matrix[i] == null || lastRow != matrix[i].length) {
                return null;
            }

            lastRow = matrix[i].length;
        }


        Matrix m = new Matrix(matrix.length, matrix[0].length);

        for (int i = 0; i < matrix.length; i++) {

            for (int j = 0; j < matrix[0].length; j++) {
                m.array[i][j] = matrix[i][j];
            }
        }

        return m;
    }

    /*
     *
     */

    /*public static void main(String[] args) {
        Matrix matrix = create(new double[][]{
                {1, 2},
                {8, 0},
                {0, 1}
        });

        System.out.println(matrix);
    }*/
}
