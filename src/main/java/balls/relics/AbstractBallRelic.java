package balls.relics;

import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;

import balls.BallsInitializer;
import balls.util.TextureLoader;
import basemod.abstracts.CustomRelic;

public abstract class AbstractBallRelic extends CustomRelic {

    protected RelicStrings relicStrings;

    public CardColor cardColor;

    public AbstractBallRelic(String id, String imgName, RelicTier tier, LandingSound sfx) {
        super(
            id,
            TextureLoader.getTexture(BallsInitializer.makeImagePath("relics/" + imgName + ".png")),
            TextureLoader.getTexture(BallsInitializer.makeImagePath("relics/outline/" + imgName + ".png")),
            tier,
            sfx
        );
        cardColor = null;
        relicStrings = CardCrawlGame.languagePack.getRelicStrings(id);
        this.description = relicStrings.DESCRIPTIONS[0];
    }

    public AbstractBallRelic(String id, String imgName, RelicTier tier, LandingSound sfx, CardColor cardColor) {
        this(id, imgName, tier, sfx);
        this.cardColor = cardColor;
    }

    @Override
    public String getUpdatedDescription() {
        if (relicStrings == null) {
            relicStrings = CardCrawlGame.languagePack.getRelicStrings(this.relicId);
        }
        return relicStrings.DESCRIPTIONS[0];
    }
}
