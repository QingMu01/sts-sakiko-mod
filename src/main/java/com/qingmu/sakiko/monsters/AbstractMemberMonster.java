package com.qingmu.sakiko.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.ui.MusicSlotItem;

public abstract class AbstractMemberMonster extends CustomMonster {

    protected MusicSlotItem musicSlotItem = new MusicSlotItem(this);

    protected boolean canPlayMusic = false;

    public AbstractMemberMonster(String name, String id, String img, float x, float y) {
        super(name, id, 100, 0.0F, 0.0F, 200.0F, 220.0F, img, x, y);
    }

    protected void obtainMusic() {
    }


    @Override
    public void update() {
        super.update();
        if (this.canPlayMusic) {
            CardGroup cardGroup = MusicBattleFiledPatch.MusicQueue.musicQueue.get(this);
            if (cardGroup.isEmpty()) {
                this.musicSlotItem.removeMusic();
            } else {
                this.musicSlotItem.setMusic((AbstractMusic) cardGroup.getTopCard());
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (this.canPlayMusic) {
            musicSlotItem.render(sb, this.hb.cX, this.hb.cY + 300.0f);
        }
    }

}