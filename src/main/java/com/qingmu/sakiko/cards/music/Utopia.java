package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.MemberHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Utopia extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Utopia.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Utopia.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Utopia() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_ATTACK);

        this.initMusicAttr(2, 2);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.baseDamage = (int) (this.musicNumber * Math.pow(2, MemberHelper.getBandMemberCount()));
        this.isDamageModified = MemberHelper.getBandMemberCount() > 0;
    }

    @Override
    public void play() {
        this.addToTop(new DamageAction(this.m_target, new DamageInfo(this.m_source, this.damage, this.damageType)));
    }
}
