package balls.relics;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import balls.BallsInitializer;

public class CheeseBall extends AbstractBallRelic {

    private static final String NAME = CheeseBall.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    public CheeseBall() {
        super(RELIC_ID, NAME, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void onEquip() {
        addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, AbstractDungeon.player.maxHealth / 2));
    }

    @Override
    public int onPlayerHeal(int healAmount) {
        return healAmount + 2;
    }
}
