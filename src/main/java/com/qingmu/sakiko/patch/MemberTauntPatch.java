package com.qingmu.sakiko.patch;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.monsters.AbstractFriendlyMonster;
import com.qingmu.sakiko.powers.FallApartPower;
import com.qingmu.sakiko.powers.MemberPower;
import com.qingmu.sakiko.utils.DungeonHelper;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class MemberTauntPatch {

    // 修改攻击目标
    @SpirePatch(clz = DamageAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCreature.class, DamageInfo.class, AbstractGameAction.AttackEffect.class})
    public static class DamageActionPatch {
        public static void Postfix(DamageAction __instance, AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect) {
            AbstractFriendlyMonster monster = getEffectMonster();
            if (monster != null) {
                if (info.type == DamageInfo.DamageType.NORMAL && !__instance.source.isPlayer && !(__instance.source instanceof AbstractFriendlyMonster) && !DungeonHelper.getPlayer().hasPower(FallApartPower.POWER_ID)) {
                    __instance.target = monster;
                }
            }
        }
    }

    // 修改BUFF目标
    @SpirePatch(clz = ApplyPowerAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCreature.class, AbstractCreature.class, AbstractPower.class, int.class, boolean.class, AbstractGameAction.AttackEffect.class})
    public static class ApplyPowerActionPatch {
        public static void Postfix(ApplyPowerAction __instance) {
            AbstractFriendlyMonster monster = getEffectMonster();
            if (monster != null) {
                if (__instance.target.isPlayer && !__instance.source.isPlayer && !(__instance.source instanceof AbstractFriendlyMonster) && !DungeonHelper.getPlayer().hasPower(FallApartPower.POWER_ID)) {
                    __instance.target = monster;
                    AbstractPower powerToApply = ReflectionHacks.getPrivate(__instance, ApplyPowerAction.class, "powerToApply");
                    powerToApply.owner = monster;
                }
            }
        }
    }

    // 重新计算攻击伤害
    @SpirePatch(clz = AbstractMonster.class, method = "applyPowers")
    public static class ApplyPowersPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getFieldName().equals("player")) {
                        f.replace("{ " +
                                "$_ = $proceed($$);" +
                                "if (" + MemberTauntPatch.class.getName() + ".getEffectMonster() != null) {" +
                                " $_ = " + MemberTauntPatch.class.getName() + ".getEffectMonster(); " +
                                "}else {" +
                                "$_ = " + AbstractDungeon.class.getName() + ".player;" +
                                "}" +
                                "}"
                        );
                    }
                }
            };
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "calculateDamage")
    public static class CalculateDamagePatch {
        public static SpireReturn<Void> Prefix(AbstractMonster __instance, int dmg, @ByRef int[] ___intentDmg) {
            AbstractFriendlyMonster effectMonster = getEffectMonster();
            if (effectMonster != null) {
                float tmp = (float) dmg;
                for (AbstractPower p : __instance.powers) {
                    tmp = p.atDamageGive(tmp, DamageInfo.DamageType.NORMAL);
                }
                for (AbstractPower p : effectMonster.powers) {
                    tmp = p.atDamageReceive(tmp, DamageInfo.DamageType.NORMAL);
                }
                for (AbstractPower p : __instance.powers) {
                    tmp = p.atDamageFinalGive(tmp, DamageInfo.DamageType.NORMAL);
                }
                for (AbstractPower p : effectMonster.powers) {
                    tmp = p.atDamageFinalReceive(tmp, DamageInfo.DamageType.NORMAL);
                }
                dmg = MathUtils.floor(tmp);
                if (dmg < 0) {
                    dmg = 0;
                }
                ___intentDmg[0] = dmg;
                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    public static AbstractFriendlyMonster getEffectMonster() {
        MonsterGroup monsterGroup = DungeonHelper.getFriendlyMonsterGroup();
        if (monsterGroup != null) {
            for (AbstractMonster monster : monsterGroup.monsters) {
                if (!monster.isDying && !monster.isDead && !monster.isEscaping) {
                    if (monster.hasPower(MemberPower.POWER_ID)) {
                        return (AbstractFriendlyMonster) monster;
                    }
                }
            }
        }
        return null;
    }
}
