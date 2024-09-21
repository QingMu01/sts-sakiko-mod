package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

public class Mas_uerade extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Mas_uerade.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Mas_uerade.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public Mas_uerade() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initBaseAttr(0, 6, 0, 2);

        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_ATTACK);

        this.isMultiDamage = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
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
        this.baseDamage += PowerHelper.getPowerAmount(KirameiPower.POWER_ID) * this.magicNumber;
        super.calculateCardDamage(mo);
        this.baseDamage = realBaseDamage;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    @Override
    public void play() {
        this.addToTop(new DamageAllEnemiesAction(this.music_source, this.multiDamage, this.damageType, AbstractGameAction.AttackEffect.NONE));
        this.addToTop(new VFXAction(this.music_source, new CleaveEffect(), 0.1F));
        this.addToTop(new SFXAction("ATTACK_HEAVY"));

    }
}
