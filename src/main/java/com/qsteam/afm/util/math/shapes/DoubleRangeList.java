package com.qsteam.afm.util.math.shapes;

import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;

public class DoubleRangeList extends AbstractDoubleList {

    private final int sectionCount;

    DoubleRangeList(int sectionCount) {
        this.sectionCount = sectionCount;
    }

    @Override
    public double getDouble(int index) {
        return (double) index / this.sectionCount;
    }

    @Override
    public int size() {
        return this.sectionCount + 1;
    }

}
