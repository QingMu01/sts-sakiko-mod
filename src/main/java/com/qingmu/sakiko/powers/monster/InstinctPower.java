package com.qingmu.sakiko.powers.monster;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.EndTurnDeathPower;
import com.qingmu.sakiko.inteface.TriggerOnPlayerGotPower;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;

public class InstinctPower extends AbstractPower implements TriggerOnPlayerGotPower {

    public static final String POWER_ID = ModNameHelper.make(InstinctPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/InstinctPower48.png";
    private static final String path128 = "SakikoModResources/img/powers/InstinctPower128.png";

    public InstinctPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void onInitialApplication() {
        for (AbstractPower power : DungeonHelper.getPlayer().powers) {
            if (power.type == PowerType.BUFF){
                this.addToBot(new RemoveSpecificPowerAction(DungeonHelper.getPlayer(), this.owner, power));
            }
        }
    }

    @Override
    public void triggerOnPlayerGotPower(AbstractPower instance) {
        if (this.owner.isPlayer || instance.type == PowerType.DEBUFF) return;

        ArrayList<AbstractPower> playerPowers = DungeonHelper.getPlayer().powers;
        ArrayList<AbstractPower> instinctSakikoPowers = this.owner.powers;

        int buffCount = 1;
        int needRemoveCount;
        // 检查玩家持有buff数量
        for (AbstractPower playerPower : playerPowers) {
            // 已拥有则不进行操作
            if (playerPower.ID.equals(instance.ID)) {
                return;
            } else if (playerPower.type == PowerType.BUFF && !playerPower.ID.equals(EndTurnDeathPower.POWER_ID)) {
                buffCount++;
            }
        }
        needRemoveCount = buffCount - this.amount;
        if (needRemoveCount > 0) {
            this.flash();
            AbstractPower playerRemove = null;
            AbstractPower instinctSakikoRemove = null;
            // 玩家获取怪物有的正面能力时，掠夺过来
            for (AbstractPower instinctSakikoPower : instinctSakikoPowers) {
                if (instinctSakikoPower.ID.equals(instance.ID)) {
                    instinctSakikoRemove = instinctSakikoPower;
                    break;
                }
            }
            // 玩家获取超过限制的正面能力时，被掠夺
            for (AbstractPower playerPower : playerPowers) {
                if (playerPower.type == PowerType.BUFF && !playerPower.ID.equals(EndTurnDeathPower.POWER_ID)) {
                    playerRemove = playerPower;
                    break;
                }
            }
            if (playerRemove != null) {
                this.addToBot(new RemoveSpecificPowerAction(DungeonHelper.getPlayer(), this.owner, playerRemove));
                this.addToBot(new ApplyPowerAction(this.owner, this.owner, playerRemove, playerRemove.amount));
            }
            if (instinctSakikoRemove != null) {
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, instinctSakikoRemove));
                this.addToBot(new ApplyPowerAction(DungeonHelper.getPlayer(), this.owner, instinctSakikoRemove, instinctSakikoRemove.amount));
            }
        }

    }
}
