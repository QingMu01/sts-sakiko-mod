package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.qingmu.sakiko.action.KillKissAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class KillKiss extends AbstractMusic {

    public static final String ID = ModNameHelper.make(KillKiss.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Angles.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public KillKiss() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_ATTACK);

        this.initMusicAttr(4, 2, 1, 1);
    }

    @Override
    public void play() {
        this.submitActionsToTop(
                new DamageAction(this.m_target, new DamageInfo(this.m_source, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL),
                new KillKissAction(this)
        );
    }
}
