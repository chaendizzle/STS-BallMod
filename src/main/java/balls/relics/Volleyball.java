package balls.relics;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Volleyball extends AbstractBallRelic {

    private static final String NAME = Volleyball.class.getSimpleName();
    public static final String RELIC_ID = balls.BallsInitializer.makeID(NAME);

    private CardType lastType = null;

    public Volleyball() {
        super(RELIC_ID, NAME, RelicTier.UNCOMMON, LandingSound.FLAT);
        lastType = null;
    }

    @Override
    public void atBattleStart() {
        lastType = null;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if ((lastType == CardType.ATTACK && c.type == CardType.SKILL)
            || (lastType == CardType.SKILL && c.type == CardType.ATTACK)) {
                this.flash();
                addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 2));
        }
        lastType = c.type;
    }
}
