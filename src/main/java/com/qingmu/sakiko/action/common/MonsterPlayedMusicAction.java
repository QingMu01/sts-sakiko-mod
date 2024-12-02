package com.qingmu.sakiko.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbActivateEffect;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.monsters.AbstractSakikoMonster;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.utils.DungeonHelper;

public class MonsterPlayedMusicAction extends AbstractGameAction {

    private final AbstractMusic music;
    private final AbstractSakikoMonster sakikoMonster;

    public MonsterPlayedMusicAction(AbstractMusic music, AbstractCreature source) {
        this.music = music;
        this.source = source;
        this.sakikoMonster = (AbstractSakikoMonster) source;
        sakikoMonster.musicSlotItem.updateLocation = false;
        this.target = music.m_source == null ? DungeonHelper.getPlayer() : music.m_source;
        this.addToBot(new AnimateJumpAction(this.source));
    }

    @Override
    public void update() {
        AbstractDungeon.effectsQueue.add(new PlasmaOrbActivateEffect(music.hb.cX, music.hb.cY));
        this.music.play();
        AbstractDungeon.effectsQueue.add(new ExhaustCardEffect(this.music));
        CardGroup queue = MusicBattleFiledPatch.MusicQueue.musicQueue.get(source);
        queue.removeCard(this.music);
        sakikoMonster.musicSlotItem.updateLocation = true;
        this.isDone = true;
    }
}
