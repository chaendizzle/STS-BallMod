package balls.patches;

import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import balls.relics.Baal;

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
}
