package com.qingmu.sakiko.relics;

import basemod.abstracts.CustomSavable;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.qingmu.sakiko.monsters.boss.InstinctSakiko;
import com.qingmu.sakiko.monsters.friendly.LinkedAnon;
import com.qingmu.sakiko.patch.filed.FriendlyMonsterGroupFiled;
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
        if (this.isSleep && room instanceof MonsterRoomBoss && AbstractDungeon.id.equals(TheEnding.ID)) {
            AbstractDungeon.bossList.add(0, InstinctSakiko.ID);
            AbstractDungeon.bossKey = InstinctSakiko.ID;
            this.pulse = true;
            this.beginPulse();
        } else {
            this.pulse = false;
        }
    }

    @Override
    public int onPlayerGainedBlock(float blockAmount) {
        MonsterGroup monsterGroup = FriendlyMonsterGroupFiled.friendlyMonsterGroup.get(AbstractDungeon.getCurrRoom());
        if (monsterGroup != null) {
            AbstractMonster monster = monsterGroup.getMonster(LinkedAnon.ID);
            if (monster != null){
                this.flash();
                this.addToBot(new GainBlockAction(monster, DungeonHelper.getPlayer(), (int) (blockAmount / 2)));
            }
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
