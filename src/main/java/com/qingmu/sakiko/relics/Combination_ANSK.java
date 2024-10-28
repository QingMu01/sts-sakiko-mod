package com.qingmu.sakiko.relics;

import basemod.abstracts.CustomSavable;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.qingmu.sakiko.monsters.boss.InstinctSakiko;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.lang.reflect.Type;

public class Combination_ANSK extends AbstractSakikoRelic implements CustomSavable<Boolean> {
    // 遗物ID
    public static final String ID = ModNameHelper.make(Combination_ANSK.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "SakikoModResources/img/relics/ansk.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;

    public boolean isSleep = false;

    public Combination_ANSK() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (this.isSleep && room instanceof MonsterRoomBoss) {
            AbstractDungeon.bossList.add(0, InstinctSakiko.ID);
            AbstractDungeon.bossKey = InstinctSakiko.ID;
        }
    }

    @Override
    public int onPlayerGainedBlock(float blockAmount) {
        AbstractMonster anon = AbstractDungeon.getCurrRoom().monsters.getMonster("");
        if (anon != null && !anon.isDying) {
            this.addToBot(new GainBlockAction(anon, DungeonHelper.getPlayer(), (int) blockAmount));
        }
        return super.onPlayerGainedBlock(blockAmount);
    }

    @Override
    public void onLoad(Boolean bool) {
        this.isSleep = bool;
    }

    @Override
    public Boolean onSave() {
        return this.isSleep;
    }

    @Override
    public Type savedType() {
        return new TypeToken<Boolean>() {
        }.getType();
    }

}
