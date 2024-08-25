package com.qingmu.sakiko.characters;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.qingmu.sakiko.SakikoModCore;
import com.qingmu.sakiko.cards.sakiko.MusicalComposition;
import com.qingmu.sakiko.relics.ClassicPiano;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;

import static com.qingmu.sakiko.patch.SakikoEnum.CharacterEnum.QINGMU_SAKIKO;
import static com.qingmu.sakiko.patch.SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD;

public class TogawaSakiko extends CustomPlayer {
    // 火堆的人物立绘（行动前）
    private static final String MY_CHARACTER_SHOULDER_1 = "SakikoModResources/img/characters/sakiko/shoulder1.png";
    // 火堆的人物立绘（行动后）
    private static final String MY_CHARACTER_SHOULDER_2 = "SakikoModResources/img/characters/sakiko/shoulder2.png";
    // 人物死亡图像
    private static final String CORPSE_IMAGE = "SakikoModResources/img/characters/sakiko/corpse.png";
    // 战斗界面左下角能量图标的每个图层
    private static final String[] ORB_TEXTURES = new String[]{
            "SakikoModResources/img/ui/orb/layer1.png",
            "SakikoModResources/img/ui/orb/layer2.png",
            "SakikoModResources/img/ui/orb/layer3.png",
            "SakikoModResources/img/ui/orb/layer4.png",
            "SakikoModResources/img/ui/orb/layer5.png",
            "SakikoModResources/img/ui/orb/layer6.png",
            "SakikoModResources/img/ui/orb/layer1d.png",
            "SakikoModResources/img/ui/orb/layer2d.png",
            "SakikoModResources/img/ui/orb/layer3d.png",
            "SakikoModResources/img/ui/orb/layer4d.png",
            "SakikoModResources/img/ui/orb/layer5d.png",
    };
    // 每个图层的旋转速度
    private static final float[] LAYER_SPEED = new float[]{-40.0F, -32.0F, 20.0F, -20.0F, 2.0F};    // 人物的本地化文本，如卡牌的本地化文本一样，如何书写见下
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString("SakikoMod:TogawaSakiko");

    // 歌单
    private List<AbstractCard> musics;

    public TogawaSakiko(String name) {
        super(name, QINGMU_SAKIKO, ORB_TEXTURES, "SakikoModResources/img/ui/orb/vfx.png", LAYER_SPEED, null, null);
        this.initializeClass(
                "SakikoModResources/img/characters/sakiko/TogawaSakiko.png", // 人物图片
                MY_CHARACTER_SHOULDER_2, MY_CHARACTER_SHOULDER_1,
                CORPSE_IMAGE, // 人物死亡图像
                this.getLoadout(),
                0.0F, 0.0F,
                200.0F, 220.0F, // 人物碰撞箱大小，越大的人物模型这个越大
                new EnergyManager(3) // 初始每回合的能量
        );
    }

    @Override
    public ArrayList<String> getStartingDeck() {

        ArrayList<String> decks = new ArrayList<>();
        decks.add(ModNameHelper.make("Strike_Sakiko"));
        decks.add(ModNameHelper.make("Strike_Sakiko"));
        decks.add(ModNameHelper.make("Strike_Sakiko"));
        decks.add(ModNameHelper.make("Strike_Sakiko"));
        decks.add(ModNameHelper.make("Defend_Sakiko"));
        decks.add(ModNameHelper.make("Defend_Sakiko"));
        decks.add(ModNameHelper.make("Defend_Sakiko"));
        decks.add(ModNameHelper.make("Defend_Sakiko"));
        decks.add(ModNameHelper.make("Professional"));
        decks.add(ModNameHelper.make("MusicalComposition"));
        return decks;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> relics = new ArrayList<>();
        relics.add(ClassicPiano.ID);
        return relics;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(
                characterStrings.NAMES[0], // 人物名字
                characterStrings.TEXT[0], // 人物介绍
                75, // 当前血量
                75, // 最大血量
                0, // 初始充能球栏位
                49, // 初始携带金币
                5, // 每回合抽牌数量
                this, // 别动
                this.getStartingRelics(), // 初始遗物
                this.getStartingDeck(), // 初始卡组
                false // 别动
        );
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return characterStrings.NAMES[0];
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return QINGMU_SAKIKO_CARD;
    }

    @Override
    public Color getCardRenderColor() {
        return SakikoModCore.SAKIKO_COLOR;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new MusicalComposition();
    }

    @Override
    public Color getCardTrailColor() {
        return SakikoModCore.SAKIKO_COLOR;
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 8;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontBlue;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_HEAVY";
    }

    @Override
    public String getLocalizedCharacterName() {
        return characterStrings.NAMES[0];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new TogawaSakiko(this.name);
    }

    @Override
    public String getSpireHeartText() {
        return characterStrings.TEXT[1];
    }

    @Override
    public Color getSlashAttackColor() {
        return SakikoModCore.SAKIKO_COLOR;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.SLASH_DIAGONAL};    }

    @Override
    public String getVampireText() {
        return Vampires.DESCRIPTIONS[1];
    }

}
