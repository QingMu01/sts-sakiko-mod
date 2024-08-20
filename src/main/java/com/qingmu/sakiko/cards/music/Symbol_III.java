package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

import static com.qingmu.sakiko.patch.SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD;

public class Symbol_III extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Symbol_III.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/skill.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    private static final int COST = 1;

    private static final CardColor COLOR = QINGMU_SAKIKO_CARD;
    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Symbol_III() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, COLOR, RARITY, TARGET);
        this.tags.add(CardTags.HEALING);
        this.baseMagicNumber = 2;
        this.upgradeRequestNumber = 1;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (this.timesUpgraded < 2) {
            this.upgradeMagicNumber(1);
            this.timesUpgraded++;
            this.name = NAME + "+" + this.timesUpgraded;
            this.upgraded = true;
            this.initializeTitle();

        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        this.addToBot(new HealAction(p, p, this.magicNumber < 0 ? this.baseMagicNumber : this.magicNumber));
    }
}
