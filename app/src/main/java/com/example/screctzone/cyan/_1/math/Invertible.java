package com.example.screctzone.cyan._1.math;

public interface Invertible {

    /**
     * @param x 必须是 {@link Group} 的成员,
     * @return 返回<var>x</var>的逆元, 如果<var>x</var>不是{@link Group} 的成员, 则返回{@code }
     */
    double invert(double x) throws GroupException;
}
