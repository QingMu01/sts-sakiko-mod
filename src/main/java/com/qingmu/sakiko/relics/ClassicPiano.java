package com.qingmu.sakiko.relics;

import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.powers.FeverPower;
import com.qingmu.sakiko.powers.FeverReadyPower;
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
        PowerStrings musicalNote = CardCrawlGame.languagePack.getPowerStrings(MusicalNotePower.POWER_ID);
        PowerStrings feverReady = CardCrawlGame.languagePack.getPowerStrings(FeverReadyPower.POWER_ID);
        PowerStrings fever = CardCrawlGame.languagePack.getPowerStrings(FeverPower.POWER_ID);
        this.tips.add(new PowerTip(musicalNote.NAME, musicalNote.DESCRIPTIONS[0]));
        this.tips.add(new PowerTip(feverReady.NAME, feverReady.DESCRIPTIONS[0]));
        this.tips.add(new PowerTip(fever.NAME, fever.DESCRIPTIONS[0] + 1 + fever.DESCRIPTIONS[1]));

    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!(card instanceof AbstractMusic)) {
            this.flash();
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MusicalNotePower(AbstractDungeon.player, 2)));
        }
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
