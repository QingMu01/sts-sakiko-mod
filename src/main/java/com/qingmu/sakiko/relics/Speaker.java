package com.qingmu.sakiko.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.characters.TogawaSakiko;
import com.qingmu.sakiko.powers.MusicalNotePower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Speaker extends AbstractSakikoRelic {

    // 遗物ID
    public static final String ID = ModNameHelper.make(Speaker.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "SakikoModResources/img/relics/Speaker.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public Speaker() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        this.counter = 1;
        this.amount = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        if (this.counter > 0) {
            this.counter--;
            this.flash();
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MusicalNotePower(AbstractDungeon.player, 18)));
        }
    }

    @Override
    public void triggerOnPlayMusicCard(AbstractMusic music) {
        this.amount++;
        if (amount >= 20) {
            this.counter++;
            if (this.counter > 1) {
                this.counter = 1;
            }
            this.amount = 0;
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player instanceof TogawaSakiko;
    }
}
