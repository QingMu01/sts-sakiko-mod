package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.common.AutoPlayPileCardAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Kimewaza extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Kimewaza.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Kimewaza.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Kimewaza() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(4, 0, 0, 0);
        this.setUpgradeAttr(3, 0, 0, 0);
        this.setExhaust(true, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new AutoPlayPileCardAction(999,false,true, AutoPlayPileCardAction.DrawPileType.MUSIC_PILE));
    }
}
