package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.MusicDreamPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class MusicDream extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(MusicDream.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/power.png";

    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public MusicDream() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(2, 0, 0, 4);
        this.tags.add(SakikoEnum.CardTagEnum.MOONLIGHT);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(2);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new MusicDreamPower(p, this.magicNumber)));
    }
}
