package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class RIOT_RAS extends AbstractMusic {

    public static final String ID = ModNameHelper.make(RIOT_RAS.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/RIOT_RAS.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public RIOT_RAS() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 1);

        this.tags.add(SakikoEnum.CardTagEnum.COUNTER);
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDescription();
            this.exhaust = false;
        }
    }

    @Override
    public void triggerInBufferUsedCard(AbstractCard card) {
        this.amount++;
    }

    @Override
    public void applyAmount() {
        this.appendDescription(this.amount);
    }

    @Override
    public void play() {
        this.addToTop(new ApplyPowerAction(this.music_source, this.music_source, new VigorPower(this.music_source, this.amount)));
    }
}
