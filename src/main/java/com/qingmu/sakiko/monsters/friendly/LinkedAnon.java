package com.qingmu.sakiko.monsters.friendly;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.qingmu.sakiko.monsters.AbstractSakikoMonster;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;

public class LinkedAnon extends AbstractSakikoMonster {

    public static final String ID = ModNameHelper.make(LinkedAnon.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;

    protected int baseAttack = 8, baseSlash = 12, baseMulti = 6, multiCount = 10;
    private static final String IMG = "SakikoModResources/img/monster/anon.png";

    public LinkedAnon(float x, float y) {
        super(NAME, ID, IMG, x, y);
        this.halfDead = true;
        this.setHp(DungeonHelper.getPlayer().maxHealth);

    }

    @Override
    public void usePreBattleAction() {
    }


    @Override
    protected List<IntentAction> initIntent() {
        ArrayList<IntentAction> intentActions = new ArrayList<>();
        return intentActions;
    }

    @Override
    protected List<SpecialIntentAction> initSpecialIntent() {
        ArrayList<SpecialIntentAction> specialIntentActions = new ArrayList<>();
        return specialIntentActions;
    }
}
