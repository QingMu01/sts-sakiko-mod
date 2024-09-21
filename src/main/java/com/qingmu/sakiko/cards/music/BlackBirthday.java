package com.qingmu.sakiko.cards.music;

import com.qingmu.sakiko.action.BlackBirthdayAction;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class BlackBirthday extends AbstractMusic {

    public static final String ID = ModNameHelper.make(BlackBirthday.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/BlackBirthday.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public BlackBirthday() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 0);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDescription();
        }
    }

    @Override
    public void play() {
        this.addToBot(new BlackBirthdayAction(this.upgraded));
    }

}
