package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

public class HeyDay_AG extends AbstractMusic {

    public static final String ID = ModNameHelper.make(HeyDay_AG.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/HeyDay_AG.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public HeyDay_AG() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 2);

        this.exhaust = true;
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
        this.addToTop(new ApplyPowerAction(this.music_target, this.music_source
                , new StrengthPower(this.music_target, -(this.magicNumber))));
        int powerAmount = PowerHelper.getPowerAmount(KirameiPower.POWER_ID);
        if (powerAmount > 0 && this.upgraded){
            if (!this.music_target.hasPower("Artifact")) {
                this.addToTop(new ApplyPowerAction(this.music_target, this.music_source, new GainStrengthPower(this.music_target, powerAmount), powerAmount, true, AbstractGameAction.AttackEffect.NONE));
            }
            this.addToTop(new ApplyPowerAction(this.music_target, this.music_source, new StrengthPower(this.music_target,-powerAmount)));
        }
    }
}
