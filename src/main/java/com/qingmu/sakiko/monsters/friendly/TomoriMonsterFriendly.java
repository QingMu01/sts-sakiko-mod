package com.qingmu.sakiko.monsters.friendly;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.RitualPower;
import com.qingmu.sakiko.monsters.AbstractFriendlyMonster;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.powers.MemberPower;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;

public class TomoriMonsterFriendly extends AbstractFriendlyMonster {

    public static final String ID = ModNameHelper.make(TomoriMonsterFriendly.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;

    private static final String IMG = "SakikoModResources/img/monster/tomori.png";

    public float chance = 0.2f;

    public TomoriMonsterFriendly(float x, float y) {
        super(NAME, ID, IMG, x, y);
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new MemberPower(this)));
    }

    @Override
    protected List<SpecialIntentAction> initSpecialIntent() {
        List<SpecialIntentAction> specialIntentActions = new ArrayList<>();
        // 为玩家施加一个强力BUFF
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setMoveName(MOVES[0])
                .setIntent(Intent.MAGIC)
                .setRemovable(m -> false)
                .setPredicate(m -> {
                    boolean b = AbstractDungeon.cardRandomRng.randomBoolean(chance);
                    if (b) {
                        this.chance = 0.2f;
                    } else {
                        this.chance += 0.1f;
                    }
                    return b;
                })
                .setActions(() -> {
                    ArrayList<AbstractGameAction> actions = new ArrayList<>();
                    int random = AbstractDungeon.cardRandomRng.random(99);
                    AbstractPlayer target = DungeonHelper.getPlayer();
                    if (random < 10) {
                        actions.add(new ApplyPowerAction(target, this, new KirameiPower(target, 1), 1));
                    } else if (random < 20) {
                        actions.add(new ApplyPowerAction(target, this, new IntangiblePlayerPower(target, 1), 1));
                    } else if (random < 30) {
                        actions.add(new ApplyPowerAction(target, this, new BarricadePower(target)));
                    } else {
                        actions.add(new ApplyPowerAction(target, this, new RitualPower(target, 2, true), 2));
                    }
                    return actions.toArray(new AbstractGameAction[0]);
                })
                .build());
        return specialIntentActions;
    }
}

