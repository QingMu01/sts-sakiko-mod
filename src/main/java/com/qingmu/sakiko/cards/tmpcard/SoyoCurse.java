package com.qingmu.sakiko.cards.tmpcard;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.qingmu.sakiko.SakikoModCore;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.patch.anonmod.HeavyHelper;
import com.qingmu.sakiko.utils.ModNameHelper;


public class SoyoCurse extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(SoyoCurse.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/skill.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    private static final int COST = -2;

    private static final CardType TYPE = CardType.CURSE;
    private static final CardColor COLOR = CardColor.CURSE;
    private static final CardRarity RARITY = CardRarity.CURSE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public SoyoCurse() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
        }
    }

    @Override
    public void triggerWhenDrawn() {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ConstrictedPower(AbstractDungeon.player, AbstractDungeon.player, 5)));
        if (Loader.isModLoaded("AnonMod") && SakikoModCore.SAKIKO_CONFIG.getBool("enableAnonCard")) {
            HeavyHelper.applyHeavy(AbstractDungeon.player, AbstractDungeon.player, 3);
        }

        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            this.addToBot(new ApplyPowerAction(monster, AbstractDungeon.player, new ConstrictedPower(monster, AbstractDungeon.player, 5)));
            if (Loader.isModLoaded("AnonMod") && SakikoModCore.SAKIKO_CONFIG.getBool("enableAnonCard")) {
                HeavyHelper.applyHeavy(monster, AbstractDungeon.player, 3);
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.onChoseThisOption();
    }

}
