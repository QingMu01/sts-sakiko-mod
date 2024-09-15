package com.qingmu.sakiko.constant;

import com.badlogic.gdx.graphics.Color;

public enum CardTypeColorHelper {
    NORMAL(Color.SKY),
    ATTACK(Color.RED),
    SKILL(Color.GREEN),
    POWER(Color.BLUE),
    STATUS(Color.GRAY),
    CURSE(Color.PURPLE),
    MUSIC(Color.ORANGE);

    private final Color color;

    CardTypeColorHelper(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
