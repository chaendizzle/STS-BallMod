package balls.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import balls.BallsInitializer;

public class SoccerBall extends AbstractBallRelic {

    private static final String NAME = SoccerBall.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    private int excessEnergy = 0;

    public SoccerBall() {
        super(RELIC_ID, NAME, RelicTier.UNCOMMON, LandingSound.SOLID);
        this.excessEnergy = 0;
    }

    @Override
    public void atBattleStart() {
        this.excessEnergy = 0;
    }

    @Override
    public void atTurnStartPostDraw() {
        if (excessEnergy > 0) {
            this.flash();
            addToBot(new DrawCardAction(AbstractDungeon.player, this.excessEnergy));
        }
        this.excessEnergy = 0;
    }

    @Override
    public void onPlayerEndTurn() {
        this.excessEnergy = EnergyPanel.getCurrentEnergy();
    }
}
