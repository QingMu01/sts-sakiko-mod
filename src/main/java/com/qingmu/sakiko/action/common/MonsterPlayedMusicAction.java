package com.qingmu.sakiko.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.monsters.AbstractSakikoMonster;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;

public class MonsterPlayedMusicAction extends AbstractGameAction {

    private final AbstractMusic music;
    private final AbstractSakikoMonster sakikoMonster;

    public MonsterPlayedMusicAction(AbstractMusic music, AbstractCreature source) {
        this.music = music;
        this.source = source;
        this.sakikoMonster = (AbstractSakikoMonster) source;
        sakikoMonster.musicSlotItem.updateLocation = false;
        this.target = music.m_source == null ? AbstractDungeon.player : music.m_source;
        this.addToBot(new AnimateJumpAction(this.source));
    }

    @Override
    public void update() {
        this.music.play();
        AbstractDungeon.effectList.add(new ExhaustCardEffect(this.music));
        CardGroup queue = MusicBattleFiledPatch.MusicQueue.musicQueue.get(source);
        queue.removeCard(this.music);
        sakikoMonster.musicSlotItem.updateLocation = true;
        this.isDone = true;
    }
}
