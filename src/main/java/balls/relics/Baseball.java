package balls.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import balls.BallsInitializer;

public class Baseball extends AbstractBallRelic {

    private static final String NAME = Baseball.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    public Baseball() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.RARE, AbstractRelic.LandingSound.FLAT);
        this.counter = 0;
    }

    @Override
    public void atBattleStart() {
        if (this.counter > 0)
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, this.counter), this.counter));
    }

    @Override
    public void onTrigger() {
        this.counter++;
        this.updateDescription(AbstractDungeon.player.chosenClass);
        this.flash();
    }

    @Override
    public String getUpdatedDescription() {
        if (relicStrings == null)
            relicStrings = CardCrawlGame.languagePack.getRelicStrings(relicId);
        int val = this.counter;
        if (val < 0)
            val = 0;
        return relicStrings.DESCRIPTIONS[0] + val + relicStrings.DESCRIPTIONS[1];
    }
}
