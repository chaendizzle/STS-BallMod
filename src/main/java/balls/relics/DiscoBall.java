package balls.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnSkipCardRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;

import balls.BallsInitializer;

public class DiscoBall extends AbstractBallRelic implements OnSkipCardRelic {

    private final static String NAME = DiscoBall.class.getSimpleName();
    public final static String RELIC_ID = BallsInitializer.makeID(NAME);

    public boolean canSkip;

    public DiscoBall() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.UNCOMMON, AbstractRelic.LandingSound.CLINK);
        this.canSkip = false;
    }

    @Override
    public void onSkipCard(RewardItem item) {
        this.canSkip = true;
        AbstractDungeon.getCurrRoom().rewards.remove(item);
    }

    @Override
    public void onSkipSingingBowl(RewardItem item) {}
}
