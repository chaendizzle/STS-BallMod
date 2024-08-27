package balls.relics;

import java.util.ArrayList;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardItem.RewardType;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import balls.BallsInitializer;

public class PachinkoBall extends AbstractBallRelic implements ClickableRelic {

    private static final String NAME = PachinkoBall.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);
    private static final AbstractRelic.RelicTier RELIC_TIER = AbstractRelic.RelicTier.UNCOMMON;
    private static final AbstractRelic.LandingSound SFX = AbstractRelic.LandingSound.CLINK;

    public PachinkoBall() {
        super(RELIC_ID, NAME, RELIC_TIER, SFX);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        this.stopPulse();
        this.grayscale = false;
    }

    @Override
    public void onVictory() {
        this.beginLongPulse();
    }

    @Override
    public void onRightClick() {
        if (grayscale)
            return;

        if (AbstractDungeon.getCurrRoom().isBattleOver) {
            this.flash();
            ArrayList<RewardItem> rewards = new ArrayList<RewardItem>(AbstractDungeon.getCurrRoom().rewards);
            if (rewards.size() > 0 && AbstractDungeon.miscRng.randomBoolean()) {
                for (RewardItem item : rewards) {
                    if (item.type == RewardType.GOLD || item.type == RewardType.STOLEN_GOLD) {
                        item.incrementGold(item.goldAmt + item.bonusGold);
                    }
                }
            } else {
                for (RewardItem item : rewards) {
                    if (item.type == RewardType.GOLD || item.type == RewardType.STOLEN_GOLD) {
                        item.incrementGold((item.goldAmt + item.bonusGold) * -1); // doing something wacky because it won't change gold value unless through the API
                    }
                }
                this.grayscale = true;
                this.usedUp = true;
                this.stopPulse();
            }
        }
    }
}
