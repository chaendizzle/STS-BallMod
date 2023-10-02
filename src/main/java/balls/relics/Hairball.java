package balls.relics;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
// import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
// import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EntanglePower;

public class Hairball extends AbstractBallRelic {

    private static final String NAME = Hairball.class.getSimpleName();
    public static final String RELIC_ID = balls.BallsInitializer.makeID(NAME);

    // private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(RELIC_ID);

    public Hairball() {
        super(RELIC_ID, NAME, RelicTier.BOSS, LandingSound.FLAT);
        this.counter = 0;

        // this.tips.add(new PowerTip("Creator", POWER_STRINGS.DESCRIPTIONS[1]));
        this.tips.add(new PowerTip("Creator", "Credit to #gpgames-food for coming up with the effect for this relic."));
    }

    @Override
    public void atTurnStartPostDraw() {
        if (AbstractDungeon.relicRng.randomBoolean()) {
            this.flash();
            this.counter++;
            AbstractMonster monster = AbstractDungeon.getRandomMonster();
            addToBot(new ApplyPowerAction(monster, AbstractDungeon.player, new StunMonsterPower(monster)));
            if (this.counter == 3) {
                this.counter = 0;
                addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EntanglePower(AbstractDungeon.player)));
                addToBot(new LoseEnergyAction(1));
            }
        }
    }
}
