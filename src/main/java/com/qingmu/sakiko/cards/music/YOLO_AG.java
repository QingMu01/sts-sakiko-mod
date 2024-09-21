package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class YOLO_AG extends AbstractMusic {

    public static final String ID = ModNameHelper.make(YOLO_AG.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/YOLO_AG.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public YOLO_AG() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 3);

        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_POWER);
        this.tags.add(SakikoEnum.CardTagEnum.COUNTER);
        this.tags.add(SakikoEnum.CardTagEnum.ENCORE);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(3);
        }
    }

    @Override
    public void applyAmount() {
        this.amount = Math.min(this.amount, this.magicNumber);

        this.rawDescription = DESCRIPTION + String.format(EXTENDED_DESCRIPTION[0], this.amount);
        this.initializeDescription();
    }


    @Override
    public void triggerInBufferPlayedMusic(AbstractMusic music) {
        this.amount++;
    }

    @Override
    public void play() {
        this.addToTop(new ApplyPowerAction(this.music_source, this.music_source
                , new BufferPower(this.music_source, this.amount)));
    }
}
