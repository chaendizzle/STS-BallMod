package balls.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.MarkOfTheBloom;

import balls.relics.Fudgeball;

public class NegateMarkOfTheBloom {

    @SpirePatch2(
        clz = MarkOfTheBloom.class,
        method = "onPlayerHeal"
    )
    public static class NegateMarkOfTheBloomPatch {
        public static SpireReturn<Integer> Prefix(MarkOfTheBloom __instance, int healAmount) {
            if (AbstractDungeon.player.hasRelic(Fudgeball.RELIC_ID)) {
                AbstractDungeon.player.getRelic(Fudgeball.RELIC_ID).flash();
                __instance.grayscale = true;
                return SpireReturn.Return(healAmount);
            }
            return SpireReturn.Continue();
        }
    }
}
