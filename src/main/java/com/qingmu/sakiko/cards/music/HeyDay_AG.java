package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

public class HeyDay_AG extends AbstractMusic {

    public static final String ID = ModNameHelper.make(HeyDay_AG.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/HeyDay_AG.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public HeyDay_AG() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, RARITY, TARGET);
        this.baseMagicNumber = 2;
        this.exhaust = true;
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
        this.addToTop(new ApplyPowerAction(this.music_target, this.music_source
                , new StrengthPower(this.music_target, -(Math.max(this.magicNumber, this.baseMagicNumber)))));
        int powerAmount = PowerHelper.getPowerAmount(KirameiPower.POWER_ID);
        if (powerAmount > 0){
            if (!this.music_target.hasPower("Artifact")) {
                this.addToTop(new ApplyPowerAction(this.music_target, this.music_source, new GainStrengthPower(this.music_target, powerAmount), powerAmount, true, AbstractGameAction.AttackEffect.NONE));
            }
            this.addToTop(new ApplyPowerAction(this.music_target, this.music_source, new StrengthPower(this.music_target,-powerAmount)));
        }
    }
}
