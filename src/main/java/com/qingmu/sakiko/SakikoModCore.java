package com.qingmu.sakiko;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import basemod.eventUtil.AddEventParams;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.qingmu.sakiko.characters.TogawaSakiko;
import com.qingmu.sakiko.events.InvasionEvent;
import com.qingmu.sakiko.monsters.*;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.rewards.MusicCardReward;
import com.qingmu.sakiko.screens.MusicDrawPileViewScreen;
import com.qingmu.sakiko.utils.InvasionChangeSaved;

import java.nio.charset.StandardCharsets;

import static com.qingmu.sakiko.patch.SakikoEnum.CharacterEnum.QINGMU_SAKIKO;
import static com.qingmu.sakiko.patch.SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD;


@SpireInitializer
public class SakikoModCore implements EditCardsSubscriber, EditRelicsSubscriber, StartGameSubscriber,
        EditCharactersSubscriber, EditStringsSubscriber,
        EditKeywordsSubscriber, PostInitializeSubscriber {

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

    public static final Color SAKIKO_COLOR = new Color(119.0F / 255.0F, 153.0F / 255.0F, 204.0F / 255.0F, 1.0F);

    public SakikoModCore() {
        BaseMod.subscribe(this);
        BaseMod.addColor(QINGMU_SAKIKO_CARD, SAKIKO_COLOR, SAKIKO_COLOR, SAKIKO_COLOR, SAKIKO_COLOR, SAKIKO_COLOR, SAKIKO_COLOR, SAKIKO_COLOR, BG_ATTACK_512, BG_SKILL_512, BG_POWER_512, ENERGY_ORB, BG_ATTACK_1024, BG_SKILL_1024, BG_POWER_1024, BIG_ORB, SMALL_ORB);
    }

    public static void initialize() {
        new SakikoModCore();
    }

    @Override
    public void receiveEditCards() {
        new AutoAdd("sakikoMod")
                .packageFilter("com.qingmu.sakiko.cards")
                .setDefaultSeen(true)
                .cards();
    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd("sakikoMod")
                .packageFilter("com.qingmu.sakiko.relics")
                .any(CustomRelic.class, (info, relic) -> {
                    BaseMod.addRelicToCustomPool(relic, QINGMU_SAKIKO_CARD);
                    UnlockTracker.markRelicAsSeen(relic.relicId);
                    if (info.seen) {
                        UnlockTracker.markRelicAsSeen(relic.relicId);
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
    }

    @Override
    public void receivePostInitialize() {
        // 添加成员入侵事件
        BaseMod.addEvent(new AddEventParams.Builder(InvasionEvent.ID, InvasionEvent.class).playerClass(QINGMU_SAKIKO)
                .spawnCondition(() -> false).bonusCondition(() -> false)
                .endsWithRewardsUI(false).create());

        // 添加音乐堆预览页面
        BaseMod.addCustomScreen(new MusicDrawPileViewScreen());

        // 注册音乐牌奖励
        BaseMod.registerCustomReward(SakikoEnum.RewardType.MUSIC_TYPE,
                (rewardSave) -> new MusicCardReward(rewardSave.id),
                (customReward) -> new RewardSave(customReward.type.toString(), ((MusicCardReward) customReward).id));

        // 添加乐队成员
        BaseMod.addMonster(UikaMonster.ID, UikaMonster.ID, () -> new UikaMonster(0.0F, 0.0F));
        BaseMod.addMonster(MutsumiMonster.ID, MutsumiMonster.ID, () -> new MutsumiMonster(0.0F, 0.0F));
        BaseMod.addMonster(NyamuchiMonster.ID, NyamuchiMonster.ID, () -> new NyamuchiMonster(0.0F, 0.0F));
        BaseMod.addMonster(UmiriMonster.ID, UmiriMonster.ID, () -> new UmiriMonster(0.0F, 0.0F));
        BaseMod.addMonster(TomoriMonster.ID, TomoriMonster.ID, () -> new TomoriMonster(0.0F, 0.0F));
        BaseMod.addMonster(AnonMonster.ID, AnonMonster.ID, () -> new AnonMonster(0.0F, 0.0F));
        BaseMod.addMonster(TakiMonster.ID, TakiMonster.ID, () -> new TakiMonster(0.0F, 0.0F));
        BaseMod.addMonster(SoyoMonster.ID, SoyoMonster.ID, () -> new SoyoMonster(0.0F, 0.0F));
        BaseMod.addMonster(RanaMonster.ID, RanaMonster.ID, () -> new RanaMonster(0.0F, 0.0F));
        // 添加成员遭遇战
        BaseMod.addMonsterEncounter(TheEnding.ID, new MonsterInfo(UikaMonster.ID, 0));
        BaseMod.addMonsterEncounter(TheEnding.ID, new MonsterInfo(MutsumiMonster.ID, 0));
        BaseMod.addMonsterEncounter(TheEnding.ID, new MonsterInfo(NyamuchiMonster.ID, 0));
        BaseMod.addMonsterEncounter(TheEnding.ID, new MonsterInfo(UmiriMonster.ID, 0));
        BaseMod.addMonsterEncounter(TheEnding.ID, new MonsterInfo(TomoriMonster.ID, 0));
        BaseMod.addMonsterEncounter(TheEnding.ID, new MonsterInfo(AnonMonster.ID, 0));
        BaseMod.addMonsterEncounter(TheEnding.ID, new MonsterInfo(TakiMonster.ID, 0));
        BaseMod.addMonsterEncounter(TheEnding.ID, new MonsterInfo(SoyoMonster.ID, 0));
        BaseMod.addMonsterEncounter(TheEnding.ID, new MonsterInfo(RanaMonster.ID, 0));

        // 添加成员入侵事件概率
        BaseMod.addSaveField("chance", new InvasionChangeSaved());
    }

    @Override
    public void receiveStartGame() {
        if (AbstractDungeon.floorNum == 0) {
            ((InvasionChangeSaved) BaseMod.getSaveFields().get("chance")).chance = 0;
        }
    }
}

