package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class A2Z_PP extends AbstractMusic {

    public static final String ID = ModNameHelper.make(A2Z_PP.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/A2Z_PP.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public A2Z_PP() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.ENCORE);
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_ATTACK);
        this.initMusicAttr(6, 2, 2, 1);
    }

    @Override
    public void triggerInBufferPlayedMusic(AbstractMusic music) {
        this.amount++;
    }

    @Override
    public void applyPowers() {
        int realBaseMusicNumber = this.baseMusicNumber;
        this.baseMusicNumber += this.amount * this.magicNumber;
        super.applyPowers();
        this.baseMusicNumber = realBaseMusicNumber;
        this.isModifiedMusicNumber = (this.musicNumber != this.baseMusicNumber);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBaseMusicNumber = this.baseMusicNumber;
        this.baseMusicNumber += this.amount * this.magicNumber;
        super.calculateCardDamage(mo);
        this.baseMusicNumber = realBaseMusicNumber;
        this.isModifiedMusicNumber = (this.musicNumber != this.baseMusicNumber);
    }

    @Override
    public void play() {
        this.addToTop(new DamageAllEnemiesAction(this.m_source, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }
}
