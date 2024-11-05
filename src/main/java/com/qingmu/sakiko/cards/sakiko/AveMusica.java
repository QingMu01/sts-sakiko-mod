package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.common.DrawMusicAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.stances.PlayerStance;
import com.qingmu.sakiko.utils.ModNameHelper;

public class AveMusica extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(AveMusica.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/AveMusica.png";


    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public AveMusica() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 0, 1);
        this.setUpgradeAttr(1, 0, 0, 1);

        this.setSelfRetain(false,true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.stance.ID.equals(PlayerStance.STANCE_ID)) {
            this.addToBot(new DrawMusicAction(this.magicNumber));
        } else {
            this.addToBot(new ChangeStanceAction(PlayerStance.STANCE_ID));
        }
    }
}
