package balls.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import balls.BallsInitializer;

public class BeachBall extends AbstractBallRelic {

    private static final String NAME = BeachBall.class.getSimpleName();
    public final static String RELIC_ID = BallsInitializer.makeID(NAME);

    private boolean powerPlayedThisTurn = false;
    private boolean removed = false;

    public BeachBall() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.UNCOMMON, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 2), 2));
        this.flash();
        this.beginPulse();
        this.powerPlayedThisTurn = false;
        this.removed = false;
    }

    @Override
    public void atTurnStart() {
        this.powerPlayedThisTurn = false;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (c.type == AbstractCard.CardType.POWER) {
            this.powerPlayedThisTurn = true;
        }
    }

    @Override
    public void onPlayerEndTurn() {
        if (!this.powerPlayedThisTurn && !this.removed) {
            this.stopPulse();
            this.removed = true;
            addToBot(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, DexterityPower.POWER_ID, 2));
        }
    }
}
