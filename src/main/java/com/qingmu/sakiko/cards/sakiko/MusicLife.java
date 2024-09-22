package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.CardSelectorAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class MusicLife extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(MusicLife.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/MusicLife.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public MusicLife() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 1);
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDescription();
            this.exhaust = false;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new CardSelectorAction(this.magicNumber, false, card -> CardGroup.CardGroupType.HAND, SakikoEnum.CardGroupEnum.DRAW_MUSIC_PILE));
    }

}
