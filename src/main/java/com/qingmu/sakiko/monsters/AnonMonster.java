package com.qingmu.sakiko.monsters;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.*;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import com.qingmu.sakiko.SakikoModCore;
import com.qingmu.sakiko.constant.SoundHelper;
import com.qingmu.sakiko.powers.monster.AnonDasuruPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class AnonMonster extends AbstractMemberMonster {

    public static final String ID = ModNameHelper.make(AnonMonster.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String NAME = monsterStrings.NAME;
    private static final String[] MOVES = monsterStrings.MOVES;
    // 怪物的图片，请自行添加
    private static final String IMG = "SakikoModResources/img/monster/anon.png";

    private boolean isHeal = false;
    private boolean isFirstTurn = true;

    public AnonMonster(float x, float y) {
        super(NAME, ID, IMG, x, y);
        this.damage.add(new DamageInfo(this, this.baseAttack));
        this.damage.add(new DamageInfo(this, this.baseSlash));
        this.damage.add(new DamageInfo(this, this.baseMulti));
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
        CardCrawlGame.sound.playV(SoundHelper.ANON_INIT.name(),2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
    }

    @Override
    public void die() {
        super.die();
        this.addToBot(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
        CardCrawlGame.sound.playV(SoundHelper.ANON_DEATH.name(),2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 0: {
                this.addToBot(new GainBlockAction(this, this, this.baseBlock * 2, true));
                break;
            }
            case 1: {
                CardCrawlGame.sound.playV(SoundHelper.ANON_LAUGH.name(),2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
                this.addToBot(new HealAction(this, this, this.maxHealth / 2));
                break;
            }
            case 2: {
                this.addToBot(new AnimateJumpAction(this));
                this.addToBot(new VFXAction(new BorderFlashEffect(Color.SKY)));
                if (Settings.FAST_MODE) {
                    this.addToBot(new VFXAction(new SmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), 0.1F));
                } else {
                    this.addToBot(new VFXAction(new SmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), 0.3F));
                }
                CardCrawlGame.sound.playV(SoundHelper.ANON_YEAH.name(),2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new AnonDasuruPower(AbstractDungeon.player, this, 1)));
                break;
            }
            case 3: {
                for (int i = 0; i < this.multiCount; i++) {
                    this.addToBot(new AnimateFastAttackAction(this));
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(2)));
                }
                break;
            }
            case 4: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
                break;
            }
            case 5: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
                this.addToBot(new GainBlockAction(this, this, this.baseBlock, true));
            }
        }
        this.addToBot(new RollMoveAction(this));
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
        if (i < 20) {
            // 20%概率上buff，使攻击伤害转换为治疗
            this.setMove(MOVES[0], (byte) 2, Intent.MAGIC);
        } else if (i < 35) {
            // 15%概率防御
            this.setMove((byte) 0, Intent.DEFEND);
        } else if (i < 55) {
            // 20%概率连击
            this.setMove((byte) 3, Intent.ATTACK, this.damage.get(2).base, this.multiCount, true);
        } else if (i < 80) {
            // 25%概率重击
            this.setMove((byte) 4, Intent.ATTACK, this.damage.get(1).base);
        } else {
            // 20%概率普通攻击+防御
            this.setMove((byte) 5, Intent.ATTACK_DEFEND, this.damage.get(0).base);
        }
    }
}

