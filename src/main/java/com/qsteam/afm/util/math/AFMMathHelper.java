package com.qsteam.afm.util.math;

public final class AFMMathHelper {

    public static float lerp(float prev, float current, float partialTicks) {
        return prev + partialTicks * (current - prev);
    }

    public static double lerp(double prev, double current, double partialTicks) {
        return prev + partialTicks * (current - prev);
    }

}
