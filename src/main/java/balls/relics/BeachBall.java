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
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    private boolean powerPlayedThisTurn = false;
    private boolean removed = false;

    public BeachBall() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.COMMON, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 3), 3));
        this.flash();
        this.beginLongPulse();
        this.powerPlayedThisTurn = false;
        this.removed = false;
        this.grayscale = false;
    }

    @Override
    public void atTurnStart() {
        this.powerPlayedThisTurn = false;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (c.type == AbstractCard.CardType.POWER) {
            this.powerPlayedThisTurn = true;
            this.flash();
        }
    }

    @Override
    public void onPlayerEndTurn() {
        if (!this.powerPlayedThisTurn && !this.removed) {
            this.flash();
            this.grayscale = true;
            this.stopPulse();
            this.removed = true;
            addToBot(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, DexterityPower.POWER_ID, 3));
        }
    }
}
