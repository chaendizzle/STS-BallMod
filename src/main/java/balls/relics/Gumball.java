package balls.relics;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class Gumball extends AbstractBallRelic {

    private static final String NAME = Gumball.class.getSimpleName();
    private static final String RELIC_ID = balls.BallsInitializer.makeID(NAME);

    public Gumball() {
        super(RELIC_ID, NAME, RelicTier.RARE, LandingSound.SOLID);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        AbstractDungeon.player.heal(1);
    }
}
