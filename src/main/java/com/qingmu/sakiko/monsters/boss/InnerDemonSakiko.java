package com.qingmu.sakiko.monsters.boss;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import com.qingmu.sakiko.action.RenameMonsterAction;
import com.qingmu.sakiko.action.common.ExprAction;
import com.qingmu.sakiko.action.common.PlayBGMAction;
import com.qingmu.sakiko.action.effect.OverrideBackgroundEffect;
import com.qingmu.sakiko.action.monster.CrychicHonorAction;
import com.qingmu.sakiko.action.monster.ExhaustDemonSakikoCardAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.cards.music.monster.AbolitionCase;
import com.qingmu.sakiko.cards.music.monster.DarkHeaven;
import com.qingmu.sakiko.cards.music.monster.Daten;
import com.qingmu.sakiko.cards.music.monster.Kings;
import com.qingmu.sakiko.cards.other.DistantPast;
import com.qingmu.sakiko.constant.MusicHelper;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.monsters.AbstractSakikoMonster;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.monsters.member.*;
import com.qingmu.sakiko.patch.filed.BossInfoFiled;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.powers.monster.*;
import com.qingmu.sakiko.utils.ActionHelper;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.*;

public class InnerDemonSakiko extends AbstractSakikoMonster {

    public static final String ID = ModNameHelper.make(InnerDemonSakiko.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;

    private int baseAttack = 25, baseSlash = 60, baseMultiDamage = 5, multiCount = 12, baseBlock = 20, baseHp = 200;

    private static final String IMG = "SakikoModResources/img/monster/sakikoBoss.png";

    private int moveCount = 0;

    private boolean repeatSummon = false;

    private final OverrideBackgroundEffect bg = new OverrideBackgroundEffect(Arrays.asList(
            ImageMaster.loadImage("SakikoModResources/img/bg/innerSakiko_p1.png"),
            ImageMaster.loadImage("SakikoModResources/img/bg/innerSakiko_p2.png"),
            ImageMaster.loadImage("SakikoModResources/img/bg/innerSakiko_p3.png"),
            ImageMaster.loadImage("SakikoModResources/img/bg/innerSakiko_p4.png")));

    private float timer = 0f;

    public InnerDemonSakiko(float x, float y) {
        super(MOVES[0] + NAME, ID, IMG, x, y);
        this.type = EnemyType.BOSS;
        this.canPlayMusic = true;
        this.setHp(this.baseHp);

        this.damage.add(new DamageInfo(this, this.baseAttack));
        // 重击
        this.damage.add(new DamageInfo(this, this.baseSlash));
        // 连击
        this.damage.add(new DamageInfo(this, this.baseMultiDamage));
    }

    @Override
    public void usePreBattleAction() {
        BossInfoFiled.canBattleWithDemonSakiko.set(DungeonHelper.getPlayer(), false);
        AbstractDungeon.getCurrRoom().cannotLose = true;
        this.addToBot(new ApplyPowerAction(this, this, new ResiliencePower(this, 2), 2));
        this.addToBot(new ApplyPowerAction(this, this, new MusicalAbilityPower(this)));
        ActionHelper.effectToList(bg);
    }

    @Override
    public void die(boolean triggerRelics) {
        super.die(triggerRelics);
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            this.onBossVictoryLogic();
            this.onFinalBossVictoryLogic();
            CardCrawlGame.stopClock = true;
        }
    }

    @Override
    protected IntentAction getRandomEffectiveIntent(int random) {
        if (this.phase != 1 && MusicBattleFiledPatch.MusicQueue.musicQueue.get(this).isEmpty()) {
            this.obtainMusic(this.readyMusic());
        }
        return super.getRandomEffectiveIntent(random);
    }

    @Override
    protected boolean canPhaseSwitch() {
        if (this.currentHealth <= 0 && !this.halfDead) {
            // 阶段转换
            this.phase++;
            this.halfDead = true;
            if (this.phase < 4) {
                return true;
            }
            this.halfDead = false;
            AbstractDungeon.getCurrRoom().cannotLose = false;
        }
        return false;
    }

