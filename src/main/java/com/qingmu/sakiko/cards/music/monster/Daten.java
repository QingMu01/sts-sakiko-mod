package com.qingmu.sakiko.cards.music.monster;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.action.common.ExprAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.cards.colorless.PowerDebris;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.inteface.SakikoModEnable;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;

@SakikoModEnable(enable = false)
public class Daten extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Daten.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Daten.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Daten() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initMusicAttr(0, 0, 0, 0);
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void play() {
        this.addToTop(new ExprAction(() -> {
            if (this.m_target.powers.isEmpty()) {
                return;
            }
            ArrayList<AbstractPower> powers = new ArrayList<>();
            for (AbstractPower power : this.m_target.powers) {
                if (power.type == AbstractPower.PowerType.BUFF) {
                    powers.add(power);
                }
            }
            AbstractPower selectedPower = powers.get(AbstractDungeon.aiRng.random(powers.size() - 1));
            this.addToBot(new MakeTempCardInHandAction(new PowerDebris(selectedPower, selectedPower.amount)));
            this.addToBot(new RemoveSpecificPowerAction(this.m_target, this.m_source, selectedPower.ID));
        }));
    }
}
