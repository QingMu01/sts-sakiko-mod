package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Mas_uerade extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Mas_uerade.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Mas_uerade.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public Mas_uerade() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_ATTACK);

        this.initMusicAttr(5, 3);
        this.isMultiDamage = true;
    }

    @Override
    public void applyPowers() {
        this.applyPowersToMusicNumber();
        this.baseDamage = this.musicNumber;
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.applyPowersToMusicNumber();
        this.baseDamage = this.musicNumber;
        super.calculateCardDamage(mo);
    }

    @Override
    public void play() {
        this.addToTop(new DamageAllEnemiesAction(this.m_source, this.multiDamage, DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.FIRE));
    }
}
