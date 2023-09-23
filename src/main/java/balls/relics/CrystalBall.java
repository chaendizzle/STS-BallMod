package balls.relics;

import balls.BallsInitializer;

public class CrystalBall extends AbstractBallRelic {

    private static final String NAME = CrystalBall.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    public CrystalBall() {
        super(RELIC_ID, NAME, RelicTier.COMMON, LandingSound.CLINK);
    }
}
