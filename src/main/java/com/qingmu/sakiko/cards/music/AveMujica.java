package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.qingmu.sakiko.action.SummonedByRelicAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class AveMujica extends AbstractMusic {

    public static final String ID = ModNameHelper.make(AveMujica.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/AveMujica.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public AveMujica() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
        this.tags.add(CardTags.HEALING);

        this.initMusicAttr(20, 30, 10, 15);
        this.setExhaust(true, true);
    }

    @Override
    public void play() {
        this.addToTop(new SummonedByRelicAction(this.musicNumber));
    }

    @Override
    public void interruptReady() {
        MonsterGroup friendlyMonsterGroup = DungeonHelper.getFriendlyMonsterGroup();
        if (friendlyMonsterGroup != null) {
            for (AbstractMonster monster : friendlyMonsterGroup.monsters) {
                if (!monster.isDying && !monster.isDead && !monster.isEscaping) {
                    this.addToBot(new GainBlockAction(monster, this.m_source, this.magicNumber));
                }
            }
        }
    }

}
