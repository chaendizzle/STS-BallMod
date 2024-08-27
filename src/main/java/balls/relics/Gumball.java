package balls.relics;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class Gumball extends AbstractBallRelic {

    private static final String NAME = Gumball.class.getSimpleName();
    private static final String RELIC_ID = balls.BallsInitializer.makeID(NAME);

    public Gumball() {
        super(RELIC_ID, NAME, RelicTier.RARE, LandingSound.SOLID);
        this.counter = 0;
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        this.counter = 0;
    }

    @Override
    public void onVictory() {
        this.counter = 0;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction useCardAction) {
        if (card.type != AbstractCard.CardType.ATTACK)
            return;

        this.counter++;
        if (this.counter >= 3) {
            this.counter = 0;
            this.flash();
            AbstractDungeon.player.heal(1);
        }
    }
}
