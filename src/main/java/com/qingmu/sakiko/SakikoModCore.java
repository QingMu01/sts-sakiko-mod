package com.qingmu.sakiko;

import basemod.*;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.audio.MusicMaster;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.events.beyond.Falling;
import com.megacrit.cardcrawl.events.exordium.Sssserpent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.characters.TogawaSakiko;
import com.qingmu.sakiko.constant.ColorHelp;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.events.*;
import com.qingmu.sakiko.inteface.SakikoModEnable;
import com.qingmu.sakiko.monsters.boss.InnerDemonSakiko;
import com.qingmu.sakiko.monsters.member.*;
import com.qingmu.sakiko.relics.AbstractSakikoRelic;
import com.qingmu.sakiko.rewards.MusicCardReward;
import com.qingmu.sakiko.screens.MusicDrawPileViewScreen;
import com.qingmu.sakiko.utils.InvasionChangeSaved;
import com.qingmu.sakiko.utils.ModNameHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Properties;

import static com.qingmu.sakiko.constant.SakikoEnum.CharacterEnum.QINGMU_SAKIKO;
import static com.qingmu.sakiko.constant.SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD;


@SpireInitializer
public class SakikoModCore implements EditCardsSubscriber, EditRelicsSubscriber, StartGameSubscriber,
        EditCharactersSubscriber, EditStringsSubscriber, OnCardUseSubscriber,
        EditKeywordsSubscriber, PostInitializeSubscriber {
    public static final Logger logger = LogManager.getLogger(SakikoModCore.class.getName());
    // 人物选择界面按钮的图片
    private static final String MY_CHARACTER_BUTTON = "SakikoModResources/img/characters/sakiko/Character_Button.png";
    // 人物选择界面的立绘
    private static final String MY_CHARACTER_PORTRAIT = "SakikoModResources/img/characters/sakiko/Character_Portrait.png";
    // 攻击牌的背景（小尺寸）
    private static final String BG_ATTACK_512 = "SakikoModResources/img/512/bg_attack_512.png";
    // 能力牌的背景（小尺寸）
    private static final String BG_POWER_512 = "SakikoModResources/img/512/bg_power_512.png";
    // 技能牌的背景（小尺寸）
    private static final String BG_SKILL_512 = "SakikoModResources/img/512/bg_skill_512.png";
    // 在卡牌和遗物描述中的能量图标
    private static final String SMALL_ORB = "SakikoModResources/img/characters/sakiko/small_orb.png";
    // 攻击牌的背景（大尺寸）
    private static final String BG_ATTACK_1024 = "SakikoModResources/img/1024/bg_attack.png";
    // 能力牌的背景（大尺寸）
    private static final String BG_POWER_1024 = "SakikoModResources/img/1024/bg_power.png";
    // 技能牌的背景（大尺寸）
    private static final String BG_SKILL_1024 = "SakikoModResources/img/1024/bg_skill.png";
    // 在卡牌预览界面的能量图标
    private static final String BIG_ORB = "SakikoModResources/img/characters/sakiko/card_orb.png";
    // 小尺寸的能量图标（战斗中，牌堆预览）
    private static final String ENERGY_ORB = "SakikoModResources/img/characters/sakiko/cost_orb.png";

    public static SpireConfig SAKIKO_CONFIG;

    private static final Color SAKIKO_COLOR = ColorHelp.SAKIKO_COLOR.cpy();

    public SakikoModCore() {
        BaseMod.subscribe(this);
        BaseMod.addColor(QINGMU_SAKIKO_CARD, SAKIKO_COLOR, SAKIKO_COLOR, SAKIKO_COLOR, SAKIKO_COLOR, SAKIKO_COLOR, SAKIKO_COLOR, SAKIKO_COLOR, BG_ATTACK_512, BG_SKILL_512, BG_POWER_512, ENERGY_ORB, BG_ATTACK_1024, BG_SKILL_1024, BG_POWER_1024, BIG_ORB, SMALL_ORB);
    }

    public static void initialize() {
        new SakikoModCore();
        try {
            // 设置默认值
            Properties defaults = new Properties();
            defaults.setProperty("enableAnonCard", Boolean.toString(false));
            defaults.setProperty("enableBoss", Boolean.toString(false));
            defaults.setProperty("ascensionUnlock", Boolean.toString(false));
            defaults.setProperty("modSound", Float.toString(1.00f));
            SAKIKO_CONFIG = new SpireConfig("SakikoMod", "Common", defaults);
        } catch (IOException var2) {
            logger.error(var2);
        }
    }

    @Override
    public void receiveEditCards() {
        new AutoAdd("sakikoMod")
                .packageFilter("com.qingmu.sakiko.cards")
                .setDefaultSeen(false)
                .any(AbstractSakikoCard.class, (info, card) -> {
                    if (card.getClass().isAnnotationPresent(SakikoModEnable.class)) {
                        SakikoModEnable annotation = card.getClass().getAnnotation(SakikoModEnable.class);
                        if (annotation.enable()) {
                            BaseMod.addCard(card);
                        }
                    } else {
                        BaseMod.addCard(card);
                    }
                });
        BaseMod.addDynamicVariable(new AbstractMusic.MusicNumberVariable());
    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd("sakikoMod")
                .packageFilter("com.qingmu.sakiko.relics")
                .any(AbstractSakikoRelic.class, (info, relic) -> {
                    if (relic.getClass().isAnnotationPresent(SakikoModEnable.class)) {
                        SakikoModEnable annotation = relic.getClass().getAnnotation(SakikoModEnable.class);
                        if (annotation.enable()) {
                            BaseMod.addRelicToCustomPool(relic, QINGMU_SAKIKO_CARD);
                            if (info.seen) {
                                UnlockTracker.markRelicAsSeen(relic.relicId);
                            }
                        }
                    } else {
                        BaseMod.addRelicToCustomPool(relic, QINGMU_SAKIKO_CARD);
                        if (info.seen) {
                            UnlockTracker.markRelicAsSeen(relic.relicId);
                        }
                    }
                });
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new TogawaSakiko(CardCrawlGame.playerName), MY_CHARACTER_BUTTON, MY_CHARACTER_PORTRAIT, QINGMU_SAKIKO);
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String lang = "ZHS";
        FileHandle internal = Gdx.files.internal("SakikoModResources/localization/" + Settings.language);
        if (internal.exists()) {
            lang = Settings.language.toString();
        }
        String json = Gdx.files.internal("SakikoModResources/localization/" + lang + "/keyword.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword("sakikomod", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receiveEditStrings() {
        String lang = "ZHS";
        FileHandle internal = Gdx.files.internal("SakikoModResources/localization/" + Settings.language);
        if (internal.exists()) {
            lang = Settings.language.toString();
        }
        BaseMod.loadCustomStringsFile(CardStrings.class, "SakikoModResources/localization/" + lang + "/cards.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "SakikoModResources/localization/" + lang + "/characters.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, "SakikoModResources/localization/" + lang + "/ui.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "SakikoModResources/localization/" + lang + "/powers.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, "SakikoModResources/localization/" + lang + "/relics.json");
        BaseMod.loadCustomStringsFile(TutorialStrings.class, "SakikoModResources/localization/" + lang + "/tutorial.json");
        BaseMod.loadCustomStringsFile(MonsterStrings.class, "SakikoModResources/localization/" + lang + "/monsters.json");
        BaseMod.loadCustomStringsFile(EventStrings.class, "SakikoModResources/localization/" + lang + "/events.json");
        BaseMod.loadCustomStringsFile(StanceStrings.class, "SakikoModResources/localization/" + lang + "/stances.json");
    }

    @Override
    public void receivePostInitialize() {
        // 注册配置UI
        this.registerConfigUI();
        // 注册成员收集事件
        this.registerMemberCollect();
        // 注册Boss战斗
        this.registerBoss();
        // 设置解锁进阶
        this.unlockedAscension();
        // 添加歌单预览页面
        BaseMod.addCustomScreen(new MusicDrawPileViewScreen());
        // 注册音乐牌奖励
        BaseMod.registerCustomReward(SakikoEnum.RewardType.MUSIC_TYPE,
                (rewardSave) -> new MusicCardReward(rewardSave.id),
                (customReward) -> new RewardSave(customReward.type.toString(), ((MusicCardReward) customReward).id));

        // 注册1层事件
        BaseMod.addEvent(new AddEventParams.Builder(SoyoEvent.ID, SoyoEvent.class)
                .playerClass(QINGMU_SAKIKO)
                .eventType(EventUtils.EventType.OVERRIDE)
                .overrideEvent(Sssserpent.ID)
                .create());
        // 注册2层事件
        BaseMod.addEvent(new AddEventParams.Builder(FatherEvent.ID, FatherEvent.class)
                .playerClass(QINGMU_SAKIKO)
                .dungeonID(TheCity.ID)
                .eventType(EventUtils.EventType.NORMAL)
                .create());
        // 注册3层事件
        BaseMod.addEvent(new AddEventParams.Builder(DilapidatedCorridorEvent.ID, DilapidatedCorridorEvent.class)
                .playerClass(QINGMU_SAKIKO)
                .dungeonID(TheBeyond.ID)
                .eventType(EventUtils.EventType.NORMAL)
                .create());
        // 注册所有层数通用事件
        BaseMod.addEvent(new AddEventParams.Builder(AlbumSell.ID, AlbumSell.class)
                .playerClass(QINGMU_SAKIKO)
                .eventType(EventUtils.EventType.NORMAL)
                .create());
        // 重写坠落，使其支持音乐牌
        BaseMod.addEvent(new AddEventParams.Builder(SakikoFalling.ID, SakikoFalling.class)
                .playerClass(QINGMU_SAKIKO)
                .eventType(EventUtils.EventType.OVERRIDE)
                .overrideEvent(Falling.ID)
                .create());
    }

    @Override
    public void receiveStartGame() {
        if (AbstractDungeon.floorNum == 0) {
            ((InvasionChangeSaved) BaseMod.getSaveFields().get("chance")).chance = 0;
        }

        // 尝试修复sl导致的音频丢失问题
        CardCrawlGame.music.dispose();
        HashMap<String, Sfx> map = ReflectionHacks.getPrivate(CardCrawlGame.sound, SoundMaster.class, "map");
        for (Sfx sfx : map.values()) {
            Sound sound = ReflectionHacks.getPrivate(sfx, Sfx.class, "sound");
            if (sound != null) {
                sound.dispose();
            }
        }
        CardCrawlGame.sound = new SoundMaster();
        CardCrawlGame.music = new MusicMaster();
    }

    @Override
    public void receiveCardUsed(AbstractCard card) {
        if (card instanceof AbstractSakikoCard) {
            AbstractSakikoCard sakikoCard = (AbstractSakikoCard) card;
            if (sakikoCard.upgraded) {
                sakikoCard.rawDescription = sakikoCard.UPGRADE_DESCRIPTION == null ? sakikoCard.DESCRIPTION : sakikoCard.UPGRADE_DESCRIPTION.isEmpty() ? sakikoCard.DESCRIPTION : sakikoCard.UPGRADE_DESCRIPTION;
            } else {
                sakikoCard.rawDescription = sakikoCard.DESCRIPTION;
            }
            sakikoCard.initializeDescription();
        }
    }

    public void unlockedAscension() {
        if (SAKIKO_CONFIG.getBool("ascensionUnlock")) {
            BaseMod.getModdedCharacters().forEach(character -> {
                if (character instanceof TogawaSakiko) {
                    Prefs prefs = character.getPrefs();
                    if (prefs.getInteger("WIN_COUNT", 0) < 20) {
                        prefs.putInteger("WIN_COUNT", 20);
                    }
                    prefs.putBoolean(character.chosenClass.name() + "_WIN", true);
                    prefs.putBoolean("ASCEND_0", true);
                    prefs.putInteger("ASCENSION_LEVEL", 20);
                    prefs.putInteger("LAST_ASCENSION_LEVEL", 20);
                    prefs.flush();
                }
            });
        }
    }

    public void registerConfigUI() {
        UIStrings config = CardCrawlGame.languagePack.getUIString(ModNameHelper.make("Config"));
        // config页面
        ModPanel modPanel = new ModPanel();
        modPanel.addUIElement(new ModMinMaxSlider(config.TEXT[0], 500.0f, 720.0f, 0, 1, SAKIKO_CONFIG.getFloat("modSound"), null, modPanel, (slider) -> {
            SAKIKO_CONFIG.setFloat("modSound", slider.getValue());
            try {
                SAKIKO_CONFIG.save();
            } catch (IOException e) {
                logger.error(e);
            }
        }));
        if (Loader.isModLoaded("AnonMod")) {
            modPanel.addUIElement(new ModLabeledToggleButton(config.TEXT[3], 1000.0f, 700.0f, Color.WHITE, FontHelper.buttonLabelFont, SAKIKO_CONFIG.getBool("enableAnonCard"), modPanel, (modLabel) -> {
            }, (modToggleButton) -> {
                SAKIKO_CONFIG.setBool("enableAnonCard", modToggleButton.enabled);
                try {
                    SAKIKO_CONFIG.save();
                } catch (IOException e) {
                    logger.error(e);
                }
            }));
        }
        modPanel.addUIElement(new ModLabeledToggleButton(config.TEXT[1], 390.0f, 630.0f, Color.WHITE, FontHelper.buttonLabelFont, SAKIKO_CONFIG.getBool("enableBoss"), modPanel, (modLabel) -> {
        }, (modToggleButton) -> {
            SAKIKO_CONFIG.setBool("enableBoss", modToggleButton.enabled);
            try {
                SAKIKO_CONFIG.save();
            } catch (IOException e) {
                logger.error(e);
            }
        }));
        ModLabeledToggleButton ascensionUnlock = new ModLabeledToggleButton(config.TEXT[4], 390.0f, 550.0f, Color.WHITE, FontHelper.buttonLabelFont, SAKIKO_CONFIG.getBool("ascensionUnlock"), modPanel, (modLabel) -> {
        }, (modToggleButton) -> {
            SAKIKO_CONFIG.setBool("ascensionUnlock", modToggleButton.enabled);
            try {
                SAKIKO_CONFIG.save();
            } catch (IOException e) {
                logger.error(e);
            }
        });
        ascensionUnlock.tooltip = config.EXTRA_TEXT[1];
        modPanel.addUIElement(ascensionUnlock);
        BaseMod.registerModBadge(new Texture("SakikoModResources/img/sakikomod_badge32.png"), "sakikoMod", "QingMu", "sakikoMod", modPanel);
    }

    public void registerMemberCollect() {
        // 添加成员入侵事件
        BaseMod.addEvent(new AddEventParams.Builder(InvasionEvent.ID, InvasionEvent.class).playerClass(QINGMU_SAKIKO)
                .spawnCondition(() -> false).bonusCondition(() -> false)
                .endsWithRewardsUI(true).create());
        // 添加乐队成员
        BaseMod.addMonster(UikaMonster.ID, UikaMonster.NAME, () -> new UikaMonster(0.0F, 0.0F));
        BaseMod.addMonster(MutsumiMonster.ID, MutsumiMonster.NAME, () -> new MutsumiMonster(0.0F, 0.0F));
        BaseMod.addMonster(NyamuchiMonster.ID, NyamuchiMonster.NAME, () -> new NyamuchiMonster(0.0F, 0.0F));
        BaseMod.addMonster(UmiriMonster.ID, UmiriMonster.NAME, () -> new UmiriMonster(0.0F, 0.0F));
        BaseMod.addMonster(TomoriMonster.ID, TomoriMonster.NAME, () -> new TomoriMonster(0.0F, 0.0F));
        BaseMod.addMonster(AnonMonster.ID, AnonMonster.NAME, () -> new AnonMonster(0.0F, 0.0F));
        BaseMod.addMonster(TakiMonster.ID, TakiMonster.NAME, () -> new TakiMonster(0.0F, 0.0F));
        BaseMod.addMonster(SoyoMonster.ID, SoyoMonster.NAME, () -> new SoyoMonster(0.0F, 0.0F));
        BaseMod.addMonster(RanaMonster.ID, RanaMonster.NAME, () -> new RanaMonster(0.0F, 0.0F));
        // 添加成员遭遇战
        BaseMod.addMonsterEncounter(TheEnding.ID, new MonsterInfo(UikaMonster.NAME, 0));
        BaseMod.addMonsterEncounter(TheEnding.ID, new MonsterInfo(MutsumiMonster.NAME, 0));
        BaseMod.addMonsterEncounter(TheEnding.ID, new MonsterInfo(NyamuchiMonster.NAME, 0));
        BaseMod.addMonsterEncounter(TheEnding.ID, new MonsterInfo(UmiriMonster.NAME, 0));
        BaseMod.addMonsterEncounter(TheEnding.ID, new MonsterInfo(TomoriMonster.NAME, 0));
        BaseMod.addMonsterEncounter(TheEnding.ID, new MonsterInfo(AnonMonster.NAME, 0));
        BaseMod.addMonsterEncounter(TheEnding.ID, new MonsterInfo(TakiMonster.NAME, 0));
        BaseMod.addMonsterEncounter(TheEnding.ID, new MonsterInfo(SoyoMonster.NAME, 0));
        BaseMod.addMonsterEncounter(TheEnding.ID, new MonsterInfo(RanaMonster.NAME, 0));
        // 添加成员入侵事件概率
        BaseMod.addSaveField("chance", new InvasionChangeSaved());
    }

    public void registerBoss() {
        BaseMod.addMonster(InnerDemonSakiko.ID, InnerDemonSakiko.NAME, () -> new InnerDemonSakiko(0.0F, 0.0F));
        BaseMod.addMonsterEncounter(TheEnding.ID, new MonsterInfo(InnerDemonSakiko.NAME, 0));
    }

}

