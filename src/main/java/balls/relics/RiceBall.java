package balls.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import balls.BallsInitializer;

public class RiceBall extends AbstractBallRelic {

    private static final String NAME = RiceBall.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    public RiceBall() {
        super(RELIC_ID, NAME, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        int hpToRecover = (int)((AbstractDungeon.player.maxHealth - AbstractDungeon.player.currentHealth) / 2.0F);
        if (hpToRecover > 0) {
            this.flash();
            AbstractDungeon.player.heal(hpToRecover);
        }
    }
}
