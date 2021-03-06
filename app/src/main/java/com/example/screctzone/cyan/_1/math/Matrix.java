package com.example.screctzone.cyan._1.math;


import com.example.screctzone.matrix.MatrixException;

import java.text.DecimalFormat;
import java.util.Locale;

public class Matrix implements Cloneable {
    public static final DecimalFormat formatter = new DecimalFormat();

    private double[][] member;

    /**
     * @param data 行列都必须大于0, 且{@param data}的列长保持一致;
     */
    public Matrix(double[][] data) {
        try {
            int row, column;

            row = data.length;
            column = data[0].length;

            member = new double[row][column];
            for (int i = 0; i < row; i++) {
                System.arraycopy(data[i], 0, member[i], 0, column);
            }

        } catch (Exception e) {
            try {
                throw new MatrixException();
            } catch (MatrixException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Matrix clone(){
        int row = row(), column = column();
        double[][] data = member;
        double[][] mem = new double[row][column];

        for (int i = 0; i < row; i++) {
            if (column >= 0) System.arraycopy(data[i], 0, mem[i], 0, column);
        }

        return new Matrix(mem);
    }

    public double get(int a, int b){
        return member[a][b];
    }

    public int row(){
        return member.length;
    }

    public int column(){
        return member[0].length;
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        int row = row(), column = column();

        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                String n;
                double thi = get(i, j);

                try {
                    if (thi == (long) thi) {
                        n = String.format(Locale.CHINA, "%d", (long) thi);
                    } else {
                        n = String.format("%s", thi);
                    }
                    s.append(n);
                } catch (Exception e){
                    e.printStackTrace();
                    s.append(thi);
                }

                char c = j < column - 1 ? ' ' : '\n';
                s.append(c);
            }
        }
        return s.toString();
    }
}
