package com.qingmu.sakiko.monsters.boss;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateShakeAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.HeartAnimListener;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.BloodShotEffect;
import com.megacrit.cardcrawl.vfx.combat.HeartBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.HeartMegaDebuffEffect;
import com.megacrit.cardcrawl.vfx.combat.ViceCrushEffect;
import com.qingmu.sakiko.action.common.AnimationDamageAction;
import com.qingmu.sakiko.action.common.ForceWaitAction;
import com.qingmu.sakiko.action.common.PlayBGMAction;
import com.qingmu.sakiko.action.common.SummonFriendlyMonsterAction;
import com.qingmu.sakiko.action.effect.OverrideBackgroundEffect;
import com.qingmu.sakiko.constant.ColorHelp;
import com.qingmu.sakiko.constant.MusicHelper;
import com.qingmu.sakiko.monsters.AbstractSakikoMonster;
import com.qingmu.sakiko.monsters.friendly.LinkedAnon;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.patch.filed.BossInfoFiled;
import com.qingmu.sakiko.powers.monster.InstinctPower;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;

public class InstinctSakiko extends AbstractSakikoMonster {

    public static final String ID = ModNameHelper.make(InstinctSakiko.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;

    protected int baseAttack = 8, baseSlash = 12, baseMulti = 6, multiCount = 10;
    private int multi = this.multiCount;
    private int bloodHitCount;
    private int buffLimit;
    private int moveCount = 0;
    private int buffCount = 0;
    private static final String IMG = "SakikoModResources/img/monster/InstinctSakiko.png";

    private HeartAnimListener animListener = new HeartAnimListener();

    private OverrideBackgroundEffect bg = new OverrideBackgroundEffect("SakikoModResources/img/bg/bg_ansk.png");

    public InstinctSakiko(float x, float y) {
        super(NAME, ID, IMG, x, y, 476.0F, 410.0F);
        this.loadAnimation("images/npcs/heart/skeleton.atlas", "images/npcs/heart/skeleton.json", 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTimeScale(1.5F);
        this.state.addListener(this.animListener);
        this.type = EnemyType.BOSS;
        this.setHp(800);
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(800);
        } else {
            this.setHp(750);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.damage.add(new DamageInfo(this, 45));
            this.damage.add(new DamageInfo(this, 2));
            this.bloodHitCount = 15;
            this.buffLimit = 6;
        } else {
            this.damage.add(new DamageInfo(this, 40));
            this.damage.add(new DamageInfo(this, 2));
            this.bloodHitCount = 12;
            this.buffLimit = 8;
        }
    }

    @Override
    public void usePreBattleAction() {
        BossInfoFiled.canBattleWithDemonSakiko.set(DungeonHelper.getPlayer(), false);
        AbstractDungeon.getCurrRoom().cannotLose = true;
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_ENDING");
        int invincibleAmt = 300;
        if (AbstractDungeon.ascensionLevel >= 19) {
            invincibleAmt -= 100;
        }
        int beatAmount = 1;
        if (AbstractDungeon.ascensionLevel >= 19) {
            ++beatAmount;
        }
        this.addToBot(new ApplyPowerAction(this, this, new InvinciblePower(this, invincibleAmt), invincibleAmt));
        this.addToBot(new ApplyPowerAction(this, this, new BeatOfDeathPower(this, beatAmount), beatAmount));
    }

    @Override
    protected boolean canPhaseSwitch() {
        if (this.currentHealth <= this.maxHealth / 2 && this.phase == 0) {
            this.phase++;
            if (this.currentHealth <= 0) {
                this.halfDead = true;
            }
            return true;
        }
        return false;
    }

    @Override
    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
            this.addToBot(new TalkAction(this, DIALOG[3], 1.0F, 2.0F));
            this.onBossVictoryLogic();
            this.onFinalBossVictoryLogic();
            CardCrawlGame.stopClock = true;
        } else {
            this.phaseSwitchLogic();
        }
    }

    @Override
    protected void phaseSwitchLogic() {
        this.atlas = null;
        this.state.removeListener(this.animListener);
        this.updateHitbox(this.hb_x, DungeonHelper.getPlayer().hb_y, 200, 220);
        this.refreshHitboxLocation();
        this.refreshIntentHbLocation();
        this.healthBarUpdatedEvent();
        this.maxHealth = 1000;
        this.powers.removeIf(p -> p.type == AbstractPower.PowerType.DEBUFF);
        this.specialIntentList.add(0, new SpecialIntentAction.Builder()
                .setIntent(Intent.UNKNOWN)
                .setPredicate(m -> true)
                .setActions(() -> {
                    ArrayList<AbstractGameAction> actions = new ArrayList<>();
                    actions.add(new VFXAction(this.bg));
                    actions.add(new PlayBGMAction(MusicHelper.OTEME, this));
                    actions.add(new HealAction(this, this, this.maxHealth));
                    actions.add(new ApplyPowerAction(this, this, new InstinctPower(this, this.buffLimit), this.buffLimit));
                    actions.add(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
                    actions.add(new ForceWaitAction(1.5f));
                    actions.add(new SummonFriendlyMonsterAction(LinkedAnon.ID, true));
                    return actions.toArray(new AbstractGameAction[0]);
                })
                .setCallback(ia -> this.halfDead = false)
                .build());
        this.getMove(0);
        this.createIntent();
        AbstractDungeon.getCurrRoom().cannotLose = false;
    }

    @Override
    protected List<IntentAction> updateIntent() {
        ArrayList<IntentAction> intentActions = new ArrayList<>();
        intentActions.add(new IntentAction.Builder()
                .setID("multi")
                .setWeight(50)
                .setIntent(Intent.ATTACK)
                .setMultiplier(this.multi)
                .setDamageAmount(this.damage.get(1))
                .setActions(() -> this.generateMultiAttack(this.damage.get(1), this.multi))
                .build());
        intentActions.add(new IntentAction.Builder()
                .setWeight(25)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(1))
                .setMultiplier(this.bloodHitCount)
                .setActions(() -> {
                    ArrayList<AbstractGameAction> actions = new ArrayList<>();
                    actions.add(new VFXAction(new BloodShotEffect(this.hb.cX, this.hb.cY, DungeonHelper.getPlayer().hb.cX, DungeonHelper.getPlayer().hb.cY, this.bloodHitCount), 0.25F));
                    for (int i = 0; i < this.bloodHitCount; i++) {
                        actions.add(new DamageAction(DungeonHelper.getPlayer(), this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
                    }
                    return actions.toArray(new AbstractGameAction[0]);
                })
                .build());
        intentActions.add(new IntentAction.Builder()
                .setWeight(25)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(0))
                .setActions(() -> new AbstractGameAction[]{
                        new VFXAction(new ViceCrushEffect(DungeonHelper.getPlayer().hb.cX, DungeonHelper.getPlayer().hb.cY), 0.5F),
                        new DamageAction(DungeonHelper.getPlayer(), this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                })
                .build());
        intentActions.add(new IntentAction.Builder()
                .setWeight(25)
                .setIntent(Intent.BUFF)
                .setActions(() -> {
                    ArrayList<AbstractGameAction> actions = new ArrayList<>();
                    int additionalAmount = 0;
                    if (this.hasPower("Strength") && this.getPower("Strength").amount < 0) {
                        additionalAmount = -this.getPower("Strength").amount;
                    }
                    actions.add(new VFXAction(new BorderFlashEffect(ColorHelp.SAKIKO_COLOR.cpy())));
                    actions.add(new VFXAction(new HeartBuffEffect(this.hb.cX, this.hb.cY)));
                    actions.add(new ApplyPowerAction(this, this, new StrengthPower(this, additionalAmount + 2), additionalAmount + 2));
                    if (this.getPower(InstinctPower.POWER_ID).amount > 1) {
                        actions.add(new ReducePowerAction(this, this, InstinctPower.POWER_ID, 1));
                    }
                    switch (GameActionManager.turn % 4) {
                        case 0:
                            actions.add(new ApplyPowerAction(this, this, new ArtifactPower(this, 3), 3));
                            break;
                        case 1:
                            actions.add(new ApplyPowerAction(this, this, new BeatOfDeathPower(this, 1), 1));
                            break;
                        case 2:
                            if (!this.hasPower(PainfulStabsPower.POWER_ID)) {
                                actions.add(new ApplyPowerAction(this, this, new PainfulStabsPower(this)));
                                break;
                            }
                        case 3:
                            actions.add(new ApplyPowerAction(this, this, new StrengthPower(this, 3), 3));
                            break;
                        default:
                            actions.add(new ApplyPowerAction(this, this, new StrengthPower(this, 50), 50));
                    }
                    return actions.toArray(new AbstractGameAction[0]);
                })
                .build());
        return intentActions;
    }

    @Override
    protected IntentAction getRandomEffectiveIntent(int random) {
        if (this.phase != 0) {
            for (IntentAction action : this.intentList) {
                if (action.ID.equals("multi")) {
                    int buff = 0;
                    for (AbstractPower power : this.powers) {
                        if (power.type == AbstractPower.PowerType.BUFF && !power.ID.equals(InstinctPower.POWER_ID) && !power.ID.equals(InvinciblePower.POWER_ID) && !power.ID.equals(BeatOfDeathPower.POWER_ID)) {
                            buff++;
                        }
                    }
                    action.multiplier = this.multiCount + buff;
                    this.multi = action.multiplier;
                }
            }
        }
        return super.getRandomEffectiveIntent(random);
    }

    @Override
    protected List<IntentAction> initIntent() {
        // 一阶段，拟态心脏
        ArrayList<IntentAction> intentActions = new ArrayList<>();
        intentActions.add(new IntentAction.Builder()
                .setWeight(50)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(1))
                .setMultiplier(this.bloodHitCount)
                .setCallback(ia -> this.moveCount++)
                .setRepeatInterval(2)
                .setActions(() -> {
                    ArrayList<AbstractGameAction> actions = new ArrayList<>();
                    actions.add(new VFXAction(new BloodShotEffect(this.hb.cX, this.hb.cY, DungeonHelper.getPlayer().hb.cX, DungeonHelper.getPlayer().hb.cY, this.bloodHitCount), 0.25F));
                    for (int i = 0; i < this.bloodHitCount; i++) {
                        actions.add(new DamageAction(DungeonHelper.getPlayer(), this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
                    }
                    return actions.toArray(new AbstractGameAction[0]);
                })
                .build());
        intentActions.add(new IntentAction.Builder()
                .setWeight(50)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(0))
                .setCallback(ia -> this.moveCount++)
                .setRepeatInterval(2)
                .setActions(() -> new AbstractGameAction[]{
                        new VFXAction(new ViceCrushEffect(DungeonHelper.getPlayer().hb.cX, DungeonHelper.getPlayer().hb.cY), 0.5F),
                        new DamageAction(DungeonHelper.getPlayer(), this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                })
                .build());
        return intentActions;
    }

    @Override
    protected List<SpecialIntentAction> initSpecialIntent() {
        ArrayList<SpecialIntentAction> specialIntentActions = new ArrayList<>();
        // 心脏五福临门
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setPredicate(m -> true)
                .setIntent(Intent.STRONG_DEBUFF)
                .setActions(() -> new AbstractGameAction[]{
                        new VFXAction(new HeartMegaDebuffEffect()),
                        new ApplyPowerAction(DungeonHelper.getPlayer(), this, new VulnerablePower(DungeonHelper.getPlayer(), 2, true), 2),
                        new ApplyPowerAction(DungeonHelper.getPlayer(), this, new WeakPower(DungeonHelper.getPlayer(), 2, true), 2),
                        new ApplyPowerAction(DungeonHelper.getPlayer(), this, new FrailPower(DungeonHelper.getPlayer(), 2, true), 2),
                        new MakeTempCardInDrawPileAction(new Dazed(), 1, true, false, false, Settings.WIDTH * 0.2F, Settings.HEIGHT / 2.0F),
                        new MakeTempCardInDrawPileAction(new Slimed(), 1, true, false, false, Settings.WIDTH * 0.35F, Settings.HEIGHT / 2.0F),
                        new MakeTempCardInDrawPileAction(new Wound(), 1, true, false, false, Settings.WIDTH * 0.5F, Settings.HEIGHT / 2.0F),
                        new MakeTempCardInDrawPileAction(new Burn(), 1, true, false, false, Settings.WIDTH * 0.65F, Settings.HEIGHT / 2.0F),
                        new MakeTempCardInDrawPileAction(new VoidCard(), 1, true, false, false, Settings.WIDTH * 0.8F, Settings.HEIGHT / 2.0F),
                })
                .build());
        // 心脏强化
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setPredicate(m -> this.phase == 0 && this.moveCount == 2)
                .setIntent(Intent.BUFF)
                .setRemovable(m -> false)
                .setActions(() -> {
                    ArrayList<AbstractGameAction> actions = new ArrayList<>();
                    int additionalAmount = 0;
                    if (this.hasPower("Strength") && this.getPower("Strength").amount < 0) {
                        additionalAmount = -this.getPower("Strength").amount;
                    }
                    actions.add(new VFXAction(new BorderFlashEffect(new Color(0.8F, 0.5F, 1.0F, 1.0F))));
                    actions.add(new VFXAction(new HeartBuffEffect(this.hb.cX, this.hb.cY)));
                    actions.add(new ApplyPowerAction(this, this, new StrengthPower(this, additionalAmount + 2), additionalAmount + 2));
                    switch (this.buffCount) {
                        case 0:
                            actions.add(new ApplyPowerAction(this, this, new ArtifactPower(this, 2), 2));
                            break;
                        case 1:
                            actions.add(new ApplyPowerAction(this, this, new BeatOfDeathPower(this, 1), 1));
                            break;
                        case 2:
                            actions.add(new ApplyPowerAction(this, this, new PainfulStabsPower(this)));
                            break;
                        case 3:
                            actions.add(new ApplyPowerAction(this, this, new StrengthPower(this, 10), 10));
                            break;
                        default:
                            actions.add(new ApplyPowerAction(this, this, new StrengthPower(this, 50), 50));
                    }
                    return actions.toArray(new AbstractGameAction[0]);
                })
                .setCallback(ia -> {
                    this.moveCount = 0;
                    ++this.buffCount;
                })
                .build());
        // 事已至此，先吃饭吧
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setMoveName(MOVES[0])
                .setPredicate(m -> this.phase != 0 && this.currentHealth < this.maxHealth / 2)
                .setIntent(Intent.BUFF)
                .setActions(() -> {
                    ArrayList<AbstractGameAction> actions = new ArrayList<>();
                    actions.add(new TalkAction(this, DIALOG[2], 2.0F, 2.0F));
                    actions.add(new AnimateJumpAction(this));
                    actions.add(new AnimateShakeAction(this, 0.5f, 0.5f));
                    actions.add(new AnimateJumpAction(this));
                    actions.add(new HealAction(this, this, 10));
                    actions.add(new HealAction(DungeonHelper.getPlayer(), this, 10));
                    AbstractMonster monster = DungeonHelper.getFriendlyMonster(LinkedAnon.ID);
                    if (monster != null) {
                        actions.add(new HealAction(monster, this, 10));
                    }
                    return actions.toArray(new AbstractGameAction[0]);
                })
                .build());
        // 攻击联机爱音
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setMoveName(MOVES[1])
                .setPredicate(m -> {
                    MonsterGroup monsterGroup = DungeonHelper.getFriendlyMonsterGroup();
                    if (monsterGroup != null) {
                        AbstractMonster target = monsterGroup.getMonster(LinkedAnon.ID);
                        return target != null && !target.isDead && !target.isDying && AbstractDungeon.aiRng.randomBoolean(0.4f);
                    }
                    return false;
                })
                .setIntent(Intent.MAGIC)
                .setDamageAmount(MathUtils.ceil(DungeonHelper.getPlayer().maxHealth / 2.0f))
                .setActions(() -> {
                    ArrayList<AbstractGameAction> actions = new ArrayList<>();
                    actions.add(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
                    actions.add(new AnimationDamageAction(DungeonHelper.getFriendlyMonster(LinkedAnon.ID), new DamageInfo(this, MathUtils.ceil(DungeonHelper.getPlayer().maxHealth / 2.0f)), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    return actions.toArray(new AbstractGameAction[0]);
                })
                .setCallback(ia -> {
                    if (AbstractDungeon.aiRng.randomBoolean()) {
                        this.specialIntentList.add(0, new SpecialIntentAction.Builder()
                                .setIntent(Intent.STUN)
                                .setActions(() -> new AbstractGameAction[]{new TalkAction(this, DIALOG[4], 1.0F, 2.0F)})
                                .build());
                    }
                })
                .build());
        return specialIntentActions;
    }

}
