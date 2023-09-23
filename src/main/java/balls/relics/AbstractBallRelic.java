package balls.relics;

import basemod.abstracts.CustomRelic;

public abstract class AbstractBallRelic extends CustomRelic {

    public AbstractBallRelic(String id, String imgName, RelicTier tier, LandingSound sfx) {
        super(id, imgName, tier, sfx);
    }
    
}
