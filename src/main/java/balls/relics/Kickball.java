package balls.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import balls.BallsInitializer;

public class Kickball extends AbstractBallRelic {

    private final static String NAME = Kickball.class.getSimpleName();
    public final static String RELIC_ID = BallsInitializer.makeID(NAME);

    public Kickball() {
        super(RELIC_ID, NAME, RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageType.NORMAL && damageAmount < 10) {
            this.flash();
            addToBot(new DamageAction(info.owner, new DamageInfo(AbstractDungeon.player, 10), AttackEffect.BLUNT_HEAVY, true));
        }
        return damageAmount;
    }
}
