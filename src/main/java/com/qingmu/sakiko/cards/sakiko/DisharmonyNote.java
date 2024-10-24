package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.DisharmonyNoteAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class DisharmonyNote extends AbstractSakikoCard {
    public static final String ID = ModNameHelper.make(DisharmonyNote.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/DisharmonyNote.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public DisharmonyNote() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 0);
        this.setUpgradeAttr(0, 0, 0, 0);
        this.setExhaust(true, false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DisharmonyNoteAction(p));
    }

}
