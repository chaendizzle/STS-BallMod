package balls.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import balls.relics.Pinball;
import balls.relics.PingPongBall;

public class ModifyDamageToMonsterPatch {
    
    @SpirePatch2(
        clz = AbstractMonster.class,
        method = "damage"
    )
    public static class ModifyDamagePatch {
        public static void Prefix(AbstractMonster __instance, DamageInfo info) {
            if (info.owner != null && info.type == DamageType.NORMAL && info.output > 0) {
                for (AbstractRelic relic : AbstractDungeon.player.relics) {
                    if (relic.relicId.equals(Pinball.RELIC_ID) || relic.relicId.equals(PingPongBall.RELIC_ID)) {
                        info.output += relic.counter;
                    }
                }

                if (__instance.hasPower(IntangiblePlayerPower.POWER_ID) || __instance.hasPower(IntangiblePower.POWER_ID))
                    info.output = 1;
            }
        }
    }
}
