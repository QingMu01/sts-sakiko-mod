package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.qingmu.sakiko.action.ActiveKabeAction;
import com.qingmu.sakiko.powers.monster.TomoriBlessingPower;
import com.qingmu.sakiko.relics.Combination_TMSK;
import com.qingmu.sakiko.utils.ModNameHelper;

public class KokoroNoKabePower extends AbstractPower {

    public static final String POWER_ID = ModNameHelper.make(KokoroNoKabePower.class.getSimpleName());

    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    private static final String NAME = powerStrings.NAME;

    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/KokoroNoKabe48.png";

    private static final String path128 = "SakikoModResources/img/powers/KokoroNoKabe128.png";


    public KokoroNoKabePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.priority = 1;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.calculateKabeDamage() + DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.addToBot(new ActiveKabeAction(this.owner, false));
    }

    /*
     * 被攻击后触发的钩子
     * @DamageInfo 攻击信息，存有伤害来源、类型、攻击伤害等信息
     * @damageAmount 计算完格挡后的伤害
     * */
    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != this.owner && damageAmount > 0) {
            // 免伤
            if (this.owner.isPlayer && ((AbstractPlayer) this.owner).hasRelic(Combination_TMSK.ID)) {
                AbstractRelic relic = ((AbstractPlayer) this.owner).getRelic(Combination_TMSK.ID);
                relic.flash();
                this.addToBot(new RelicAboveCreatureAction(this.owner, relic));
                return damageAmount;
            }
            int buffed;
            int reduceDamage = 0;
            // 计算祝福减伤
            AbstractPower blessing = this.owner.getPower(TomoriBlessingPower.POWER_ID);
            if (blessing != null) {
                blessing.flash();
                reduceDamage += blessing.amount;
            }
            buffed = this.calculateKabeDamage() - reduceDamage;
            if (buffed > 0) {
                this.flash();
                this.addToTop(new LoseHPAction(this.owner, this.owner, buffed));
            }
        }
        return damageAmount;
    }

    @Override
    public void stackPower(int stackAmount) {
        if (AbstractDungeon.player.hasPower(TomoriBlessingPower.POWER_ID)) {
            AbstractDungeon.player.getPower(TomoriBlessingPower.POWER_ID).flash();
            stackAmount = stackAmount / 2;
        }
        this.amount += stackAmount;
        if (this.amount >= 999) {
            this.amount = 999;
        }
        this.updateDescription();
    }

    @Override
    public void reducePower(int reduceAmount) {
        this.amount -= reduceAmount;
        if (this.amount <= 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
        this.updateDescription();
    }

    private int calculateKabeDamage() {
        return (int) Math.floor(12.0f * this.amount / 90.0f);
    }

}
