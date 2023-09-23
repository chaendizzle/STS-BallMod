package balls.patches;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import balls.relics.BowlingBall;
import javassist.CtBehavior;

public class SimultaneousExecution {

    private static int actionMonsterCount;

    @SpirePatch2(
        clz = DamageAllEnemiesAction.class,
        method = SpirePatch.CONSTRUCTOR
    )
    public static class ResetActionMonsterCountPatch {
        public static void Postfix() {
            actionMonsterCount = 0;
        }
    }

    @SpirePatch2(
        clz = DamageAllEnemiesAction.class,
        method = "update"
    )
    public static class IncrementAndTriggerPatch {
        @SpireInsertPatch(
            locator = IncLocator.class
        )
        public static void IncInsert() {
            actionMonsterCount++;
        }

        @SpireInsertPatch(
            locator = TrigLocator.class
        )
        public static void TrigInsert() {
            if (actionMonsterCount == AbstractDungeon.getMonsters().monsters.size()) {
                for (AbstractRelic relic : AbstractDungeon.player.relics) {
                    if (relic.relicId.equals(BowlingBall.RELIC_ID)) {
                        relic.onTrigger();
                    }
                }
            }
        }

        private static class IncLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "damage");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }

        private static class TrigLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(GameActionManager.class, "clearPostCombatActions");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
