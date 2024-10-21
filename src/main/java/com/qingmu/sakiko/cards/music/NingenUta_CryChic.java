package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.NingenUtaPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class NingenUta_CryChic extends AbstractMusic {

    public static final String ID = ModNameHelper.make(NingenUta_CryChic.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/NingenUta_CryChic.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public NingenUta_CryChic() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_POWER);

        this.initMusicAttr(0, 0, 1, 0);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded){
            this.tags.add(SakikoEnum.CardTagEnum.MOONLIGHT);
        }
        super.upgrade();
    }

    @Override
    public void play() {
        this.addToTop(new ApplyPowerAction(this.m_source, this.m_source, new NingenUtaPower(this.m_source), this.magicNumber));
    }
}
