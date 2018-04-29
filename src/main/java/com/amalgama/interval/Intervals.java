package com.amalgama.interval;

import java.util.*;

/**
 * Intervals - Contains a range tree
 * <p>
 * </br> 1. (-Infinity.. 1],  (2.. 5],  [10..25]
 *
 * <pre>
 * {@code
 *         Intervals intervals = new Intervals(
 *                  Range.closed(Double.NEGATIVE_INFINITY, 1d),
 *                  Range.openClosed(2, 5),
 *                  Range.closed(10, 25)
 *                  );
 *         System.out.println(intervals);
 *
 *         output..
 *         * {(-Infinity.. 1], (2.. 5], [10..25]}
 *  }
 *  </pre>
 */
public class Intervals {

    private TreeSet<Range> treeSet = new TreeSet<>(new RangeComparator());

    /**
     * Create, initialization and filling of the interval tree
     *
     * @param range Array of range
     * @see Range
     */
    public Intervals(Range... range) {
        treeSet.addAll(Arrays.asList(range));
    }

    /**
     * @return TreeSet<Range>
     */

    TreeSet<Range> getTreeSet() {
        return treeSet;
    }

    Intervals subInterval(Range range) {
        Intervals newIntervals = new Intervals();
        for (Range rangeHead : treeSet) {
            if (rangeHead.getLowerPoint() > range.getUpperPoint()) {
                break;
            }
            if (range.getLowerPoint() > rangeHead.getUpperPoint()) {
                continue;
            }
            Range newR = create(rangeHead, range);
            if (newR != null) {
                newIntervals.add(newR);
            }
        }
        return newIntervals;
    }

    private Range create(Range r1, Range r2) {
        Range range;
        double lowerPoint;
        double upperPoint;
        Range.Bound lowBound;
        Range.Bound upBound;

        if (r1.getLowerPoint() > r2.getLowerPoint()) {
            lowBound = r1.getBoundLower();
            lowerPoint = r1.getLowerPoint();
        } else if (r1.getLowerPoint() < r2.getLowerPoint()) {
            lowBound = r2.getBoundLower();
            lowerPoint = r2.getLowerPoint();
        } else {
            lowerPoint = r1.getLowerPoint();
            lowBound = (r1.getBoundLower() == Range.Bound.OPEN || r2.getBoundLower() == Range.Bound.OPEN)
                    ? Range.Bound.OPEN : Range.Bound.CLOSED;
        }

        if (r1.getUpperPoint() < r2.getUpperPoint()) {
            upperPoint = r1.getUpperPoint();
            upBound = r1.getBoundUpper();
        } else if (r1.getUpperPoint() > r2.getUpperPoint()) {
            upperPoint = r2.getUpperPoint();
            upBound = r2.getBoundUpper();
        } else {
            upperPoint = r2.getUpperPoint();
            upBound = (r1.getBoundUpper() == Range.Bound.OPEN || r2.getBoundUpper() == Range.Bound.OPEN)
                    ? Range.Bound.OPEN : Range.Bound.CLOSED;
        }
        range = new Range(lowerPoint, lowBound, upperPoint, upBound);
        if (lowerPoint == upperPoint && (lowBound == Range.Bound.OPEN || upBound == Range.Bound.OPEN)) {
            range = null;
        }
        return range;
    }

    /**
     * Add all Intervals
     *
     * @param intervals Intervals
     */

    void addAll(Intervals intervals) {
        this.treeSet.addAll(intervals.getTreeSet());
    }

    /**
     * Add range, for test
     *
     * @param range Range
     */
    void add(Range range) {
        treeSet.add(range);
    }

    @Override
    public String toString() {
        return treeSet.toString();
    }
}


