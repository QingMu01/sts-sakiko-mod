package com.qingmu.sakiko.cards.choose;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.inteface.SakikoModEnable;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

@SakikoModEnable(enable = false)
public class Musical extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Musical.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Hype.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Musical(int amount) {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);

        this.initBaseAttr(-2, 0, 0, amount);
        this.setUpgradeAttr(-2, 0, 0, 1);
    }

    public Musical() {
        this(1);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {

    }

    @Override
    public void onChoseThisOption() {
        this.addToBot(new ApplyPowerAction(DungeonHelper.getPlayer(), DungeonHelper.getPlayer(), new KirameiPower(DungeonHelper.getPlayer(), this.magicNumber), this.magicNumber));
    }
}
