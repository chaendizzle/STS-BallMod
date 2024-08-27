package balls.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import balls.BallsInitializer;
import basemod.abstracts.CustomCard;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public class TesticularTorsion extends CustomCard {

    private static final String NAME = TesticularTorsion.class.getSimpleName();
    private static final String IMG = NAME + ".png";
    public static final String CARD_ID = BallsInitializer.makeID(NAME);

    private static final CardRarity RARITY = CardRarity.CURSE;
    private static final CardColor COLOR = CardColor.CURSE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.CURSE;

    private static final CardStrings CARD_STRINGS = languagePack.getCardStrings(CARD_ID);

    public TesticularTorsion() {
        super(CARD_ID, CARD_STRINGS.NAME, BallsInitializer.makeImagePath("cards/" + IMG), -2, CARD_STRINGS.DESCRIPTION,
                TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void upgrade() {
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }
}
