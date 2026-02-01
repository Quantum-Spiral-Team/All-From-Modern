package com.qsteam.afm.util.math.shapes;

import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleList;

public class NonOverlappingMerger extends AbstractDoubleList implements IDoubleListMerger {
    private final DoubleList lower;
    private final DoubleList upper;
    private final boolean swap;

    public NonOverlappingMerger(DoubleList lower, DoubleList upper, boolean swap) {
        this.lower = lower;
        this.upper = upper;
        this.swap = swap;
    }

    public int size() {
        return this.lower.size() + this.upper.size();
    }

    @Override
    public boolean forMergedIndexes(IDoubleListMerger.IConsumer consumer) {
        return this.swap ? this.forMergedIndexesInternal((lowerIndex, upperIndex, mergedIndex) ->
                consumer.merge(upperIndex, lowerIndex, mergedIndex)) : this.forMergedIndexesInternal(consumer);
    }

    private boolean forMergedIndexesInternal(IDoubleListMerger.IConsumer consumer) {
        int i = this.lower.size() - 1;

        for (int j = 0; j < i; ++j) {
            if (!consumer.merge(j, -1, j)) {
                return false;
            }
        }

        if (!consumer.merge(i, -1, i)) {
            return false;
        } else {
            for (int k = 0; k < this.upper.size(); ++k) {
                if (!consumer.merge(i, k, i + 1 + k)) {
                    return false;
                }
            }

            return true;
        }
    }

    public double getDouble(int index) {
        return index < this.lower.size() ? this.lower.getDouble(index)
                : this.upper.getDouble(index - this.lower.size());
    }

    @Override
    public DoubleList getList() {
        return this;
    }
}
