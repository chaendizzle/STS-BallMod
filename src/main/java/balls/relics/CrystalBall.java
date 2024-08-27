package balls.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import balls.BallsInitializer;
import balls.helpers.CombatHelper;
import basemod.BaseMod;
import basemod.interfaces.StartActSubscriber;

public class CrystalBall extends AbstractBallRelic implements ClickableRelic, StartActSubscriber {

    private static final String NAME = CrystalBall.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    public CrystalBall() {
        super(RELIC_ID, NAME, RelicTier.UNCOMMON, LandingSound.CLINK);
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
            this.flash();
            this.stopPulse();
            this.grayscale = true;
            int cardsRemaining = AbstractDungeon.player.drawPile.size();
            if (cardsRemaining > 0) {
                addToBot(new ScryAction(cardsRemaining));
            }
        }
    }
}
