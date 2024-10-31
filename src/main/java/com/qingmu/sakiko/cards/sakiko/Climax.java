package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.qingmu.sakiko.action.common.DrawMusicAction;
import com.qingmu.sakiko.action.common.ReadyToPlayMusicAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.stances.CreatorStance;
import com.qingmu.sakiko.stances.PlayerStance;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Climax extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Climax.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Climax.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Climax() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 5, 0, 0);
        this.setUpgradeAttr(1, 3, 0, 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.submitActionsToBot(
                new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY),
                this.getStanceAction(p.stance)
        );
    }

    private AbstractGameAction getStanceAction(AbstractStance stance) {
        AbstractGameAction action = new WaitAction(0.1f);
        if (stance instanceof PlayerStance) {
            action = new DrawMusicAction(3);
        }
        if (stance instanceof CreatorStance) {
            action = new ReadyToPlayMusicAction(3);
        }
        return action;
    }

}
