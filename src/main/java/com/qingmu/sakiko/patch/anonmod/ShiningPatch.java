package com.qingmu.sakiko.patch.anonmod;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.qingmu.sakiko.powers.KirameiPower;
import javassist.*;
import power.Shining;

@SpirePatch(requiredModId = "AnonMod", optional = true, clz = Shining.class, method = SpirePatch.STATICINITIALIZER)
public class ShiningPatch {
    public static void Raw(CtBehavior ctBehavior) throws Exception {
        ClassPool pool = ctBehavior.getDeclaringClass().getClassPool();

        CtClass shining = pool.get(Shining.class.getName());
        CtClass kiramei = pool.get(KirameiPower.class.getName());

        shining.setInterfaces(kiramei.getInterfaces());
        CtMethod newMethod = CtNewMethod.make("public float modifyMusicNumber(com.megacrit.cardcrawl.cards.AbstractCard card, float musicNumber) {\n" +
                "        return com.qingmu.sakiko.SakikoModCore.SAKIKO_CONFIG.getBool(\"enableAnonCard\")?musicNumber + this.amount:musicNumber;\n" +
                "    }", shining);
        shining.addMethod(newMethod);

    }
}
