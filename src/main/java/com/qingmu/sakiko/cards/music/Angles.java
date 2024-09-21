package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

public class Angles extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Angles.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Angles.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Angles() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_ATTACK);
        this.baseMagicNumber = 2;
        this.baseDamage = 4;
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
        int count = Math.max(this.magicNumber, this.baseMagicNumber);
        for (int i = 0; i < count; i++) {
            this.addToTop(new DamageAction(this.music_target, new DamageInfo(this.music_source, this.damage, this.damageType),true));
        }
    }
}
