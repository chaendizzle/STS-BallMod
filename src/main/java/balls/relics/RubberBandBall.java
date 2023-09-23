package balls.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import balls.BallsInitializer;

public class RubberBandBall extends AbstractBallRelic implements ClickableRelic {

    private final static String NAME = RubberBandBall.class.getSimpleName();
    public final static String RELIC_ID = BallsInitializer.makeID(NAME);

    public RubberBandBall() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.RARE, AbstractRelic.LandingSound.SOLID);
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        if (!this.usedUp) {
            this.counter++;
        }
    }

    @Override
    public void onRightClick() {
        addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, 5 * this.counter, DamageType.NORMAL, AttackEffect.SLASH_DIAGONAL));
        this.flash();
        this.usedUp();
    }
}
