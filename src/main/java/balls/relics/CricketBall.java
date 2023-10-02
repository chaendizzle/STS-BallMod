package balls.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
// import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
// import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.FreeAttackPower;

public class CricketBall extends AbstractBallRelic implements OnAfterUseCardRelic {

    private static final String NAME = CricketBall.class.getSimpleName();
    public static final String RELIC_ID = balls.BallsInitializer.makeID(NAME);

    // private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(RELIC_ID);

    private boolean firstAttack;

    public CricketBall() {
        super(RELIC_ID, NAME, RelicTier.RARE, LandingSound.SOLID);
        // TODO: localizations broken for some god forsaken reason
        // this.tips.add(new PowerTip("Creator", POWER_STRINGS.DESCRIPTIONS[1]));
        this.tips.add(new PowerTip("Creator", "Credit to #gBento #gBaker #gPraymordis for coming up with the effect for this relic."));
    }

    @Override
    public void atBattleStart() {
        this.firstAttack = true;
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FreeAttackPower(AbstractDungeon.player, 1), 1));
    }

    @Override
    public float atDamageModify(float damage, AbstractCard c) {
        if (this.firstAttack)
            return damage * 2.0F;
        return damage;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (this.firstAttack && c.type == AbstractCard.CardType.ATTACK)
            this.firstAttack = false;
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (this.firstAttack && card.type == AbstractCard.CardType.ATTACK) {
            this.firstAttack = false;
            this.stopPulse();
        }
    }
}
