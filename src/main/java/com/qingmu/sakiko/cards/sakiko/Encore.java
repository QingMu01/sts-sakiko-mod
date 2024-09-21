package com.qingmu.sakiko.cards.sakiko;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.EncorePower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Encore extends AbstractSakikoCard {
    public static final String ID = ModNameHelper.make(Encore.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Encore.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Encore() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(2, 0, 0, 0);
        this.exhaust = true;

        FlavorText.AbstractCardFlavorFields.boxColor.get(this).set(new Color(1.0F,136.0F / 255.0F,153.0F / 255.0F,1.0F));
        FlavorText.AbstractCardFlavorFields.textColor.get(this).set(new Color(1.0F,1.0F,1.0F,1.0F));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new EncorePower(p, 1)));
    }

}
