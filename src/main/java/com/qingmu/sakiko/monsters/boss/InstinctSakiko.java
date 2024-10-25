package com.qingmu.sakiko.monsters.boss;

import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.HeartAnimListener;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.HeartMegaDebuffEffect;
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

        // 重击
        this.damage.add(new DamageInfo(this, 60));
        // 连击
        this.damage.add(new DamageInfo(this, 5));
    }

    @Override
    public void usePreBattleAction() {
        BossInfoFiled.canBattleWithDemonSakiko.set(CardCrawlGame.dungeon, false);
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
    protected List<IntentAction> initEffectiveIntentActions() {
        ArrayList<IntentAction> intentActions = new ArrayList<>();
        intentActions.add(new IntentAction.Builder()
                .setWeight(50)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(0))
                .setActions(() -> new AbstractGameAction[]{
                        new DamageAction(AbstractDungeon.player, this.damage.get(0))
                })
                .build());
        intentActions.add(new IntentAction.Builder()
                .setID("multi")
                .setWeight(50)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(1))
                .setMultiplier(this.multiCount)
                .setActions(() -> {
                    AbstractGameAction[] actions = new AbstractGameAction[this.multiCount * 2];
                    for (int i = 0; i < actions.length; i++) {
                        if (i % 2 == 0) {
                            actions[i] = new AnimateFastAttackAction(this);
                        } else {
                            actions[i] = new DamageAction(AbstractDungeon.player, this.damage.get(1));
                        }
                    }
                    return actions;
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
                        new WaitAction(1.0f),
                        new TalkAction(this, DIALOG[0], 1.0F, 2.0F)
                })
                .setCallback(ia -> {
                    this.atlas = null;
                    this.state.removeListener(this.animListener);
                    this.addToBot(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
                })
                .build());
        return specialIntentActions;
    }

}
