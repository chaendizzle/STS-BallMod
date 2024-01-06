package balls.relics;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import balls.BallsInitializer;

public class Handball extends AbstractBallRelic {

    private static final String NAME = Handball.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    public Handball() {
        super(RELIC_ID, NAME, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        AbstractMonster randomMonster = AbstractDungeon.getMonsters().getRandomMonster();
        if (randomMonster != null && info.type == DamageType.NORMAL)
            randomMonster.damage(new DamageInfo(null, 2, DamageType.NORMAL));
    }
}
