package com.amalgama.interval;

/**
 * Thrown when an application attempts to use empty IntersectIntervals
 *
 */
public class EmptyIntersectIntervalsException extends Exception {

    EmptyIntersectIntervalsException(String message) {
        super(message);
    }
}
