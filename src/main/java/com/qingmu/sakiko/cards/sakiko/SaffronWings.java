package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.qingmu.sakiko.action.common.DamageCallbackAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class SaffronWings extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(SaffronWings.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/attack.png";


    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public SaffronWings() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.tags.add(CardTags.HEALING);

        this.initBaseAttr(2, 13, 0, 0);
        this.setUpgradeAttr(2, 5, 0, 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int targetHealth = m.currentHealth;
        int targetBlock = m.currentBlock;
        this.addToBot(new DamageCallbackAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, (damageAmount) -> {
            if (damageAmount == targetHealth) {
                int i = this.damage - damageAmount - targetBlock;
                if (i > 0 && !m.hasPower(MinionPower.POWER_ID))
                    AbstractDungeon.player.heal(i);
            }
        }));
    }
}
