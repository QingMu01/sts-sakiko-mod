package com.qingmu.sakiko.cards.music;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.qingmu.sakiko.action.MusicUpgradeAction;
import com.qingmu.sakiko.action.ReadyToPlayMusicAction;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.powers.FeverReadyPower;

import static com.qingmu.sakiko.patch.SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD;

public abstract class AbstractMusic extends CustomCard {

    private static final String BG_SKILL_512 = "SakikoModResources/img/512/bg_skill_512.png";
    private static final String BG_SKILL_1024 = "SakikoModResources/img/1024/bg_skill.png";

    private static final CardColor COLOR = QINGMU_SAKIKO_CARD;
    private static final CardType CARD_TYPE = SakikoEnum.CardTypeEnum.MUSIC;

    protected int enchanted;
    protected int sublimated;

    protected AbstractCreature music_source;
    protected AbstractCreature music_target;


    public AbstractMusic(String id, String name, String img, int cost, String rawDescription, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, CARD_TYPE, COLOR, rarity, target);
        super.setBackgroundTexture(BG_SKILL_512, BG_SKILL_1024);
        this.enchanted = -1;
        this.sublimated = -1;
    }

    public abstract void play();

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.music_source = p;
        this.music_target = m;
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FeverReadyPower(AbstractDungeon.player, 1)));
        this.addToBot(new ReadyToPlayMusicAction(this));
    }

    @Override
    public void triggerWhenDrawn() {
        if (this.enchanted > 0){
            this.addToBot(new MusicUpgradeAction(this, this.enchanted));
        }
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (c == this) {
            AbstractCard tmp = c.makeCopy();
            this.baseBlock = tmp.baseBlock;
            this.baseDamage = tmp.baseDamage;
            this.baseMagicNumber = tmp.baseMagicNumber;
            this.name = tmp.name;
            this.upgraded = false;
            this.timesUpgraded = 0;
            this.resetAttributes();
            this.initializeTitle();
        }
    }

    @Override
    public boolean canUpgrade() {
        return AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !this.upgraded;
    }

    @SpireOverride
    public void renderSkillPortrait(SpriteBatch sb, float x, float y) {
        SpireSuper.call(sb, x, y);
    }

    @SpireOverride
    public void renderHelper(SpriteBatch sb, Color color, TextureAtlas.AtlasRegion img, float drawX, float drawY) {
        SpireSuper.call(sb, color, img, drawX, drawY);
    }
}
