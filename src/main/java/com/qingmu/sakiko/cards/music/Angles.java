package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

public class Angles extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Angles.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Angles.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Angles() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initBaseAttr(0, 4, 0, 2);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_ATTACK);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
            this.upgradeDamage(2);
        }
    }

    @Override
    public void applyPowers() {
        int realBaseMagicNumber = this.baseMagicNumber;
        this.baseMagicNumber += PowerHelper.getPowerAmount(KirameiPower.POWER_ID) + this.extraNumber;
        this.magicNumber = this.baseMagicNumber;
        super.applyPowers();
        this.baseMagicNumber = realBaseMagicNumber;
        this.isMagicNumberModified = (this.magicNumber != this.baseMagicNumber);
    }

    @Override
    public void play() {
        int count = this.magicNumber;
        for (int i = 0; i < count; i++) {
            this.addToTop(new DamageAction(this.music_target, new DamageInfo(this.music_source, this.damage, this.damageType),true));
        }
    }
}
