package Client;

import Server.Task;
import Server.TaskResult;

public class MatrixMultiplier implements Task {
    private int id;

    private int[][] matrix1;
    private int[][] matrix2;

    public MatrixMultiplier(int id,int[][] matrix1, int[][] matrix2) {
        this.id=id;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
    }


    @Override
    public TaskResult execute() {
        int m1Rows = matrix1.length;
        int m1Cols = matrix1[0].length;
        int m2Rows = matrix2.length;
        int m2Cols = matrix2[0].length;

        if (m1Cols != m2Rows) {

            throw new IllegalArgumentException("Matrices cannot be multiplied");
        }

        int[][] result = new int[m1Rows][m2Cols];

        for (int i = 0; i < m1Rows; i++) {
            for (int j = 0; j < m2Cols; j++) {
                for (int k = 0; k < m1Cols; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }

        return new TaskResult(id,result);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void run() {

    }
}
