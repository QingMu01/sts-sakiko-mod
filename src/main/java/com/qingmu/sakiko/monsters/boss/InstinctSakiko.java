package com.qingmu.sakiko.monsters.boss;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.BloodShotEffect;
import com.megacrit.cardcrawl.vfx.combat.HeartBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.HeartMegaDebuffEffect;
import com.megacrit.cardcrawl.vfx.combat.ViceCrushEffect;
import com.qingmu.sakiko.monsters.AbstractSakikoMonster;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.patch.filed.BossInfoFiled;
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
    private int moveCount = 0;
    private int buffCount = 0;
    private static final String IMG = "SakikoModResources/img/monster/sakikoBoss.png";

    private HeartAnimListener animListener = new HeartAnimListener();

    public InstinctSakiko(float x, float y) {
        super(NAME, ID, IMG, x, y);
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
        } else {
            this.damage.add(new DamageInfo(this, 40));
            this.damage.add(new DamageInfo(this, 2));
            this.bloodHitCount = 12;
        }
    }

    @Override
    public void usePreBattleAction() {
        BossInfoFiled.canBattleWithDemonSakiko.set(AbstractDungeon.player, false);
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
    public void damage(DamageInfo info) {
        super.damage(info);
        if (this.phase == 0 && this.currentHealth <= this.maxHealth / 2) {
            this.phase++;
            this.effectiveIntentAction = this.phaseSwitchAndUpdateIntentActions();
            this.atlas = null;
            this.state.removeListener(this.animListener);
            this.setHp(1000);
            this.addToBot(new HealAction(this, this, 1000));
            this.addToBot(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
        }
    }

    @Override
    protected List<IntentAction> phaseSwitchAndUpdateIntentActions() {
        ArrayList<IntentAction> intentActions = new ArrayList<>();
        return super.phaseSwitchAndUpdateIntentActions();
    }

    @Override
    protected List<IntentAction> initEffectiveIntentActions() {
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
                    actions.add(new VFXAction(new BloodShotEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.bloodHitCount), 0.25F));
                    for (int i = 0; i < this.bloodHitCount; i++) {
                        actions.add(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
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
                        new VFXAction(new ViceCrushEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.5F),
                        new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                })
                .build());
        return intentActions;
    }

    @Override
    protected List<SpecialIntentAction> initSpecialIntent() {
        ArrayList<SpecialIntentAction> specialIntentActions = new ArrayList<>();
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setPredicate(m -> true)
                .setIntent(Intent.STRONG_DEBUFF)
                .setActions(() -> new AbstractGameAction[]{
                        new VFXAction(new HeartMegaDebuffEffect()),
                        new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, 2, true), 2),
                        new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 2, true), 2),
                        new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 2, true), 2),
                        new MakeTempCardInDrawPileAction(new Dazed(), 1, true, false, false, Settings.WIDTH * 0.2F, Settings.HEIGHT / 2.0F),
                        new MakeTempCardInDrawPileAction(new Slimed(), 1, true, false, false, Settings.WIDTH * 0.35F, Settings.HEIGHT / 2.0F),
                        new MakeTempCardInDrawPileAction(new Wound(), 1, true, false, false, Settings.WIDTH * 0.5F, Settings.HEIGHT / 2.0F),
                        new MakeTempCardInDrawPileAction(new Burn(), 1, true, false, false, Settings.WIDTH * 0.65F, Settings.HEIGHT / 2.0F),
                        new MakeTempCardInDrawPileAction(new VoidCard(), 1, true, false, false, Settings.WIDTH * 0.8F, Settings.HEIGHT / 2.0F),
                })
                .build());
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
        return specialIntentActions;
    }

}
