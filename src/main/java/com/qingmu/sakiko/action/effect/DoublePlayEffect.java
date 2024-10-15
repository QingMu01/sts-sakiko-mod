package com.qingmu.sakiko.action.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.ExhaustBlurEffect;
import com.megacrit.cardcrawl.vfx.ExhaustEmberEffect;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;

public class DoublePlayEffect extends AbstractGameEffect {

    private AbstractCard music;


    public DoublePlayEffect(AbstractMusic music) {

        this.music = music.makeSameInstanceOf();
        this.music.current_x = music.current_x;
        this.music.current_y = music.current_y;
        this.music.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        this.music.target_y = Settings.HEIGHT / 2.0F;
        this.music.calculateCardDamage((AbstractMonster) music.m_target);

        this.duration = 1.0f;
    }

    @Override
    public void update() {
        this.music.update();
        if (this.music.current_x == this.music.target_x) {
            if (this.duration == 1.0f) {
                if (this.music.hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER)) {
                    AbstractDungeon.player.limbo.empower(this.music);
                } else {
                    AbstractDungeon.player.limbo.removeCard(this.music);
                    int i;
                    for (i = 0; i < 90; ++i) {
                        AbstractDungeon.effectsQueue.add(new ExhaustBlurEffect(this.music.current_x, this.music.current_y));
                    }

                    for (i = 0; i < 50; ++i) {
                        AbstractDungeon.effectsQueue.add(new ExhaustEmberEffect(this.music.current_x, this.music.current_y));
                    }
                }
            }
            this.duration -= Gdx.graphics.getDeltaTime();
            if (this.duration < 0.0F) {
                this.isDone = true;
            }

        }
    }

    @Override
    public void render(SpriteBatch sb) {
        this.music.render(sb);
    }

    @Override
    public void dispose() {

    }
}
