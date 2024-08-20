package com.qingmu.sakiko.cards.music;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.qingmu.sakiko.action.MusicUpgradeAction;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.powers.FeverReadyPower;

@AutoAdd.NotSeen
public abstract class AbstractMusic extends CustomCard {

    private static final String BG_SKILL_512 = "SakikoModResources/img/512/bg_skill_512.png";
    private static final String BG_SKILL_1024 = "SakikoModResources/img/1024/bg_skill.png";

    private static final CardType CARD_TYPE = SakikoEnum.CardTypeEnum.MUSIC;
    private static final CardRarity RARITY = CardRarity.SPECIAL;

    protected int upgradeRequestNumber;

    public AbstractMusic(String id, String name, String img, int cost, String rawDescription, CardColor color, CardTarget target) {
        super(id, name, img, cost, rawDescription, CARD_TYPE, color, RARITY, target);
        super.setBackgroundTexture(BG_SKILL_512, BG_SKILL_1024);
        this.upgradeRequestNumber = 1;
    }

    public AbstractMusic(String id, String name, String img, int cost, String rawDescription, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, CARD_TYPE, color, rarity, target);
        super.setBackgroundTexture(BG_SKILL_512, BG_SKILL_1024);
        this.upgradeRequestNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FeverReadyPower(AbstractDungeon.player, 1)));
    }

    @Override
    public void triggerWhenDrawn() {
        this.addToBot(new MusicUpgradeAction(this, this.upgradeRequestNumber));
    }
    // 升华卡记得重下这个方法
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
            this.initializeTitle();
        }
    }
    // 升华卡记得重下这个方法
    @Override
    public boolean canUpgrade() {
        return (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT);
    }

    @SpireOverride
    public void renderSkillPortrait(SpriteBatch sb, float x, float y) {
        SpireSuper.call(sb, x, y);
    }

    @SpireOverride
    public void renderHelper(SpriteBatch sb, Color color, TextureAtlas.AtlasRegion img, float drawX, float drawY) {
        SpireSuper.call(sb, color, img, drawX, drawY);
    }

    @SpireOverride
    public void dynamicFrameRenderHelper(SpriteBatch sb, TextureAtlas.AtlasRegion img, float x, float y, float xOffset, float xScale) {
        SpireSuper.call(sb, img, x, y, xOffset, xScale);
    }
}
