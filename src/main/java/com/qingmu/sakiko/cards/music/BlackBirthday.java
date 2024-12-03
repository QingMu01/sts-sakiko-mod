package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.common.DamageCallbackAction;
import com.qingmu.sakiko.action.common.PlayerPlayedMusicAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.rewards.CardRemoveReward;
import com.qingmu.sakiko.rewards.CardUpgradeReward;
import com.qingmu.sakiko.utils.ModNameHelper;

public class BlackBirthday extends AbstractMusic {

    public static final String ID = ModNameHelper.make(BlackBirthday.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/BlackBirthday.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public boolean isPlayed = true;

    public BlackBirthday() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_ATTACK);
        this.tags.add(CardTags.HEALING);

        this.initMusicAttr(6, 4);
        this.setExhaust(true, true);
    }

    @Override
    public void applyPowers() {
        this.applyPowersToMusicNumber();
        this.baseDamage = this.musicNumber;
        super.applyPowers();
        this.isDamageModified = (this.musicNumber != this.baseMusicNumber);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.applyPowersToMusicNumber();
        this.baseDamage = this.musicNumber;
        super.calculateCardDamage(mo);
    }


    @Override
    public void play() {
        this.addToTop(new DamageCallbackAction(this.m_target, new DamageInfo(this.m_source, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.LIGHTNING, damageAmount -> {
            if ((this.m_target.isDying || this.m_target.currentHealth <= 0) && !this.m_target.halfDead && !this.m_target.hasPower("Minion")) {
                if (this.isPlayed){
                    AbstractDungeon.getCurrRoom().rewards.add(new CardUpgradeReward());
                }else {
                    AbstractDungeon.getCurrRoom().rewards.add(new CardRemoveReward());
                }
            }
            this.isPlayed = true;
        }));
    }

    @Override
    public void interruptReady() {
        this.isPlayed = false;
        this.addToTop(new PlayerPlayedMusicAction(this,true));
    }
}
