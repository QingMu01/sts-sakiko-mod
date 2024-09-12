package com.qingmu.sakiko.monsters;

import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.qingmu.sakiko.SakikoModCore;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.SoundHelper;

public class UikaMonster extends AbstractMemberMonster {

    public static final String ID = ModNameHelper.make(UikaMonster.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    private static final String NAME = monsterStrings.NAME;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private static final String IMG = "SakikoModResources/img/monster/uika.png";

    public UikaMonster(float x, float y) {
        super(NAME, ID, IMG, x, y);
        this.powerful = 1;
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.powerful = 2;
        }
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        this.addToBot(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
        CardCrawlGame.sound.playV(SoundHelper.UIKA_INIT.name(),2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
    }

    @Override
    public void die() {
        super.die();
        this.addToBot(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
        CardCrawlGame.sound.playV(SoundHelper.UIKA_DEATH.name(),2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 0: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, this.powerful, true)));
                break;
            }
            case 1: {
                this.addToBot(new AnimateJumpAction(this));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, this.powerful, true)));
                break;
            }
            case 2: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
                break;
            }
            case 3: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
                this.addToBot(new GainBlockAction(this, this.baseBlock));
                break;
            }
            case 4: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageCallbackAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SMASH, (amount) -> {
                    if (amount > 0) {
                        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, this.powerful, true)));
                    }
                }));
            }
        }
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (i < 30) {
            if (i < 15) {
                this.setMove((byte) 0, Intent.DEBUFF);
            } else {
                this.setMove((byte) 1, Intent.DEBUFF);
            }
        } else if (i < 60) {
            this.setMove((byte) 2, Intent.ATTACK, this.damage.get(1).base);
        } else if (i < 80) {
            this.setMove((byte) 3, Intent.ATTACK_DEFEND, this.damage.get(0).base);
        } else {
            this.setMove((byte) 4, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
        }
    }
}
