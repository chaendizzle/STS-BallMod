package balls.relics;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class WhiffleBall extends AbstractBallRelic {

    private static final String NAME = WhiffleBall.class.getSimpleName();
    public static final String RELIC_ID = balls.BallsInitializer.makeID(NAME);

    public WhiffleBall() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.UNCOMMON, AbstractRelic.LandingSound.CLINK, CardColor.GREEN);
        this.counter = 0;
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        this.counter++;
        if (this.counter == 4) {
            this.counter = 0;
            this.flash();
            addToBot(new DrawCardAction(1));
            addToBot(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, 1, false));
        }
    }
}