    @Override
    protected void phaseSwitchLogic() {
        for (AbstractPower power : this.powers) {
            power.onDeath();
        }
        for (AbstractRelic relic : DungeonHelper.getPlayer().relics) {
            relic.onMonsterDeath(this);
        }
        this.addToTop(new ClearCardQueueAction());
        // 静音bgm
        CardCrawlGame.music.justFadeOutTempBGM();
        this.isPlayBGM = false;

        if (this.phase == 1) {
            this.maxHealth = 400;
            this.addToBot(new TalkAction(this, DIALOG[1], 2.0F, 2.0F));
        } else if (this.phase == 2) {
            this.maxHealth = 400;
            this.addToBot(new TalkAction(this, DIALOG[3], 2.0F, 2.0F));
        } else if (this.phase == 3) {
            this.maxHealth = 600;
            this.addToBot(new TalkAction(this, DIALOG[5], 2.0F, 2.0F));
        } else if (this.phase == 4) {
            this.addToBot(new TalkAction(this, DIALOG[7], 2.0F, 2.0F));
        }
        Set<String> powerIdsToKeep = new HashSet<>(Arrays.asList(
                IdealFukkenPower.POWER_ID,
                FakeKirameiPower.POWER_ID,
                BeatOfDeathPower.POWER_ID,
                ThornsPower.POWER_ID,
                SharpHidePower.POWER_ID
        ));

        this.powers.removeIf(power -> !powerIdsToKeep.contains(power.ID));
        this.powers.removeIf(power -> power.ID.equals(StrengthPower.POWER_ID) && power.amount < 0);
        // 清除歌单
        MusicBattleFiledPatch.MusicQueue.musicQueue.get(this).clear();
        // 清除爪牙
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!monster.id.equals(ID) && !monster.isDead && !monster.isDying) {
                this.addToTop(new HideHealthBarAction(monster));
                this.addToTop(new SuicideAction(monster));
                this.addToTop(new VFXAction(monster, new InflameEffect(monster), 0.2F));
            }
        }
        this.applyPowers();
        // 设置阶段切换意图
        this.specialIntentList.add(0, new SpecialIntentAction.Builder()
                .setIntent(Intent.UNKNOWN)
                .setPredicate(m -> true)
                .setActions(() -> new AbstractGameAction[]{
                        new HealAction(this, this, this.maxHealth),
                        new RenameMonsterAction(this, MOVES[this.phase] + NAME)
                })
                .setCallback(ia -> {
                    this.halfDead = false;
                    if (this.phase == 1) {
                        this.addToBot(new PlayBGMAction(MusicHelper.AME, this));
                        this.addToBot(new TalkAction(this, DIALOG[2], 2.0F, 2.0F));
                        this.addToBot(new ApplyPowerAction(this, this, new InvinciblePower(this, 200), 200));
                        this.addToBot(new ExprAction(bg::changeBackground));
                    } else if (this.phase == 2) {
                        this.addToBot(new ApplyPowerAction(this, this, new ResiliencePower(this, 2, true), 2));
                        this.addToBot(new ApplyPowerAction(this, this, new MusicalAbilityPower(this)));
                        this.addToBot(new ExprAction(() -> {
                            bg.stopShader();
                            bg.changeBackground();
                        }));
                    } else if (this.phase == 3) {
                        this.addToBot(new PlayBGMAction(MusicHelper.MOONLIGHT, this));
                        this.addToBot(new TalkAction(this, DIALOG[6], 2.0F, 2.0F));
                        this.addToBot(new ApplyPowerAction(this, this, new FadingPower(this, (this.maxHealth / 200) + 2), (this.maxHealth / 200) + 2));
                        this.addToBot(new ApplyPowerAction(this, this, new InvinciblePower(this, 200), 200));
                        this.addToBot(new ApplyPowerAction(this, this, new MusicalAbilityPower(this)));
                        this.addToBot(new CanLoseAction());
                        this.addToBot(new ExprAction(bg::changeBackground));
                    }
                })
                .build());
        // 立刻更新意图
        this.getMove(0);
        this.createIntent();
    }

    public List<IntentAction> updateIntent() {
        ArrayList<IntentAction> intentActions = new ArrayList<>();
        // 更换行动模式
        switch (this.phase) {
            case 1: {
                // 二阶段 低攻击性，塞牌叠减伤
                intentActions.add(new IntentAction.Builder()
                        .setWeight(25)
                        .setIntent(Intent.BUFF)
                        .setActions(() -> new AbstractGameAction[]{
                                new ApplyPowerAction(this, this, new SharpHidePower(this, 3), 3)
                        })
                        .build());
                intentActions.add(new IntentAction.Builder()
                        .setWeight(25)
                        .setIntent(Intent.BUFF)
                        .setActions(() -> new AbstractGameAction[]{
                                new ApplyPowerAction(this, this, new ThornsPower(this, 2), 2)
                        })
                        .build());
                intentActions.add(new IntentAction.Builder()
                        .setWeight(25)
                        .setIntent(Intent.BUFF)
                        .setActions(() -> new AbstractGameAction[]{
                                new ApplyPowerAction(this, this, new BeatOfDeathPower(this, 1), 1)
                        })
                        .build());
                intentActions.add(new IntentAction.Builder()
                        .setWeight(10)
                        .setIntent(Intent.ATTACK)
                        .setDamageAmount(this.damage.get(0))
                        .setActions(() -> new AbstractGameAction[]{
                                new AnimateSlowAttackAction(this),
                                new DamageAction(DungeonHelper.getPlayer(), this.damage.get(0))
                        })
                        .build());
                intentActions.add(new IntentAction.Builder()
                        .setWeight(15)
                        .setIntent(Intent.ATTACK)
                        .setDamageAmount(this.damage.get(1))
                        .setActions(() -> new AbstractGameAction[]{
                                new AnimateSlowAttackAction(this),
                                new DamageAction(DungeonHelper.getPlayer(), this.damage.get(1))
                        })
                        .build());
                break;
            }
            case 2: {
                // 三阶段 中攻击性，清除二阶段塞牌，塞歌牌提高攻击力
                this.addToBot(new ExhaustDemonSakikoCardAction());
                intentActions.add(new IntentAction.Builder()
                        .setWeight(30)
                        .setMoveName(MOVES[5])
                        .setIntent(Intent.STRONG_DEBUFF)
                        .setActions(() -> new AbstractGameAction[]{
                                new MakeTempCardInDiscardAction(new AbolitionCase(), 1)
                        })
                        .build());
                intentActions.add(new IntentAction.Builder()
                        .setWeight(20)
                        .setIntent(Intent.BUFF)
                        .setActions(() -> new AbstractGameAction[]{
                                new RemoveDebuffsAction(this),
                                new ApplyPowerAction(this, this, new FakeKirameiPower(this, 5), 5),
                                new ApplyPowerAction(this, this, new IdealFukkenPower(this, 1), 1)
                        })
                        .build());
                intentActions.add(new IntentAction.Builder()
                        .setWeight(25)
                        .setIntent(Intent.ATTACK)
                        .setDamageAmount(this.damage.get(2))
                        .setMultiplier(this.multiCount)
                        .setActions(() -> this.generateMultiAttack(this.damage.get(2), this.multiCount))
                        .build());
                intentActions.add(new IntentAction.Builder()
                        .setWeight(25)
                        .setIntent(Intent.ATTACK)
                        .setDamageAmount(this.damage.get(1))
                        .setActions(() -> new AbstractGameAction[]{
                                new AnimateSlowAttackAction(this),
                                new DamageAction(DungeonHelper.getPlayer(), this.damage.get(1))
                        })
                        .build());
                break;
            }
            case 3: {
                // 四阶段 高攻击性
                this.addToBot(new ExhaustDemonSakikoCardAction());
                intentActions.add(new IntentAction.Builder()
                        .setWeight(50)
                        .setIntent(Intent.ATTACK)
                        .setDamageAmount(this.damage.get(2))
                        .setMultiplier(this.multiCount)
                        .setCallback(ia -> this.moveCount++)
                        .setRepeatInterval(2)
                        .setActions(() -> this.generateMultiAttack(this.damage.get(2), this.multiCount))
                        .build());
                intentActions.add(new IntentAction.Builder()
                        .setWeight(50)
                        .setIntent(Intent.ATTACK)
                        .setDamageAmount(this.damage.get(1))
                        .setCallback(ia -> this.moveCount++)
                        .setRepeatInterval(2)
                        .setActions(() -> new AbstractGameAction[]{
                                new AnimateSlowAttackAction(this),
                                new DamageAction(DungeonHelper.getPlayer(), this.damage.get(1))
                        })
                        .build());
                break;
            }
        }
        return intentActions;
    }

    @Override
    protected List<IntentAction> initIntent() {
        // 一阶段：召唤队友，辅助队友
        ArrayList<IntentAction> intentActions = new ArrayList<>();
        intentActions.add(new IntentAction.Builder()
                .setWeight(40)
                .setIntent(Intent.DEFEND)
                .setActions(() -> {
                    List<AbstractGameAction> actions = new ArrayList<>();
                    for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                        if (!monster.isDead && !monster.isDying) {
                            actions.add(new GainBlockAction(monster, this, this.baseBlock, true));
                        }
                    }
                    return actions.toArray(new AbstractGameAction[0]);
                })
                .build());
        intentActions.add(new IntentAction.Builder()
                .setWeight(10)
                .setIntent(Intent.BUFF)
                .setActions(() -> {
                    List<AbstractGameAction> actions = new ArrayList<>();
                    for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                        if (!monster.isDead && !monster.isDying) {
                            actions.add(new HealAction(monster, this, this.baseBlock * 2));
                        }
                    }
                    return actions.toArray(new AbstractGameAction[0]);
                })
                .build());
        intentActions.add(new IntentAction.Builder()
                .setWeight(25)
                .setIntent(Intent.BUFF)
                .setActions(() -> {
                    ArrayList<AbstractGameAction> actions = new ArrayList<>();
                    for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                        actions.add(new ApplyPowerAction(monster, this, new StrengthPower(monster, 2), 2));
                    }
                    return actions.toArray(new AbstractGameAction[0]);
                })
                .build());
        intentActions.add(new IntentAction.Builder()
                .setWeight(25)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(0))
                .setActions(() -> new AbstractGameAction[]{
                        new AnimateSlowAttackAction(this),
                        new DamageAction(DungeonHelper.getPlayer(), this.damage.get(0))
                })
                .build());
        return intentActions;
    }

    @Override
    protected List<SpecialIntentAction> initSpecialIntent() {
        ArrayList<SpecialIntentAction> specialIntentActions = new ArrayList<>();
        // 一阶段，召唤队友，全体队友共同承受伤害
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setPredicate(m -> this.phase == 0)
                .setMoveName(MOVES[4])
                .setIntent(Intent.UNKNOWN)
                .setActions(() -> {
                    List<AbstractGameAction> actions = new ArrayList<>();
                    // 演出效果
                    actions.add(new PlayBGMAction(MusicHelper.NINGENUTA, this));
                    actions.add(new SFXAction("MONSTER_COLLECTOR_SUMMON"));
                    actions.add(new TalkAction(this, DIALOG[0], 2.0F, 2.0F));

                    //召唤队友
                    actions.add(new SpawnMonsterAction(new TomoriMonster(-(this.hb_w / Settings.xScale) - 20, (this.hb_h / Settings.yScale) + 100), true));
                    actions.add(new SpawnMonsterAction(new TakiMonster((this.hb_w / Settings.xScale) + 20, (this.hb_h / Settings.yScale) + 100), true));
                    actions.add(new SpawnMonsterAction(new SoyoMonster(-(this.hb_w / Settings.xScale) - 20, -(this.hb_h * 0.25f / Settings.yScale)), true));
                    actions.add(new SpawnMonsterAction(new MutsumiMonster((this.hb_w / Settings.xScale) + 20, -(this.hb_h * 0.25f / Settings.yScale)), true));
                    actions.add(new CrychicHonorAction(this));
                    return actions.toArray(new AbstractGameAction[0]);
                })
                .build());
        // 二阶段，塞牌
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setPredicate(m -> {
                    ArrayList<AbstractCard> cards = new ArrayList<>();
                    cards.addAll(CardsHelper.dp().group);
                    cards.addAll(CardsHelper.h().group);
                    cards.addAll(CardsHelper.dsp().group);
                    for (AbstractCard card : cards) {
                        if (card.cardID.equals(DistantPast.ID) && !card.tags.contains(SakikoEnum.CardTagEnum.MOONLIGHT)) {
                            return false;
                        }
                    }
                    return this.phase == 1 && AbstractDungeon.aiRng.randomBoolean();
                })
                .setMoveName(MOVES[5])
                .setIntent(Intent.STRONG_DEBUFF)
                .setRemovable(m -> false)
                .setActions(() -> new AbstractGameAction[]{
                        new MakeTempCardInDiscardAction(new DistantPast(), 1)
                })
                .build());
        // 三阶段 召唤队友
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setPredicate(m -> this.phase == 2 && AbstractDungeon.getCurrRoom().monsters.monsters.stream().noneMatch(mo -> mo.hasPower(MinionPower.POWER_ID)))
                .setMoveName(MOVES[6])
                .setIntent(Intent.UNKNOWN)
                .setRemovable(m -> false)
                .setActions(() -> {
                    ArrayList<AbstractGameAction> actions = new ArrayList<>();
                    actions.add(new PlayBGMAction(MusicHelper.AVEMUJICA, this));
                    actions.add(new SFXAction("MONSTER_COLLECTOR_SUMMON"));
                    if (this.repeatSummon) {
                        actions.add(new TalkAction(this, DIALOG[8], 2.0F, 2.0F));
                    } else {
                        actions.add(new TalkAction(this, DIALOG[4], 2.0F, 2.0F));
                    }
                    actions.add(new SpawnMonsterAction(new UikaMonster(-(this.hb_w / Settings.xScale) - 20, (this.hb_h / Settings.yScale) + 100), true));
                    actions.add(new SpawnMonsterAction(new MutsumiMonster((this.hb_w / Settings.xScale) + 20, (this.hb_h / Settings.yScale) + 100), true));
                    actions.add(new SpawnMonsterAction(new UmiriMonster(-(this.hb_w / Settings.xScale) - 20, -(this.hb_h * 0.25f / Settings.yScale)), true));
                    actions.add(new SpawnMonsterAction(new NyamuchiMonster((this.hb_w / Settings.xScale) + 20, -(this.hb_h * 0.25f / Settings.yScale)), true));
                    if (!this.hasPower(AveMujicaDictatorshipPower.POWER_ID)) {
                        actions.add(new ApplyPowerAction(this, this, new AveMujicaDictatorshipPower(this)));
                    }
                    return actions.toArray(new AbstractGameAction[0]);
                })
                .setCallback(ia -> this.repeatSummon = true)
                .build());
        // 四阶段 五福临门
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setPredicate(m -> this.phase == 3)
                .setIntent(Intent.STRONG_DEBUFF)
                .setActions(() -> new AbstractGameAction[]{
                        new MakeTempCardInDiscardAction(new DistantPast(), 1),
                        new MakeTempCardInDiscardAction(new AbolitionCase(), 2),
                        new ApplyPowerAction(DungeonHelper.getPlayer(), this, new VulnerablePower(DungeonHelper.getPlayer(), 2, true), 2),
                        new ApplyPowerAction(DungeonHelper.getPlayer(), this, new WeakPower(DungeonHelper.getPlayer(), 2, true), 2),
                        new ApplyPowerAction(DungeonHelper.getPlayer(), this, new FrailPower(DungeonHelper.getPlayer(), 2, true), 2),
                })
                .build());
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setPredicate(m -> this.phase == 3 && this.moveCount == 2)
                .setIntent(Intent.STRONG_DEBUFF)
                .setActions(() -> {
                    AbstractCard card;
                    if (AbstractDungeon.aiRng.randomBoolean()) {
                        card = new DistantPast();
                    } else {
                        card = new AbolitionCase();
                    }
                    return new AbstractGameAction[]{new MakeTempCardInDiscardAction(card, 1)};
                })
                .setCallback(ia -> this.moveCount = 0)
                .build());
        return specialIntentActions;
    }

    public AbstractMusic readyMusic() {
        AbstractMusic music;
        int rng = AbstractDungeon.aiRng.random(99);
        if (rng < 30) {
            music = new Daten();
        } else if (rng < 65) {
            music = new Kings();
        } else {
            music = new DarkHeaven();
        }
        return music;
    }
}
