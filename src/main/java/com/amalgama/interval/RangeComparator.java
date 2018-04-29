package com.amalgama.interval;

import java.util.Comparator;

class RangeComparator implements Comparator<Range> {

    @Override
    public int compare(Range o1, Range o2) {
        int result = Double.compare(o1.getLowerPoint(), o2.getLowerPoint());
        return result == 0 ? Double.compare(o1.getUpperPoint(), o2.getUpperPoint()) : result;
    }
}

