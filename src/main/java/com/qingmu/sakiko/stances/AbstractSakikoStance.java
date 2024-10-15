package com.qingmu.sakiko.stances;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public abstract class AbstractSakikoStance extends AbstractStance {

    public static final Map<String, AbstractSakikoStance> stanceMap = new HashMap<>();

    protected final StanceStrings stanceString;
    protected final String NAME;
    protected final String[] DESCRIPTIONS;

    public AbstractSakikoStance(String id) {
        this.stanceString = CardCrawlGame.languagePack.getStanceString(id);
        this.ID = id;
        this.NAME = stanceString.NAME;
        this.DESCRIPTIONS = stanceString.DESCRIPTION;

        this.name = stanceString.NAME;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    protected void submitActionsToTop(AbstractGameAction... actions) {
        if (actions != null) {
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    for (AbstractGameAction action : actions) {
                        this.addToTop(action);
                    }
                    this.isDone = true;
                }
            });
        }
    }

    protected void submitActionsToBot(AbstractGameAction... actions) {
        if (actions != null) {
            if (actions.length > 1) {
                Collections.reverse(Arrays.asList(actions));
            }
            AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    for (AbstractGameAction action : actions) {
                        this.addToTop(action);
                    }
                    this.isDone = true;
                }
            });
        }
    }

    public abstract AbstractSakikoStance makeCopy();
}
