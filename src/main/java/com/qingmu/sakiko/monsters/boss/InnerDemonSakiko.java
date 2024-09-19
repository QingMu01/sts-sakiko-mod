package com.qingmu.sakiko.monsters.boss;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.qingmu.sakiko.monsters.AbstractMemberMonster;
import com.qingmu.sakiko.utils.ModNameHelper;

public class InnerDemonSakiko extends AbstractMemberMonster {

    public static final String ID = ModNameHelper.make(InnerDemonSakiko.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String NAME = monsterStrings.NAME;
    private static final String[] MOVES = monsterStrings.MOVES;
    // 怪物的图片，请自行添加
    private static final String IMG = "SakikoModResources/img/monster/sakikoBoss.png";

    private int phase = 0;

    public InnerDemonSakiko(float x, float y) {
        super(NAME, ID, IMG, x, y);
    }

    @Override
    public void usePreBattleAction() {

    }

    @Override
    public void die() {
        if (this.phase == 4) {
            super.die();
        } else {
            this.setMove((byte) 0, Intent.UNKNOWN);
            this.phase++;
        }
    }

    @Override
    public void takeTurn() {

    }

    @Override
    protected void getMove(int i) {

    }
}
