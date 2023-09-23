package balls.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import balls.BallsInitializer;

public class RubberBandBall extends AbstractBallRelic implements ClickableRelic {

    private final static String NAME = RubberBandBall.class.getSimpleName();
    public final static String RELIC_ID = BallsInitializer.makeID(NAME);

    public RubberBandBall() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.RARE, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        if (!this.usedUp) {
            this.counter = 0;
        }
    }

    @Override
    public void atTurnStart() {
        if (!this.usedUp) {
            this.counter++;
        }
    }

    @Override
    public void onRightClick() {
        addToBot(new DamageAllEnemiesAction(null, new int[] {this.counter * 5}, DamageType.NORMAL, AttackEffect.SLASH_HORIZONTAL));
        this.flash();
        this.usedUp();
    }
}
