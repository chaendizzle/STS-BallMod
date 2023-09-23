package balls.patches;

import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardItem.RewardType;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.vfx.ObtainKeyEffect;

import balls.relics.Baal;

public class MakeAllElitesBurning {

    @SpirePatch2(
        clz = MonsterRoomElite.class,
        method = "addEmeraldKey"
    )
    public static class AddEmeraldKeyAfterElitePatch {
        public static void Postfix(MonsterRoomElite __instance) {
            if (Settings.hasEmeraldKey && AbstractDungeon.player.hasRelic(Baal.RELIC_ID)) {
                __instance.rewards.add(
                    new RewardItem(
                        __instance.rewards.get(__instance.rewards.size() - 1),
                        RewardType.EMERALD_KEY));
            }
        }
    }

    @SpirePatch2(
        clz = AbstractDungeon.class,
        method = "setEmeraldElite"
    )
    public static class MakeEveryEliteBurningPatch {
        public static void Postfix() {
            if (AbstractDungeon.player.hasRelic(Baal.RELIC_ID)) {
                ArrayList<ArrayList<MapRoomNode>> map = AbstractDungeon.map;
                for (int i = 0; i < map.size(); i++) {
                    for (int j = 0; j < (map.get(i)).size(); j++) {
                        MapRoomNode roomNode = ((MapRoomNode)(map.get(i)).get(j));
                        if (roomNode.room instanceof MonsterRoomElite) {
                            roomNode.hasEmeraldKey = true;
                        }
                    }
                  }
            }
        }
    }

    @SpirePatch2(
        clz = RewardItem.class,
        method = "claimReward"
    )
    public static class PreventDisposingEmeraldKeyPatch {
        @SpireInsertPatch(
            loc = 369
        )
        public static SpireReturn<Boolean> Insert() {
            if (AbstractDungeon.player.hasRelic(Baal.RELIC_ID)
                    && Settings.hasEmeraldKey) {
                return SpireReturn.Return(false);
            }
            AbstractDungeon.topLevelEffects.add(new ObtainKeyEffect(ObtainKeyEffect.KeyColor.GREEN));
            return SpireReturn.Return(true);
        }
    }
}
