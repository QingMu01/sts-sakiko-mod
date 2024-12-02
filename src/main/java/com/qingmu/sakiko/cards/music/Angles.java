package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.MemberHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Angles extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Angles.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Angles.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Angles() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_ATTACK);

        this.initMusicAttr(3, 1, 1, 1);
    }

    @Override
    public void applyPowers() {
        this.applyPowersToMusicNumber();
        this.baseDamage = this.musicNumber;
        int realBaseMagicNumber = this.baseMagicNumber;
        this.baseMagicNumber += MemberHelper.getCount();
        super.applyPowers();
        this.magicNumber = this.baseMagicNumber;
        this.baseMagicNumber = realBaseMagicNumber;
        this.isMagicNumberModified = (this.magicNumber != this.baseMagicNumber);
        this.isDamageModified = (this.musicNumber != this.baseMusicNumber);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.applyPowersToMusicNumber();
        this.baseDamage = this.musicNumber;
        int realBaseMagicNumber = this.baseMagicNumber;
        this.baseMagicNumber += MemberHelper.getCount();
        super.calculateCardDamage(mo);
        this.magicNumber = this.baseMagicNumber;
        this.baseMagicNumber = realBaseMagicNumber;
    }

    @Override
    public void play() {
        AbstractGameAction[] actions = new AbstractGameAction[this.magicNumber];
        for (int i = 0; i < actions.length; i++) {
            actions[i] = new DamageAction(this.m_target, new DamageInfo(this.m_source, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        }
        this.submitActionsToTop(actions);
    }

    @Override
    public void interruptReady() {
        this.baseMagicNumber++;
    }
}
