package balls.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RubberBouncyBall extends AbstractBallRelic implements ClickableRelic {

    private final static String NAME = RubberBouncyBall.class.getSimpleName();
    public final static String RELIC_ID = balls.BallsInitializer.makeID(NAME);

    private boolean used = false;

    public RubberBouncyBall() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.UNCOMMON, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void atTurnStart() {
        used = false;
        this.beginPulse();
    }

    @Override
    public void onVictory() {
        this.stopPulse();
    }

    @Override
    public void onRightClick() {
        if (!this.used) {
            this.used = true;
            this.flash();
            this.stopPulse();
            addToBot(new DamageRandomEnemyAction(new DamageInfo(null, 2), AttackEffect.SLASH_DIAGONAL));
        }
    }
}
