package balls.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RubberBouncyBall extends AbstractBallRelic implements ClickableRelic {

    private static final String NAME = RubberBouncyBall.class.getSimpleName();
    public static final String RELIC_ID = balls.BallsInitializer.makeID(NAME);

    private boolean used = false;

    public RubberBouncyBall() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.COMMON, AbstractRelic.LandingSound.SOLID);
    }

    @Override
    public void atTurnStart() {
        used = false;
        this.beginLongPulse();
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
            addToBot(new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player, 7), AttackEffect.SLASH_DIAGONAL));
        }
    }
}
