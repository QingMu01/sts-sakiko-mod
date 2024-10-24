package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.MeteorMoonPower;
import com.qingmu.sakiko.stances.ObliviousStance;
import com.qingmu.sakiko.utils.ModNameHelper;

public class MeteorMoon extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(MeteorMoon.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/MeteorMoon.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public MeteorMoon() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);

        this.initBaseAttr(3, 0, 0, 0);
        this.setUpgradeAttr(2, 0, 0, 0);

        this.setExhaust(true, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.submitActionsToBot(
                new ChangeStanceAction(ObliviousStance.STANCE_ID),
                new ApplyPowerAction(p, p, new MeteorMoonPower(p))
        );
    }
}
