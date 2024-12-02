package com.qingmu.sakiko.monsters;

import basemod.ReflectionHacks;
import basemod.animations.AbstractAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.powers.FallApartPower;
import com.qingmu.sakiko.powers.FukkenPower;
import com.qingmu.sakiko.powers.MemberPower;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public abstract class AbstractFriendlyMonster extends AbstractSakikoMonster {

    public static final Map<String, BiFunction<Float,Float,AbstractFriendlyMonster>> FRIENDLY_MONSTER_MAP = new HashMap<>();

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make(AbstractFriendlyMonster.class.getSimpleName()));
    private static final String[] TEXT = uiStrings.TEXT;

    protected int baseAttack = 3, baseSlash = 6, baseMulti = 2, multiCount = 2, baseBlock = 5, powerful = 2;

    private AbstractMonster targetMonster;

    public AbstractFriendlyMonster(String name, String id, String img, float x, float y) {
        super(name, id, img, x, y);
        this.damage.add(new DamageInfo(this, this.baseAttack, DamageInfo.DamageType.THORNS));
        this.damage.add(new DamageInfo(this, this.baseSlash, DamageInfo.DamageType.THORNS));
        this.damage.add(new DamageInfo(this, this.baseMulti, DamageInfo.DamageType.THORNS));
    }

    public void setHp(int baseHp) {
        super.setHp(baseHp);
    }


    public AbstractMonster getTarget() {
        return this.targetMonster;
    }

    @Override
    protected List<IntentAction> initIntent() {
        ArrayList<IntentAction> intentActions = new ArrayList<>();
        // 30概率防御
        intentActions.add(new IntentAction.Builder()
                .setWeight(30)
                .setIntent(Intent.DEFEND)
                .setActions(() -> this.aoeBlock(this.baseBlock))
                .build());
        // 30概率连击
        intentActions.add(new IntentAction.Builder()
                .setWeight(30)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(2).base)
                .setMultiplier(this.multiCount)
                .setActions(() -> this.generateMultiAttack(this.damage.get(2), this.multiCount))
                .build());
        // 30概率重击
        intentActions.add(new IntentAction.Builder()
                .setWeight(30)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(1).base)
                .setActions(() -> {
                    ArrayList<AbstractGameAction> actions = new ArrayList<>();
                    actions.add(new AnimateSlowAttackAction(this));
                    if (this.targetMonster != null) {
                        actions.add(new DamageAction(this.targetMonster, this.damage.get(1)));
                    }
                    return actions.toArray(new AbstractGameAction[0]);
                }).build());
        // 10概率普通攻击
        intentActions.add(new IntentAction.Builder()
                .setWeight(10)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(0).base)
                .setActions(() -> {
                    ArrayList<AbstractGameAction> actions = new ArrayList<>();
                    actions.add(new AnimateSlowAttackAction(this));
                    if (this.targetMonster != null) {
                        actions.add(new DamageAction(this.targetMonster, this.damage.get(0)));
                    }
                    return actions.toArray(new AbstractGameAction[0]);
                }).build());
        return intentActions;
    }

    protected AbstractGameAction[] aoeBlock(int amount) {
        ArrayList<AbstractGameAction> actions = new ArrayList<>();
        actions.add(new GainBlockAction(DungeonHelper.getPlayer(), this, amount, true));
        MonsterGroup friendlyMonsterGroup = DungeonHelper.getFriendlyMonsterGroup();
        if (friendlyMonsterGroup != null) {
            for (AbstractMonster monster : friendlyMonsterGroup.monsters) {
                if (!monster.isDying && !monster.isDead && !monster.isEscaping) {
                    actions.add(new GainBlockAction(monster, this, amount, true));
                }
            }
        }
        return actions.toArray(new AbstractGameAction[0]);
    }

    @Override
    protected AbstractGameAction[] generateMultiAttack(DamageInfo info, int multiplier) {
        ArrayList<AbstractGameAction> actions = new ArrayList<>();
        actions.add(new AnimateFastAttackAction(this));
        if (this.targetMonster != null) {
            int animationInsert = multiplier / 5;
            for (int i = 0; i < multiplier; i++) {
                if ((i + 1) % 5 == 0 && animationInsert > 0) {
                    actions.add(new AnimateJumpAction(this));
                    animationInsert--;
                }
                actions.add(new DamageAction(this.targetMonster, info));
            }
        }
        return actions.toArray(new AbstractGameAction[0]);

    }

    @Override
    protected boolean canPhaseSwitch() {
        return false;
    }

    @Override
    protected void phaseSwitchLogic() {
    }

    @Override
    protected List<IntentAction> updateIntent() {
        return this.intentList;
    }

    @Override
    public void applyPowers() {
        if (this.hasPower(MemberPower.POWER_ID) && DungeonHelper.getPlayer().hasPower(FallApartPower.POWER_ID)) {
            if (this.intentAction.intent != Intent.STUN) {
                this.specialIntentList.add(0, new SpecialIntentAction.Builder()
                        .setIntent(Intent.STUN)
                        .setActions(() -> new AbstractGameAction[]{new WaitAction(0.1f)})
                        .build());
                this.getMove(0);
            }
            return;
        }
        if (this.targetMonster != null) {
            for (DamageInfo dmg : this.damage) {
                float tmp = dmg.base;
                for (AbstractPower p : dmg.owner.powers) {
                    tmp = p.atDamageGive(tmp, dmg.type);
                }
                AbstractPower power = DungeonHelper.getPlayer().getPower(FukkenPower.POWER_ID);
                if (power != null) {
                    tmp = power.atDamageGive(tmp, DamageInfo.DamageType.THORNS);
                }
                for (AbstractPower p : this.targetMonster.powers) {
                    tmp = p.atDamageReceive(tmp, dmg.type);
                }
                for (AbstractPower p : dmg.owner.powers) {
                    tmp = p.atDamageFinalGive(tmp, dmg.type);
                }
                for (AbstractPower p : this.targetMonster.powers) {
                    tmp = p.atDamageFinalReceive(tmp, dmg.type);
                }
                if (dmg.base != (int) tmp) {
                    dmg.isModified = true;
                }
                dmg.output = MathUtils.floor(tmp);
                if (dmg.output < 0) {
                    dmg.output = 0;
                }
            }
        } else {
            for (DamageInfo dmg : this.damage) {
                dmg.output = 0;
            }
        }
        EnemyMoveInfo privateMove = ReflectionHacks.getPrivate(this, AbstractMonster.class, "move");
        if (privateMove.baseDamage > -1) {
            float tmp = privateMove.baseDamage;
            if (this.targetMonster != null) {
                for (AbstractPower p : this.powers) {
                    tmp = p.atDamageGive(tmp, DamageInfo.DamageType.THORNS);
                }
                AbstractPower power = DungeonHelper.getPlayer().getPower(FukkenPower.POWER_ID);
                if (power != null) {
                    tmp = power.atDamageGive(tmp, DamageInfo.DamageType.THORNS);
                }
                for (AbstractPower p : this.targetMonster.powers) {
                    tmp = p.atDamageReceive(tmp, DamageInfo.DamageType.THORNS);
                }
                for (AbstractPower p : this.powers) {
                    tmp = p.atDamageFinalGive(tmp, DamageInfo.DamageType.THORNS);
                }
                for (AbstractPower p : this.targetMonster.powers) {
                    tmp = p.atDamageFinalReceive(tmp, DamageInfo.DamageType.THORNS);
                }
                tmp = MathUtils.floor(tmp);
                if (tmp < 0) {
                    tmp = 0;
                }
            } else {
                tmp = 0;
            }
            ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentDmg", (int) tmp);
        }
        ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentImg", ReflectionHacks.privateMethod(AbstractMonster.class, "getIntentImg").invoke(this));
        this.updateIntentTip();
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!this.isDead && !this.escaped) {
            if (this.animation != null && this.animation.type() == AbstractAnimation.Type.SPRITE) {
                this.animation.renderSprite(sb, this.drawX + this.animX, this.drawY + this.animY + AbstractDungeon.sceneOffsetY);
            } else if (this.atlas == null) {
                sb.setColor(this.tint.color);
                sb.draw(this.img, this.drawX - (float) this.img.getWidth() * Settings.scale / 2.0F + this.animX, this.drawY + this.animY + AbstractDungeon.sceneOffsetY, (float) this.img.getWidth() * Settings.scale, (float) this.img.getHeight() * Settings.scale, 0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, this.flipVertical);
            } else {
                this.state.update(Gdx.graphics.getDeltaTime());
                this.state.apply(this.skeleton);
                this.skeleton.updateWorldTransform();
                this.skeleton.setPosition(this.drawX + this.animX, this.drawY + this.animY + AbstractDungeon.sceneOffsetY);
                this.skeleton.setColor(this.tint.color);
                this.skeleton.setFlip(this.flipHorizontal, this.flipVertical);
                sb.end();
                CardCrawlGame.psb.begin();
                AbstractMonster.sr.draw(CardCrawlGame.psb, this.skeleton);
                CardCrawlGame.psb.end();
                sb.begin();
                sb.setBlendFunction(770, 771);
            }

            if (!this.isDying && !this.isEscaping && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.player.isDead && this.intent != Intent.NONE && !Settings.hideCombatElements) {
                this.renderIntentVfxBehind(sb);
                this.renderIntent(sb);
                this.renderIntentVfxAfter(sb);
                this.renderDamageRange(sb);
            }

            this.hb.render(sb);
            this.intentHb.render(sb);
            this.healthHb.render(sb);
        }

        if (!AbstractDungeon.player.isDead) {
            this.renderHealth(sb);
            this.renderName(sb);
        }
    }

    @Override
    public void rollMove() {
        this.targetMonster = AbstractDungeon.getRandomMonster();
        this.getMove(AbstractDungeon.cardRandomRng.random(99));
    }

    @SpireOverride
    public void updateIntentTip() {
        PowerTip intentTip = ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentTip");
        boolean isMultiDmg = ReflectionHacks.getPrivate(this, AbstractMonster.class, "isMultiDmg");
        int intentDmg = ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentDmg");
        int intentMultiAmt = ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentMultiAmt");
        switch (this.intent) {
            case ATTACK:
                intentTip.header = TEXT[0];
                if (isMultiDmg) {
                    intentTip.body = TEXT[1] + intentDmg + TEXT[2] + intentMultiAmt + TEXT[3];
                } else {
                    intentTip.body = TEXT[4] + intentDmg + TEXT[5];
                }
                intentTip.img = ReflectionHacks.privateMethod(AbstractMonster.class, "getAttackIntentTip").invoke(this);
                break;
            case ATTACK_BUFF:
                intentTip.header = TEXT[6];
                if (isMultiDmg) {
                    intentTip.body = TEXT[7] + intentDmg + TEXT[2] + intentMultiAmt + TEXT[8];
                } else {
                    intentTip.body = TEXT[9] + intentDmg + TEXT[5];
                }
                intentTip.img = ImageMaster.INTENT_ATTACK_BUFF;
                break;
            case ATTACK_DEBUFF:
                intentTip.header = TEXT[10];
                intentTip.body = TEXT[11] + intentDmg + TEXT[5];
                intentTip.img = ImageMaster.INTENT_ATTACK_DEBUFF;
                break;
            case ATTACK_DEFEND:
                intentTip.header = TEXT[0];
                if (isMultiDmg) {
                    intentTip.body = TEXT[12] + intentDmg + TEXT[2] + intentMultiAmt + TEXT[3];
                } else {
                    intentTip.body = TEXT[12] + intentDmg + TEXT[5];
                }
                intentTip.img = ImageMaster.INTENT_ATTACK_DEFEND;
                break;
            case BUFF:
                intentTip.header = TEXT[10];
                intentTip.body = TEXT[19];
                intentTip.img = ImageMaster.INTENT_BUFF;
                break;
            case DEBUFF:
                intentTip.header = TEXT[10];
                intentTip.body = TEXT[20];
                intentTip.img = ImageMaster.INTENT_DEBUFF;
                break;
            case STRONG_DEBUFF:
                intentTip.header = TEXT[10];
                intentTip.body = TEXT[21];
                intentTip.img = ImageMaster.INTENT_DEBUFF2;
                break;
            case DEFEND:
                intentTip.header = TEXT[13];
                intentTip.body = TEXT[22];
                intentTip.img = ImageMaster.INTENT_DEFEND;
                break;
            case DEFEND_DEBUFF:
                intentTip.header = TEXT[13];
                intentTip.body = TEXT[23];
                intentTip.img = ImageMaster.INTENT_DEFEND;
                break;
            case DEFEND_BUFF:
                intentTip.header = TEXT[13];
                intentTip.body = TEXT[24];
                intentTip.img = ImageMaster.INTENT_DEFEND_BUFF;
                break;
            case ESCAPE:
                intentTip.header = TEXT[14];
                intentTip.body = TEXT[25];
                intentTip.img = ImageMaster.INTENT_ESCAPE;
                break;
            case MAGIC:
                intentTip.header = TEXT[15];
                intentTip.body = TEXT[26];
                intentTip.img = ImageMaster.INTENT_MAGIC;
                break;
            case SLEEP:
                intentTip.header = TEXT[16];
                intentTip.body = TEXT[27];
                intentTip.img = ImageMaster.INTENT_SLEEP;
                break;
            case STUN:
                intentTip.header = TEXT[17];
                intentTip.body = TEXT[28];
                intentTip.img = ImageMaster.INTENT_STUN;
                break;
            case UNKNOWN:
                intentTip.header = TEXT[18];
                intentTip.body = TEXT[29];
                intentTip.img = ImageMaster.INTENT_UNKNOWN;
                break;
            case NONE:
                intentTip.header = "";
                intentTip.body = "";
                intentTip.img = ImageMaster.INTENT_UNKNOWN;
                break;
            default:
                intentTip.header = "NOT SET";
                intentTip.body = "NOT SET";
                intentTip.img = ImageMaster.INTENT_UNKNOWN;
        }
        ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentTip", intentTip);
    }

    @Override
    public void renderTip(SpriteBatch sb) {
        this.tips.clear();
        if (this.intentAlphaTarget == 1.0F && this.intent != Intent.NONE) {
            this.tips.add(ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentTip"));
        }
        for (AbstractPower p : this.powers) {
            if (p.region48 != null) {
                this.tips.add(new PowerTip(p.name, p.description, p.region48));
                continue;
            }
            this.tips.add(new PowerTip(p.name, p.description, p.img));
        }
        if (!this.tips.isEmpty()) {
            if (this.hb.cX + this.hb.width / 2.0F < TIP_X_THRESHOLD) {
                TipHelper.queuePowerTips(this.hb.cX + this.hb.width / 2.0F + TIP_OFFSET_R_X, this.hb.cY + TipHelper.calculateAdditionalOffset(this.tips, this.hb.cY), this.tips);
            } else {
                TipHelper.queuePowerTips(this.hb.cX - this.hb.width / 2.0F + TIP_OFFSET_L_X, this.hb.cY + TipHelper.calculateAdditionalOffset(this.tips, this.hb.cY), this.tips);
            }
        }
    }
}
