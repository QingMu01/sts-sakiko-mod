package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.action.OnlyOneselfAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.KokoroNoKabePower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class OnlyOneself extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(OnlyOneself.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/OnlyOneself.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public OnlyOneself() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 0, 0);
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        AbstractPower power = AbstractDungeon.player.getPower(KokoroNoKabePower.POWER_ID);
        if (power != null) {
            KokoroNoKabePower kokoroNoKabePower = (KokoroNoKabePower) power;
            this.appendDescription(kokoroNoKabePower.limit - kokoroNoKabePower.counter);
        } else {
            this.appendDescription(10);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new OnlyOneselfAction());
    }
}
