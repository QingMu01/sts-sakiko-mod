package com.qingmu.sakiko.utils;

import basemod.abstracts.CustomSavable;

public class InvasionChangeSaved implements CustomSavable<Float> {
    public float chance = 0;

    @Override
    public Float onSave() {
        return this.chance;
    }

    @Override
    public void onLoad(Float integer) {
        this.chance = integer;
    }
}
