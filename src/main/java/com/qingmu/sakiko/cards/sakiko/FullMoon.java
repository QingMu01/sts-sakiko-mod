package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ReduceCostAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.common.ExprAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.stances.FeverStance;
import com.qingmu.sakiko.stances.ObliviousStance;
import com.qingmu.sakiko.utils.ModNameHelper;

public class FullMoon extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(FullMoon.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/FullMoon.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    private int reduceCostHistory = 0;

    public FullMoon() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(4, 0, 0, 0);
        this.setUpgradeAttr(3, 0, 0, 0);
        this.setSelfRetain(true, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.costForTurn == 0 || this.cost == 0) {
            this.addToBot(new ChangeStanceAction(FeverStance.STANCE_ID));
        } else {
            this.addToBot(new ChangeStanceAction(ObliviousStance.STANCE_ID));
        }
        this.addToBot(new ExprAction(() -> {
            this.cost += reduceCostHistory;
            this.setCostForTurn(this.cost);
            this.reduceCostHistory = 0;
        }));
    }

    @Override
    public void onRetained() {
        this.addToBot(new ReduceCostAction(this));
        this.reduceCostHistory++;
    }
}
