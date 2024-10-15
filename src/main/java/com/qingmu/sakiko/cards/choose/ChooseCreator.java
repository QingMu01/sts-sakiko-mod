package com.qingmu.sakiko.cards.choose;

import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.inteface.SakikoModEnable;
import com.qingmu.sakiko.stances.CreatorStance;
import com.qingmu.sakiko.utils.ModNameHelper;

@SakikoModEnable(enable = false)
public class ChooseCreator extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(ChooseCreator.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/ChoirSChoir.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public ChooseCreator() {
        super(ID, IMG_PATH, TYPE, CardColor.COLORLESS, RARITY, TARGET);

        this.initBaseAttr(-2, 0, 0, 0);
        this.setUpgradeAttr(-2, 0, 0, 0);

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ChangeStanceAction(CreatorStance.STANCE_ID));
    }

    @Override
    public void onChoseThisOption() {
        this.addToBot(new ChangeStanceAction(CreatorStance.STANCE_ID));
    }
}

