package com.qingmu.sakiko.cards.sakiko;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.ReproduceAction;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.utils.ModNameHelper;

import static com.qingmu.sakiko.patch.SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD;

public class Reproduce extends CustomCard {

    public static final String ID = ModNameHelper.make(Reproduce.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Reproduce.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final int COST = 1;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = QINGMU_SAKIKO_CARD;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Reproduce() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = 1;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ReproduceAction(p, (this.magicNumber < 0 ? this.baseMagicNumber : this.magicNumber)));

    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        CardGroup cardGroup = MusicBattleFiledPatch.DrawMusicPile.drawMusicPile.get(AbstractDungeon.player);
        this.cantUseMessage = CARD_STRINGS.EXTENDED_DESCRIPTION[0];
        return !cardGroup.isEmpty();
    }
}
