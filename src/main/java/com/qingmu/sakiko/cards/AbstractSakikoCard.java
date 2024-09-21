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
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.filed.CardStringsMiniTitleField;

import static com.qingmu.sakiko.constant.SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD;

public abstract class AbstractSakikoCard extends CustomCard {
    private static final CardColor COLOR = QINGMU_SAKIKO_CARD;
    private final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(this.cardID);
    protected String miniTitle = CardStringsMiniTitleField.miniTitle.get(cardStrings);

    public AbstractSakikoCard(String id, String name, String img, int cost, String rawDescription, AbstractCard.CardType type, CardRarity rarity, CardTarget target) {
        this(id, name, img, cost, rawDescription, type, COLOR, rarity, target);
    }

    public AbstractSakikoCard(String id, String name, String img, int cost, String rawDescription, AbstractCard.CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }
    public void triggerOnPlayMusic(AbstractMusic music){}

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
