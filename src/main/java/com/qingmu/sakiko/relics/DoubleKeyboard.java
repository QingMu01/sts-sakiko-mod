package com.qingmu.sakiko.relics;

import basemod.BaseMod;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.utils.ModNameHelper;

public class DoubleKeyboard extends AbstractSakikoRelic {

    public static final String ID = ModNameHelper.make(DoubleKeyboard.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/relics/DoubleKeyboard.png";

    private static final RelicTier RELIC_TIER = RelicTier.BOSS;

    public DoubleKeyboard() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER);
        this.tips.add(new PowerTip(BaseMod.getKeywordTitle(SakikoConst.KEYWORD_FLOW),BaseMod.getKeywordDescription(SakikoConst.KEYWORD_FLOW)));
        this.tips.add(new PowerTip(BaseMod.getKeywordTitle(SakikoConst.KEYWORD_FEVER),BaseMod.getKeywordDescription(SakikoConst.KEYWORD_FEVER)));
        this.tips.add(new PowerTip(BaseMod.getKeywordTitle(SakikoConst.KEYWORD_OBLIVIOUS),BaseMod.getKeywordDescription(SakikoConst.KEYWORD_OBLIVIOUS)));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(ClassicPiano.ID);
    }

    @Override
    public void obtain() {
        this.instantObtain(AbstractDungeon.player, 0, true);
        this.flash();
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        SakikoConst.STANCE_CHANGE_THRESHOLD_USED -= 2;
    }

    @Override
    public void onEquip() {
        SakikoConst.STANCE_CHANGE_THRESHOLD_USED -= 2;
    }

    @Override
    public void onUnequip() {
        SakikoConst.STANCE_CHANGE_THRESHOLD_USED = SakikoConst.STANCE_CHANGE_THRESHOLD;
    }
}
