package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.MemberHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Utopia extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Utopia.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Utopia.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private int playedCount = 0;

    public Utopia() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_ATTACK);

        this.initMusicAttr(2, 2);
    }

    @Override
    public void applyPowers() {
        this.applyPowersToMusicNumber();
        int powed = (int) Math.pow(2, Math.min(MemberHelper.getCount(), this.playedCount));
        this.baseDamage = this.musicNumber * powed;
        super.applyPowers();
        this.isDamageModified = (this.musicNumber != this.baseMusicNumber);
        this.appendDescription(powed);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.applyPowersToMusicNumber();
        this.baseDamage = (int) (this.musicNumber * Math.pow(2, Math.min(MemberHelper.getCount(), this.playedCount)));
        super.calculateCardDamage(mo);
        this.isDamageModified = (this.musicNumber != this.baseMusicNumber);
    }

    @Override
    public void play() {
        this.addToTop(new DamageAction(this.m_target, new DamageInfo(this.m_source, this.damage, this.damageType)));
        this.playedCount++;
    }
}
