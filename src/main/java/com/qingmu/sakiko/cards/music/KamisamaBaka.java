package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.powers.watcher.MantraPower;
import com.megacrit.cardcrawl.stances.DivinityStance;
import com.qingmu.sakiko.action.GodPenaltyAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class KamisamaBaka extends AbstractMusic {

    public static final String ID = ModNameHelper.make(KamisamaBaka.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/KamisamaBaka.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public KamisamaBaka() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);

        this.initMusicAttr(3, 1, 1, 0);
        this.setExhaust(true, false);
    }


    @Override
    public void play() {
        this.addToTop(new GodPenaltyAction(this.m_source, this.magicNumber));
        this.addToTop(new ChangeStanceAction(new DivinityStance()));
    }

    @Override
    public void interruptReady() {
        this.addToBot(new ApplyPowerAction(this.m_source, this.m_source, new MantraPower(this.m_source, this.musicNumber), this.musicNumber));
    }
}
