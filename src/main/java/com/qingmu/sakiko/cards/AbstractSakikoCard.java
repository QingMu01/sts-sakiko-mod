package com.qingmu.sakiko.cards;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.patch.filed.CardStringsMiniTitleField;

public abstract class AbstractSakikoCard extends CustomCard {

    private final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(this.cardID);

    protected final String NAME = cardStrings.NAME;
    protected final String DESCRIPTION = cardStrings.DESCRIPTION;
    protected final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    protected final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    protected String miniTitle = CardStringsMiniTitleField.miniTitle.get(cardStrings);

    public AbstractSakikoCard(String id, String img, AbstractCard.CardType type, CardRarity rarity, CardTarget target) {
        this(id, "", img, -2, "", type, SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD, rarity, target);
        this.originalName = NAME;
        this.name = NAME;
        this.rawDescription = DESCRIPTION;
        this.initializeTitle();
        this.initializeDescription();
    }

    public AbstractSakikoCard(String id, String img, AbstractCard.CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        this(id, "", img, -2, "", type, color, rarity, target);
    }

    public AbstractSakikoCard(String id, String name, String img, int cost, String rawDescription, AbstractCard.CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    // 演奏时触发的钩子
    public void triggerOnPlayMusic(AbstractMusic music) {
    }

    // 设置卡牌基本属性
    protected void initBaseAttr(int cost, int baseDamage, int baseBlock, int baseMagicNumber) {
        this.cost = cost;
        this.costForTurn = cost;

        this.baseDamage = baseDamage;
        this.baseBlock = baseBlock;
        this.baseMagicNumber = baseMagicNumber;
    }

    // 升级卡牌时自动更新说明
    protected void upgradeDescription() {
        this.rawDescription = UPGRADE_DESCRIPTION.isEmpty() ? DESCRIPTION : UPGRADE_DESCRIPTION;
        this.initializeDescription();
    }

    // 添加额外说明
    protected void appendDescription(Object... strings) {
        if (this.upgraded) {
            this.rawDescription = UPGRADE_DESCRIPTION.isEmpty() ? DESCRIPTION : UPGRADE_DESCRIPTION;
            this.rawDescription += String.format(EXTENDED_DESCRIPTION[0], strings);
        } else {
            this.rawDescription = DESCRIPTION + String.format(EXTENDED_DESCRIPTION[0], strings);
        }
        this.initializeDescription();
    }

    // 清除额外说明
    @Override
    public void onMoveToDiscard() {
        if (this.upgraded) {
            this.rawDescription = UPGRADE_DESCRIPTION.isEmpty() ? DESCRIPTION : UPGRADE_DESCRIPTION;
        } else {
            this.rawDescription = DESCRIPTION;
        }
        this.initializeDescription();
    }

    // 清除额外说明
    @Override
    public void triggerOnExhaust() {
        if (this.upgraded) {
            this.rawDescription = UPGRADE_DESCRIPTION.isEmpty() ? DESCRIPTION : UPGRADE_DESCRIPTION;
        } else {
            this.rawDescription = DESCRIPTION;
        }
        this.initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        this.magicNumber = this.baseMagicNumber;
    }

    // 渲染小标题
    @Override
    public void renderTitle(SpriteBatch sb) {
        super.renderTitle(sb);
        float offsetY = 410.0F * Settings.scale * this.drawScale / 2.0F;
        BitmapFont.BitmapFontData fontData = FontHelper.cardTitleFont.getData();
        float originalScale = fontData.scaleX;
        float scaleMultiplier = 0.8F;
        fontData.setScale(scaleMultiplier * this.drawScale * 0.85F);
        Color color = Settings.CREAM_COLOR.cpy();
        color.a = this.transparency;
        FontHelper.renderRotatedText(sb, FontHelper.cardTitleFont, this.miniTitle, this.current_x, this.current_y, 0.0F, offsetY, this.angle, true, color);
        fontData.setScale(originalScale);
    }
}
