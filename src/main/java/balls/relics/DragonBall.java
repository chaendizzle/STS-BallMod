package balls.relics;

import java.util.ArrayList;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.optionCards.BecomeAlmighty;
import com.megacrit.cardcrawl.cards.optionCards.FameAndFortune;
import com.megacrit.cardcrawl.cards.optionCards.LiveForever;

import balls.BallsInitializer;
import balls.helpers.CombatHelper;
import basemod.BaseMod;
import basemod.interfaces.StartActSubscriber;

public class DragonBall extends AbstractBallRelic implements ClickableRelic, StartActSubscriber {

    private static final String NAME = DragonBall.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    public DragonBall() {
        super(RELIC_ID, NAME, RelicTier.UNCOMMON, LandingSound.FLAT);
        BaseMod.subscribe(this);
    }

    @Override
    public void atBattleStart() {
        if (!this.grayscale) {
            this.beginLongPulse();
        }
    }

    @Override
    public void onVictory() {
        this.stopPulse();
    }

    @Override
    public void onRightClick() {
        if (CombatHelper.isInCombat() && !this.grayscale) {
            this.flash();
            this.stopPulse();
            this.grayscale = true;
            ArrayList<AbstractCard> wishes = new ArrayList<>();
            wishes.add(new BecomeAlmighty());
            wishes.add(new FameAndFortune());
            wishes.add(new LiveForever());
            for (AbstractCard c : wishes)
                c.upgrade();
            addToBot(new ChooseOneAction(wishes));
        }
    }

    @Override
    public void receiveStartAct() {
        this.grayscale = false;
    }
}
