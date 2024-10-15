package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class StarBeat_PPP extends AbstractMusic {

    public static final String ID = ModNameHelper.make(StarBeat_PPP.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/StarBeat_PPP.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public StarBeat_PPP() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initMusicAttr(1, 1);

        this.tags.add(SakikoEnum.CardTagEnum.ENCORE);
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
            AbstractCard copy = music.makeSameInstanceOf();
            copy.retain = true;
            this.addToTop(new MakeTempCardInHandAction(copy, 1));
        }
    }

    @Override
    public void play() {
    }
}
