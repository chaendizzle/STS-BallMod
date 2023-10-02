package balls.relics;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import balls.BallsInitializer;

public class PaddleBall extends AbstractBallRelic {

    private static final String NAME = PaddleBall.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    private boolean firstAttack = true;

    public PaddleBall() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.COMMON, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        this.firstAttack = true;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (firstAttack && damageAmount > 0) {
            this.firstAttack = false;
            this.flash();
            addToBot(new DamageAction(AbstractDungeon.getMonsters().getRandomMonster(), info));
            return 0;
        }
        return damageAmount;
    }
}
