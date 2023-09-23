package balls.relics;

import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import balls.BallsInitializer;

public class Snowball extends AbstractBallRelic {

    private final static String NAME = Snowball.class.getSimpleName();
    public final static String RELIC_ID = BallsInitializer.makeID(NAME);

    public Snowball() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.UNCOMMON, AbstractRelic.LandingSound.MAGICAL, CardColor.BLUE);
        this.counter = 0;
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void onVictory() {
        this.counter = 0;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (c.type == CardType.ATTACK) {
            this.counter++;
            if (counter >= 3) {
                this.flash();
                this.counter = 0;
                addToBot(new ChannelAction(new Frost()));
            }
        }
    }
}
