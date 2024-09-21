package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.qingmu.sakiko.cards.tmpcard.Confront_Sakiko;
import com.qingmu.sakiko.cards.tmpcard.Run_Sakiko;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;

public class Haruhikage_CryChic extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Haruhikage_CryChic.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Haruhikage_CryChic.png";


    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Haruhikage_CryChic() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 0);
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
