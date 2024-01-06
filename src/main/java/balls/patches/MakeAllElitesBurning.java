package balls.patches;

import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import balls.relics.Baal;
import javassist.CtBehavior;

public class MakeAllElitesBurning {

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
        clz = TheEnding.class,
        method = "generateSpecialMap"
    )
    public static class BurningSpearAndShieldPatch {
        @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"enemyNode"}
        )
        public static void Insert(MapRoomNode enemyNode) {
            if (AbstractDungeon.player.hasRelic(Baal.RELIC_ID)) {
                enemyNode.hasEmeraldKey = true;
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(TheEnding.class, "connectNode");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }
}
