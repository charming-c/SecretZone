package com.example.screctzone.matrix;

import java.util.ArrayList;
import java.util.Map;

public class DeMatrix {
    private int column;
    private int row;

    public static final DeMatrix _1 = new DeMatrix(1, 1);

    private final ArrayList<ArrayList<Integer>> core;

    public DeMatrix(int row, int column){
        this.row = row;
        this.column = column;
        core = new ArrayList<>(row);
        for(int i = 0; i < row; i++ ){
            ArrayList<Integer> cl = new ArrayList<>(column);
            for (int j = 0; j < column; j++) {
                cl.add(0);
            }
            core.add(i, cl);
        }
    }

    public static DeMatrix getFrom(int[][] data){
        int row = data.length;
        int column = data[0].length;
        DeMatrix m = new DeMatrix(row, column);

        for (int i = 0; i < row; i++){
            for (int j = 0; j < column; j++) {
                m.set(i, j, data[i][j]);
            }
        }

        return m;
    }

    public static DeMatrix decode(String s){
        String[] ss = s.split("\n");
        int row = ss.length, column = 0;

        for (int i = 0; i < row; i++) {
            String[] cs = ss[i].split(" ");
            int l1 = cs.length;
            column = Math.max(column, l1);
        }

        DeMatrix m = new DeMatrix(row, column);

        for(int i = 0; i < row; i++){
            String[] cs = ss[i].split(" ");
            int l1 = cs.length;
            for(int j = 0; j < l1 && j < column; j++){
                m.set(i, j, Integer.decode(cs[j]));
            }
        }

        return m;
    }

