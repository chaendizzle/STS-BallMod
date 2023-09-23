package balls.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.Instanceof;
import javassist.expr.MethodCall;

public class DontGenerateNewEventsPatch {

    @SpirePatch2(
        clz = AbstractEvent.class,
        method = SpirePatch.CLASS
    )
    public static class EventFields {
        public static SpireField<Boolean> isShrine = new SpireField<>(() -> false);
        public static SpireField<String> eventKey = new SpireField<>(() -> "");
    }

    @SpirePatch2(
        clz = AbstractDungeon.class,
        method = "getEvent"
    )
    public static class StoreEventKeyInEventPatch {
        @SpireInstrumentPatch
        public static ExprEditor Editor() {
            return new ExprEditor() {
                public void edit(MethodCall m) {
                    try {
                        if (m.getMethodName().equals("getEvent")) {
                            m.replace(""
                              + "{"
                              + " com.megacrit.cardcrawl.events.AbstractEvent event = com.megacrit.cardcrawl.helpers.EventHelper.getEvent(tmpKey);"
                              + " balls.patches.DontGenerateNewEventsPatch.EventFields.eventKey.set(event, tmpKey);"
                              + " $_ = event;"
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
        clz = AbstractDungeon.class,
        method = "getShrine"
    )
    public static class StoreEventKeyInShrinePatch {
        @SpireInstrumentPatch
        public static ExprEditor Editor() {
            return new ExprEditor() {
                public void edit(MethodCall m) {
                    try {
                        if (m.getMethodName().equals("getEvent")) {
                            m.replace(""
                            + "{"
                            + " com.megacrit.cardcrawl.events.AbstractEvent event = com.megacrit.cardcrawl.helpers.EventHelper.getEvent(tmpKey);"
                            + " balls.patches.DontGenerateNewEventsPatch.EventFields.eventKey.set(event, tmpKey);"
                            + " balls.patches.DontGenerateNewEventsPatch.EventFields.isShrine.set(event, Boolean.TRUE);"
                            + " $_ = event;"
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
        clz = AbstractDungeon.class,
        method = "nextRoomTransition",
        paramtypez = { SaveFile.class }
    )
    public static class PreventGeneratingRoomWhenAlreadyGeneratedPatch {
        private static boolean secondTime = false;

        @SpireInstrumentPatch
        public static ExprEditor Editor() {
            return new ExprEditor() {
                public void edit(Instanceof i) {
                    try {
                        if (secondTime) {
                            if (i.getType().getName().equals("com.megacrit.cardcrawl.rooms.EventRoom")) {
                                i.replace("$_ = $_ && (nextRoom.room.event == null);");
                            }
                        } else {
                            secondTime = true;
                        }
                    } catch (CannotCompileException e) {
                        e.printStackTrace();
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
    }

    public static AbstractEvent generateEvent(AbstractRoom room, Random eventRngDuplicate) {
        if (room.event == null)
            return AbstractDungeon.generateEvent(eventRngDuplicate);
        return room.event;
    }

    @SpirePatch2(
        clz = EventRoom.class,
        method = "onPlayerEntry"
    )
    public static class PreventGeneratingEventWhenAlreadyGeneratedPatch {
        @SpireInstrumentPatch
        public static ExprEditor Editor() {
            return new ExprEditor() {
                public void edit(MethodCall m) {
                    try {
                        if (m.getMethodName().equals("generateEvent")) {
                            m.replace("$_ = balls.patches.DontGenerateNewEventsPatch.generateEvent(this, eventRngDuplicate);");
                        }
                    } catch (CannotCompileException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
    }
}
