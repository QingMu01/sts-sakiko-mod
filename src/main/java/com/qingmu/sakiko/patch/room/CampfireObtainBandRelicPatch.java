package com.qingmu.sakiko.patch.room;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.qingmu.sakiko.relics.*;
import com.qingmu.sakiko.relics.menbers.*;
import com.qingmu.sakiko.utils.MemberHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpirePatch(clz = RestRoom.class, method = "onPlayerEntry")
public class CampfireObtainBandRelicPatch {

    public static final List<String> AVE_MUJICA = Arrays.asList(Uika.ID, Mutsumi.ID, Nyamuchi.ID, Umiri.ID);

    public static final List<String> CRYCHIC = Arrays.asList(Tomori.ID, Taki.ID, Soyo.ID, Mutsumi.ID);


    public static void Postfix(RestRoom __instance) {
        if (AbstractDungeon.id.equals(TheBeyond.ID)) {
            ArrayList<MapEdge> edges = AbstractDungeon.getCurrMapNode().getEdges();
            if (edges.size() == 1 && edges.get(0).dstY == 16) {
                if (MemberHelper.getBandMemberCount() == 1) {
                    // 联机做梦Boss钥匙
                    if (AbstractDungeon.player.hasRelic(Anon.ID) && !AbstractDungeon.player.hasRelic(Combination_ANSK.ID)) {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f, new Combination_ANSK());
                    }
                    // 加成道具
                    if (AbstractDungeon.player.hasRelic(Tomori.ID) && !AbstractDungeon.player.hasRelic(Combination_TMSK.ID)) {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f, new Combination_TMSK());
                    }
                    // 加成道具
                    if (AbstractDungeon.player.hasRelic(Uika.ID) && !AbstractDungeon.player.hasRelic(Combination_UKSK.ID)) {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f, new Combination_UKSK());
                    }
                } else {
                    if (AVE_MUJICA.stream().allMatch(AbstractDungeon.player::hasRelic) && !AbstractDungeon.player.hasRelic(Band_AVEMUJICA.ID)) {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f, new Band_AVEMUJICA());
                    } else if (CRYCHIC.stream().allMatch(AbstractDungeon.player::hasRelic) && !AbstractDungeon.player.hasRelic(Band_CRYCHIC.ID)) {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f, new Band_CRYCHIC());
                    }
                }
            }
        }
    }

}
