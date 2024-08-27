package balls.relics;

import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import balls.BallsInitializer;

public class EightBall extends AbstractBallRelic {

    private static final String NAME = EightBall.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    public EightBall() {
        super(RELIC_ID, NAME, RelicTier.UNCOMMON, LandingSound.SOLID);
    }

    @Override
    public void atTurnStartPostDraw() {
        CardGroup discarded = AbstractDungeon.player.discardPile;
        if (discarded.size() > 0) {
            this.flash();
            addToBot(new DiscardToHandAction(discarded.getRandomCard(true)));
        }
    }
}
