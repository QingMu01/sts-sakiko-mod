package com.qingmu.sakiko.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.qingmu.sakiko.constant.MusicHelper;
import com.qingmu.sakiko.monsters.AbstractSakikoMonster;

public class PlayBGMAction extends AbstractGameAction {
    private MusicHelper musicHelper;
    private boolean isForce;
    private AbstractSakikoMonster monster;

    public PlayBGMAction(MusicHelper musicHelper, AbstractSakikoMonster monster, boolean isForce) {
        this.musicHelper = musicHelper;
        this.isForce = isForce;
        this.monster = monster;
    }

    public PlayBGMAction(MusicHelper musicHelper, AbstractSakikoMonster monster) {
        this(musicHelper, monster, false);
    }

    @Override
    public void update() {
        if (this.isForce) {
            CardCrawlGame.music.silenceBGMInstantly();
            CardCrawlGame.music.playTempBgmInstantly(this.musicHelper.name(), true);
            this.isDone = true;
            return;
        }
        if (!this.monster.isPlayBGM) {
            CardCrawlGame.music.silenceBGMInstantly();
            CardCrawlGame.music.playTempBgmInstantly(this.musicHelper.name(), true);
            this.monster.isPlayBGM = true;
        }
        this.isDone = true;
    }
}
