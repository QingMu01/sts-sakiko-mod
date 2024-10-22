package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.utils.ModNameHelper;

public class LoftMoon extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(LoftMoon.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/skill.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public LoftMoon() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 0, 1);
        this.setUpgradeAttr(1, 0, 0, 1);
    }

    @Override
    public void applyPowers() {
        int realBaseMagicNumber = this.baseMagicNumber;
        this.baseMagicNumber += MusicBattleFiledPatch.MusicQueue.musicQueue.get(AbstractDungeon.player).size();
        super.applyPowers();
        this.magicNumber = this.baseMagicNumber;
        this.baseMagicNumber = realBaseMagicNumber;
        this.isMagicNumberModified = (this.magicNumber != this.baseMagicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DrawCardAction(this.magicNumber));
    }
}
