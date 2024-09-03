package com.qingmu.sakiko.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.*;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import com.qingmu.sakiko.powers.monster.AnonDasuruPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.SoundHelper;

public class AnonMonster extends CustomMonster {

    public static final String ID = ModNameHelper.make(AnonMonster.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String NAME = monsterStrings.NAME;
    private static final String[] MOVES = monsterStrings.MOVES;
    // 怪物的图片，请自行添加
    private static final String IMG = "SakikoModResources/img/monster/anon.png";

    private boolean isHeal = false;
    private boolean isFirstTurn = true;

    private int baseHp = 70, baseAttack = 8, baseSlash = 14, baseMulti = 6, multiCount = 2, baseBlock = 6;

    public AnonMonster(float x, float y) {
        super(NAME, ID, 100, 0.0F, 0.0F, 200.0F, 220.0F, IMG, x, y);
        // 进阶3 强化伤害
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.baseAttack += 2;
            this.baseSlash += 4;
            this.baseMulti += 1;
        }
        // 进阶8 强化生命
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.baseHp += 30;
            this.baseBlock += 3;
        }
        // 进阶18 强化行动
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.baseAttack += 3;
            this.multiCount++;
            this.baseBlock += 4;
        }

        // act1 基本属性
        if (AbstractDungeon.id.equals(Exordium.ID)) {
            this.setHp(baseHp - 5, baseHp + 5);
        }
        // act2 基本属性
        if (AbstractDungeon.id.equals(TheCity.ID)) {
            this.baseAttack += 3;
            this.baseHp += 50;
            this.baseSlash += 6;
            this.baseMulti += 2;
            this.multiCount += 1;
            this.setHp(baseHp - 10, baseHp + 10);
        }
        // act3 基本属性
        if (AbstractDungeon.id.equals(TheBeyond.ID)) {
            this.baseAttack += 5;
            this.baseHp += 100;
            this.baseSlash += 6;
            this.baseMulti += 2;
            this.multiCount += 2;
            this.setHp(baseHp - 10, baseHp + 10);
        }

        this.damage.add(new DamageInfo(this, this.baseAttack));
        this.damage.add(new DamageInfo(this, this.baseSlash));
        this.damage.add(new DamageInfo(this, this.baseMulti));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
        CardCrawlGame.sound.play(SoundHelper.ANON_INIT.name());
    }

    @Override
    public void die() {
        super.die();
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
        CardCrawlGame.sound.play(SoundHelper.ANON_DEATH.name());
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 0: {
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.baseBlock * 2, true));
                break;
            }
            case 1: {
                CardCrawlGame.sound.play(SoundHelper.ANON_LAUGH.name());
                AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.maxHealth / 2));
                break;
            }
            case 2: {
                AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.SKY)));
                if (Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), 0.1F));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), 0.3F));
                }
                CardCrawlGame.sound.play(SoundHelper.ANON_YEAH.name());
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new AnonDasuruPower(AbstractDungeon.player, this, 1)));
                break;
            }
            case 3: {
                for (int i = 0; i < this.multiCount; i++) {
                    AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2)));
                }
                break;
            }
            case 4: {
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
                break;
            }
            case 5: {
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.baseBlock, true));
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (this.isFirstTurn) {
            this.isFirstTurn = false;
            // 首回合防御
            this.setMove((byte) 0, Intent.DEFEND);
            return;
        }
        if (!this.isHeal && this.currentHealth < this.baseHp / 2) {
            this.isHeal = true;
            // 半血回复 最大生命值的四分之一
            this.setMove((byte) 1, Intent.BUFF);
            return;
        }
        if (i < 5) {
            // 5%概率上Debuff，使攻击伤害转换为治疗
            this.setMove(MOVES[0], (byte) 2, Intent.MAGIC);
        } else if (i < 25) {
            // 20%概率防御
            this.setMove((byte) 0, Intent.DEFEND);
        } else if (i < 50) {
            // 25%概率连击
            this.setMove((byte) 3, Intent.ATTACK, this.damage.get(2).base, this.multiCount, true);
        } else if (i < 80) {
            // 30%概率重击
            this.setMove((byte) 4, Intent.ATTACK, this.damage.get(1).base);
        } else {
            // 20%概率普通攻击+防御
            this.setMove((byte) 5, Intent.ATTACK_DEFEND, this.damage.get(0).base);
        }
    }
}

