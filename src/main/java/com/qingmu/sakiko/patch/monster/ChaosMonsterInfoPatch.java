package com.qingmu.sakiko.patch.monster;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.monsters.AbstractFriendlyMonster;
import com.qingmu.sakiko.powers.MashiroGiftPower;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;
import javassist.CtBehavior;

import java.util.Arrays;
import java.util.List;

public class ChaosMonsterInfoPatch {

    private static final float HEALTH_BAR_HEIGHT = 20.0F * Settings.scale;

    private static final float HEALTH_BAR_OFFSET_Y = -28.0F * Settings.scale;

    private static final float HEALTH_TEXT_OFFSET_Y = 6.0F * Settings.scale;

    private static final float BLOCK_ICON_X = -14.0F * Settings.scale;

    private static final float BLOCK_ICON_Y = -14.0F * Settings.scale;

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make("ChaosInfo"));

    private static final List<Texture> INTENT_TEXTURES = Arrays.asList(ImageMaster.INTENT_ATK_TIP_1, ImageMaster.INTENT_ATK_TIP_2, ImageMaster.INTENT_ATK_TIP_3, ImageMaster.INTENT_ATK_TIP_4, ImageMaster.INTENT_ATK_TIP_5, ImageMaster.INTENT_ATK_TIP_6, ImageMaster.INTENT_ATK_TIP_7, ImageMaster.INTENT_BUFF, ImageMaster.INTENT_DEBUFF, ImageMaster.INTENT_DEBUFF2, ImageMaster.INTENT_DEFEND, ImageMaster.INTENT_DEFEND, ImageMaster.INTENT_DEFEND_BUFF, ImageMaster.INTENT_ESCAPE, ImageMaster.INTENT_MAGIC, ImageMaster.INTENT_SLEEP, ImageMaster.INTENT_UNKNOWN, ImageMaster.INTENT_STUN);


    @SpirePatch(clz = AbstractMonster.class, method = "createIntent")
    public static class CreateIntentPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void insert(AbstractMonster __instance, @ByRef int[] ___intentBaseDmg, @ByRef int[] ___intentMultiAmt, @ByRef boolean[] ___isMultiDmg) {
            if (__instance instanceof AbstractFriendlyMonster) return;
            if (DungeonHelper.getPlayer().hasPower(MashiroGiftPower.POWER_ID)) {
                __instance.intent = AbstractMonster.Intent.values()[AbstractDungeon.aiRng.random(AbstractMonster.Intent.values().length - 1)];
                ___intentBaseDmg[0] = AbstractDungeon.aiRng.random(80);
                boolean isMultiDamage = AbstractDungeon.aiRng.randomBoolean();
                if (isMultiDamage) {
                    ___intentMultiAmt[0] = AbstractDungeon.aiRng.random(5);
                    ___isMultiDmg[0] = true;
                } else {
                    ___intentMultiAmt[0] = -1;
                    ___isMultiDmg[0] = false;
                }
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "getIntentImg");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "updateIntentTip")
    public static class RandomIntentTips {
        public static SpireReturn<Void> Prefix(AbstractMonster __instance, @ByRef PowerTip[] ___intentTip) {
            if (__instance instanceof AbstractFriendlyMonster) return SpireReturn.Continue();
            if (DungeonHelper.getPlayer().hasPower(MashiroGiftPower.POWER_ID)) {
                String[] header = uiStrings.TEXT;
                String[] body = uiStrings.EXTRA_TEXT;
                int random = AbstractDungeon.aiRng.random(header.length - 1);
                ___intentTip[0].header = header[random];
                ___intentTip[0].body = body[random];
                ___intentTip[0].img = INTENT_TEXTURES.get(AbstractDungeon.aiRng.random(INTENT_TEXTURES.size()-1));
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "renderHealthText")
    public static class ChaosHpPatch {
        public static SpireReturn<Void> Prefix(AbstractCreature __instance, SpriteBatch sb, float y, Color ___hbTextColor, float ___healthHideTimer) {
            if (__instance instanceof AbstractFriendlyMonster) return SpireReturn.Continue();
            if (!__instance.isPlayer && DungeonHelper.getPlayer().hasPower(MashiroGiftPower.POWER_ID)) {
                FakeMonsterInfo fakeMonsterInfo = FakeMonsterInfoPatch.fakeMonsterInfo.get(__instance);
                ___hbTextColor.a *= ___healthHideTimer;
                FontHelper.renderFontCentered(sb, FontHelper.healthInfoFont, fakeMonsterInfo.currentHp + "/" + fakeMonsterInfo.maxHp, __instance.hb.cX, y + HEALTH_BAR_OFFSET_Y + HEALTH_TEXT_OFFSET_Y + 5.0F * Settings.scale, ___hbTextColor);
                return SpireReturn.Return();
            } else return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "renderOrangeHealthBar")
    public static class ChaosOrangeHpPatch {
        public static SpireReturn<Void> Prefix(AbstractCreature __instance, SpriteBatch sb, float x, float y, Color ___orangeHbBarColor, float ___healthBarWidth) {
            if (__instance instanceof AbstractFriendlyMonster) return SpireReturn.Continue();
            if (!__instance.isPlayer && DungeonHelper.getPlayer().hasPower(MashiroGiftPower.POWER_ID)) {
                sb.setColor(___orangeHbBarColor);
                sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
                sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, ___healthBarWidth, HEALTH_BAR_HEIGHT);
                sb.draw(ImageMaster.HEALTH_BAR_R, x + ___healthBarWidth, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
                return SpireReturn.Return();
            } else return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "renderGreenHealthBar")
    public static class ChaosGreenHpPatch {
        public static SpireReturn<Void> Prefix(AbstractCreature __instance, SpriteBatch sb, float x, float y, Color ___greenHbBarColor, float ___healthBarWidth) {
            if (__instance instanceof AbstractFriendlyMonster) return SpireReturn.Continue();
            if (!__instance.isPlayer && DungeonHelper.getPlayer().hasPower(MashiroGiftPower.POWER_ID)) {
                sb.setColor(___greenHbBarColor);
                FakeMonsterInfo fakeMonsterInfo = FakeMonsterInfoPatch.fakeMonsterInfo.get(__instance);
                if (fakeMonsterInfo.currentHp > 0)
                    sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
                sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, ___healthBarWidth, HEALTH_BAR_HEIGHT);
                sb.draw(ImageMaster.HEALTH_BAR_R, x + ___healthBarWidth, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
                return SpireReturn.Return();
            } else return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "renderRedHealthBar")
    public static class ChaosRedHpPatch {
        public static SpireReturn<Void> Prefix(AbstractCreature __instance, SpriteBatch sb, float x, float y, Color ___blueHbBarColor, Color ___redHbBarColor, float ___targetHealthBarWidth) {
            if (__instance instanceof AbstractFriendlyMonster) return SpireReturn.Continue();
            if (!__instance.isPlayer && DungeonHelper.getPlayer().hasPower(MashiroGiftPower.POWER_ID)) {
                FakeMonsterInfo fakeMonsterInfo = FakeMonsterInfoPatch.fakeMonsterInfo.get(__instance);
                if (__instance.currentBlock > 0) {
                    sb.setColor(___blueHbBarColor);
                } else {
                    sb.setColor(___redHbBarColor);
                }
                if (!__instance.hasPower("Poison")) {
                    if (fakeMonsterInfo.currentHp > 0) {
                        sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
                    }
                    sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, ___targetHealthBarWidth, HEALTH_BAR_HEIGHT);
                    sb.draw(ImageMaster.HEALTH_BAR_R, x + ___targetHealthBarWidth, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
                } else {
                    int poisonAmt = (__instance.getPower("Poison")).amount;
                    if (poisonAmt > 0 && __instance.hasPower("Intangible"))
                        poisonAmt = 1;
                    if (fakeMonsterInfo.currentHp > poisonAmt) {
                        float w = 1.0F - (float) (fakeMonsterInfo.currentHp - poisonAmt) / fakeMonsterInfo.currentHp;
                        w *= ___targetHealthBarWidth;
                        if (fakeMonsterInfo.currentHp > 0)
                            sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
                        sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, ___targetHealthBarWidth - w, HEALTH_BAR_HEIGHT);
                        sb.draw(ImageMaster.HEALTH_BAR_R, x + ___targetHealthBarWidth - w, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
                    }
                }
                return SpireReturn.Return();
            } else return SpireReturn.Continue();
        }

        @SpirePatch(clz = AbstractCreature.class, method = "renderBlockOutline")
        public static class ChaosBlockOutLinePatch {
            public static SpireReturn<Void> Prefix(AbstractCreature __instance, SpriteBatch sb, float x, float y, Color ___blockOutlineColor) {
                if (__instance instanceof AbstractFriendlyMonster) return SpireReturn.Continue();
                if (!__instance.isPlayer && DungeonHelper.getPlayer().hasPower(MashiroGiftPower.POWER_ID)) {
                    sb.setColor(___blockOutlineColor);
                    sb.setBlendFunction(770, 1);
                    sb.draw(ImageMaster.BLOCK_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
                    sb.draw(ImageMaster.BLOCK_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, __instance.hb.width, HEALTH_BAR_HEIGHT);
                    sb.draw(ImageMaster.BLOCK_BAR_R, x + __instance.hb.width, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
                    sb.setBlendFunction(770, 771);
                    return SpireReturn.Return();
                } else return SpireReturn.Continue();
            }
        }

        @SpirePatch(clz = AbstractCreature.class, method = "renderBlockIconAndValue")
        public static class ChaosBlockValue {
            public static SpireReturn<Void> Prefix(AbstractCreature __instance, SpriteBatch sb, float x, float y, Color ___blockColor, Color ___blockTextColor, float ___blockScale, float ___blockOffset) {
                if (__instance instanceof AbstractFriendlyMonster) return SpireReturn.Continue();
                if (!__instance.isPlayer && DungeonHelper.getPlayer().hasPower(MashiroGiftPower.POWER_ID) && __instance.currentBlock > 0) {
                    FakeMonsterInfo fakeMonsterInfo = FakeMonsterInfoPatch.fakeMonsterInfo.get(__instance);
                    sb.setColor(___blockColor);
                    sb.draw(ImageMaster.BLOCK_ICON, x + BLOCK_ICON_X - 32.0F, y + BLOCK_ICON_Y - 32.0F + ___blockOffset, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
                    FontHelper.renderFontCentered(sb, FontHelper.blockInfoFont, Integer.toString(__instance.currentBlock + fakeMonsterInfo.currentBlock), x + BLOCK_ICON_X, y - 16.0F * Settings.scale, ___blockTextColor, ___blockScale);
                    return SpireReturn.Return();
                } else return SpireReturn.Continue();
            }
        }

    }

    @SpirePatch(clz = AbstractCreature.class, method = "healthBarUpdatedEvent")
    public static class FakeUpdateEvent {
        public static SpireReturn<Void> Prefix(AbstractCreature __instance, @ByRef float[] ___healthBarAnimTimer, @ByRef float[] ___targetHealthBarWidth, @ByRef float[] ___healthBarWidth) {
            if (__instance instanceof AbstractFriendlyMonster) return SpireReturn.Continue();
            if (!__instance.isPlayer && DungeonHelper.getPlayer().hasPower(MashiroGiftPower.POWER_ID)) {
                FakeMonsterInfo fakeMonsterInfo = FakeMonsterInfoPatch.fakeMonsterInfo.get(__instance);
                ___healthBarAnimTimer[0] = 1.2F;
                ___targetHealthBarWidth[0] = __instance.hb.width * (float) fakeMonsterInfo.currentHp / (float) fakeMonsterInfo.maxHp;
                if (fakeMonsterInfo.maxHp == fakeMonsterInfo.currentHp) {
                    ___healthBarWidth[0] = ___targetHealthBarWidth[0];
                } else if (fakeMonsterInfo.currentHp == 0) {
                    ___healthBarWidth[0] = 0.0F;
                    ___targetHealthBarWidth[0] = 0.0F;
                }
                if (___targetHealthBarWidth[0] > ___healthBarWidth[0]) {
                    ___healthBarWidth[0] = ___targetHealthBarWidth[0];
                }
                return SpireReturn.Return();
            } else return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class FakeDamageInfoPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void insert(AbstractMonster __instance, DamageInfo info) {
            if (__instance instanceof AbstractFriendlyMonster) return;
            if (DungeonHelper.getPlayer().hasPower(MashiroGiftPower.POWER_ID)) {
                FakeMonsterInfo fakeMonsterInfo = FakeMonsterInfoPatch.fakeMonsterInfo.get(__instance);
                fakeMonsterInfo.updateCurrentHp(info.output);
                if (__instance.currentBlock > fakeMonsterInfo.currentBlock) {
                    fakeMonsterInfo.currentBlock -= info.output;
                } else {
                    fakeMonsterInfo.currentBlock = 0;
                }
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "healthBarUpdatedEvent");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }

    }


    public static class FakeMonsterInfo {
        public int currentHp;
        public int maxHp;
        public int currentBlock;
        private boolean isInit = false;

        public FakeMonsterInfo() {
        }

        public void updateCurrentHp(int currentHp) {
            this.currentHp -= currentHp;
            if (this.currentHp < 0) this.currentHp = AbstractDungeon.aiRng.random(10, maxHp);
        }


        public void init(int currentHp, int maxHp, int currentBlock) {
            if (!this.isInit) {
                this.currentHp = AbstractDungeon.aiRng.random(Math.max(currentHp - 50, 10), currentHp + 50);
                this.maxHp = AbstractDungeon.aiRng.random(this.currentHp, maxHp + 50);
                this.currentBlock = AbstractDungeon.aiRng.random(10, currentBlock + 20);
                this.isInit = true;
            }
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = SpirePatch.CLASS)
    public static class FakeMonsterInfoPatch {
        public static SpireField<FakeMonsterInfo> fakeMonsterInfo = new SpireField<>(FakeMonsterInfo::new);
    }
}
