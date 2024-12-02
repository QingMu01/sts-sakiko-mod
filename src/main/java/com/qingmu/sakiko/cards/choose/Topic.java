package com.qingmu.sakiko.cards.choose;

import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.SpotlightPlayerEffect;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.inteface.SakikoModEnable;
import com.qingmu.sakiko.utils.ModNameHelper;

@SakikoModEnable(enable = false)
public class Topic extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Topic.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Hype.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Topic(int amount) {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);

        this.initBaseAttr(-2, 0, 0, amount * 10);
        this.setUpgradeAttr(-2, 0, 0, amount * 15);
    }

    public Topic() {
        this(1);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {

    }

    @Override
    public void onChoseThisOption() {
        AbstractDungeon.effectList.add(new RainingGoldEffect(this.magicNumber * 2, true));
        AbstractDungeon.effectsQueue.add(new SpotlightPlayerEffect());
        this.addToBot(new GainGoldAction(this.magicNumber));
    }
}
