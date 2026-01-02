package com.qsteam.afm.potion;

public class PotionSlowFalling extends PotionBase {

    public PotionSlowFalling() {
        super("slow_falling", false, 15978425, 0, 0);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

}
