package com.qingmu.sakiko.monsters.boss;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateShakeAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
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
import com.qingmu.sakiko.action.common.SummonFriendlyMonsterAction;
import com.qingmu.sakiko.monsters.AbstractSakikoMonster;
import com.qingmu.sakiko.monsters.friendly.LinkedAnon;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.patch.filed.BossInfoFiled;
import com.qingmu.sakiko.patch.filed.FriendlyMonsterGroupFiled;
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
    private int bloodHitCount;
    private int buffLimit;
    private int moveCount = 0;
    private int buffCount = 0;
    private static final String IMG = "SakikoModResources/img/monster/sakikoBoss.png";

    private HeartAnimListener animListener = new HeartAnimListener();

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
            this.buffLimit = 4;
        }
    }

    @Override
    public void usePreBattleAction() {
        BossInfoFiled.canBattleWithDemonSakiko.set(DungeonHelper.getPlayer(), false);
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
        if (currentHealth <= this.maxHealth / 2 && this.phase == 0) {
            this.phase++;
        }
        return this.phase == 0;
    }

    @Override
    protected void phaseSwitchLogic() {
        this.atlas = null;
        this.state.removeListener(this.animListener);
        this.hb.resize(200, 220);
        this.maxHealth = 1000;
        this.powers.removeIf(p -> p.type == AbstractPower.PowerType.DEBUFF);
        this.specialIntentList.add(0, new SpecialIntentAction.Builder()
                .setIntent(Intent.UNKNOWN)
                .setPredicate(m -> true)
                .setActions(() -> {
                    ArrayList<AbstractGameAction> actions = new ArrayList<>();
                    actions.add(new HealAction(this, this, this.maxHealth));
                    actions.add(new ApplyPowerAction(this, this, new InstinctPower(this, this.buffLimit), this.buffLimit));
                    actions.add(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
                    actions.add(new ForceWaitAction(1.5f));
                    actions.add(new SummonFriendlyMonsterAction(new LinkedAnon(-(DungeonHelper.getPlayer().drawX + (Settings.WIDTH - this.drawX) - DungeonHelper.getPlayer().hb_w - 20) / Settings.xScale, 0), true, -Settings.WIDTH));
                    return actions.toArray(new AbstractGameAction[0]);
                })
                .setCallback(ia -> this.halfDead = false)
                .build());
        this.getMove(0);
        this.createIntent();
    }

    @Override
    protected List<IntentAction> updateIntent() {
        ArrayList<IntentAction> intentActions = new ArrayList<>();
        intentActions.add(new IntentAction.Builder()

                .build());
        return intentActions;
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
                .setPredicate(m -> this.moveCount == 2)
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
                    actions.add(new HealAction(this, this, this.maxHealth / 2 - this.currentHealth));
                    return actions.toArray(new AbstractGameAction[0]);
                })
                .build());
        // 攻击联机爱音
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setMoveName(MOVES[1])
                .setPredicate(m -> {
                    MonsterGroup monsterGroup = FriendlyMonsterGroupFiled.friendlyMonsterGroup.get(AbstractDungeon.getCurrRoom());
                    if (monsterGroup != null) {
                        AbstractMonster target = monsterGroup.getMonster(LinkedAnon.ID);
                        return target != null && !target.isDead && !target.isDying && AbstractDungeon.aiRng.randomBoolean(0.4f);
                    }
                    return false;
                })
                .setIntent(Intent.MAGIC)
                .setDamageAmount(this.damage.get(0))
                .setActions(() -> new AbstractGameAction[]{
                        new AnimateJumpAction(this),
                        new AnimationDamageAction(FriendlyMonsterGroupFiled.friendlyMonsterGroup.get(AbstractDungeon.getCurrRoom()).getMonster(LinkedAnon.ID), this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                })
                .build());
        return specialIntentActions;
    }

}
