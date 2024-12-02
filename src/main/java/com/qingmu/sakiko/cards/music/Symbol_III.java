package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.SymbolWaterPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Symbol_III extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Symbol_III.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Symbol_III.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Symbol_III() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(CardTags.HEALING);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);

        this.initMusicAttr(6, 2, 1, 0);

        this.setExhaust(true, true);
    }

    @Override
    public void play() {
        this.addToTop(new HealAction(this.m_source, this.m_source, this.musicNumber));
    }

    @Override
    public void interruptReady() {
        this.addToBot(new ApplyPowerAction(this.m_source, this.m_source, new SymbolWaterPower(this.m_source, this.magicNumber), this.magicNumber));
    }
}
