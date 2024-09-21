package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.MemberHelper;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

public class KizunaMusic_PPP extends AbstractMusic {

    public static final String ID = ModNameHelper.make(KizunaMusic_PPP.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/KizunaMusic_PPP.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public KizunaMusic_PPP() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.baseBlock = 4;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(2);
        }
    }

    @Override
    protected void applyPowersToBlock() {
        int realBaseBlock = this.baseBlock;
        this.baseBlock += PowerHelper.getPowerAmount(KirameiPower.POWER_ID);
        super.applyPowersToBlock();
        this.baseBlock = realBaseBlock;
        this.isBlockModified = (this.block != this.baseBlock);
    }

    @Override
    public void play() {
        int bandMemberCount = MemberHelper.getBandMemberCount();
        for (int i = 0; i < bandMemberCount; i++) {
            this.addToTop(new GainBlockAction(this.music_source, this.music_source, this.block,true));
        }
    }
}
