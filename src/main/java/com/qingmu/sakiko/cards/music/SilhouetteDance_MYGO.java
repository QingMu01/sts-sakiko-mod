package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.action.common.DamageCallbackAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class SilhouetteDance_MYGO extends AbstractMusic {

    public static final String ID = ModNameHelper.make(SilhouetteDance_MYGO.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/SilhouetteDance_MYGO.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public SilhouetteDance_MYGO() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_ATTACK);

        this.initMusicAttr(8, 4, 3, 2);
    }

    @Override
    public void applyPowers() {
        AbstractPower strength = AbstractDungeon.player.getPower(KirameiPower.POWER_ID);
        if (strength != null) {
            strength.amount *= this.magicNumber;
        }

        super.applyPowers();
        if (strength != null) {
            strength.amount /= this.magicNumber;
        }
    }

    @Override
    public void play() {
        this.addToTop(new DamageCallbackAction(this.m_target, new DamageInfo(this.m_source, this.musicNumber, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HEAVY, (damageAmount) -> {
            if (damageAmount > 0) {
                this.addToTop(new GainBlockAction(this.m_source, damageAmount));
            }
        }));
    }
}
