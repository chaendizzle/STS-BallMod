package balls.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;

import balls.BallsInitializer;

public class Pokeball extends AbstractBallRelic {

    private final static String NAME = Pokeball.class.getSimpleName();
    public final static String RELIC_ID = BallsInitializer.makeID(NAME);

    public Pokeball() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.COMMON, AbstractRelic.LandingSound.MAGICAL);
        this.counter = 0;
    }

    @Override
    public void onVictory() {
        if (this.counter >= 1) {
            this.counter = 0;
            this.flash();
            switch(AbstractDungeon.relicRng.random(0, 2)) {
                case 0:
                    AbstractDungeon.getCurrRoom().addGoldToRewards(25);
                    break;
                case 1:
                    AbstractDungeon.getCurrRoom().addCardReward(new RewardItem());
                    break;
                case 2:
                    AbstractDungeon.getCurrRoom().addPotionToRewards(AbstractDungeon.returnRandomPotion());
                    break;
            }
        } else {
            this.counter++;
        }
    }
}
