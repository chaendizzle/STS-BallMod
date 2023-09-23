package balls.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.stances.CalmStance;
import com.megacrit.cardcrawl.stances.WrathStance;

public class WaterPoloBall extends AbstractBallRelic implements ClickableRelic {

    private final static String NAME = WaterPoloBall.class.getSimpleName();
    public final static String RELIC_ID = balls.BallsInitializer.makeID(NAME);

    private boolean used = false;

    public WaterPoloBall() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.UNCOMMON, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        used = false;
        this.beginPulse();
    }

    @Override
    public void onVictory() {
        this.stopPulse();
    }

    @Override
    public void onRightClick() {
        if (!used) {
            if (AbstractDungeon.player.stance.ID.equals(CalmStance.STANCE_ID)) {
                addToBot(new ChangeStanceAction(WrathStance.STANCE_ID));
            } else {
                addToBot(new ChangeStanceAction(CalmStance.STANCE_ID));
            }
            this.used = true;
            this.flash();
            this.stopPulse();
        }
    }

}
