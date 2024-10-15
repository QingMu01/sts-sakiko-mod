package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.common.AutoPlayPileCardAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Expose_RAS extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Expose_RAS.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Expose_RAS.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Expose_RAS() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.ENCORE);

        this.initMusicAttr(1, 1);
        this.setExhaust(true, true);
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
            this.addToBot(new AutoPlayPileCardAction(1, true, false, AutoPlayPileCardAction.DrawPileType.DRAW_PILE));
        }
    }

    @Override
    public void play() {

    }
}
