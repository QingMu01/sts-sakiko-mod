package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class SilhouetteDance_MYGO extends AbstractMusic {

    public static final String ID = ModNameHelper.make(SilhouetteDance_MYGO.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/SilhouetteDance_MYGO.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public SilhouetteDance_MYGO() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initBaseAttr(0, 4, 0, 0);

        this.tags.add(SakikoEnum.CardTagEnum.COUNTER);
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_ATTACK);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2);
        }
    }

    @Override
    public void triggerInBufferUsedCard(AbstractCard card) {
        this.amount++;
    }

    @Override
    public void applyAmount() {
        this.appendDescription(this.amount);
    }


    @Override
    public void play() {
        for (int i = 0; i < this.amount; i++) {
            this.addToTop(new DamageRandomEnemyAction(new DamageInfo(this.music_source, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
    }
}
