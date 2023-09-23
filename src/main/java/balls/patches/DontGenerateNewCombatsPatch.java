package balls.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;

import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import balls.BallsInitializer;
import balls.helpers.EventHelper;

public class DontGenerateNewCombatsPatch {

    @SpirePatch2(
        clz = MonsterGroup.class,
        method = SpirePatch.CLASS
    )
    public static class MonsterGroupFields {
        public static SpireField<String> encounterKey = new SpireField<>(() -> "");
    }

    @SpirePatch2(
        clz = AbstractRoom.class,
        method = SpirePatch.CLASS
    )
    public static class RoomFields {
        public static SpireField<Boolean> initMonsters = new SpireField<>(() -> false);
    }

    @SpirePatch2(
        clz = AbstractDungeon.class,
        method = "getMonsterForRoomCreation"
    )
    public static class StoreEncounterKeyInMonsterGroupPatch {

        @SpireInstrumentPatch
        public static ExprEditor Editor() {
            return new ExprEditor() {
                public void edit(MethodCall m) {
                    try {
                        if (m.getMethodName().equals("getEncounter")) {
                            m.replace(""
                              + "{"
                              + " com.megacrit.cardcrawl.monsters.MonsterGroup monsters = com.megacrit.cardcrawl.helpers.MonsterHelper.getEncounter((String)monsterList.get(0));"
                              + " balls.patches.DontGenerateNewCombatsPatch.MonsterGroupFields.encounterKey.set(monsters, (String)monsterList.get(0));"
                              + " $_ = monsters;"
                              + "}");
                        }
                    } catch (CannotCompileException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
    }

    @SpirePatch2(
        clz = MonsterRoom.class,
        method = "onPlayerEntry"
    )
    public static class InitMonstersWhenPreGeneratedPatch {
        public static void Postfix(MonsterRoom __instance) {
            BallsInitializer.logger.info("HERE");
            BallsInitializer.logger.info(RoomFields.initMonsters.get(__instance));
            if (RoomFields.initMonsters.get(__instance)) {
                __instance.monsters.init();
                RoomFields.initMonsters.set(__instance, false);
                EventHelper.generateNextEncounter();
            }
        }
    }

    @SpirePatch2(
        clz = MonsterRoomElite.class,
        method = "onPlayerEntry"
    )
    public static class InitElitesWhenPreGeneratedPatch {
        public static void Postfix(MonsterRoomElite __instance) {
            if (RoomFields.initMonsters.get(__instance)) {
                __instance.monsters.init();
                RoomFields.initMonsters.set(__instance, false);
            }
        }
    }
}
