package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.qingmu.sakiko.action.common.CleanMusicQueueAction;
import com.qingmu.sakiko.action.common.ReadyToPlayMusicAction;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;

public class DisharmonyNoteAction extends AbstractGameAction {
    private AbstractPlayer player;

    public DisharmonyNoteAction(AbstractPlayer player) {
        this.player = player;
    }

    @Override
    public void update() {
        if (MusicBattleFiledPatch.MusicQueue.musicQueue.get(this.player).isEmpty()){
            this.isDone = true;
            return;
        }
        this.addToBot(new ReadyToPlayMusicAction(1));
        this.addToBot(new CleanMusicQueueAction(this.player));
        this.isDone = true;
    }
}
