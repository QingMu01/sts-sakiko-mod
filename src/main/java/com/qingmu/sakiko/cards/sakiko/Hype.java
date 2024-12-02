package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.common.XAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.cards.choose.CP;
import com.qingmu.sakiko.cards.choose.Musical;
import com.qingmu.sakiko.cards.choose.Topic;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;

public class Hype extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Hype.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Hype.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Hype() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(-1, 0, 0, 0, new CP(), new Musical(), new Topic());
        this.setUpgradeAttr(-1, 0, 0, 0, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new XAction(p, this.freeToPlayOnce, this.energyOnUse, effect -> {
            ArrayList<AbstractCard> cards = new ArrayList<>();
            cards.add(new CP(effect));
            cards.add(new Musical(effect));
            cards.add(new Topic(effect));
            if (this.upgraded) {
                for (AbstractCard card : cards) {
                    card.upgrade();
                }
            }
            this.addToBot(new ChooseOneAction(cards));
        }));
    }
}
