package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class YOLO_AG extends AbstractMusic {

    public static final String ID = ModNameHelper.make(YOLO_AG.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/YOLO_AG.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public YOLO_AG() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.ENCORE);

        this.initMusicAttr(1, 1, 1, 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        this.amount = this.musicNumber;
    }

    @Override
    public void triggerInBufferPlayedMusic(AbstractMusic music) {
        if (this.amount > 0) {
            this.amount--;
            this.addToTop(new GainEnergyAction(this.magicNumber));
        }
    }

    @Override
    public void play() {
    }
}
