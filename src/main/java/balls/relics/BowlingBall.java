package balls.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import balls.BallsInitializer;
import balls.powers.BowlingBallPower;

public class BowlingBall extends AbstractBallRelic implements OnAfterUseCardRelic {

    private static final String NAME = BowlingBall.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    public boolean doubleDamage = false;
    private int startingMonsters;
    private int livingMonsters;

    public BowlingBall() {
        super(RELIC_ID, NAME, AbstractRelic.RelicTier.UNCOMMON, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        this.startingMonsters = AbstractDungeon.getMonsters().monsters.size();
        this.livingMonsters = this.startingMonsters;
        if (this.doubleDamage) {
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BowlingBallPower(AbstractDungeon.player, 1), 1));
            this.doubleDamage = false;
        }
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
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (this.pulse && card.type == AbstractCard.CardType.ATTACK) {
            this.stopPulse();
        }
    }
}
