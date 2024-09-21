package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.MusicalNotePower;
import com.qingmu.sakiko.utils.MemberHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class AveMusica extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(AveMusica.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/AveMusica.png";


    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public AveMusica() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 4);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.upgraded) {
            this.addToBot(new ApplyPowerAction(p, p, new MusicalNotePower(p, this.magicNumber + (2 * MemberHelper.getBandMemberCount()))));
        } else {
            this.addToBot(new ApplyPowerAction(p, p, new MusicalNotePower(p, this.magicNumber)));
        }
    }
}
