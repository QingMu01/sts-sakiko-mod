package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.watcher.JudgementAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class ChoirSChoir extends AbstractMusic {

    public static final String ID = ModNameHelper.make(ChoirSChoir.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/ChoirSChoir.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public ChoirSChoir() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, RARITY, TARGET);
        this.enchanted = 1;
        this.baseMagicNumber = 10;
    }

    @Override
    public void upgrade() {
        this.upgradeMagicNumber(5);
        ++this.timesUpgraded;
        this.upgraded = true;
        this.name = NAME + "+" + this.timesUpgraded;
        this.initializeTitle();
    }


    @Override
    public void play() {
        if (this.music_target != null && !this.music_target.isDeadOrEscaped()){
            this.addToBot(new JudgementAction(this.music_target, this.magicNumber < 0 ? this.baseMagicNumber : this.magicNumber));
        }
    }
}
