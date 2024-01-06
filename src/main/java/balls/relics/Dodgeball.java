package balls.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import balls.BallsInitializer;
import balls.powers.DodgeballPower;

public class Dodgeball extends AbstractBallRelic {

    private static final String NAME = Dodgeball.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);
    private static final AbstractRelic.RelicTier RELIC_TIER = AbstractRelic.RelicTier.COMMON;
    private static final AbstractRelic.LandingSound SFX = AbstractRelic.LandingSound.FLAT;

    public Dodgeball() {
        super(RELIC_ID, NAME, RELIC_TIER, SFX);
    }

    @Override
    public void atBattleStart() {
        flash();
        AbstractPlayer player = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new DodgeballPower(1), 1));
    }
}
