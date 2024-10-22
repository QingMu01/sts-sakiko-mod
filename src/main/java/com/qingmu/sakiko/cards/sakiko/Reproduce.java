package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.ReproduceAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Reproduce extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Reproduce.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Reproduce.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Reproduce() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 0, 2);
        this.setUpgradeAttr(1, 0, 0, 1);
        this.setExhaust(true, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ReproduceAction(p, this.magicNumber));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = EXTENDED_DESCRIPTION[0];
        return !MusicBattleFiledPatch.DrawMusicPile.drawMusicPile.get(p).isEmpty();
    }
}
