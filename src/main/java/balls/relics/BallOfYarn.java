package balls.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import balls.BallsInitializer;

public class BallOfYarn extends AbstractBallRelic {

    private final static String NAME = BallOfYarn.class.getSimpleName();
    public final static String RELIC_ID = BallsInitializer.makeID(NAME);
    private final static AbstractRelic.RelicTier RELIC_TIER = AbstractRelic.RelicTier.RARE;
    private final static AbstractRelic.LandingSound SFX = AbstractRelic.LandingSound.MAGICAL;

    private static final int LIVES = 3;

    public BallOfYarn() {
        super(RELIC_ID, NAME, RELIC_TIER, SFX);
        this.counter = LIVES;
    }

    @Override
    public void onEquip() {
        this.counter = LIVES;
    }

    @Override
    public int onLoseHpLast(int damageAmount) {
        if (AbstractDungeon.player.currentHealth - damageAmount <= 0
            && this.counter > 0) {
            this.flash();
            this.counter--;
            if (this.counter <= 0)
                this.grayscale = true;
            return 0;
        } else {
            return damageAmount;
        }
    }
}
