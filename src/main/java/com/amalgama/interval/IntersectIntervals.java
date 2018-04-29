package com.amalgama.interval;

import java.util.*;

/**
 * Creates a tree of intersecting intervals from the list of received intervals.
 * </br> 1. [-INFINITY; 1] U [2; +INFINITY)
 * </br> 2. [0; 3] U [5; +INFINITY]
 * <pre>
 * {@code
 *         List<Intervals> listRS = new ArrayList<>();
 *         Intervals rs = new Intervals();
 *         rs.add(Range.closed(Double.NEGATIVE_INFINITY, 1));
 *         rs.add(Range.closed(2, Double.POSITIVE_INFINITY));
 *         listRS.add(rs);
 *         rs = rs = new Intervals();
 *         rs.add(Range.closed(0, 3));
 *         rs.add(Range.closed(5, Double.POSITIVE_INFINITY));
 *         listRS.add(rs);
 *         IntersectIntervals intersectIntervals = new IntersectIntervals(listRS);
 *         System.out.println(intersectIntervals);
 *
 *         output..
 *         * IntersectIntervals{[[0.0..1.0], [2.0..3.0], [5.0..Infinity)]]}
 *  }
 *  </pre>
 *
 */
public class IntersectIntervals {

    private TreeSet<Range> treeSetRange;

    private double accuracy = 0.01d;

    /**
     * Create, initialization and filling of the interval tree
     * @param intervals List of RangeSet
     */
    public IntersectIntervals(List<Intervals> intervals) {
        treeSetRange = new TreeSet<>(new RangeComparator());
        unionAllSets(intervals);
    }

    private void unionAllSets(List<Intervals> list) {
        if (list.isEmpty()){
            return;
        }

        Intervals intersectIntervals = list.get(0);
        if (list.size()==1){
            treeSetRange.addAll(intersectIntervals.getTreeSet());
            return;
        }

        Intervals tmp = new Intervals();

        for (int i = 1; i < list.size(); i++) {

            TreeSet<Range> ranges = list.get(i).getTreeSet();
            for (Range range : ranges) {
                Intervals intersR = intersectIntervals.subInterval(range);
                tmp.addAll(intersR);
            }
            intersectIntervals = tmp;
            tmp = new Intervals();
        }
        treeSetRange.addAll(intersectIntervals.getTreeSet());
    }
    /**
     * This method returns value of the nearest interval point.
     *
     * @param point Point value for search.
     * @return Value of the nearest interval point
     * @throws EmptyIntersectIntervalsException if there are no intersections of sets.
     */

    public double findClosest(double point) throws EmptyIntersectIntervalsException {
        if (treeSetRange.isEmpty()) {
            throw new EmptyIntersectIntervalsException("Intersection of intervals is empty");
        }
        Range pp = Range.singleton(point);
        Range higher = treeSetRange.ceiling(pp);
        Range lower = treeSetRange.floor(pp);
        double result;
        if (higher == null) {
            result = Objects.requireNonNull(lower).getUpperPoint();
            if (lower.getBoundUpper() == Range.Bound.OPEN){
                result-= accuracy;
            }

        } else if (lower == null) {
            result = higher.getLowerPoint();
            if (higher.getBoundLower() == Range.Bound.OPEN){
                result+= accuracy;
            }

        } else if (higher.contains(point) || lower.contains(point)) {
            result = point;

        }else {
            double lowerP = Math.abs(Objects.requireNonNull(lower).getUpperPoint() - point);
            double higherP = Math.abs(Objects.requireNonNull(higher).getLowerPoint() - point);

            if (lowerP < higherP) {
                result = lower.getLowerPoint();
                if (lower.getBoundLower() == Range.Bound.OPEN) {
                    result += accuracy;
                }
            } else {
                result = higher.getLowerPoint();
                if (higher.getBoundUpper() == Range.Bound.OPEN) {
                    result -= accuracy;
                }
            }
        }
        return result;
    }

    /**
     * This method returns list of the intersection range.
     *
     * @return List of the intersection range.
     */
    public List<Range> getListIntersectRange() {
        return new ArrayList<>(treeSetRange);
    }

    void add(Range range) {
        treeSetRange.add(range);
    }

    @Override
    public String toString() {
        return "IntersectIntervals{" +
                treeSetRange +
                '}';
    }

    public boolean isEmpty() {
        return treeSetRange.isEmpty();
    }
}
