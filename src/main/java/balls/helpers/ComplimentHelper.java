package balls.helpers;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import balls.BallsInitializer;

public class ComplimentHelper {

    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(BallsInitializer.makeID("ComplimentHelper"));
    private static final String[] TEXT = UI_STRINGS.TEXT;

    public static String getCompliment() {
        return TEXT[(int)(Math.random() * TEXT.length)];
    }

    public static String getMysteriousSpherePhrase() {
        UIStrings specialOccasionStrings = CardCrawlGame.languagePack.getUIString(BallsInitializer.makeID("SpecialInteractions"));
        String[] text = specialOccasionStrings.TEXT;
        return text[0];
    }
}
