package balls.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerToRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import balls.BallsInitializer;

public class Marble extends AbstractBallRelic implements ClickableRelic {

    private final static String NAME = Marble.class.getSimpleName();
    public final static String RELIC_ID = BallsInitializer.makeID(NAME);

    private boolean used = false;

    public Marble() {
        super(RELIC_ID, NAME, RelicTier.COMMON, LandingSound.CLINK, CardColor.RED);
    }

    @Override
    public void onRightClick() {
        if (!this.used && !AbstractDungeon.getMonsters().areMonstersBasicallyDead() && !AbstractDungeon.getCurrRoom().isBattleOver) {
            this.used = true;
            addToBot(new ApplyPowerToRandomEnemyAction(AbstractDungeon.player, new VulnerablePower(null, 1, false)));
            this.flash();
            this.stopPulse();
        }
    }

    @Override
    public void atBattleStart() {
        this.beginPulse();
        this.used = false;
    }

    @Override
    public void onVictory() {
        this.stopPulse();
        this.used = false;
    }
}
