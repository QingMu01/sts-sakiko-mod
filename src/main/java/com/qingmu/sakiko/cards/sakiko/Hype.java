package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import com.qingmu.sakiko.action.ResetMusicalNoteAction;
import com.qingmu.sakiko.action.common.XAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Hype extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Hype.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/attack.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public Hype() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(-1, 5, 0, 0);
        this.isMultiDamage = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new XAction(p, this.freeToPlayOnce, this.energyOnUse,effect->{
            for(int i = 0; i < effect; ++i) {
                if (i == 0) {
                    this.addToBot(new SFXAction("ATTACK_WHIRLWIND"));
                    this.addToBot(new VFXAction(new WhirlwindEffect(), 0.0F));
                }
                this.addToBot(new SFXAction("ATTACK_HEAVY"));
                this.addToBot(new VFXAction(p, new CleaveEffect(), 0.0F));
                this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageType, AbstractGameAction.AttackEffect.NONE, true));
            }
            this.addToBot(new ResetMusicalNoteAction());
        }));
    }
}
