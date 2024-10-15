package com.qingmu.sakiko.relics.menbers;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.qingmu.sakiko.powers.FukkenPower;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;

@AutoAdd.Seen
public class Umiri extends AbstractBandMember {

    public static final String ID = ModNameHelper.make(Umiri.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/relics/members/umiri_relic.png";

    public Umiri() {
        super(ID, IMG_PATH);
        PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(KirameiPower.POWER_ID);
        this.tips.add(new PowerTip(powerStrings.NAME, powerStrings.DESCRIPTIONS[0]));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FukkenPower(AbstractDungeon.player, 1),1));
    }
}
