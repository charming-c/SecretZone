package com.example.screctzone.cyan._1.math;

public class AssGroup extends Group {
    int[] member = {-1, 1};

    @Override
    public double o(double a, double b) {
        return a * b;
    }

    @Override
    public double identity() {
        return 1;
    }

    @Override
    public double invert(double x) throws GroupException {
        int[] m = member;
        for (int value : m) {
            if (value == x) {
                return 1 / x;
            }
        }
        throw new GroupException();
    }
}
