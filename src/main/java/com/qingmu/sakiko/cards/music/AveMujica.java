package com.qingmu.sakiko.cards.music;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.MemberHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class AveMujica extends AbstractMusic {

    public static final String ID = ModNameHelper.make(AveMujica.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/AveMujica.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public AveMujica() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 2);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }


    @Override
    public void play() {
        this.addToTop(new AddTemporaryHPAction(this.music_source, this.music_source
                , (this.magicNumber * MemberHelper.getBandMemberCount()) + this.extraNumber));
    }
}
