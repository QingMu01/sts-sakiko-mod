package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.qingmu.sakiko.cards.music.AbstractMusic;

public class ReadyToPlayMusicAction extends AbstractGameAction {

    private final AbstractMusic music;

    public ReadyToPlayMusicAction(AbstractMusic music) {
        this.music = music;
    }

    @Override
    public void update() {

    }
}
