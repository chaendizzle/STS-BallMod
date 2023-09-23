package balls;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.OnStartBattleSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostRenderSubscriber;
import basemod.interfaces.PostUpdateSubscriber;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.beyond.MysteriousSphere;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import balls.helpers.ComplimentHelper;
import balls.relics.*;
import balls.ui.informationpanel.CombatInformationPanel;
import balls.ui.informationpanel.EventInformationPanel;
import balls.util.IDCheckDontTouchPls;
import balls.util.TextureLoader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class BallsInitializer implements
    EditRelicsSubscriber,
    EditStringsSubscriber,
    OnStartBattleSubscriber,
    PostInitializeSubscriber,
    PostRenderSubscriber,
    PostUpdateSubscriber {

    public static final Logger logger = LogManager.getLogger(BallsInitializer.class.getName());
    private static String modID;

    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties ballsProperties = new Properties();
    public static final String DISABLE_BALL_COMPLIMENTS = "disableBallCompliments";
    public static final String DISABLE_PREVIEW_RELICS = "disablePreviewRelics";
    public static boolean disableBallCompliments = false;
    public static boolean disablePreviewRelics = false;

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "Balls";
    private static final String AUTHOR = "dandylion1740";
    private static final String DESCRIPTION = "Adds balls.";

    // =============== INPUT TEXTURE LOCATION =================

    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "ballsResources/images/Badge.png";

    // =============== MAKE IMAGE PATHS =================

    public static String makeImagePath(String resourcePath) {
        return getModID() + "Resources/images/" + resourcePath;
    }

    // =============== /MAKE IMAGE PATHS/ =================


    // =============== SUBSCRIBE, INITIALIZE =================

    public BallsInitializer(){
        logger.info("Subscribe to BlankMod hooks");

        BaseMod.subscribe(this);
        setModID("balls");

        logger.info("Done subscribing");

        logger.info("Adding mod settings");
        ballsProperties.setProperty(DISABLE_BALL_COMPLIMENTS, "FALSE");
        ballsProperties.setProperty(DISABLE_PREVIEW_RELICS, "FALSE");
        try {
            SpireConfig config = new SpireConfig("balls", "ballsConfig", ballsProperties);
            config.load();
            disableBallCompliments = config.getBool(DISABLE_BALL_COMPLIMENTS);
            disablePreviewRelics = config.getBool(DISABLE_PREVIEW_RELICS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");
    }

    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP

    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = BallsInitializer.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
    } // NO

    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH

    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = BallsInitializer.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = BallsInitializer.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO

    // ====== YOU CAN EDIT AGAIN ======

    //Used by @SpireInitializer
    public static void initialize(){
        logger.info("========================= Initializing Balls. =========================");
        //This creates an instance of our classes and gets our code going after BaseMod and ModTheSpire initialize.
        new BallsInitializer();
        logger.info("========================= /Balls Initialized./ =========================");
    }

    // ============== /SUBSCRIBE, INITIALIZE/ =================


    // =============== POST-INITIALIZE =================

    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");

        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();

        // Create the on/off button:
        ModLabeledToggleButton disableBallComplimentsButton = new ModLabeledToggleButton("Please stop complimenting my balls",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                disableBallCompliments,
                settingsPanel,
                (label) -> {},
                (button) -> {

            disableBallCompliments = button.enabled;
            try {
                SpireConfig config = new SpireConfig("balls", "ballsConfig", ballsProperties);
                config.setBool(DISABLE_BALL_COMPLIMENTS, disableBallCompliments);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        ModLabeledToggleButton disablePreviewRelicsButton = new ModLabeledToggleButton("Disable Eight Ball & Crystal Ball",
                350.0f, 650.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                disablePreviewRelics,
                settingsPanel,
                (label) -> {},
                (button) -> {

            disablePreviewRelics = button.enabled;
            try {
                SpireConfig config = new SpireConfig("balls", "ballsConfig", ballsProperties);
                config.setBool(DISABLE_PREVIEW_RELICS, disablePreviewRelics);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        settingsPanel.addUIElement(disableBallComplimentsButton);
        settingsPanel.addUIElement(disablePreviewRelicsButton);

        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        logger.info("Done loading badge Image and mod options");
    }

    // =============== /POST-INITIALIZE/ =================


    // ================ LOAD THE TEXT ===================

    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings for mod with ID: " + getModID());

        pathCheck();

        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
            getModID() + "Resources/localization/eng/Balls-Relic-Strings.json");

        // UIStrings
        BaseMod.loadCustomStringsFile(UIStrings.class,
            getModID() + "Resources/localization/eng/Balls-UI-Strings.json");

        logger.info("Done editing strings");
    }

    // ================ /LOAD THE TEXT/ ===================


    // ================ ADD RELICS ===================

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        // TOOD: like usual, auto add doesn't work for some reason so I have to add the relics manually
        // new AutoAdd(modID)
        //     .packageFilter(AbstractBallRelic.class)
        //     .any(CustomRelic.class, (info, relic) -> {
        //         logger.info("HERE");
        //         logger.info(relic.relicId);
        //         if (relic instanceof AbstractBallRelic) {
        //             if (((AbstractBallRelic)relic).cardColor == null) {
        //                 BaseMod.addRelic((AbstractRelic)relic, RelicType.SHARED);
        //             } else {
        //                 BaseMod.addRelicToCustomPool((AbstractRelic)relic, ((AbstractBallRelic)relic).cardColor);
        //             }
        //         }
        //         if (info.seen)
        //             UnlockTracker.markRelicAsSeen(relic.relicId);
        //     });

        BaseMod.addRelic(new Baal(), RelicType.SHARED);
        BaseMod.addRelic(new BallOfYarn(), RelicType.SHARED);
        BaseMod.addRelic(new Baseball(), RelicType.SHARED);
        BaseMod.addRelic(new Basketball(), RelicType.SHARED);
        BaseMod.addRelic(new BeachBall(), RelicType.SHARED);
        BaseMod.addRelic(new BowlingBall(), RelicType.SHARED);
        BaseMod.addRelic(new CheeseBall(), RelicType.SHARED);
        BaseMod.addRelic(new CrystalBall(), RelicType.SHARED);
        // BaseMod.addRelic(new DiscoBall(), RelicType.SHARED);
        BaseMod.addRelic(new Dodgeball(), RelicType.SHARED);
        // BaseMod.addRelic(new DragonBall(), RelicType.SHARED);
        BaseMod.addRelic(new EightBall(), RelicType.SHARED);
        BaseMod.addRelic(new EnergyBall(), RelicType.SHARED);
        // BaseMod.addRelic(new Eyeball(), RelicType.SHARED);
        BaseMod.addRelic(new Football(), RelicType.SHARED);
        // BaseMod.addRelic(new FudgeBall(), RelicType.SHARED);
        BaseMod.addRelic(new GolfBall(), RelicType.SHARED);
        // BaseMod.addRelic(new Gumball(), RelicType.SHARED);
        BaseMod.addRelic(new Handball(), RelicType.SHARED);
        BaseMod.addRelic(new Kickball(), RelicType.SHARED);
        BaseMod.addRelic(new Marble(), RelicType.RED);
        BaseMod.addRelic(new MedicineBall(), RelicType.SHARED);
        BaseMod.addRelic(new PachinkoBall(), RelicType.SHARED);
        BaseMod.addRelic(new PaddleBall(), RelicType.SHARED);
        BaseMod.addRelic(new Pinball(), RelicType.SHARED);
        BaseMod.addRelic(new PingPongBall(), RelicType.SHARED);
        BaseMod.addRelic(new Pokeball(), RelicType.SHARED);
        BaseMod.addRelic(new RiceBall(), RelicType.SHARED);
        BaseMod.addRelic(new RubberBandBall(), RelicType.SHARED);
        BaseMod.addRelic(new RubberBouncyBall(), RelicType.SHARED);
        // BaseMod.addRelic(new RugbyBall(), RelicType.SHARED);
        BaseMod.addRelic(new Snowball(), RelicType.BLUE);
        // BaseMod.addRelic(new SoccerBall(), RelicType.SHARED);
        BaseMod.addRelic(new TennisBall(), RelicType.SHARED);
        BaseMod.addRelic(new Volleyball(), RelicType.SHARED);
        BaseMod.addRelic(new WaterPoloBall(), RelicType.PURPLE);
        BaseMod.addRelic(new WhiffleBall(), RelicType.GREEN);
        BaseMod.addRelic(new WreckingBall(), RelicType.SHARED);

        logger.info ("Done adding relics");
    }

    // ================ /ADD RELICS/ ===================


    // ================ COMPLIMENT BALLS ===================

    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
        if (disableBallCompliments)
            return;
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof AbstractBallRelic) {
                for (AbstractMonster monster : room.monsters.monsters) {
                    if (monster.id.equals(MysteriousSphere.ID)) {
                        AbstractDungeon.actionManager.addToBottom(
                            new TalkAction(monster, ComplimentHelper.getMysteriousSpherePhrase(), 3.0F, 3.0F));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(
                            new TalkAction(monster, ComplimentHelper.getCompliment(), 3.0F, 3.0F));
                    }
                }
                break;
            }
        }
    }

    // ================ /COMPLIMENT BALLS/ ===================


    // ================ UPDATE & RENDER ===================

    @Override
    public void receivePostUpdate() {
        if (!disablePreviewRelics) {
            EventInformationPanel.panel().update();
            CombatInformationPanel.panel().update();
        }
    }

    @Override
    public void receivePostRender(SpriteBatch sb) {
        if (!disablePreviewRelics) {
            EventInformationPanel.panel().render(sb);
            CombatInformationPanel.panel().render(sb);
        }
    }

    // ================ /UPDATE & RENDER/ ===================


    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }
}
