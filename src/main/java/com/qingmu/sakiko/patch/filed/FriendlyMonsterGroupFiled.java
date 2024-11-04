package com.qingmu.sakiko.patch.filed;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

@SpirePatch(clz = AbstractRoom.class, method = SpirePatch.CLASS)
public class FriendlyMonsterGroupFiled {

    public static SpireField<MonsterGroup> friendlyMonsterGroup = new SpireField<>(() -> null);

}
