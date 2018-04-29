package com.amalgama.interval;

import java.util.Objects;

/**
 *
 * A range defines the boundaries around a contiguous span of values. Example (4..5), [4..5), (-Infinity.. 5]
 * Bound can be open and closed. If point infinity bound always open
 * The upper endpoint may not be less than the lower. (@link IllegalArgumentException)
 */

public class Range {

    private Bound boundLower;
    private Bound boundUpper;
    private double lowerPoint;
    private double upperPoint;

    Range(double lowerPoint, Bound boundLower, double upperPoint, Bound boundUpper) {

        if (lowerPoint > upperPoint) {
            throw new IllegalArgumentException("'lowerPoint' can not be larger than 'upperPoint'!");
        }
        this.lowerPoint = lowerPoint;
        this.upperPoint = upperPoint;

        this.boundLower = this.lowerPoint == Double.NEGATIVE_INFINITY ? Bound.Infinity : boundLower;
        this.boundUpper = this.upperPoint == Double.POSITIVE_INFINITY ? Bound.Infinity : boundUpper;
    }

    /**
     * Return {@link Range} with singleton point
     * @param point singleton point of segment
     * @return {@link Range} singleton (point - 3, return [3..3])
     */

    public static Range singleton(double point) {
        return new Range(point, Range.Bound.CLOSED, point, Range.Bound.CLOSED);
    }

    /**
     * Return {@link Range} with open start point and closed end point
     * if point infinity bound always open
     * @param lowerPoint starting point of a segment
     * @param upperPoint end point of a segment
     * @return {@link Range} (example - (4..5] )
     * @throws IllegalArgumentException The upper endpoint may not be less than the lower.
     */
    public static Range openClosed(double lowerPoint, double upperPoint) {
        return new Range(lowerPoint, Bound.OPEN, upperPoint, Bound.CLOSED);
    }

    /**
     * Return {@link Range} with closed start point and open end point
     * if point infinity bound always open
     * @param lowerPoint starting point of a segment
     * @param upperPoint end point of a segment
     * @return {@link Range} (example - [4..5) )
     * @throws IllegalArgumentException The upper endpoint may not be less than the lower.
     */
    public static Range closedOpen(double lowerPoint, double upperPoint) {
        return new Range(lowerPoint, Bound.CLOSED, upperPoint, Bound.OPEN);
    }

    /**
     * Return {@link Range} with closed start and end point
     * if point infinity bound always open
     * @param lowerPoint starting point of a segment
     * @param upperPoint end point of a segment
     * @return {@link Range} (example - [4..5] )
     * @throws IllegalArgumentException The upper endpoint may not be less than the lower.
     */
    public static Range closed(double lowerPoint, double upperPoint) {
        return new Range(lowerPoint, Bound.CLOSED, upperPoint, Bound.CLOSED);
    }

    /**
     * Return {@link Range} with open start and end point
     * if point infinity bound always open
     * @param lowerPoint starting point of a segment
     * @param upperPoint end point of a segment
     * @return {@link Range} (example - (4..5) )
     * @throws IllegalArgumentException The upper endpoint may not be less than the lower.
     */
    public static Range open(double lowerPoint, double upperPoint) {
        return new Range(lowerPoint, Bound.OPEN, upperPoint, Bound.OPEN);
    }

    boolean contains(double p) {
        return p >= lowerPoint && p <= upperPoint;
    }

    /**
     * Determines the inclusion of extreme values of points in the interval
     */
    public enum Bound {
        Infinity, OPEN, CLOSED
    }

    Bound getBoundLower() {
        return boundLower;
    }

    Bound getBoundUpper() {
        return boundUpper;
    }

    double getLowerPoint() {
        return lowerPoint;
    }

    double getUpperPoint() {
        return upperPoint;
    }

    @Override
    public String toString() {
        String lowerBkr = boundLower == Bound.CLOSED ? "[" : "(";
        String upperBkr = boundUpper == Bound.CLOSED ? "]" : ")";
        return lowerBkr + lowerPoint + ".." + upperPoint + upperBkr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Range)) return false;
        Range range = (Range) o;
        return Double.compare(range.lowerPoint, lowerPoint) == 0 &&
                Double.compare(range.upperPoint, upperPoint) == 0 &&
                boundLower == range.boundLower &&
                boundUpper == range.boundUpper;
    }

    @Override
    public int hashCode() {
        return Objects.hash(boundLower, boundUpper, lowerPoint, upperPoint);
    }
}
