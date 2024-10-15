package com.qingmu.sakiko.action.effect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.stance.DivinityStanceChangeParticle;
import com.megacrit.cardcrawl.vfx.stance.WrathStanceChangeParticle;

public class SakikoStanceChangeParticleGenerator extends AbstractGameEffect {

    private float x;
    private float y;
    private String stanceId;

    public SakikoStanceChangeParticleGenerator(float x, float y, String stanceId) {
        this.x = x;
        this.y = y;
        this.stanceId = stanceId;
    }

    public void update() {
        switch (this.stanceId) {
            case "Divinity":
                for (int i = 0; i < 20; ++i) {
                    AbstractDungeon.effectsQueue.add(new DivinityStanceChangeParticle(Color.PINK, this.x, this.y));
                }
                break;
            case "Wrath":
                for (int i = 0; i < 10; ++i) {
                    AbstractDungeon.effectsQueue.add(new WrathStanceChangeParticle(this.x));
                }
                break;
            case "Neutral":
            default:
                for (int i = 0; i < 20; ++i) {
                    AbstractDungeon.effectsQueue.add(new DivinityStanceChangeParticle(Color.WHITE, this.x, this.y));
                }
                break;
        }
        this.isDone = true;
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }

}
