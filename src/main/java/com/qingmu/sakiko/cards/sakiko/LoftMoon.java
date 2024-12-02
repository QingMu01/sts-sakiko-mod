package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.common.CleanMusicQueueAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class LoftMoon extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(LoftMoon.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/LoftMoon.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public LoftMoon() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 0);
        this.setUpgradeAttr(0, 0, 0, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new CleanMusicQueueAction(p, integer -> this.addToBot(new DrawCardAction(integer))));
        if (this.upgraded){
            this.addToBot(new DrawCardAction(this.magicNumber));
        }
    }
}
