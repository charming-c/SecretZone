package com.example.screctzone.cyan._1.math;

import com.example.screctzone.matrix.MatrixException;

public class MatrixRing {
    private static MatrixRing matrixRing = new MatrixRing();
    private static Matrix zero = new Matrix(new double[0][0]);

    private MatrixRing(){}

    public static MatrixRing instance(){
        return matrixRing;
    }


    public Matrix plus(Matrix a, Matrix b) {
        if(a == zero){
            return b;
        }

        if (b == zero) {
            return a;
        }

        int r = a.row(), c = b.column();
        if (r != b.row() || c != b.column()) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        double[][] member = new double[r][c];

        for (int i = 0; i < member.length; i++) {
            double[] d = member[i];
            for (int j = 0; j < d.length; j++) {
                d[j] = a.get(i, j) + b.get(i, j);
            }
        }

        return new Matrix(member);
    }


    public Matrix multiple(Matrix a, Matrix b){
        int k1 = a.row(), l = a.column(), k2 = b.column();
        if (l != b.row()) {
            try {
                throw new MatrixException();
            } catch (MatrixException e) {
                e.printStackTrace();
                return null;
            }
        }

        double[][] member = new double[k1][k2];
        for (int i = 0; i < member.length; i++) {
            double[] d = member[i];
            for (int j = 0; j < d.length; j++) {
                double total = 0;
                for (int k = 0; k < l; k++) {
                    total += a.get(i, k) * b.get(k, j);
                }
                member[i][j] = total;
            }
        }

        return new Matrix(member);
    }

    public Matrix zero(){
        return zero;
    }

    public Matrix zeroElement(int r, int column) {
        return new Matrix(new double[r][column]);
    }


    public Matrix negativeElement(Matrix x) {
        int r = x.row(), c = x.column();
        double[][] member = new double[r][c];

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                member[i][j] = -x.get(i, j);
            }
        }
        return new Matrix(member);
    }
}
