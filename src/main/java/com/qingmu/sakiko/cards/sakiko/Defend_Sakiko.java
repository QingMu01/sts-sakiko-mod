package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Defend_Sakiko extends AbstractSakikoCard {
    public static final String ID = ModNameHelper.make(Defend_Sakiko.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Defend_Sakiko.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Defend_Sakiko() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 5, 0);
        this.setUpgradeAttr(1, 0, 3, 0);

        this.tags.add(CardTags.STARTER_DEFEND);
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (Settings.isDebug) {
            this.addToBot(new GainBlockAction(p, p, 999));
        } else {
            this.addToBot(new GainBlockAction(p, p, this.block));
        }
    }
}
