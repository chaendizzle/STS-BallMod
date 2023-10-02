package balls.helpers;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import balls.BallsInitializer;

public class ComplimentHelper {

    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(BallsInitializer.makeID("ComplimentHelper"));
    private static final String[] TEXT = UI_STRINGS.TEXT;

    private static final UIStrings SPECIAL_UI_STRINGS = CardCrawlGame.languagePack.getUIString(BallsInitializer.makeID("SpecialInteractions"));
    private static final String[] SPECIAL_TEXT = SPECIAL_UI_STRINGS.TEXT;

    public static String getCompliment() {
        return TEXT[(int)(Math.random() * TEXT.length)];
    }

    public static String getMysteriousSpherePhrase() {
        return SPECIAL_TEXT[0];
    }

    public static String getEyeballPhrase() {
        return SPECIAL_TEXT[1];
    }
}
