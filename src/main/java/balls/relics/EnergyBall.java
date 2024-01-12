package balls.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;

import balls.BallsInitializer;
import balls.helpers.CombatHelper;
import basemod.BaseMod;
import basemod.interfaces.StartActSubscriber;

public class EnergyBall extends AbstractBallRelic implements ClickableRelic, StartActSubscriber {

    private static final String NAME = EnergyBall.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    public EnergyBall() {
        super(RELIC_ID, NAME, RelicTier.RARE, LandingSound.MAGICAL);
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
    public void onRightClick() {
        if (!this.grayscale && CombatHelper.isInCombat()) {
            this.grayscale = true;
            this.flash();
            this.stopPulse();
            addToBot(new GainEnergyAction(1));
        }
    }

    @Override
    public void receiveStartAct() {
        this.grayscale = false;
    }
}
