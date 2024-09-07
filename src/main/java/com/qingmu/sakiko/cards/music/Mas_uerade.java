package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Mas_uerade extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Mas_uerade.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Mas_uerade.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public Mas_uerade() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
        this.enchanted = 1;
        this.baseDamage = 6;
        this.isMultiDamage = true;
    }

    @Override
    public void upgrade() {
        this.upgradeDamage(3 + this.timesUpgraded);
        ++this.timesUpgraded;
        this.upgraded = true;
        this.name = NAME + "+" + this.timesUpgraded;
        this.initializeTitle();
    }


    @Override
    public void play() {
        this.addToTop(new DamageAllEnemiesAction(this.music_source, this.multiDamage, this.damageType, AbstractGameAction.AttackEffect.NONE));
        this.addToTop(new VFXAction(this.music_source, new CleaveEffect(), 0.1F));
        this.addToTop(new SFXAction("ATTACK_HEAVY"));

    }
}
