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
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.powers.FeverReadyPower;

import static com.qingmu.sakiko.patch.SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD;

public abstract class AbstractMusic extends CustomCard {

    private static final String BG_SKILL_512 = "SakikoModResources/img/512/bg_skill_512.png";
    private static final String BG_SKILL_1024 = "SakikoModResources/img/1024/bg_skill.png";

    private static final CardColor COLOR = QINGMU_SAKIKO_CARD;
    private static final CardType CARD_TYPE = SakikoEnum.CardTypeEnum.MUSIC;

    protected int enchanted;

    protected AbstractCreature music_source;
    protected AbstractCreature music_target;

    public int amount = 0;

    public boolean isPlayed = false;

    public AbstractMusic(String id, String name, String img, String rawDescription, CardRarity rarity, CardTarget target) {
        super(id, name, img, 0, rawDescription, CARD_TYPE, COLOR, rarity, target);
        super.setBackgroundTexture(BG_SKILL_512, BG_SKILL_1024);
        this.enchanted = -1;
    }

    // 实现的时候最好使用addToTop()方法，否则会导致所有被演奏卡牌的演奏动画播放完毕才生效
    public abstract void play();

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.music_source = p;
        this.music_target = m;
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FeverReadyPower(AbstractDungeon.player, 1)));
    }

    @Override
    public void triggerWhenDrawn() {
        if (this.enchanted > 0) {
            this.addToBot(new MusicUpgradeAction(this, this.enchanted));
        }
    }

    // 更新计数
    public void applyAmount(){}

    // 存在待演奏区时，有卡牌被打出时触发的钩子
    public void triggerInBufferPlayCard(AbstractCard card) {}

    public void resetMusicCard() {
        AbstractCard tmp = this.makeCopy();
        this.baseBlock = tmp.baseBlock;
        this.baseDamage = tmp.baseDamage;
        this.baseMagicNumber = tmp.baseMagicNumber;
        this.name = tmp.name;
        this.upgraded = false;
        this.timesUpgraded = 0;
        this.amount = 0;
        this.isPlayed = false;
        this.resetAttributes();
        this.initializeTitle();
    }

//    @Override
//    public void update() {
//        updateFlashVfx();
//        if (this.hoverTimer != 0.0F) {
//            this.hoverTimer -= Gdx.graphics.getDeltaTime();
//            if (this.hoverTimer < 0.0F)
//                this.hoverTimer = 0.0F;
//        }
//        if (AbstractDungeon.player != null && AbstractDungeon.player.isDraggingCard && this == AbstractDungeon.player.hoveredCard) {
//            this.current_x = MathHelper.cardLerpSnap(this.current_x, this.target_x);
//            this.current_y = MathHelper.cardLerpSnap(this.current_y, this.target_y);
//            if (AbstractDungeon.player.hasRelic("Necronomicon"))
//                if (this.cost >= 2 && this.type == CardType.ATTACK && AbstractDungeon.player.getRelic("Necronomicon")
//                        .checkTrigger()) {
//                    AbstractDungeon.player.getRelic("Necronomicon").beginLongPulse();
//                } else {
//                    AbstractDungeon.player.getRelic("Necronomicon").stopPulse();
//                }
//        }
//        if (Settings.FAST_MODE) {
//            this.current_x = MathHelper.cardLerpSnap(this.current_x, this.target_x);
//            this.current_y = MathHelper.cardLerpSnap(this.current_y, this.target_y);
//        }
//        this.current_x = MathHelper.cardLerpSnap(this.current_x, this.target_x);
//        this.current_y = MathHelper.cardLerpSnap(this.current_y, this.target_y);
//        this.hb.move(this.current_x, this.current_y);
//        this.hb.resize(300.0F * Settings.scale * this.drawScale, 420.0F * Settings.scale * this.drawScale);
//        if (this.hb.clickStarted && this.hb.hovered) {
//            this.drawScale = MathHelper.cardScaleLerpSnap(this.drawScale, this.targetDrawScale * 0.9F);
//            this.drawScale = MathHelper.cardScaleLerpSnap(this.drawScale, this.targetDrawScale * 0.9F);
//        } else {
//            this.drawScale = MathHelper.cardScaleLerpSnap(this.drawScale, this.targetDrawScale);
//        }
//        if (this.angle != this.targetAngle)
//            this.angle = MathHelper.angleLerpSnap(this.angle, this.targetAngle);
//        updateTransparency();
//        updateColor();
//    }

    @Override
    public boolean canUpgrade() {
        return AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
    }

    @SpireOverride
    public void renderSkillPortrait(SpriteBatch sb, float x, float y) {
        SpireSuper.call(sb, x, y);
    }

    @SpireOverride
    public void renderHelper(SpriteBatch sb, Color color, TextureAtlas.AtlasRegion img, float drawX, float drawY) {
        SpireSuper.call(sb, color, img, drawX, drawY);
    }
//    @SpireOverride
//    private void updateFlashVfx() {
//        SpireSuper.call();
//    }
//    @SpireOverride
//    private void updateTransparency() {
//        SpireSuper.call();
//    }
//    @SpireOverride
//    private void updateColor() {
//        SpireSuper.call();
//    }
}
