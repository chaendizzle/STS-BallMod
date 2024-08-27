package balls.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;

import balls.BallsInitializer;

public class Kickball extends AbstractBallRelic {

    private static final String NAME = Kickball.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    public Kickball() {
        super(RELIC_ID, NAME, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (info.type == DamageType.NORMAL && damageAmount < 10) {
            this.flash();
            addToBot(new DamageAction(info.owner, info, AttackEffect.BLUNT_HEAVY, true));
        }
        return damageAmount;
    }
}
