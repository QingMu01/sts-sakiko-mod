package com.qingmu.sakiko.cards.music;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.actions.watcher.JudgementAction;
import com.megacrit.cardcrawl.vfx.combat.GiantTextEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

public class ChoirSChoir extends AbstractMusic {

    public static final String ID = ModNameHelper.make(ChoirSChoir.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/ChoirSChoir.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public ChoirSChoir() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 10);

        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
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
        int realBaseMagicNumber = this.baseMagicNumber + this.extraNumber;
        this.baseMagicNumber += realBaseMagicNumber + (PowerHelper.getPowerAmount(KirameiPower.POWER_ID) * 5);
        this.magicNumber = this.baseMagicNumber;
        super.applyPowers();
        this.baseMagicNumber = realBaseMagicNumber;
        this.isMagicNumberModified = (this.magicNumber != this.baseMagicNumber);
    }


    @Override
    public void play() {
        if (this.music_target != null && !this.music_target.isDeadOrEscaped()){
            this.addToTop(new JudgementAction(this.music_target, this.magicNumber));
            this.addToTop(new VFXAction(new GiantTextEffect(this.music_target.hb.cX, this.music_target.hb.cY)));
            this.addToTop(new WaitAction(0.8F));
            this.addToTop(new VFXAction(new WeightyImpactEffect(this.music_target.hb.cX, this.music_target.hb.cY, Color.GOLD.cpy())));

        }
    }
}
