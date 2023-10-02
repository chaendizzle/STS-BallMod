package balls.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import balls.BallsInitializer;

public class BowlingBall extends AbstractBallRelic implements OnAfterUseCardRelic {

    private static final String NAME = BowlingBall.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    private boolean doubleDamage = false;
    private int startingMonsters;
    private int livingMonsters;

    public BowlingBall() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.UNCOMMON, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        this.startingMonsters = AbstractDungeon.getMonsters().monsters.size();
        this.livingMonsters = this.startingMonsters;
    }

    @Override
    public void onPlayerEndTurn() {
        this.livingMonsters = 0;
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped())
                this.livingMonsters++;
        }
    }

    @Override
    public void onVictory() {
        if (startingMonsters == livingMonsters) {
            this.doubleDamage = true;
            this.beginLongPulse();
        }
    }

    @Override
    public float atDamageModify(float damage, AbstractCard c) {
        if (this.doubleDamage)
            return damage * 2.0F;
        return damage;
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (this.doubleDamage && card.type == AbstractCard.CardType.ATTACK) {
            this.doubleDamage = false;
            this.stopPulse();
        }
    }
}
