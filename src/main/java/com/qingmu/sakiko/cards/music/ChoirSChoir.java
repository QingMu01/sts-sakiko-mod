package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.optionCards.ChooseCalm;
import com.megacrit.cardcrawl.cards.optionCards.ChooseWrath;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.cards.choose.ChooseCreator;
import com.qingmu.sakiko.cards.choose.ChoosePlayer;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class ChoirSChoir extends AbstractMusic {

    public static final String ID = ModNameHelper.make(ChoirSChoir.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/ChoirSChoir.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private final ArrayList<AbstractCard> chooseCardList = new ArrayList<>(Arrays.asList(new ChooseCreator(), new ChoosePlayer(), new ChooseCalm(), new ChooseWrath()));

    public ChoirSChoir() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initMusicAttr(0, 0, 0, 0);
        this.setExhaust(true, false);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
    }


    @Override
    public void play() {
        this.addToTop(new ChooseOneAction(new ArrayList<>(this.chooseCardList)));
    }
}
