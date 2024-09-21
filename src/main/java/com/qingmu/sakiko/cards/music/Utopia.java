package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

public class Utopia extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Utopia.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Utopia.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Utopia() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initBaseAttr(0, 6, 0, 4);

        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_ATTACK);
        this.baseDamage = 6;
        this.baseMagicNumber = 4;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(2);
        }
    }

    @Override
    public void applyPowers() {
        int realBaseDamage = this.baseDamage + this.extraNumber;
        this.baseDamage += PowerHelper.getPowerAmount(KirameiPower.POWER_ID) * this.magicNumber;
        super.applyPowers();
        this.baseDamage = realBaseDamage;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBaseDamage = this.baseDamage + this.extraNumber;
        this.baseDamage += PowerHelper.getPowerAmount(KirameiPower.POWER_ID) * Math.max(this.magicNumber, this.baseMagicNumber);
        super.calculateCardDamage(mo);
        this.baseDamage = realBaseDamage;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    @Override
    public void play() {
        this.addToTop(new DamageAction(this.music_target, new DamageInfo(this.music_source, this.damage, this.damageType)));
    }
}
