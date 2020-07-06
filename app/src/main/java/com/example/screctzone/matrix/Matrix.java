package com.example.screctzone.matrix;

import java.util.ArrayList;

public class Matrix {
    private int column;
    private int row;

    private final ArrayList<ArrayList<Integer>> core;

    public Matrix(int row, int column){
        this.row = row;
        this.column = column;
        core = new ArrayList<>(row);
        for(int i = 0; i < row; i++ ){
            core.set(i, new ArrayList<Integer>(column));
        }
    }

    public void set(int row, int column, int data){
        core.get(row).set(column, data);
    }

    public void setRow(int data[], int row){
        ArrayList<Integer> listInRow = core.get(row);
        int l = data.length;
        for (int i = 0; i < column && i < l; i++){
            listInRow.set(i, data[i]);
        }
    }

    public void setColumn(int data[], int column) {
        for (int i = 0; i < row && i < data.length; i++) {
            core.get(i).set(column, data[i]);
        }
    }



}