    public DeMatrix copy(){
        DeMatrix m = new DeMatrix(row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                m.set(i, j, get(i, j));
            }
        }
        return m;
    }

    public static DeMatrix plus(DeMatrix a, DeMatrix b) throws MatrixException{
        return addOrMinus(a, b, true);
    }

    public static DeMatrix minus(DeMatrix a, DeMatrix b) throws MatrixException{
        return addOrMinus(a, b, false);
    }

    private static DeMatrix addOrMinus(DeMatrix a, DeMatrix b, boolean add) throws MatrixException {
        int row = a.row , column = a.column;
        if(row != b.row || column != b.column){
            throw new MatrixException();
        }
        DeMatrix matrix = new DeMatrix(row, column);
        for (int i = 0; i < row; i++){
            for (int j = 0; j < column; j++){
                int aa = a.get(i,j), bb= b.get(i,j);
                int cc = add ? aa + bb : aa - bb;
                matrix.set(i, j, cc);
            }
        }

        return matrix;
    }

    public static DeMatrix time(DeMatrix a, DeMatrix b) throws MatrixException{
        int r = a.row, l = a.column, c = b.column;
        if(l != b.row) throw new MatrixException();
        DeMatrix m = new DeMatrix(r, c);
        for(int i = 0; i < r; i++){
            for(int j = 0; j < c; j++){
                int cc = 0;
                for(int k = 0; k < l; k++){
                    cc += a.get(i, k) * b.get(k, j);
                }
                m.set(i, j, cc);
            }
        }
        return m;
    }

    public static DeMatrix exec(Map<String, DeMatrix> map, String raw) throws MatrixException{
        raw = raw.trim();
        int l = raw.length();
        boolean outFromBrackets = false;

        if (!tool.containsOperator(raw)) {
            return map.get(raw);
        }

        int cl = l / 2, ml = cl + 1;

        char[] cs = new char[cl];
        DeMatrix[] ms = new DeMatrix[ml];

        int p1 = 0, p2 = -1,k = 0;

        for (int i = 0; i < l; i++) {
            char c = raw.charAt(i);

            // 不检测括号前后是否为运算符,要求用户输入时自行保证;
            if (c == '(') {
                if (raw.contains(")")) {
                    int ind = raw.lastIndexOf(")");
                    if(ind - i > 1){
                        String s = raw.substring(i + 1, ind);
                        ms[k] = exec(map, s);
                        outFromBrackets = true;
                    }
                    i = ind;
                } else {
                    if(i == l - 1 && i > p1){
                        String s = raw.substring(p1, i);
                        ms[k++] = map.get(s);
                        break;
                    }
                }
                p2 = i;

                continue;
            }

            if (tool.isOperator(c)) {
                if(!outFromBrackets){
                    String s = raw.substring(p1, p2 + 1);
                    ms[k] = map.get(s);
                } else {
                    outFromBrackets = false;
                }
                cs[k] = c;
                k++;
                p1 = p2 = i + 1;
            } else {
                p2 = i;
                if(i == l - 1){
                    String s = raw.substring(p1, p2 + 1);
                    ms[k++] = map.get(s);
                }
            }
        }

        char[] ccc = new char[k - 1];
        DeMatrix[] mmm = new DeMatrix[k];
        System.arraycopy(cs, 0, ccc, 0, k - 1);
        System.arraycopy(ms, 0, mmm, 0, k);

        return calc(ccc, mmm);
    }

    private static DeMatrix calc(char[] cs, DeMatrix... ms) throws MatrixException{
        int cl = cs.length, ml = ms.length;
        if(cl != ml - 1 || cl < 1) throw new MatrixException();
        DeMatrix[] ne = new DeMatrix[ml];
        char[] pom = new char[cl];
        int p = 0, cp = 0;
        ne[0] = ms[0];
        for (int i = 0; i < cl; i++) {
            if(tool.isTime(cs[i])){
                ne[p] = time(ne[p], ms[i + 1]);
            } else if (tool.isPlusOrMinus(cs[i])) {
                cp++;
                pom[cp - 1] = cs[i];

                p++;
                ne[p] = ms[i + 1];
            } else throw new MatrixException();
        }

        if(p == 0){
            return ne[0];
        } else {
            DeMatrix[] mmm = new DeMatrix[p + 1];
            char[] ccc = new char[cp];
            System.arraycopy(ne, 0, mmm, 0, p + 1);
            System.arraycopy(pom, 0, ccc, 0, cp);
            return plusOrMinus(ccc, mmm);
        }
    }

    private static DeMatrix times(DeMatrix... ms) throws MatrixException{
        int l = ms.length;
        if(l < 1) throw new MatrixException();
        if(l == 1) return ms[0].copy();
        int row = ms[0].row;
        int column = ms[l - 1].column;
        DeMatrix m = ms[0];
        for(int i = 0; i < l - 1; i++){
            m = time(ms[i], ms[i+1]);
        }
        return m;
    }

    private static DeMatrix plusOrMinus(char[] cs, DeMatrix... ms) throws MatrixException{
        int cl = cs.length, ml = ms.length;
        if(cl != ml - 1 ) throw new MatrixException();
        DeMatrix m = ms[0];
        for (int i = 0; i < cl; i++) {
            m = addOrMinus(m, ms[i+1], tool.isPlus(cs[i]));
        }
        return m;
    }

    private static DeMatrix calc(DeMatrix a, char o, DeMatrix b) throws MatrixException{
        if(tool.isTime(o)){
            return time(a, b);
        } else if (tool.isPlus(o)){
            return plus(a,b);
        } else if(tool.isMinus(o)){
            return minus(a, b);
        }
        throw new MatrixException();
    }



    private static class tool{
        static boolean containsOperator(String s){
            return s.contains("*") || s.contains("+") || s.contains("-");
        }

        static boolean isOperator(char c){
            return isMinus(c) || isPlus(c) || isTime(c);
        }

        static boolean isTime(char c){
            return c == '*';
        }

        static boolean isPlusOrMinus(char c){
            return isPlus(c) || isMinus(c);
        }

        static boolean isPlus(char c){
            return c == '+';
        }

        static boolean isMinus(char c){
            return c == '-';
        }
    }

    public void timesFrom(DeMatrix m) throws MatrixException{

    }

    private void addOrMinusFrom(DeMatrix m, boolean add){
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                int aa = get(i, j), bb = m.get(i, j);
                int cc = add ? aa + bb : aa - bb;
                set(i,j,cc);
            }
        }
    }

    public void addFrom(DeMatrix m){
        addOrMinusFrom(m, true);
    }

    public void minusFrom(DeMatrix m){
        addOrMinusFrom(m, false);
    }

    public int getColumn(){ return column;}

    public int getRow(){ return row; }

    public int get(int row, int column){
        return core.get(row).get(column);
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

    public String toString(){
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                s.append(get(i,j));
                char c = j < column - 1 ? ' ' : '\n';
                s.append(c);
            }
        }
        return s.toString();
    }
}
