package com.qingmu.sakiko.cards.colorless;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;


public class Vindicator extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Vindicator.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/attack.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Vindicator() {
        this(0);
    }

    public Vindicator(int baseDamage) {
        super(ID, IMG_PATH, TYPE, CardColor.COLORLESS, RARITY, TARGET);

        this.initBaseAttr(1, baseDamage, 0, 0);
        this.setUpgradeAttr(0, baseDamage, 0, 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new VFXAction(new WeightyImpactEffect(m.hb.cX, m.hb.cY, DungeonHelper.getPlayer().getSlashAttackColor())));
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
    }
}
