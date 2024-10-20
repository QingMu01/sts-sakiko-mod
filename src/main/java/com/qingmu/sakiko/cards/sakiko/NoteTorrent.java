package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class NoteTorrent extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(NoteTorrent.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/NoteTorrent.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public NoteTorrent() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(0, 6, 0, 2);
        this.setUpgradeAttr(0, 0, 0, 2);
    }

    @Override
    public void triggerOnPlayMusic(AbstractMusic music) {
        if (AbstractDungeon.player.discardPile.group.contains(this)){
            this.baseDamage += this.magicNumber;
            this.addToBot(new DiscardToHandAction(this));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToTop(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }
}
