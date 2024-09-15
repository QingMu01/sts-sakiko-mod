package com.qingmu.sakiko.relics.menbers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Uika extends AbstractBandMember {

    public static final String ID = ModNameHelper.make(Uika.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/relics/members/uika_relic.png";

    public Uika() {
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
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new KirameiPower(AbstractDungeon.player,2)));
    }

    @Override
    public void removePower() {
        this.addToBot(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, KirameiPower.POWER_ID, 2));
    }

}
