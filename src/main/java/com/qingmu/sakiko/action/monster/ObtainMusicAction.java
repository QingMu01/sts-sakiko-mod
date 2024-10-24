package com.qingmu.sakiko.action.monster;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.monsters.AbstractSakikoMonster;

public class ObtainMusicAction extends AbstractGameAction {

    private AbstractMusic music;
    private AbstractSakikoMonster source;

    public ObtainMusicAction(AbstractSakikoMonster source, AbstractMusic music) {
        this.source = source;
        this.music = music;
    }

    @Override
    public void update() {
        this.source.obtainMusic(this.music);
        this.isDone = true;
    }
}
