package com.example.screctzone.cyan._1.math;

public class IntegerPlusGroup extends Group  implements Commutative {
    private static IntegerPlusGroup group = new IntegerPlusGroup();

    private IntegerPlusGroup(){
    }

    public static IntegerPlusGroup instance(){
        return group;
    }

    @Override
    public double o(double a, double b) {
        return a + b;
    }

    @Override
    public double identity() {
        return 0;
    }

    @Override
    public double invert(double x) {
        return -x;
    }
}
