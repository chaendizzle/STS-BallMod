package balls.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.campfire.CampfireLiftEffect;

import balls.relics.MedicineBall;

public class OnLift {

    @SpirePatch2(
        clz = CampfireLiftEffect.class,
        method = SpirePatch.CONSTRUCTOR
    )
    public static class OnLiftPatch {
        public static void Postfix() {
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                if (relic.relicId.equals(MedicineBall.RELIC_ID)) {
                    AbstractDungeon.player.heal((int)(AbstractDungeon.player.maxHealth * 0.15F));
                }
            }
        }
    }
}
