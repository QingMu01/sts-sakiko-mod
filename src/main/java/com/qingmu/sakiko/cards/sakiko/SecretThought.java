package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.stances.CreatorStance;
import com.qingmu.sakiko.utils.ModNameHelper;

public class SecretThought extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(SecretThought.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/SecretThought.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public SecretThought() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 0, 2);
        this.setUpgradeAttr(1, 0, 0, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.stance.ID.equals(CreatorStance.STANCE_ID)) {
            this.addToBot(new GainEnergyAction(this.magicNumber));
        } else {
            this.addToBot(new ChangeStanceAction(CreatorStance.STANCE_ID));
        }
    }
}
