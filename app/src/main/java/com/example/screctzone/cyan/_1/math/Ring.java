package com.example.screctzone.cyan._1.math;

public abstract class Ring extends Group {
    public abstract double plus(double a, double b);
    public abstract double multiple(double a, double b);
    public abstract double zeroElement();
    public abstract double negativeElement(double x);

    @Override
    public double o(double a, double b) {
        return plus(a, b);
    }

    @Override
    public double identity() {
        return zeroElement();
    }

    @Override
    public double invert(double x) {
        return negativeElement(x);
    }
}
