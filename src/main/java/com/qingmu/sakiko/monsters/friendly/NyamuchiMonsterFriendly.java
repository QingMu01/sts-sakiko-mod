package com.qingmu.sakiko.monsters.friendly;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.action.RandomRemoveDebuffAction;
import com.qingmu.sakiko.monsters.AbstractFriendlyMonster;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.powers.MemberPower;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;

public class NyamuchiMonsterFriendly extends AbstractFriendlyMonster {

    public static final String ID = ModNameHelper.make(NyamuchiMonsterFriendly.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;

    private static final String IMG = "SakikoModResources/img/monster/nyamuchi.png";

    public NyamuchiMonsterFriendly(float x, float y) {
        super(NAME, ID, IMG, x, y);
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new MemberPower(this)));
    }

    @Override
    protected List<SpecialIntentAction> initSpecialIntent() {
        List<SpecialIntentAction> specialIntentActions = new ArrayList<>();
        // 随机清DEBUFF
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setMoveName(MOVES[0])
                .setPredicate(m -> {
                    boolean hasDeBUFF = false;
                    for (AbstractPower power : DungeonHelper.getPlayer().powers) {
                        if (power.type.equals(AbstractPower.PowerType.DEBUFF)) {
                            hasDeBUFF = true;
                            break;
                        }
                    }
                    return hasDeBUFF && AbstractDungeon.cardRandomRng.randomBoolean();
                })
                .setRemovable(m -> false)
                .setIntent(Intent.MAGIC)
                .setActions(() -> new AbstractGameAction[]{
                        new RandomRemoveDebuffAction(DungeonHelper.getPlayer(), 1)
                })
                .build());
        return specialIntentActions;
    }
}

