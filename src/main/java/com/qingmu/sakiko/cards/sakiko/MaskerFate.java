package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.common.CleanMusicQueueAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class MaskerFate extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(MaskerFate.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/MaskerFate.png";


    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public MaskerFate() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);

        this.initBaseAttr(1, 0, 0, 1);
        this.setUpgradeAttr(0, 0, 0, 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new CleanMusicQueueAction(p, false, (integer -> {
            if (integer > 0) {
                this.addToBot(new GainEnergyAction(integer * this.magicNumber));
            }
        })));
    }
}
