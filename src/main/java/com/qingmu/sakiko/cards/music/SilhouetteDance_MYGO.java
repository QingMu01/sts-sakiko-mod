package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.action.common.DamageCallbackAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class SilhouetteDance_MYGO extends AbstractMusic {

    public static final String ID = ModNameHelper.make(SilhouetteDance_MYGO.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/SilhouetteDance_MYGO.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public SilhouetteDance_MYGO() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_ATTACK);

        this.initMusicAttr(6, 0, 2, 1);
    }

    @Override
    public void applyPowers() {
        AbstractPower krkrdkdk = DungeonHelper.getPlayer().getPower(KirameiPower.POWER_ID);
        if (krkrdkdk != null) {
            krkrdkdk.amount *= this.magicNumber;
        }
        this.applyPowersToMusicNumber();
        this.baseDamage = this.musicNumber;
        super.applyPowers();
        this.isDamageModified = (this.musicNumber != this.baseMusicNumber);
        if (krkrdkdk != null) {
            krkrdkdk.amount /= this.magicNumber;
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        AbstractPower krkrdkdk = DungeonHelper.getPlayer().getPower(KirameiPower.POWER_ID);
        if (krkrdkdk != null) {
            krkrdkdk.amount *= this.magicNumber;
        }
        this.applyPowersToMusicNumber();
        this.baseDamage = this.musicNumber;
        super.calculateCardDamage(mo);
        if (krkrdkdk != null) {
            krkrdkdk.amount /= this.magicNumber;
        }

    }

    @Override
    public void play() {
        this.addToTop(new DamageCallbackAction(this.m_target, new DamageInfo(this.m_source, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HEAVY, (damageAmount) -> {
            if (damageAmount > 0) {
                this.addToBot(new GainBlockAction(this.m_source, damageAmount));
            }
        }));
    }
}
