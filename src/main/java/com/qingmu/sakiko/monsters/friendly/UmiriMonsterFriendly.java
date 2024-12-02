package com.qingmu.sakiko.monsters.friendly;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.qingmu.sakiko.action.common.DrawMusicAction;
import com.qingmu.sakiko.monsters.AbstractFriendlyMonster;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.powers.MemberPower;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;

public class UmiriMonsterFriendly extends AbstractFriendlyMonster {

    public static final String ID = ModNameHelper.make(UmiriMonsterFriendly.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;

    private static final String IMG = "SakikoModResources/img/monster/umiri.png";

    public UmiriMonsterFriendly(float x, float y) {
        super(NAME, ID, IMG, x, y);
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new MemberPower(this)));
    }

    @Override
    protected List<SpecialIntentAction> initSpecialIntent() {
        List<SpecialIntentAction> specialIntentActions = new ArrayList<>();
        // 首次行动让玩家从歌单和抽牌堆各抽2
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setMoveName(MOVES[0])
                .setIntent(Intent.MAGIC)
                .setActions(() -> new AbstractGameAction[]{
                        new DrawCardAction(2),
                        new DrawMusicAction(2)
                })
                .build());
        return specialIntentActions;
    }
}
