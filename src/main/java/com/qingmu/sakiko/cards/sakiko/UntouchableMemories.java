package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.common.ObliviousAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class UntouchableMemories extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(UntouchableMemories.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/UntouchableMemories.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public UntouchableMemories() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.OBLIVIOUS);

        this.initBaseAttr(1, 8, 0, 1);
        this.setUpgradeAttr(1, 2, 0, 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.submitActionsToBot(
                new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)),
                new ObliviousAction(this.magicNumber)
        );
    }
}
