package com.qingmu.sakiko.cards.sakiko;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.inteface.TriggerOnOblivion;
import com.qingmu.sakiko.modifier.ObliviousModifier;
import com.qingmu.sakiko.utils.ModNameHelper;

public class CreateOfWorld extends AbstractSakikoCard implements TriggerOnOblivion {
    public static final String ID = ModNameHelper.make(CreateOfWorld.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/CreateOfWorld.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public CreateOfWorld() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(2, 0, 13, 1);
        this.setUpgradeAttr(2, 0, 0, 1);
    }

    @Override
    public void triggerOnPlayMusic(AbstractMusic music) {
        this.baseBlock += this.magicNumber;
    }

    @Override
    public void triggerOnOblivion() {
        AbstractCard copy = this.makeSameInstanceOf();
        CardModifierManager.removeModifiersById(copy, ObliviousModifier.ID, false);
        this.addToBot(new MakeTempCardInDiscardAction(copy, 1));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, p, this.block));
    }

}
