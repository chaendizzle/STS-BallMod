package balls.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.watcher.OmniscienceAction;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import balls.BallsInitializer;
import balls.helpers.CombatHelper;
import basemod.BaseMod;
import basemod.interfaces.StartActSubscriber;

public class Eyeball extends AbstractBallRelic implements ClickableRelic, StartActSubscriber {

    private static final String NAME = Eyeball.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    public Eyeball() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.RARE, AbstractRelic.LandingSound.FLAT);
        BaseMod.subscribe(this);
    }

    @Override
    public void atBattleStart() {
        if (!this.grayscale) {
            this.beginLongPulse();
        }
    }

    @Override
    public void onVictory() {
        this.stopPulse();
    }

    @Override
    public void receiveStartAct() {
        this.grayscale = false;
    }

    @Override
    public void onRightClick() {
        if (!this.grayscale && CombatHelper.isInCombat()) {
            this.grayscale = true;
            this.flash();
            this.stopPulse();
            addToBot(new OmniscienceAction(2));
        }
    }
}
