package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.powers.watcher.EndTurnDeathPower;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class KamisamaBaka extends AbstractMusic {

    public static final String ID = ModNameHelper.make(KamisamaBaka.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/KamisamaBaka.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public KamisamaBaka() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);

        this.initMusicAttr(0, 0);
        this.setExhaust(true, false);
    }


    @Override
    public void play() {
        this.addToTop(new ApplyPowerAction(this.m_source, this.m_source, new EndTurnDeathPower(this.m_source)));
        this.addToTop(new ChangeStanceAction("Divinity"));
    }
}
