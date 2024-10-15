package com.qingmu.sakiko.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.qingmu.sakiko.SakikoModCore;
import com.qingmu.sakiko.constant.SoundHelper;

public class PlaySoundAction extends AbstractGameAction {

    private final SoundHelper soundName;

    public PlaySoundAction(SoundHelper soundName) {
        this.soundName = soundName;
    }

    @Override
    public void update() {
        CardCrawlGame.sound.playV(this.soundName.name(), 2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
        this.isDone = true;
    }
}
