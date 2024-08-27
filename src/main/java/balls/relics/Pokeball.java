package balls.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;

import balls.BallsInitializer;

public class Pokeball extends AbstractBallRelic {

    private static final String NAME = Pokeball.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    public Pokeball() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.RARE, AbstractRelic.LandingSound.MAGICAL);
    }

    @Override
    public void onVictory() {
        this.flash();
        switch (AbstractDungeon.relicRng.random(0, 2)) {
            case 0:
                AbstractDungeon.getCurrRoom().addGoldToRewards(30);
                break;
            case 1:
                AbstractDungeon.getCurrRoom().addCardReward(new RewardItem());
                break;
            case 2:
                AbstractDungeon.getCurrRoom().addPotionToRewards(AbstractDungeon.returnRandomPotion());
                break;
        }
    }
}
