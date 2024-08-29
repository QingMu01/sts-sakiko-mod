package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.qingmu.sakiko.cards.tmpcard.Confront_Sakiko;
import com.qingmu.sakiko.cards.tmpcard.Run_Sakiko;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;

public class Haruhikage_CryChic extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Haruhikage_CryChic.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Haruhikage_CryChic.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Haruhikage_CryChic() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, RARITY, TARGET);
        this.enchanted = 999;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
        }
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void play() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        list.add(new Run_Sakiko());
        list.add(new Confront_Sakiko());
        this.addToTop(new ChooseOneAction(list));
    }
}
