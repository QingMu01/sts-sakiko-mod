package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Expose_RAS extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Expose_RAS.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Expose_RAS.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Expose_RAS() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 2);

        this.tags.add(SakikoEnum.CardTagEnum.COUNTER);
        this.tags.add(SakikoEnum.CardTagEnum.ENCORE);
    }

    @Override
    public void applyAmount() {
        this.amount = Math.min(this.amount, this.magicNumber);
        this.appendDescription(this.amount);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(2);
        }
    }

    @Override
    public void play() {
        for (int i = 0; i < this.amount; i++) {
            this.addToTop(new AbstractGameAction() {
                public void update() {
                    addToBot(new PlayTopCardAction(
                            (AbstractDungeon.getCurrRoom()).monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng), false));
                    this.isDone = true;
                }
            });
        }
    }
}
