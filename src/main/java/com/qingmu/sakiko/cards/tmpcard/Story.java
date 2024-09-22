package com.qingmu.sakiko.cards.tmpcard;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.StoryAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Story extends AbstractSakikoCard {
    public static final String ID = ModNameHelper.make(Story.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/tmpcard/Story.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Story() {
        super(ID, IMG_PATH, TYPE, CardColor.COLORLESS, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 0);

        this.exhaust = true;
        FlavorText.AbstractCardFlavorFields.boxColor.get(this).set(new Color(136.0F / 255.0F, 17.0F / 255.0F, 68.0F / 255.0F, 1.0F));
        FlavorText.AbstractCardFlavorFields.textColor.get(this).set(new Color(1.0F, 1.0F, 1.0F, 1.0F));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDescription();
            this.exhaust = false;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new StoryAction());
    }

}
