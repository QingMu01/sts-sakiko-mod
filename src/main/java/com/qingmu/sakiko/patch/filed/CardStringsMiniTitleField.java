package com.qingmu.sakiko.patch.filed;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.annotations.SerializedName;
import com.megacrit.cardcrawl.localization.CardStrings;

@SpirePatch(clz = CardStrings.class,method = SpirePatch.CLASS)
public class CardStringsMiniTitleField {
    @SerializedName("MINI_TITLE")
    public static SpireField<String> miniTitle = new SpireField<>(() -> "");
}
