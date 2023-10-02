package balls.relics;

import balls.BallsInitializer;

public class EightBall extends AbstractBallRelic {

    private static final String NAME = EightBall.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    public EightBall() {
        super(RELIC_ID, NAME, RelicTier.UNCOMMON, LandingSound.SOLID);
    }

    @Override
    public boolean canSpawn() {
        return !BallsInitializer.disablePreviewRelics;
    }
}
