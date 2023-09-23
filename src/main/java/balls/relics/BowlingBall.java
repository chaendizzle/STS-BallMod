package balls.relics;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import balls.BallsInitializer;

public class BowlingBall extends AbstractBallRelic {

    private final static String NAME = BowlingBall.class.getSimpleName();
    public final static String RELIC_ID = BallsInitializer.makeID(NAME);

    private boolean gainEnergy = false;

    public BowlingBall() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.UNCOMMON, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        if (gainEnergy) {
            this.gainEnergy = false;
            this.flash();
            addToBot(new GainEnergyAction(1));
        }
    }

    @Override
    public void onVictory() {
        if (AbstractDungeon.getMonsters().monsters.size() == 1) {
            this.gainEnergy = true;
            this.flash();
        }
    }

    @Override
    public void onTrigger() {
        this.gainEnergy = true;
        this.flash();
    }
}
