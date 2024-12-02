package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class HeyDay_AG extends AbstractMusic {

    public static final String ID = ModNameHelper.make(HeyDay_AG.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/HeyDay_AG.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public HeyDay_AG() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initMusicAttr(2, 2);
        this.setExhaust(true, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        this.amount = this.musicNumber;
    }

    @Override
    public void play() {
    }

    @Override
    public void triggerInBufferPlayedMusic(AbstractMusic music) {
        if (this.amount > 0) {
            this.amount--;
            AbstractMonster target = AbstractDungeon.getRandomMonster();
            this.addToTop(new ApplyPowerAction(this.m_source, target, new StrengthPower(target, -1), -1));
        }
    }
}
