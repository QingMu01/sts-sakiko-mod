package com.qingmu.sakiko.cards.curse;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.qingmu.sakiko.SakikoModCore;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.patch.anonmod.utils.HeavyHelper;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;


public class SoyoCurse extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(SoyoCurse.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/other/SoyoCurse.png";

    private static final CardType TYPE = CardType.CURSE;
    private static final CardRarity RARITY = CardRarity.CURSE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public SoyoCurse() {
        super(ID, IMG_PATH, TYPE, CardColor.CURSE, RARITY, TARGET);
        this.initBaseAttr(-2, 0, 0, 0);
    }

    @Override
    public void triggerWhenDrawn() {
        this.addToBot(new ApplyPowerAction(DungeonHelper.getPlayer(), DungeonHelper.getPlayer(), new ConstrictedPower(DungeonHelper.getPlayer(), DungeonHelper.getPlayer(), 5)));
        if (Loader.isModLoaded("AnonMod") && SakikoModCore.SAKIKO_CONFIG.getBool("enableAnonCard")) {
            HeavyHelper.applyHeavy(DungeonHelper.getPlayer(), DungeonHelper.getPlayer(), 3);
        }

        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            this.addToBot(new ApplyPowerAction(monster, DungeonHelper.getPlayer(), new ConstrictedPower(monster, DungeonHelper.getPlayer(), 5)));
            if (Loader.isModLoaded("AnonMod") && SakikoModCore.SAKIKO_CONFIG.getBool("enableAnonCard")) {
                HeavyHelper.applyHeavy(monster, DungeonHelper.getPlayer(), 3);
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

}
