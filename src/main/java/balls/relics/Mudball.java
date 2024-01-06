package balls.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
// import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
// import com.megacrit.cardcrawl.localization.PowerStrings;

import balls.powers.TakeDamageNextTurnPower;

public class Mudball extends AbstractBallRelic {

    private static final String NAME = Mudball.class.getSimpleName();
    public static final String RELIC_ID = balls.BallsInitializer.makeID(NAME);

    // private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(RELIC_ID);

    public Mudball() {
        super(RELIC_ID, NAME, RelicTier.UNCOMMON, LandingSound.SOLID);
        this.counter = 0;

        // this.tips.add(new PowerTip("Creator", POWER_STRINGS.DESCRIPTIONS[1]));
        this.tips.add(new PowerTip("Creator", "Credit to #gpgames-food for coming up with the effect for this relic."));
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void onVictory() {
        this.counter = 0;
    }

    @Override
    public void atTurnStartPostDraw() {
        if (this.counter > 0) {
            addToBot(new GainBlockAction(AbstractDungeon.player, this.counter));
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        this.counter++;
        return damageAmount;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (this.counter > 0) {
            this.counter--;
            if (this.counter < 0) {
                this.counter = 0;
            }
            addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new TakeDamageNextTurnPower(target, 1), 1));
        }
    }
}
