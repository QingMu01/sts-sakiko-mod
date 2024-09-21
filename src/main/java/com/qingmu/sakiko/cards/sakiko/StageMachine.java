package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.ResetMusicalNoteAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.MusicalNotePower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

public class StageMachine extends AbstractSakikoCard {
    public static final String ID = ModNameHelper.make(StageMachine.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/StageMachine.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public StageMachine() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 0, 4);
        this.tags.add(SakikoEnum.CardTagEnum.MUSICAL_NOTE);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(2);
        }
    }

    @Override
    protected void applyPowersToBlock() {
        int powerAmount = PowerHelper.getPowerAmount2(MusicalNotePower.POWER_ID);
        this.baseBlock = powerAmount * this.magicNumber;
        super.applyPowersToBlock();
        this.appendDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, this.block));
        this.addToBot(new ResetMusicalNoteAction());
    }
}
