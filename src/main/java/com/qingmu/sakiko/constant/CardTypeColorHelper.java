package com.qingmu.sakiko.constant;

import com.badlogic.gdx.graphics.Color;

public enum CardTypeColorHelper {
    NORMAL(Color.SKY.cpy()),
    ATTACK(Color.RED.cpy()),
    SKILL(Color.GREEN.cpy()),
    POWER(Color.BLUE.cpy()),
    STATUS(Color.GRAY.cpy()),
    CURSE(Color.PURPLE.cpy()),
    MUSIC(Color.ORANGE.cpy());

    private final Color color;

    CardTypeColorHelper(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
