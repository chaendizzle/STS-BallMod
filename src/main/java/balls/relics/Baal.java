package balls.relics;

import java.util.ArrayList;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardItem.RewardType;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import balls.BallsInitializer;

public class Baal extends AbstractBallRelic {

    private static final String NAME = "Baal";
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);
    private static final AbstractRelic.RelicTier RELIC_TIER = AbstractRelic.RelicTier.BOSS;
    private static final AbstractRelic.LandingSound SFX = AbstractRelic.LandingSound.HEAVY;

    public Baal() {
        super(RELIC_ID, NAME, RELIC_TIER, SFX);
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster++;

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

    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
    }

    @Override
    public void onVictory() {
        if (Settings.hasEmeraldKey && AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) {
            AbstractDungeon.combatRewardScreen.rewards.add(
                new RewardItem(
                    null,
                    RewardType.EMERALD_KEY));
        }
    }
}
