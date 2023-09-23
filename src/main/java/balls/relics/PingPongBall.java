package balls.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import balls.BallsInitializer;

public class PingPongBall extends AbstractBallRelic {

    private final static String NAME = PingPongBall.class.getSimpleName();
    public final static String RELIC_ID = BallsInitializer.makeID(NAME);

    private AbstractMonster lastMonster = null;

    public PingPongBall() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.UNCOMMON, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (lastMonster == null || m == null || lastMonster != m) {
            this.counter = 0;
        } else if (lastMonster == m && m != null) {
            this.counter += 2;
            this.flash();
        }
        lastMonster = m;
    }

    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        if (info.type == DamageType.NORMAL) {
            return damageAmount + this.counter;
        }
        return damageAmount;
    }
}
