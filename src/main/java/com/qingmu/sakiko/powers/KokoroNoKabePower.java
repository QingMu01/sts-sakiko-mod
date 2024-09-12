package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.relics.Calipers;
import com.qingmu.sakiko.action.ActiveKabeAction;
import com.qingmu.sakiko.powers.monster.TomoriBlessingPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class KokoroNoKabePower extends TwoAmountPower {

    public static final String POWER_ID = ModNameHelper.make(KokoroNoKabePower.class.getSimpleName());

    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    private static final String NAME = powerStrings.NAME;

    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/KokoroNoKabe48.png";

    private static final String path128 = "SakikoModResources/img/powers/KokoroNoKabe128.png";

    public int counter = 0;
    public int limit = 10;
    public int lastApply;

    private int blockFromKabe = 0;

    public KokoroNoKabePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = 0;
        this.stackPower(amount);
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + this.blockFromKabe + DESCRIPTIONS[2] + this.amount2 + DESCRIPTIONS[3] + (this.limit - this.counter) + DESCRIPTIONS[4];
    }

    @Override
    public void atStartOfTurn() {
        if (this.owner.hasPower(BarricadePower.POWER_ID) || this.owner.hasPower(BlurPower.POWER_ID)) {
            this.blockFromKabe = Math.max(this.blockFromKabe, 0);
        } else if (this.owner.isPlayer && ((AbstractPlayer) this.owner).hasRelic(Calipers.ID)) {
            this.blockFromKabe = Math.max(this.blockFromKabe - 15, 0);
        } else {
            this.blockFromKabe = 0;
        }
        this.updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.addToBot(new ActiveKabeAction());
        }
    }

    /*
     * 被攻击后触发的钩子
     * DamageInfo 攻击信息，存有伤害来源、类型、攻击伤害等信息
     * damageAmount 计算完格挡后的伤害
     * */
    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != this.owner) {
            // 计算不属于心之壁的格挡
            int currentBlock = this.owner.currentBlock + info.output - this.blockFromKabe;
            // 如果不造成伤害 或不属于心之壁的格挡能完全防御本次攻击则直接返回
            if (this.amount2 == 0 || info.output <= currentBlock) {
                return damageAmount;
            } else {
                // 计算使用了多少心之壁格挡
                this.blockFromKabe -= info.output - currentBlock;
            }
            // 寻找背锅
            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (monster.hasPower(ScapegoatPower.POWER_ID)) {
                    this.addToTop(new LoseHPAction(monster, monster, this.amount2));
                    return damageAmount;
                }
            }
            // 计算归宿减伤
            int buffed;
            int reduceDamage = 0;
            AbstractPower ibasyo = this.owner.getPower(IbasyoPower.POWER_ID);
            if (ibasyo != null) {
                ibasyo.flash();
                reduceDamage += ibasyo.amount;
            }
            // 计算祝福减伤
            AbstractPower blessing = this.owner.getPower(TomoriBlessingPower.POWER_ID);
            if (blessing != null) {
                blessing.flash();
                reduceDamage += blessing.amount;
            }
            buffed = this.amount2 - reduceDamage;
            if (buffed > 0) {
                this.flash();
                this.addToTop(new LoseHPAction(this.owner, this.owner, buffed));
            }
        }
        return damageAmount;
    }

    @Override
    public void stackPower(int stackAmount) {
        int tmp;
        if (this.owner.hasPower(TomoriBlessingPower.POWER_ID)){
            tmp = stackAmount / 2;
        }else {
            tmp = stackAmount;
        }
        this.amount += tmp;
        this.counter += tmp;
        this.lastApply = tmp;
        // 每10层心之壁层，增加1心之壁伤害
        if (this.counter >= this.limit) {
            do {
                this.stackDamageAmount(1);
                this.counter -= this.limit;
                this.limit += 5;
            } while (this.counter >= this.limit);
            this.counter = Math.max(this.counter, 0);
        }
        if (this.amount <= 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
        if (this.amount >= 999) {
            this.amount = 999;
        }
        this.updateDescription();
    }

    public void stackDamageAmount(int stackAmount) {
        this.amount2 += stackAmount;
        if (this.amount2 >= 999) {
            this.amount2 = 999;
        }
        this.updateDescription();
    }

    @Override
    public void reducePower(int reduceAmount) {
        this.amount -= reduceAmount;
        if (this.amount <= 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }

        if (this.amount >= 999) {
            this.amount = 999;
        }
        this.updateDescription();
    }

    public void reduceDamageAmount(int reduceAmount) {
        this.amount2 -= reduceAmount;
        if (this.amount2 <= 0) {
            this.amount2 = 0;
        }
    }

    public void stackBlockFromKabe(int blockFromKabe) {
        this.blockFromKabe += blockFromKabe;
        this.updateDescription();
    }


}
