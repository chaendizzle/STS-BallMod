package balls.relics;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import balls.BallsInitializer;

public class Football extends AbstractBallRelic {

    private static final String NAME = "PacePicante";
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);
    private static final AbstractRelic.RelicTier RELIC_TIER = AbstractRelic.RelicTier.BOSS;
    private static final AbstractRelic.LandingSound SFX = AbstractRelic.LandingSound.HEAVY;

    private Thread countdown;

    private static int COUNTER_MAX = 60;

    public Football() {
        super(RELIC_ID, NAME, RELIC_TIER, SFX);
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster++;
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
    }

    @Override
    public void atTurnStartPostDraw() {
        // start a thread that will remove 1 from the counter every second
        this.counter = COUNTER_MAX;
        if (countdown != null && !countdown.isInterrupted())
                countdown.interrupt();
        countdown = new Thread(() -> {
            int currentTurn = GameActionManager.turn;
            try {
                do {
                    Thread.sleep(1000);

                    if ((!AbstractDungeon.isScreenUp ||
                            (AbstractDungeon.isScreenUp &&
                                AbstractDungeon.screen !=
                                    AbstractDungeon.CurrentScreen.SETTINGS)
                        && currentTurn == GameActionManager.turn)) {
                        this.counter--;
                        if (this.counter <= 0) {
                            if (AbstractDungeon.isScreenUp)
                                AbstractDungeon.closeCurrentScreen();
                            AbstractDungeon.actionManager.addToBottom(new PressEndTurnButtonAction());
                            break;
                        }
                    }
                } while (currentTurn == GameActionManager.turn);
            } catch (InterruptedException e) {
                // do nothing, we don't want to disturb the gamer
            }
        });
        countdown.start();
    }

    @Override
    public void onPlayerEndTurn() {
        if (countdown != null)
            countdown.interrupt();
        this.counter = COUNTER_MAX;
    }

    @Override
    public void onVictory() {
        if (countdown != null)
            countdown.interrupt();
        this.counter = COUNTER_MAX;
    }
}
