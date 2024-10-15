package com.qingmu.sakiko.relics.menbers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.qingmu.sakiko.powers.MoonsPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Rana extends AbstractBandMember {

    public static final String ID = ModNameHelper.make(Rana.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/relics/members/rana_relic.png";

    public Rana() {
        super(ID, IMG_PATH);
        PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(MoonsPower.POWER_ID);
        this.tips.add(new PowerTip(powerStrings.NAME, powerStrings.DESCRIPTIONS[0] + 1 + powerStrings.DESCRIPTIONS[1]));

    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new MoonsPower(AbstractDungeon.player,1)));
    }
}
