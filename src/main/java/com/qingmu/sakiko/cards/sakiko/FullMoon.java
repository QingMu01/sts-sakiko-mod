package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.stances.FeverStance;
import com.qingmu.sakiko.stances.ObliviousStance;
import com.qingmu.sakiko.utils.ModNameHelper;

public class FullMoon extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(FullMoon.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/skill.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public FullMoon() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 4);
        this.setUpgradeAttr(0, 0, 0, 0);
        this.setExhaust(true, true);
        this.setSelfRetain(false, true);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.appendDescription(GameActionManager.turn);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (GameActionManager.turn >= this.magicNumber) {
            this.addToBot(new ChangeStanceAction(ObliviousStance.STANCE_ID));
        } else {
            this.addToBot(new ChangeStanceAction(FeverStance.STANCE_ID));
        }
    }
}
