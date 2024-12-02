package com.qingmu.sakiko.cards.colorless;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.cards.sakiko.Defend_Sakiko;
import com.qingmu.sakiko.cards.sakiko.Strike_Sakiko;
import com.qingmu.sakiko.inteface.SakikoModEnable;
import com.qingmu.sakiko.modifier.FakePowerModifier;
import com.qingmu.sakiko.modifier.SetAttrModifier;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.HashSet;
import java.util.Set;

@SakikoModEnable(enable = false)
public class Satellite extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Satellite.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/other/Satellite.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    private AbstractCard card1;
    private AbstractCard card2;

    public Satellite(AbstractCard card1, AbstractCard card2, int amount) {
        super(ID, IMG_PATH, TYPE, CardColor.COLORLESS, RARITY, TARGET);
        this.refreshDesc = false;
        if (card1.cost == -1 || card2.cost == -1) {
            this.initBaseAttr(-1, 0, 0, amount, card1, card2);
        } else {
            this.initBaseAttr(card1.cost + card2.cost, 0, 0, amount, card1, card2);
        }
        this.setUpgradeAttr(Math.max(0, card1.cost + card2.cost - 1), 0, 0, 0);
        if (card1.exhaust || card2.exhaust) {
            this.setExhaust(true, true);
        }
        if (card1.isEthereal || card2.isEthereal) {
            this.setEthereal(true, true);
        }
        if (card1.selfRetain || card2.selfRetain) {
            this.setSelfRetain(true, true);
        }
        if (card1.type == CardType.POWER || card2.type == CardType.POWER) {
            CardModifierManager.addModifier(this, new FakePowerModifier());
        }

        Set<CardTags> tagSet = new HashSet<>();

        tagSet.addAll(card1.tags);
        tagSet.addAll(card2.tags);

        this.tags.addAll(tagSet);

        this.card1 = card1;
        this.card2 = card2;

        CardModifierManager.addModifier(this.card1, new SetAttrModifier(this.magicNumber));
        CardModifierManager.addModifier(this.card2, new SetAttrModifier(this.magicNumber));

        this.rawDescription = DESCRIPTION + String.format(EXTENDED_DESCRIPTION[0], this.card1.name, this.card2.name);
        this.initializeDescription();
    }

    public Satellite() {
        this(new Strike_Sakiko(), new Defend_Sakiko(), 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard copy1 = this.card1.makeSameInstanceOf();
        copy1.purgeOnUse = true;
        DungeonHelper.getPlayer().limbo.addToBottom(copy1);
        copy1.target_x = MathUtils.random((Settings.WIDTH / 2.0F) - 100.0F, (Settings.WIDTH / 2.0F) + 100.0F);
        copy1.target_y = MathUtils.random((Settings.HEIGHT / 2.0F) - 50.0F, (Settings.HEIGHT / 2.0F) + 50.0F);
        copy1.calculateCardDamage(m);
        this.addToBot(new NewQueueCardAction(copy1, true, true, true));
        AbstractCard copy2 = this.card2.makeSameInstanceOf();
        copy2.purgeOnUse = true;
        DungeonHelper.getPlayer().limbo.addToBottom(copy2);
        copy2.target_x = MathUtils.random((Settings.WIDTH / 2.0F) - 100.0F, (Settings.WIDTH / 2.0F) + 100.0F);
        copy2.target_y = MathUtils.random((Settings.HEIGHT / 2.0F) - 50.0F, (Settings.HEIGHT / 2.0F) + 50.0F);
        copy2.calculateCardDamage(m);
        this.addToBot(new NewQueueCardAction(copy2, true, true, true));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Satellite(this.card1, this.card2, this.magicNumber);
    }
}
