package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.MusicCardFinder;
import com.qingmu.sakiko.utils.PowerHelper;

import java.util.ArrayList;

public class CompositionAction extends AbstractGameAction {
    private boolean retrieveCard = false;
    private final AbstractCard.CardType cardType = SakikoEnum.CardTypeEnum.MUSIC;
    private boolean upgrade;

    public CompositionAction(boolean isUpgrade) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = 1;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.upgrade = isUpgrade;
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> generatedCards = this.generateCardChoices();
        this.addToBot(new ChooseOneAction(generatedCards));
        this.isDone = true;
    }

    private ArrayList<AbstractCard> generateCardChoices() {
        ArrayList<AbstractCard> derp = new ArrayList<>();
        while (derp.size() != 3) {
            boolean dupe = false;
            AbstractCard tmp = MusicCardFinder.returnTrulyRandomCardInCombat();
            for (AbstractCard c : derp) {
                if (c.cardID.equals(tmp.cardID)) {
                    dupe = true;
                    break;
                }
            }
            if (!dupe && !tmp.hasTag(SakikoEnum.CardTagEnum.COUNTER)) {
                AbstractCard card = tmp.makeCopy();
                card.purgeOnUse = true;
                if (this.upgrade) {
                    for (int i = 0; i < calculateUpgrade(((AbstractMusic) tmp).getEnchanted()); i++) {
                        card.upgrade();
                    }
                }
                derp.add(card);
            }
        }
        return derp;
    }

    private int calculateUpgrade(int required) {
        return PowerHelper.getPowerAmount(KirameiPower.POWER_ID) / required;
    }

}
