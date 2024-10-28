package com.qingmu.sakiko.patch.room;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.relics.*;
import com.qingmu.sakiko.relics.menbers.Anon;
import com.qingmu.sakiko.relics.menbers.Tomori;
import com.qingmu.sakiko.relics.menbers.Uika;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.MemberHelper;

import java.util.ArrayList;

@SpirePatch(clz = RestRoom.class, method = "onPlayerEntry")
public class CampfireObtainBandRelicPatch {

    public static void Postfix(RestRoom __instance) {
        if (AbstractDungeon.id.equals(TheBeyond.ID)) {
            ArrayList<MapEdge> edges = AbstractDungeon.getCurrMapNode().getEdges();
            if (edges.size() == 1 && edges.get(0).dstY == 16) {
                AbstractPlayer player = DungeonHelper.getPlayer();
                if (MemberHelper.getCount() == 1) {
                    // 爱祥
                    if (player.hasRelic(Anon.ID) && !player.hasRelic(Combination_ANSK.ID)) {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f, new Combination_ANSK());
                    }
                    // 灯祥
                    if (player.hasRelic(Tomori.ID) && !player.hasRelic(Combination_TMSK.ID)) {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f, new Combination_TMSK());
                    }
                    // 初祥
                    if (player.hasRelic(Uika.ID) && !player.hasRelic(Combination_UKSK.ID)) {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f, new Combination_UKSK());
                    }
                } else {
                    if (SakikoConst.AVE_MUJICA.stream().allMatch(AbstractDungeon.player::hasRelic) && !player.hasRelic(Band_AVEMUJICA.ID)) {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f, new Band_AVEMUJICA());
                    } else if (SakikoConst.CRYCHIC.stream().allMatch(AbstractDungeon.player::hasRelic) && !player.hasRelic(Band_CRYCHIC.ID)) {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f, new Band_CRYCHIC());
                    }
                }
            }
        }
    }
}
