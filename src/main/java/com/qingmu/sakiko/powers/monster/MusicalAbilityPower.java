package com.qingmu.sakiko.powers.monster;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.qingmu.sakiko.action.common.ReadyToPlayMusicAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.monsters.boss.InnerDemonSakiko;
import com.qingmu.sakiko.powers.AbstractSakikoPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class MusicalAbilityPower extends AbstractSakikoPower {

    public static final String POWER_ID = ModNameHelper.make(MusicalAbilityPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/MusicalAbilityPower48.png";
    private static final String path128 = "SakikoModResources/img/powers/MusicalAbilityPower128.png";


    public MusicalAbilityPower(AbstractCreature owner) {
        super(POWER_ID, NAME, 0, owner, PowerType.BUFF);

        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (!(card instanceof AbstractMusic)) {
            this.flashWithoutSound();
            ++this.amount;
            if (this.amount == 8) {
                this.amount = 0;
                this.playApplyPowerSfx();
                this.addToBot(new ReadyToPlayMusicAction(1, this.owner));
                if (this.owner instanceof InnerDemonSakiko) {
                    InnerDemonSakiko innerDemonSakiko = (InnerDemonSakiko) this.owner;
                    innerDemonSakiko.obtainMusic(innerDemonSakiko.readyMusic());
                }
            }
            this.updateDescription();
        }
    }
}
