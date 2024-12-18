package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.inteface.ModifiedMusicNumber;
import com.qingmu.sakiko.inteface.TriggerOnPlayMusic;
import com.qingmu.sakiko.utils.ModNameHelper;

public class ZekkouchouPower extends AbstractSakikoPower implements ModifiedMusicNumber, TriggerOnPlayMusic {

    public static final String POWER_ID = ModNameHelper.make(ZekkouchouPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/ZekkouchouPower48.png";
    private static final String path128 = "SakikoModResources/img/powers/ZekkouchouPower128.png";

    public ZekkouchouPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, amount, owner, PowerType.BUFF);

        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public float finalModifyMusicNumber(AbstractCard card, float musicNumber) {
        return this.amount > 0 ? musicNumber * 2 : musicNumber;
    }

    @Override
    public void afterPlayedMusic(AbstractMusic music) {
        this.reducePower(1);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }
}
