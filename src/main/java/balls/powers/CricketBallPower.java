package balls.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import balls.BallsInitializer;
import balls.util.TextureLoader;

public class CricketBallPower extends AbstractPower {
    
    private static final String POWER_PREFIX = "CricketBall";
    public static final String POWER_ID = balls.BallsInitializer.makeID(POWER_PREFIX);

    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public CricketBallPower(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.amount = amount;
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.type = PowerType.BUFF;

        this.region128 = new TextureAtlas.AtlasRegion(
            TextureLoader.getTexture(BallsInitializer.makeImagePath("powers/" + POWER_PREFIX + "_power84.png")),
            0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
            TextureLoader.getTexture(BallsInitializer.makeImagePath("powers/" + POWER_PREFIX + "_power32.png")),
            0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(ID);
        this.description = strings.DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * 2.0F;
        }
        return damage;
    }
}
