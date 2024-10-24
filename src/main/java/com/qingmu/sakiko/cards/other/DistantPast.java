package com.qingmu.sakiko.cards.other;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.inteface.SakikoModEnable;
import com.qingmu.sakiko.monsters.boss.InnerDemonSakiko;
import com.qingmu.sakiko.powers.monster.FakeKirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;

@SakikoModEnable(enable = false)
public class DistantPast extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(DistantPast.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/other/DistantPast.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public DistantPast() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(-2, 0, 0, 2);
        this.setUpgradeAttr(-2, 0, 0, 0);

        this.setEthereal(true, true);
    }

    @Override
    public void triggerWhenDrawn() {
        this.addToBot(new DrawCardAction(1));
        AbstractMonster monster = AbstractDungeon.getCurrRoom().monsters.getMonster(InnerDemonSakiko.ID);
        if (monster != null) {
            this.addToBot(new ApplyPowerAction(monster, AbstractDungeon.player, new FakeKirameiPower(monster, this.magicNumber), this.magicNumber));
        }
    }

    @Override
    public void triggerOnExhaust() {
        int count = 0;
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (card.cardID.equals(ID)) {
                count++;
            }
        }
        if (count < 6) {
            this.addToBot(new MakeTempCardInDiscardAction(this, 2));
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }
}
