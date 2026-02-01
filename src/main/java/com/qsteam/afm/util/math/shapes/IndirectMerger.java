package com.qsteam.afm.util.math.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntArrayList;

public final class IndirectMerger implements IDoubleListMerger {
    private final DoubleArrayList values;
    private final IntArrayList firstIndices;
    private final IntArrayList secondIndices;

    public IndirectMerger(DoubleList list1, DoubleList list2, boolean includeFirst, boolean includeSecond) {
        int i = 0, j = 0;
        int size1 = list1.size(), size2 = list2.size();
        int totalSize = size1 + size2;

        this.values = new DoubleArrayList(totalSize);
        this.firstIndices = new IntArrayList(totalSize);
        this.secondIndices = new IntArrayList(totalSize);

        while (i < size1 || j < size2) {
            boolean hasNext1 = i < size1;
            boolean hasNext2 = j < size2;

            boolean selectFirst = hasNext1 && (!hasNext2 || list1.getDouble(i) < list2.getDouble(j) + 1.0E-7);
            double current = selectFirst ? list1.getDouble(i++) : list2.getDouble(j++);

            if ((i > 0 && hasNext1 || includeSecond) && (j > 0 && hasNext2 || includeFirst)) {
                this.push(i - 1, j - 1, current);
            }
        }

        if (this.values.isEmpty()) {
            this.values.add(Math.min(list1.getDouble(size1 - 1), list2.getDouble(size2 - 1)));
        }
    }

    private void push(int i, int j, double value) {
        if (this.values.isEmpty() || value > this.values.getDouble(this.values.size() - 1) + 1.0E-7) {
            this.firstIndices.add(i);
            this.secondIndices.add(j);
            this.values.add(value);
        } else {
            int last = this.values.size() - 1;
            this.firstIndices.set(last, i);
            this.secondIndices.set(last, j);
        }
    }

    @Override
    public boolean forMergedIndexes(IDoubleListMerger.IConsumer consumer) {
        for (int k = 0; k < this.values.size() - 1; ++k) {
            if (!consumer.merge(this.firstIndices.getInt(k), this.secondIndices.getInt(k), k)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public DoubleList getList() {
        return this.values;
    }

}
