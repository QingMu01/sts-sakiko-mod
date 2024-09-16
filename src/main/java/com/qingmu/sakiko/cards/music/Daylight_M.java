package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

public class Daylight_M extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Daylight_M.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Daylight_M.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = CARD_STRINGS.UPGRADE_DESCRIPTION;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public Daylight_M() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, RARITY, TARGET);
        this.baseMagicNumber = 3;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded){
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void applyPowers() {
        this.isMagicNumberModified = false;
        int realBaseMagicNumber = this.baseMagicNumber;
        this.baseMagicNumber += PowerHelper.getPowerAmount(KirameiPower.POWER_ID);
        super.applyPowers();
        if (this.upgraded){
            this.magicNumber = this.baseMagicNumber;
        }else {
            this.magicNumber = realBaseMagicNumber;
        }
        this.baseMagicNumber = realBaseMagicNumber;
        this.isMagicNumberModified = (this.baseMagicNumber != this.magicNumber);
    }

    @Override
    public void play() {
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            this.addToTop(new ApplyPowerAction(mo, this.music_source
                    , new WeakPower(mo, Math.max(this.magicNumber,this.baseMagicNumber), false), Math.max(this.magicNumber,this.baseMagicNumber), true, AbstractGameAction.AttackEffect.NONE));
            this.addToTop(new ApplyPowerAction(mo, this.music_source
                    , new VulnerablePower(mo, Math.max(this.magicNumber,this.baseMagicNumber), false), Math.max(this.magicNumber,this.baseMagicNumber), true, AbstractGameAction.AttackEffect.NONE));
        }

    }
}
