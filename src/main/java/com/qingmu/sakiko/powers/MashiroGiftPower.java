package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.qingmu.sakiko.action.ResetFakeIntentAction;
import com.qingmu.sakiko.patch.monster.ChaosMonsterInfoPatch;
import com.qingmu.sakiko.utils.ModNameHelper;

public class MashiroGiftPower extends AbstractSakikoPower {

    public static final String POWER_ID = ModNameHelper.make(MashiroGiftPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/MashiroGiftPower48.png";
    private static final String path128 = "SakikoModResources/img/powers/MashiroGiftPower128.png";

    public MashiroGiftPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, amount, owner, PowerType.DEBUFF);

        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void onInitialApplication() {
        AbstractDungeon.getCurrRoom().monsters.monsters.forEach((monster -> {
            monster.createIntent();
            ChaosMonsterInfoPatch.FakeMonsterInfo fakeMonsterInfo = new ChaosMonsterInfoPatch.FakeMonsterInfo();
            fakeMonsterInfo.init(this.owner.currentHealth, this.owner.maxHealth, this.owner.currentBlock);
            ChaosMonsterInfoPatch.FakeMonsterInfoPatch.fakeMonsterInfo.set(monster, fakeMonsterInfo);
            monster.healthBarUpdatedEvent();
        }));
    }

    @Override
    public void onRemove() {
        this.addToTop(new ResetFakeIntentAction());
    }

    @Override
    public void atStartOfTurn() {
        this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));

    }
}
