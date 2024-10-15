package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.qingmu.sakiko.action.CheckMonsterAllDeadAction;
import com.qingmu.sakiko.stances.ObliviousStance;
import com.qingmu.sakiko.utils.FontBitmapHelp;
import com.qingmu.sakiko.utils.ModNameHelper;

public class MeteorMoonPower extends AbstractPower {
    public static final String POWER_ID = ModNameHelper.make(MeteorMoonPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/MasqueradePower48.png";
    private static final String path128 = "SakikoModResources/img/powers/MasqueradePower84.png";

    public MeteorMoonPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = -1;
//        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
//        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        this.region128 = new TextureAtlas.AtlasRegion(FontBitmapHelp.getFontBitmap(NAME.charAt(0), FontBitmapHelp.Size.LARGE), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(FontBitmapHelp.getFontBitmap(NAME.charAt(0), FontBitmapHelp.Size.SMALL), 0, 0, 48, 48);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
        if (oldStance.ID.equals(ObliviousStance.STANCE_ID)) {
            this.flash();
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                this.addToBot(new DamageAction(monster, new DamageInfo(this.owner, this.owner.currentHealth, DamageInfo.DamageType.THORNS)));
            }
            this.addToBot(new CheckMonsterAllDeadAction(action -> {
                this.addToBot(new DamageAction(this.owner, new DamageInfo(this.owner, this.owner.currentHealth, DamageInfo.DamageType.THORNS)));
                this.addToBot(new ChangeStanceAction(NeutralStance.STANCE_ID));
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            }));
        }
    }
}
