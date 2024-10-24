package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class LastStage extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(LastStage.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/LastStage.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public LastStage() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 10, 0, 4);
        this.setUpgradeAttr(1, 0, 0, 3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new CardSelectorAction("", 999, true, card -> CardGroup.CardGroupType.EXHAUST_PILE, cardList -> {
            this.baseDamage += cardList.size() * this.magicNumber;
            this.calculateCardDamage(m);
            this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }, SakikoEnum.CardGroupEnum.DRAW_MUSIC_PILE));

    }

}
