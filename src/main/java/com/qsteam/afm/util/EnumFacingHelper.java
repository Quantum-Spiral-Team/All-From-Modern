package com.qsteam.afm.util;

import net.minecraft.util.EnumFacing.Axis;

public final class EnumFacingHelper {

    public static final class AxisHelper {

        public static int getCoordinate(Axis axis, int x, int y, int z) {
            if (axis == Axis.X) {
                return x;
            } else {
                return axis == Axis.Y ? y : z;
            }
        }

    }

}
