package com.qsteam.afm.util.math.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;

interface IDoubleListMerger {

    DoubleList getList();

    boolean forMergedIndexes(IConsumer var1);

    interface IConsumer {
        boolean merge(int var1, int var2, int var3);
    }

}
