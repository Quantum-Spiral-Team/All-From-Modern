package com.qsteam.afm.util.math.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;

public class SimpleDoubleMerger implements IDoubleListMerger {

    private final DoubleList list;

    public SimpleDoubleMerger(DoubleList list) {
        this.list = list;
    }

    public boolean forMergedIndexes(IDoubleListMerger.IConsumer consumer) {
        for(int i = 0; i <= this.list.size(); ++i) {
            if (!consumer.merge(i, i, i)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public DoubleList getList() {
        return this.list;
    }

}
