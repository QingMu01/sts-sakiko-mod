package com.qingmu.sakiko.cards.tmpcard;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.KokoroNoKabePower;
import com.qingmu.sakiko.utils.ModNameHelper;


public class Confront_Sakiko extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Confront_Sakiko.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/skill.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Confront_Sakiko() {
        super(ID, IMG_PATH, TYPE, CardColor.COLORLESS, RARITY, TARGET);
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
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.onChoseThisOption();
    }

    @Override
    public void onChoseThisOption() {
        AbstractPower power = AbstractDungeon.player.getPower(KokoroNoKabePower.POWER_ID);
        if (power != null){
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new KokoroNoKabePower(AbstractDungeon.player, power.amount)));
            ((KokoroNoKabePower)power).stackDamageAmount(((KokoroNoKabePower) power).amount2);
        }
    }
}
