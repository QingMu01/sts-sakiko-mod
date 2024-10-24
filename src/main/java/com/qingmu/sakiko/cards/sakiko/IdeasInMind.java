package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.stances.CreatorStance;
import com.qingmu.sakiko.utils.ModNameHelper;

public class IdeasInMind extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(IdeasInMind.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/IdeasInMind.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public IdeasInMind() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 5, 0);
        this.setUpgradeAttr(1, 0, 3, 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.submitActionsToBot(
                new GainBlockAction(p, p, this.block),
                new ChangeStanceAction(CreatorStance.STANCE_ID)
        );
    }
}
