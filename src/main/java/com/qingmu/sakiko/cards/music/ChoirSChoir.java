package com.qingmu.sakiko.cards.music;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.actions.watcher.JudgementAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.vfx.combat.GiantTextEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

public class ChoirSChoir extends AbstractMusic {

    public static final String ID = ModNameHelper.make(ChoirSChoir.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/ChoirSChoir.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public ChoirSChoir() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
        this.baseMagicNumber = 10;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded){
            this.upgradeName();
            this.upgradeMagicNumber(10);
        }
    }
    @Override
    public void applyPowers() {
        int realBaseMagicNumber = this.baseMagicNumber;
        this.baseMagicNumber += realBaseMagicNumber + (PowerHelper.getPowerAmount(KirameiPower.POWER_ID) * 5);
        this.magicNumber = this.baseMagicNumber;
        super.applyPowers();
        this.baseMagicNumber = realBaseMagicNumber;
        this.isMagicNumberModified = (this.magicNumber != this.baseMagicNumber);
    }


    @Override
    public void play() {
        if (this.music_target != null && !this.music_target.isDeadOrEscaped()){
            this.addToTop(new JudgementAction(this.music_target, Math.max(this.magicNumber,this.baseMagicNumber)));
            this.addToTop(new VFXAction(new GiantTextEffect(this.music_target.hb.cX, this.music_target.hb.cY)));
            this.addToTop(new WaitAction(0.8F));
            this.addToTop(new VFXAction(new WeightyImpactEffect(this.music_target.hb.cX, this.music_target.hb.cY, Color.GOLD.cpy())));

        }
    }
}
