package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.OjyousamaPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Ojyousama extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Ojyousama.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Ojyousama.png";

    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Ojyousama() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(2, 0, 0, 1);
        this.tags.add(SakikoEnum.CardTagEnum.MOONLIGHT);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new OjyousamaPower(p, this.magicNumber)));

    }
}
