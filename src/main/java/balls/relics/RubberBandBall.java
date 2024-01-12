package balls.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import balls.BallsInitializer;
import balls.helpers.CombatHelper;

public class RubberBandBall extends AbstractBallRelic implements ClickableRelic {

    private static final String NAME = RubberBandBall.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

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
        if (!this.grayscale && CombatHelper.isInCombat() && this.counter > 0) {
            addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, 5 * this.counter, DamageType.NORMAL, AttackEffect.SLASH_DIAGONAL));
            grayscale = true;
            this.flash();
            this.usedUp();
        }
    }
}
