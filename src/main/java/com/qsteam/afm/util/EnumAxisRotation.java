package com.qsteam.afm.util;

import net.minecraft.util.EnumFacing.Axis;

public enum EnumAxisRotation {

    NONE {
        @Override
        public int getCoordinate(int x, int y, int z, Axis axis) {
            switch (axis) {
                case X:
                    return x;
                case Y:
                    return y;
                case Z:
                    return z;
                default:
                    return 0;
            }
        }

        @Override
        public Axis rotate(Axis axis) {
            return axis;
        }

        @Override
        public EnumAxisRotation reverse() {
            return this;
        }
    },
    FORWARD {
        @Override
        public int getCoordinate(int x, int y, int z, Axis axis) {
            switch (axis) {
                case X:
                    return z;
                case Y:
                    return x;
                case Z:
                    return y;
                default:
                    return 0;
            }
        }

        @Override
        public Axis rotate(Axis axis) {
            return AXES[Math.floorMod(axis.ordinal() + 1, 3)];
        }

        @Override
        public EnumAxisRotation reverse() {
            return BACKWARD;
        }
    },
    BACKWARD {
        @Override
        public int getCoordinate(int x, int y, int z, Axis axis) {
            switch (axis) {
                case X:
                    return y;
                case Y:
                    return z;
                case Z:
                    return x;
                default:
                    return 0;
            }
        }

        @Override
        public Axis rotate(Axis axis) {
            return AXES[Math.floorMod(axis.ordinal() - 1, 3)];
        }

        @Override
        public EnumAxisRotation reverse() {
            return FORWARD;
        }
    };

    public static final Axis[] AXES = Axis.values();
    public static final EnumAxisRotation[] AXIS_ROTATIONS = values();

    EnumAxisRotation() {
    }

    public abstract int getCoordinate(int x, int y, int z, Axis axis);

    public abstract Axis rotate(Axis axis);

    public abstract EnumAxisRotation reverse();

    public static EnumAxisRotation from(Axis fromAxis, Axis toAxis) {
        return AXIS_ROTATIONS[Math.floorMod(toAxis.ordinal() - fromAxis.ordinal(), 3)];
    }

}
