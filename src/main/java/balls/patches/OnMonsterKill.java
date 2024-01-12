package balls.patches;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import balls.relics.Baseball;
import balls.relics.BruceAvocadoLittleLeviathanSouleater;
import javassist.CtBehavior;

public class OnMonsterKill {

    @SpirePatch2(
        clz = AbstractMonster.class,
        method = "damage"
    )
    public static class OnMonsterKillPatch {
        @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"damageAmount"}
        )
        public static void Insert(AbstractMonster __instance, int damageAmount) {
            if (damageAmount > 0) {
                int currentHealth = __instance.currentHealth - damageAmount;
                if (currentHealth == 0) {
                    for (AbstractRelic relic : AbstractDungeon.player.relics) {
                        if (relic.relicId.equals(Baseball.RELIC_ID)) {
                            relic.onTrigger();
                        }
                    }
                } else if (currentHealth < 0) {
                    for (AbstractRelic relic : AbstractDungeon.player.relics) {
                        if (relic.relicId.equals(BruceAvocadoLittleLeviathanSouleater.RELIC_ID)) {
                            relic.counter += Math.abs(currentHealth);
                            relic.flash();
                        }
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Math.class, "min");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
