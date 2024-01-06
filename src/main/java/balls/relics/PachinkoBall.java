package balls.relics;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import balls.BallsInitializer;

public class PachinkoBall extends AbstractBallRelic {

    private static final String NAME = PachinkoBall.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);
    private static final AbstractRelic.RelicTier RELIC_TIER = AbstractRelic.RelicTier.SHOP;
    private static final AbstractRelic.LandingSound SFX = AbstractRelic.LandingSound.CLINK;

    private Thread countdown;

    private static int COUNTER_MAX = 15;
    private static int GOLD_GAIN = 5;

    public PachinkoBall() {
        super(RELIC_ID, NAME, RELIC_TIER, SFX);
    }

    @Override
    public void atTurnStartPostDraw() {
        // start a thread that will remove 1 from the counter every second
        this.counter = COUNTER_MAX;
        int currentTurn = GameActionManager.turn;
        if (currentTurn < 6) { // for first 5 turns only
            this.grayscale = false;
            if (countdown != null && !countdown.isInterrupted())
                countdown.interrupt();
            countdown = new Thread(() -> {
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
                                this.grayscale = true;
                                this.counter = -1;
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
    }

    private void checkCounter() {
        if (this.counter > 0) {
            this.flash();
            AbstractDungeon.player.gainGold(GOLD_GAIN);
        }
        if (GameActionManager.turn >= 5) {
            this.grayscale = true;
        }
    }

    @Override
    public void onPlayerEndTurn() {
        if (countdown != null) {
            countdown.interrupt();
            checkCounter();
        }
        this.counter = COUNTER_MAX;
    }

    @Override
    public void onVictory() {
        if (countdown != null) {
            countdown.interrupt();
            checkCounter();
        }
        this.counter = -1;
        this.grayscale = false;
    }
}
