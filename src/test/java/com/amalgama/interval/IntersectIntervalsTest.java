package com.amalgama.interval;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IntersectIntervalsTest {

    private static List<Intervals> listIntervals = new ArrayList<>();

    static {
        Intervals tmp = new Intervals(Range.closed(Double.NEGATIVE_INFINITY, 1d),
                Range.closed(2, 10),
                Range.closed(16, Double.POSITIVE_INFINITY));
        listIntervals.add(tmp);

        tmp = new Intervals( Range.closed(-18, -15),
                Range.closed(-13, 0),
                Range.openClosed(3, 20),
                Range.closed(22, 23)
        );
        listIntervals.add(tmp);

        tmp = new Intervals(Range.closed(-18, -16),
                Range.closed(-13, -7),
                Range.closed(-4, -2),
                Range.closed(1, 2),
                Range.closed(3, 5),
                Range.closed(8, 11),
                Range.closed(14, 17),
                Range.closedOpen(19, 23)
        );
        listIntervals.add(tmp);
    }

    @Test
    void getSubSetsTest() {
        IntersectIntervals intersectIntervals = new IntersectIntervals(listIntervals);
        List<Range> expectedList = Arrays.asList(Range.closed(-18, -16),
                Range.closed(-13, -7),
                Range.closed(-4, -2),
                Range.openClosed(3, 5),
                Range.closed(8, 10),
                Range.closed(16, 17),
                Range.closed(19, 20),
                Range.closedOpen(22, 23)
        );
        assertEquals(expectedList, intersectIntervals.getListIntersectRange());
    }

    @Test
    void getClosestTest() {
        IntersectIntervals intersectIntervals = new IntersectIntervals(listIntervals);

        try {
            double actual = intersectIntervals.findClosest(4.5d);
            assertEquals(4.5d, actual);

            actual = intersectIntervals.findClosest(3.08d);
            assertEquals(3.08d, actual);

            actual = intersectIntervals.findClosest(100d);
            assertEquals(22.99d, actual);

            actual = intersectIntervals.findClosest(-20d);
            assertEquals(-18d, actual);
        }catch (EmptyIntersectIntervalsException ignored){
        }
    }

    @Test
    void getClosestIfSetIsEmpty() {
        List<Intervals> list = new ArrayList<>();
        Intervals intervals = new Intervals();
        intervals.add(Range.closed(-100, 1));
        intervals.add(Range.closed(5, 10));
        list.add(intervals);
        intervals = new Intervals();
        intervals.add(Range.closed(2, 3));
        intervals.add(Range.closedOpen(4, 5));
        list.add(intervals);

        IntersectIntervals intersectIntervals = new IntersectIntervals(list);

        Executable closureContainingCodeToTest = () -> intersectIntervals.findClosest(3d);
        assertThrows(EmptyIntersectIntervalsException.class, closureContainingCodeToTest);
    }

    @Test
    void timeline() {

        IntersectIntervals intersectIntervals = new IntersectIntervals(listIntervals);

        for (int i = 0; i < 5_000; i++) {
            double v = new Random().nextDouble();
            intersectIntervals.add(Range.closed(v, v+10));
        }

        double[] dd = new double[500];
        for (int i = 0; i < dd.length; i++) {
            dd[i] = new Random().nextDouble();
        }

        long startTime = System.nanoTime();

        for (double aDd : dd) {
            try {
                intersectIntervals.findClosest(aDd);
            } catch (EmptyIntersectIntervalsException ignored) {
            }
        }
        System.out.println("Time elapsed: " + (double) (System.nanoTime() - startTime) / 1000000);
    }

    @Test
    void findCLosestIfBoundIsOpen() {
        List<Intervals> listRangeSet2 = new ArrayList<>();
        Intervals rangeSet = new Intervals();
        rangeSet.add(Range.openClosed(2.5, 6.5));
        listRangeSet2.add(rangeSet);

        rangeSet = new Intervals();
        rangeSet.add(Range.closed(2, 7));
        listRangeSet2.add(rangeSet);

        IntersectIntervals intersectIntervals = new IntersectIntervals(listRangeSet2);

        try {
            assertEquals(2.51, intersectIntervals.findClosest(1d));
        } catch (EmptyIntersectIntervalsException ignored) {

        }
    }

    @Test
    void checkBoundInfinity(){
        List<Intervals> listRangeSet2 = new ArrayList<>();
        Intervals rangeSet = new Intervals();
        rangeSet.add(Range.closed(Double.NEGATIVE_INFINITY, 5));
        listRangeSet2.add(rangeSet);

        rangeSet = new Intervals();
        rangeSet.add(Range.closedOpen(3, 5));
        listRangeSet2.add(rangeSet);

        IntersectIntervals intersectIntervals = new IntersectIntervals(listRangeSet2);

        List<Range> expectedList = Collections.singletonList(Range.closedOpen(3, 5));
        assertEquals(expectedList, intersectIntervals.getListIntersectRange());
    }
}