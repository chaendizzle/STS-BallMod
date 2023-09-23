package balls.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import balls.BallsInitializer;

public class WreckingBall extends AbstractBallRelic {

    private final static String NAME = WreckingBall.class.getSimpleName();
    public final static String RELIC_ID = BallsInitializer.makeID(NAME);

    public WreckingBall() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.UNCOMMON, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        this.flash();
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            int damage = AbstractDungeon.monsterRng.random(5, 10);
            addToBot(new DamageAction(monster, new DamageInfo(null, damage, DamageInfo.DamageType.NORMAL), AttackEffect.BLUNT_HEAVY));
        }
    }
}
