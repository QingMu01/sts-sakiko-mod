package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.TwoInOneAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.stances.CreatorStance;
import com.qingmu.sakiko.utils.ModNameHelper;

public class TwoInOne extends AbstractSakikoCard {
    public static final String ID = ModNameHelper.make(TwoInOne.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/TwoInOne2.png";
    private static final String IMG_PATH_BACK = "SakikoModResources/img/cards/sakiko/TwoInOne1.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;


    public TwoInOne() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);

        this.initBaseAttr(1, 0, 0, 0);
        this.setUpgradeAttr(0, 0, 0, 0);
    }

    @Override
    public void applyPowers() {
        if (AbstractDungeon.player.stance.ID.equals(CreatorStance.STANCE_ID)){
            this.loadCardImage(IMG_PATH);
        }else {
            this.loadCardImage(IMG_PATH_BACK);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new TwoInOneAction(p));
    }
}
