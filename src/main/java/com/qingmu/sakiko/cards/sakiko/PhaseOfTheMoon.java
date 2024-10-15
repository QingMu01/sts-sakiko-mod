package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.CalmStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.stances.WrathStance;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.stances.CreatorStance;
import com.qingmu.sakiko.stances.PlayerStance;
import com.qingmu.sakiko.utils.ModNameHelper;

public class PhaseOfTheMoon extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(PhaseOfTheMoon.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/skill.png";


    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private String stance = NeutralStance.STANCE_ID;

    public PhaseOfTheMoon() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 0, 0);
        this.setUpgradeAttr(0, 0, 0, 0);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        int i = AbstractDungeon.actionManager.cardsPlayedThisTurn.size() % 4;
        switch (i) {
            case 0: {
                stance = CreatorStance.STANCE_ID;
                break;
            }
            case 1: {
                stance = PlayerStance.STANCE_ID;
                break;
            }
            case 2: {
                stance = CalmStance.STANCE_ID;
                break;
            }
            case 3: {
                stance = WrathStance.STANCE_ID;
                break;
            }
        }
        StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString(stance);
        this.appendDescription(stanceString.NAME);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ChangeStanceAction(stance));
    }
}
