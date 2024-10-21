package com.qingmu.sakiko.relics.menbers;

import basemod.abstracts.CustomSavable;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.qingmu.sakiko.powers.FallApartPower;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.lang.reflect.Type;

public class Anon extends AbstractBandMember implements CustomSavable<Integer> {

    public static final String ID = ModNameHelper.make(Anon.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/relics/members/anon_relic.png";

    public Anon() {
        super(ID, IMG_PATH);
        this.counter = 1;
        this.amount = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onRightClick() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if (!AbstractDungeon.player.hasPower(FallApartPower.POWER_ID)) {
                if (this.counter > 0) {
                    this.flash();
                    this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                    this.addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, 15));
                    this.counter--;
                }
            } else {
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, DESCRIPTIONS[1], true));
            }
        } else {
            AbstractDungeon.player.heal(15);
            this.counter--;
        }
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        this.amount++;
        if (this.amount >= 15) {
            this.flash();
            this.counter++;
            this.amount = 0;
        }
    }

    @Override
    public void onLoad(Integer integer) {
        this.amount = integer;
    }

    @Override
    public Integer onSave() {
        return this.amount;
    }

    @Override
    public Type savedType() {
        return new TypeToken<Integer>() {
        }.getType();
    }

}
