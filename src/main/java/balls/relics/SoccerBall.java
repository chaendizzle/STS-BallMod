package balls.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnUsePotionRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import balls.BallsInitializer;

public class SoccerBall extends AbstractBallRelic implements OnAfterUseCardRelic, BetterOnUsePotionRelic {

    private static final String NAME = SoccerBall.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    private int excessEnergy = 0;

    public SoccerBall() {
        super(RELIC_ID, NAME, RelicTier.UNCOMMON, LandingSound.SOLID);
        this.excessEnergy = 0;
    }

    @Override
    public void atBattleStart() {
        this.excessEnergy = 0;
    }

    @Override
    public void atTurnStartPostDraw() {
        if (excessEnergy > 0) {
            addToBot(new DrawCardAction(AbstractDungeon.player, excessEnergy));
        }
        this.excessEnergy = AbstractDungeon.player.energy.energy;
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        this.excessEnergy = AbstractDungeon.player.energy.energy;
    }

    @Override
    public void betterOnUsePotion(AbstractPotion potion) {
        this.excessEnergy = AbstractDungeon.player.energy.energy;
    }
}
