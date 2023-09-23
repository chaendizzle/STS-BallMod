package balls.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.MarkOfTheBloom;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import balls.BallsInitializer;

public class Fudgeball extends AbstractBallRelic {

    private static final String NAME = Fudgeball.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);
    private static final AbstractRelic.RelicTier RELIC_TIER = AbstractRelic.RelicTier.BOSS;
    private static final AbstractRelic.LandingSound SFX = AbstractRelic.LandingSound.MAGICAL;

    private static final int MAX_HP_GAIN = 20;

    public Fudgeball() {
        super(RELIC_ID, NAME, RELIC_TIER, SFX);
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.maxHealth += MAX_HP_GAIN;
        AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth);
        if (AbstractDungeon.player.hasRelic(MarkOfTheBloom.ID))
            AbstractDungeon.player.getRelic(MarkOfTheBloom.ID).grayscale = true;
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        AbstractDungeon.player.maxHealth += 1;
    }
}
