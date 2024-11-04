package com.qingmu.sakiko.room;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.qingmu.sakiko.events.EndingSakiko;

public class EndingEventRoom extends NeowRoom {

    public EndingEventRoom() {
        super(true);
    }

    @Override
    public void onPlayerEntry() {
        AbstractDungeon.overlayMenu.proceedButton.hide();
        this.event = new EndingSakiko();
        this.event.onEnterRoom();
    }
}
