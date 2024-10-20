package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.MemberHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Mujina extends AbstractSakikoCard {
    public static final String ID = ModNameHelper.make(Mujina.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Mujina.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Mujina() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(2, 12, 0, 3);
        this.setUpgradeAttr(2, 4, 0, 1);
    }

    @Override
    protected void applyPowersToBlock() {
        this.baseBlock = MemberHelper.getBandMemberCount() * this.magicNumber;
        super.applyPowersToBlock();
        this.isBlockModified = (this.block != this.baseBlock);
        this.appendDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.submitActionsToBot(
                new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL),
                new GainBlockAction(p, this.block)
        );
    }

}
