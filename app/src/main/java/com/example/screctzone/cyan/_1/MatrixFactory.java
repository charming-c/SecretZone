package com.example.screctzone.cyan._1;

import com.example.screctzone.cyan._1.math.Matrix;
import com.example.screctzone.cyan._1.math.MatrixRing;
import com.example.screctzone.matrix.DeMatrix;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class MatrixFactory {

    public static final char SIGN_PLUS_1 = '+';

    public static final char SIGN_TIMES_1 = '*';

    public static final char SIGN_TIMES_2 = '×';

    public static final char SIGN_BRACKET_LEFT_1 = '(';

    public static final char SIGN_BRACKET_LEFT_2 = '[';

    public static final char SIGN_BRACKET_LEFT_3 = '{';

    public static final char SIGN_BRACKET_RIGHT_1 = ')';

    public static final char SIGN_BRACKET_RIGHT_2 = ']';

    public static final char SIGN_BRACKET_RIGHT_3 = '}';

    public static final char SIGN_REPLACEMENT = '#';

    private Stack<Queue<Matrix>> pool = new Stack<>();

    /**
     * 指{@code STACKS}当前的长度;
     */
    private int cursor = 0;

    public static MatrixFactory newInstance(){
        return new MatrixFactory();
    }

    public Matrix decode(String sample){
        String[] ss = sample.split("\n");
        int row = ss.length, column = 0;

        for (String s : ss) {
            String[] cs = s.split(" ");
            int l1 = cs.length;
            column = Math.max(column, l1);
        }

        double[][] data = new double[row][column];

        for(int i = 0; i < row; i++){
            String[] cs = ss[i].split(" ");
            int l1 = cs.length;
            for(int j = 0; j < l1 && j < column; j++){
                data[i][j] = Double.parseDouble(cs[j]);
            }
        }

        return new Matrix(data);
    }

    /**
     * 正式的将文字指令计算成结果;
     *
     * @param cmd 可以包含括号
     * @return 返回指令 {@param cmd} 的计算结果
     */
    public Matrix decode(char[] cmd, Map<String, Matrix> map) {
        if (cmd == null) {
            return null;
        }

        sink();

        String cmd_string = new String(cmd);

        if (hasLeftBracket(cmd)) {
            int lCount = 0;
            for (int i = 0; i < cmd.length; i++) {
                char c = cmd[i];
                if (isLeftBracket(c)) {
                    lCount = 1;
                    int rPos = i + 1;
                    for (; rPos < cmd.length; rPos++) {
                        char cc = cmd[rPos];
                        if (isLeftBracket(cc)){
                            lCount++;
                        } else if (isRightBracket(cc)) {
                            lCount--;
                        }

                        if (lCount == 0) {
                            break;
                        }
                    }

                    if (lCount == 0) {
                        int len = rPos - i - 1;

                        if (len > 0) {
                            char[] toDecode = new char[len];
                            System.arraycopy(cmd, i + 1, toDecode, 0, len);

                            Matrix m = decode(toDecode, map);

                            keepInPool(cmd, i, rPos + 1, m );
                        } else if (len == 0) {
                            keepInPool(cmd, i, rPos + 1, null);
                        }

                        i = rPos;
                    }
                }
            }
        }

        Matrix ret = sep_plus(cmd, map);
        up();
        return ret;
    }

    /**
     * @param cmd 有"A+B*cc+D"的形式,没有括号, 处理清洁版命令,可以清零{@code pool}的最上层的{@link Queue<Matrix>};
     */
    private Matrix sep_plus(char[] cmd, Map<String, Matrix> map) {
        int len = 0, cmdl = cmd.length;
        int[] t1 = new int[cmdl + 1], t2 = new int[cmdl + 1];

        for (int i = 0; i < cmdl; i++) {
            if (i == 0) {
                t1[len] = 0;
            }

            if (isPlus(cmd[i])) {
                t1[len + 1] = i + 1;
                t2[len] = i;
                len++;

                if (i == cmdl - 1) {
                    len--;
                }
            }

            if (i == cmdl - 1) {
                t2[len] = cmdl;
            }
        }

        len++;

        Matrix[] cmds = new Matrix[len];

        for (int i = 0; i < len; i++) {
            int nl = t2[i] - t1[i];
            char[] dest = new char[nl];
            System.arraycopy(cmd, t1[i], dest, 0, nl);
            cmds[i] = decode_times(dest, map);
        }

        return plus(cmds);
    }

    /**
     * @param cmd 中不含括号以及"+",形如"A*B"或者"A";
     */
    private Matrix decode_times(char[] cmd, Map<String, Matrix> map) {
        int l = cmd.length;
        int[] t1 = new int[l + 1], t2 = new int[l + 1];
        int len = 0;
        for (int i = 0; i < l; i++) {
            if (i == 0) {
                t1[0] = 0;
            }

            if (isTimes(cmd[i])) {
                t1[len + 1] = i + 1;
                t2[len] = i;
                len++;

                if (i == l - 1) {
                    len--;
                }
            }

            if (i == l - 1) {
                t2[len] = l;
            }
        }

        len++;

        Matrix[] matrices = new Matrix[len];
        for (int i = 0; i < len; i++) {
            String cmd_string = new String(cmd).substring(t1[i], t2[i]).replace(" ", "");

            //在此一个一个的清除pool中queue的元素;
            matrices[i] = get(cmd_string.toCharArray(), map);
        }

        return multiple(matrices);
    }

    private Matrix multiple(Matrix... matrices) {
        if (matrices == null || matrices.length == 0) {
            return null;
        }

        Matrix m = matrices[0];
        for (int i = 1; i < matrices.length; i++) {
            Matrix toTimes = matrices[i];
            if (m == null) {
                m = toTimes;
            }

            if (toTimes == null) {
                continue;
            }

            m = MatrixRing.instance().multiple(m, matrices[i]);
        }

        return m;
    }

    private Matrix plus(Matrix... matrices) {
        if (matrices == null || matrices.length == 0) {
            return MatrixRing.instance().zero();
        }

        Matrix m = matrices[0];
        for (int i = 1; i < matrices.length; i++) {
            Matrix toAdd = matrices[i];
            if (m == null) {
                m = toAdd;
            }

            if (toAdd == null) {
                continue;
            }

            m = MatrixRing.instance().plus(m, matrices[i]);
        }

        return m;
    }

    private void keepInPool(char[] cmd, int start, int end, Matrix m) {
        if (end - start > 2){
            push(m);
            for (int i = start; i < end; i++) {
                if (i == start) {
                    cmd[i] = SIGN_REPLACEMENT;
                } else {
                    cmd[i] = ' ';
                }
            }
        }
    }

    private void sink(){
        pool.push(new ArrayDeque<Matrix>());
    }

    private void up(){
        pool.pop();
    }

    private void push(Matrix m){
        Queue<Matrix> queue = pool.peek();
        if (m != null) {
            queue.add(m);
        }
    }

    private Matrix pull(){
        Queue<Matrix> queue = pool.peek();
        return queue.remove();
    }

    private Matrix get(char[] key, Map<String, Matrix> map) {
        String k = new String(key).replace(" ", "");
        if (k.equals(Character.toString(SIGN_REPLACEMENT))) {
            return pull();
        }
        return map.get(k);
    }

    public boolean isPlus(char c) {
        return c == SIGN_PLUS_1;
    }

    public boolean isTimes(char c) {
        return c == SIGN_TIMES_1 || c == SIGN_TIMES_2;
    }

    public boolean isLeftBracket(char c) {
        return c == SIGN_BRACKET_LEFT_1 || c == SIGN_BRACKET_LEFT_2 || c == SIGN_BRACKET_LEFT_3;
    }

    public boolean isRightBracket(char c) {
        return c == SIGN_BRACKET_RIGHT_1 || c == SIGN_BRACKET_RIGHT_2 || c == SIGN_BRACKET_RIGHT_3;
    }

    public boolean hasLeftBracket(char[] cs) {
        if (cs == null) {
            return false;
        }

        for (char c : cs) {
            if (isLeftBracket(c)) {
                return true;
            }
        }

        return false;
    }
}
