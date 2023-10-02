package balls.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.watcher.OmniscienceAction;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import balls.BallsInitializer;
import basemod.BaseMod;
import basemod.interfaces.StartActSubscriber;

public class Eyeball extends AbstractBallRelic implements ClickableRelic, StartActSubscriber {

    private static final String NAME = Eyeball.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    private boolean used = false;

    public Eyeball() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.RARE, AbstractRelic.LandingSound.FLAT);
        BaseMod.subscribe(this);
    }

    @Override
    public void atBattleStart() {
        if (!this.used) {
            this.beginLongPulse();
        }
    }

    @Override
    public void onVictory() {
        this.stopPulse();
    }

    @Override
    public void receiveStartAct() {
        this.used = false;
        this.grayscale = false;
    }

    @Override
    public void onRightClick() {
        if (!this.used) {
            this.used = true;
            this.grayscale = true;
            this.flash();
            this.stopPulse();
            addToBot(new OmniscienceAction(2));
        }
    }
}
