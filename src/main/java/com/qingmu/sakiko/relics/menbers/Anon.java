package com.qingmu.sakiko.relics.menbers;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Anon extends AbstractBandMember implements ClickableRelic {

    public static final String ID = ModNameHelper.make(Anon.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/relics/members/anon_relic.png";

    public int amount;

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
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && this.canUse){
            if (this.counter > 0) {
                this.flash();
                this.addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, 15));
                this.counter--;
            }
        }
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        this.canUse = true;
        this.amount++;
        if (this.amount >= 15){
            this.flash();
            this.counter++;
            this.amount = 0;
        }
    }

    @Override
    public void removePower() {
        this.canUse = false;
    }
}
