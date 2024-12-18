package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class NoteTorrent extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(NoteTorrent.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/NoteTorrent.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public NoteTorrent() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(0, 6, 0, 0);
        this.setUpgradeAttr(0, 3, 0, 0);
    }

    @Override
    public void triggerOnPlayMusic(AbstractMusic music) {
        if (CardsHelper.dsp().contains(this)) {
            AbstractCard copy = this.makeSameInstanceOf();
            copy.purgeOnUse = true;
            copy.current_x = Settings.WIDTH + copy.hb.width + 50.0f * Settings.scale;
            copy.current_y = Settings.HEIGHT + copy.hb.height + 50.0f * Settings.scale;
            copy.target_x = Settings.WIDTH / 2.0f;
            copy.target_y = Settings.HEIGHT / 2.0f;
            this.addToBot(new NewQueueCardAction(copy, true, true, true));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }
}
