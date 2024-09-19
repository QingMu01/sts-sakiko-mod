package com.qingmu.sakiko.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.qingmu.sakiko.cards.music.AbstractMusic;

import static com.qingmu.sakiko.patch.SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD;

public abstract class AbstractSakikoCard extends CustomCard {

    private static final CardColor COLOR = QINGMU_SAKIKO_CARD;

    public AbstractSakikoCard(String id, String name, String img, int cost, String rawDescription, AbstractCard.CardType type, CardRarity rarity, CardTarget target) {
        this(id, name, img, cost, rawDescription, type, COLOR, rarity, target);
    }

    public AbstractSakikoCard(String id, String name, String img, int cost, String rawDescription, AbstractCard.CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }
    public void triggerOnPlayMusic(AbstractMusic music){}
}
