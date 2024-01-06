package balls.relics;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import balls.BallsInitializer;

public class Basketball extends AbstractBallRelic {

    private static final String NAME = Basketball.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    private CardType lastCardType = null;

    public Basketball() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.UNCOMMON, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (lastCardType == c.type) {
            this.flash();
            addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 1));
        } else {
            lastCardType = c.type;
        }
    }
}
