package balls.helpers;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import balls.BallsInitializer;

public class ComplimentHelper {

    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(BallsInitializer.makeID("ComplimentHelper"));
    private static final String[] TEXT = UI_STRINGS.TEXT;

    private static final UIStrings INSULTS = CardCrawlGame.languagePack.getUIString(BallsInitializer.makeID("Insults"));
    private static final String[] INSULT_TEXT = INSULTS.TEXT;

    private static final UIStrings SPECIAL_UI_STRINGS = CardCrawlGame.languagePack.getUIString(BallsInitializer.makeID("SpecialInteractions"));
    private static final String[] SPECIAL_TEXT = SPECIAL_UI_STRINGS.TEXT;

    public static String getCompliment() {
        return TEXT[(int)(Math.random() * TEXT.length)];
    }

    public static String getInsult() {
        return INSULT_TEXT[(int)(Math.random() * INSULT_TEXT.length)];
    }

    public static String getSphericGuardianPhrase() {
        return SPECIAL_TEXT[0];
    }

    public static String getEyeballPhrase() {
        return SPECIAL_TEXT[1];
    }

    public static String getEnergyBallPhrase() {
        return SPECIAL_TEXT[2];
    }

    public static String getBrucePhrase() {
        int numPhrases = 2;
        int idx = (int)(Math.random() * numPhrases) + 1 + numPhrases; // range: 3-4
        return SPECIAL_TEXT[idx];
    }
}
