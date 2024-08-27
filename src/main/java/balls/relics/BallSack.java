package balls.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;

import balls.BallsInitializer;

public class BallSack extends AbstractBallRelic {
    
    private static final String NAME = BallSack.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    private AbstractRelic commonBall = null;
    private AbstractRelic uncommonBall = null;
    private AbstractRelic rareBall = null;

    private boolean opened = true;

    public BallSack() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.BOSS, AbstractRelic.LandingSound.HEAVY);
    }

    public void onEquip() {
        AbstractBallRelic tmp;
        int idx = 0;

        while (commonBall == null) {
            idx = AbstractDungeon.relicRng.random(BallsInitializer.balls.size());
            tmp = BallsInitializer.balls.get(idx);
            if (tmp.tier == RelicTier.COMMON)
                commonBall = tmp.makeCopy();
        }

        while (uncommonBall == null) {
            idx = AbstractDungeon.relicRng.random(BallsInitializer.balls.size());
            tmp = BallsInitializer.balls.get(idx);
            if (tmp.tier == RelicTier.UNCOMMON)
                uncommonBall = tmp.makeCopy();
        }

        while (rareBall == null) {
            idx = AbstractDungeon.relicRng.random(BallsInitializer.balls.size());
            tmp = BallsInitializer.balls.get(idx);
            if (tmp.tier == RelicTier.RARE)
                rareBall = tmp.makeCopy();
        }

        opened = false;

        // do curse stuff here - Testicular Torsion
    }

    public void update() {
        super.update();
        if (!this.opened && !AbstractDungeon.isScreenUp) {
            AbstractDungeon.combatRewardScreen.open();
            AbstractDungeon.combatRewardScreen.rewards.clear();
            AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(commonBall));
            AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(uncommonBall));
            AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(rareBall));
            AbstractDungeon.combatRewardScreen.positionRewards();
            this.opened = true;
            AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.25F;
        }
    }
}
