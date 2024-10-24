package com.qingmu.sakiko.monsters;

import com.megacrit.cardcrawl.dungeons.*;

public abstract class AbstractMemberMonster extends AbstractSakikoMonster {

    protected int baseHp = 80, baseAttack = 8, baseSlash = 12, baseMulti = 6, multiCount = 2, baseBlock = 7, powerful = 2;

    public AbstractMemberMonster(String name, String id, String img, float x, float y) {
        super(name, id, img, x, y);
        this.type = EnemyType.NORMAL;
    }

    protected void setDefaultAttribute() {
        // act1 基本属性
        if (AbstractDungeon.id.equals(Exordium.ID)) {
            this.setHp(baseHp - 10, baseHp + 10);
        }
        // act2 基本属性
        if (AbstractDungeon.id.equals(TheCity.ID)) {
            this.baseAttack += 4; // 12
            this.baseHp += 60; // 140
            this.baseSlash += 10; // 22
            this.multiCount += 1; // 6*3
            this.baseBlock += 5; // 12
            this.setHp(baseHp - 15, baseHp + 15);
        }
        // act3 基本属性
        if (AbstractDungeon.id.equals(TheBeyond.ID) || AbstractDungeon.id.equals(TheEnding.ID)) {
            this.baseAttack += 4; // 16
            this.baseHp += 80; // 220
            this.baseSlash += 12; // 34
            this.multiCount += 1; // 6*4
            this.baseBlock += 6; // 18
            this.powerful++;
            this.setHp(baseHp - 20, baseHp + 20);
        }

        this.baseAttack = AbstractDungeon.monsterHpRng.random(this.baseAttack - 1, this.baseAttack + 1);
        this.baseSlash = AbstractDungeon.monsterHpRng.random(this.baseSlash - 2, this.baseSlash + 2);
        this.baseBlock = AbstractDungeon.monsterHpRng.random(this.baseBlock, this.baseBlock + 3);
    }

}
