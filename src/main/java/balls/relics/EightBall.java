package balls.relics;

import balls.BallsInitializer;

public class EightBall extends AbstractBallRelic {

    private final static String NAME = EightBall.class.getSimpleName();
    public final static String RELIC_ID = BallsInitializer.makeID(NAME);

    public EightBall() {
        super(RELIC_ID, NAME, RelicTier.COMMON, LandingSound.CLINK);
    }
}
