package balls.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;

import javassist.CtBehavior;
import balls.relics.DiscoBall;

public class WingedNavigationPatch {

    @SpirePatch2(
        clz = MapRoomNode.class,
        method = "wingedIsConnectedTo"
    )
    public static class WingedIsConnectedToPatch {
        @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"edge"}
        )
        public static SpireReturn<Boolean> Insert(MapRoomNode node, MapEdge edge) {
            if (node.y == edge.dstY && AbstractDungeon.player.hasRelic(DiscoBall.RELIC_ID) && ((DiscoBall)AbstractDungeon.player.getRelic(DiscoBall.RELIC_ID)).canSkip) {
                ((DiscoBall)AbstractDungeon.player.getRelic(DiscoBall.RELIC_ID)).canSkip = false;
                return SpireReturn.Return(true);
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                return LineFinder.findInOrder(ctBehavior, new Matcher.MethodCallMatcher(ModHelper.class, "isModEnabled"));
            }
        }
    }

    @SpirePatch2(
        clz = DungeonMapScreen.class,
        method = "updateControllerInput"
    )
    public static class FlightMattersPatch {
        @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"flightMatters"}
        )
        public static void Insert(@ByRef boolean[] flightMatters) {
            flightMatters[0] = flightMatters[0] || AbstractDungeon.player.hasRelic(DiscoBall.RELIC_ID);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                return LineFinder.findInOrder(ctBehavior, new Matcher.MethodCallMatcher(MapRoomNode.class, "isConnectedTo"));
            }
        }
    }
}
