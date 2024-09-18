package com.qingmu.sakiko.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.ui.MusicSlotItem;

public abstract class AbstractMemberMonster extends CustomMonster {

    protected MusicSlotItem musicSlotItem = new MusicSlotItem(this);

    protected boolean canPlayMusic = false;

    protected int baseHp = 70, baseAttack = 8, baseSlash = 14, baseMulti = 5, multiCount = 2, baseBlock = 7, powerful = 1;


    public AbstractMemberMonster(String name, String id, String img, float x, float y) {
        super(name, id, 100, 0.0F, 0.0F, 200.0F, 220.0F, img, x, y);
    }

    protected void obtainMusic() {
    }


    @Override
    public void update() {
        super.update();
        if (this.canPlayMusic) {
            CardGroup cardGroup = MusicBattleFiledPatch.MusicQueue.musicQueue.get(this);
            if (cardGroup.isEmpty()) {
                this.musicSlotItem.removeMusic();
            } else {
                this.musicSlotItem.setMusic((AbstractMusic) cardGroup.getTopCard());
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (this.canPlayMusic) {
            musicSlotItem.render(sb, this.hb.cX, this.hb.cY + 300.0f);
        }
    }

    protected void setDefaultAttribute() {
        // 进阶3 强化伤害
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.baseAttack += 2; // 10
            this.baseSlash += 2; // 16
            this.baseMulti += 1; // 6*2
        }
        // 进阶8 强化生命
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.baseHp += 30;
            this.baseBlock += 3; // 10
        }
        // 进阶18 强化行动
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.baseAttack += 2; // 12
            this.baseSlash += 2; // 18
            this.multiCount++; // 6*3
            this.baseBlock += 4; //14
            this.powerful++; // 2
        }

        // act1 基本属性
        if (AbstractDungeon.id.equals(Exordium.ID)) {
            this.setHp(baseHp - 5, baseHp + 5);
        }
        // act2 基本属性
        if (AbstractDungeon.id.equals(TheCity.ID)) {
            this.baseAttack += 2; // 14
            this.baseHp += 40;
            this.baseSlash += 3; // 21
            this.baseMulti += 1; // 7*3
            this.setHp(baseHp - 10, baseHp + 10);
        }
        // act3 基本属性
        if (AbstractDungeon.id.equals(TheBeyond.ID)) {
            this.baseAttack += 4; // 16
            this.baseHp += 80;
            this.baseSlash += 6; // 24
            this.baseMulti += 1; // 8*3
            this.setHp(baseHp - 10, baseHp + 10);
        }
        this.baseAttack = AbstractDungeon.monsterHpRng.random(this.baseAttack - 1, this.baseAttack + 1);
        this.baseSlash = AbstractDungeon.monsterHpRng.random(this.baseSlash - 2, this.baseSlash + 2);
        this.baseBlock = AbstractDungeon.monsterHpRng.random(this.baseBlock, this.baseBlock + 2);
    }
}