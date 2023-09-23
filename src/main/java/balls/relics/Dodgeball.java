package balls.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.FossilizedHelix;

import balls.BallsInitializer;

public class Dodgeball extends AbstractBallRelic {

    private static final String NAME = Dodgeball.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);
    private static final AbstractRelic.RelicTier RELIC_TIER = AbstractRelic.RelicTier.COMMON;
    private static final AbstractRelic.LandingSound SFX = AbstractRelic.LandingSound.FLAT;

    private static final int BUFFER_AMOUNT = 1;
    private int bufferAmount;

    private boolean firstTurn = false;
    private boolean secondTurn = false;

    public Dodgeball() {
        super(RELIC_ID, NAME, RELIC_TIER, SFX);
    }

    @Override
    public void atBattleStart() {
        firstTurn = true;
        secondTurn = false;
        this.grayscale = false;
        flash();
        AbstractPlayer player = AbstractDungeon.player;
        BufferPower buffer = new BufferPower(player, BUFFER_AMOUNT);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, buffer, BUFFER_AMOUNT));
    }

    @Override
    public void atTurnStart() {
        AbstractPlayer player = AbstractDungeon.player;
        if (!firstTurn) {
            if (secondTurn) {
                BufferPower bp = (BufferPower)AbstractDungeon.player.getPower(BufferPower.POWER_ID);
                if (bp != null && bp.amount >= bufferAmount) {
                    if (bufferAmount == 1)
                        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(player, player, BufferPower.POWER_ID));
                    else
                        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(player, player, BufferPower.POWER_ID, BUFFER_AMOUNT));
                }
                this.grayscale = true;
                secondTurn = false;
                this.flash();
            }
        } else {
            firstTurn = false;
            secondTurn = true;
            BufferPower bp = (BufferPower)AbstractDungeon.player.getPower(BufferPower.POWER_ID);
            if (bp != null) {
                bufferAmount = bp.amount;
            } else {
                bufferAmount = BUFFER_AMOUNT;
            }
            if (AbstractDungeon.player.hasRelic(FossilizedHelix.ID)) {
                bufferAmount += 1;
            }
        }
    }
}
