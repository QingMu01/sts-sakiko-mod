package com.qingmu.sakiko.cards.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.StoryAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Story extends AbstractSakikoCard {
    public static final String ID = ModNameHelper.make(Story.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/other/Story.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Story(boolean isUpgrade) {
        super(ID, IMG_PATH, TYPE, CardColor.COLORLESS, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 1);
        this.setUpgradeAttr(0, 0, 0, 0);
        this.setExhaust(true, true);
        if (isUpgrade) {
            this.upgrade();
        }
    }

    public Story() {
        this(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new StoryAction(this.magicNumber, this.upgraded));
    }

}
