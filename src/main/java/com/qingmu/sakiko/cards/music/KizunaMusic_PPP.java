package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.MemberHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class KizunaMusic_PPP extends AbstractMusic {

    public static final String ID = ModNameHelper.make(KizunaMusic_PPP.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/KizunaMusic_PPP.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public KizunaMusic_PPP() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initMusicAttr(2, 1, 2, 1);
    }

    @Override
    public void applyPowers() {
        this.applyPowersToMusicNumber();
        this.baseBlock = this.musicNumber;

        int realBaseMagicNumber = this.baseMagicNumber;
        this.baseMagicNumber += MemberHelper.getCount();
        super.applyPowers();
        this.magicNumber = this.baseMagicNumber;
        this.baseMagicNumber = realBaseMagicNumber;

        this.isBlockModified = (this.magicNumber != this.baseMagicNumber);
        this.isMagicNumberModified = (this.magicNumber != this.baseMagicNumber);
    }

    @Override
    public void play() {
        AbstractGameAction[] actions = new AbstractGameAction[this.magicNumber];
        for (int i = 0; i < actions.length; i++) {
            actions[i] = new GainBlockAction(this.m_source, this.m_source, this.block,true);
        }
        this.submitActionsToTop(actions);
    }
}
