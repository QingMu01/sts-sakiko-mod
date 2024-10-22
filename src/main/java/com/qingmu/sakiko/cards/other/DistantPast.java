package com.qingmu.sakiko.cards.other;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.monster.DistantPastAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.inteface.SakikoModEnable;
import com.qingmu.sakiko.utils.ModNameHelper;

@SakikoModEnable(enable = false)
public class DistantPast extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(DistantPast.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/skill.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public DistantPast() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(-2, 0, 0, 1);
        this.setUpgradeAttr(-2, 0, 0, 0);
    }

    @Override
    public void triggerWhenDrawn() {
        this.addToBot(new DrawCardAction(1));
        this.addToBot(new DistantPastAction(this));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }
}
