package com.qingmu.sakiko.cards.music.monster;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.monster.RanaHaruhikagePower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Haruhikage_Rana extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Haruhikage_Rana.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Haruhikage_CryChic.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Haruhikage_Rana() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, RARITY, TARGET);
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
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this.music_source, new RanaHaruhikagePower(AbstractDungeon.player, this.music_source, 2)));
    }
}