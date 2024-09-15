package com.qingmu.sakiko.relics;

import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.qingmu.sakiko.powers.MusicalNotePower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class ClassicPiano extends CustomRelic implements ClickableRelic {

    // 遗物ID
    public static final String ID = ModNameHelper.make(ClassicPiano.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "SakikoModResources/img/relics/ClassicPiano.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public ClassicPiano() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        this.flash();
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MusicalNotePower(AbstractDungeon.player, 2)));
    }

    @Override
    public void atBattleStart() {
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MusicalNotePower(AbstractDungeon.player, 0)));
    }

    // debug
    @Override
    public void onRightClick() {
        if (Settings.isDebug)
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MusicalNotePower(AbstractDungeon.player, 12)));

    }
}
