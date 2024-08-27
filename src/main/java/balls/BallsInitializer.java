package balls;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.abstracts.CustomSavableRaw;
import basemod.helpers.RelicType;
import basemod.interfaces.EditCardsSubscriber;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.SphericGuardian;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import balls.cards.TesticularTorsion;
import balls.helpers.ComplimentHelper;
import balls.relics.*;
import balls.util.IDCheckDontTouchPls;
import balls.util.TextureLoader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class BallsInitializer implements
    EditCardsSubscriber,
    EditRelicsSubscriber,
    EditStringsSubscriber,
    OnStartBattleSubscriber,
    PostInitializeSubscriber,
    PostRenderSubscriber,
    PostUpdateSubscriber {

    public static final Logger logger = LogManager.getLogger(BallsInitializer.class.getName());
    private static String modID;

    public static ArrayList<AbstractBallRelic> balls = new ArrayList<AbstractBallRelic>();

    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties ballsProperties = new Properties();
    public static final String DISABLE_BALL_COMPLIMENTS = "disableBallCompliments";
    public static final String DISABLE_PREVIEW_RELICS = "disablePreviewRelics";
    public static boolean disableBallCompliments = false;

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
        logger.info("========================= Fondling Balls. =========================");
        //This creates an instance of our classes and gets our code going after BaseMod and ModTheSpire initialize.
        new BallsInitializer();
        logger.info("========================= /Balls Fondled./ =========================");
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

        settingsPanel.addUIElement(disableBallComplimentsButton);

        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        logger.info("Done loading badge Image and mod options");

        // =============== SAVE FIELDS ===================
        logger.info("Adding save fields");
        BaseMod.addSaveField("perActRelics", new CustomSavableRaw() {
            @Override
            public void onLoadRaw(JsonElement json) {
                if (AbstractDungeon.player != null) {
                    if (json != null) {
                        JsonObject object = json.getAsJsonObject();
                        if (AbstractDungeon.player.hasRelic(BowlingBall.RELIC_ID) && object.has("bowlingBallActive")) {
                            JsonElement bowlingBallActive = object.get("bowlingBallActive");
                            BowlingBall bowlingBall = ((BowlingBall)AbstractDungeon.player.getRelic(BowlingBall.RELIC_ID));
                            bowlingBall.doubleDamage = bowlingBallActive.getAsBoolean();
                        }
                        if (AbstractDungeon.player.hasRelic(CrystalBall.RELIC_ID) && object.has("crystalBallUsed")) {
                            JsonElement crystalBallUsed = object.get("crystalBallUsed");
                            CrystalBall crystalBall = ((CrystalBall)AbstractDungeon.player.getRelic(CrystalBall.RELIC_ID));
                            crystalBall.grayscale = crystalBallUsed.getAsBoolean();
                        }
                        if (AbstractDungeon.player.hasRelic(DragonBall.RELIC_ID) && object.has("dargonBallUsed")) {
                            JsonElement dragonBallUsed = object.get("dragonBallUsed");
                            DragonBall dragonBall = ((DragonBall)AbstractDungeon.player.getRelic(DragonBall.RELIC_ID));
                            dragonBall.grayscale = dragonBallUsed.getAsBoolean();
                        }
                        if (AbstractDungeon.player.hasRelic(EnergyBall.RELIC_ID) && object.has("energyBallUsed")) {
                            JsonElement energyBallUsed = object.get("energyBallUsed");
                            EnergyBall energyBall = ((EnergyBall)AbstractDungeon.player.getRelic(EnergyBall.RELIC_ID));
                            energyBall.grayscale = energyBallUsed.getAsBoolean();
                        }
                        if (AbstractDungeon.player.hasRelic(Eyeball.RELIC_ID) && object.has("eyeballUsed")) {
                            JsonElement eyeballUsed = object.get("eyeballUsed");
                            Eyeball eyeball = ((Eyeball)AbstractDungeon.player.getRelic(Eyeball.RELIC_ID));
                            eyeball.grayscale = eyeballUsed.getAsBoolean();
                        }
                        if (AbstractDungeon.player.hasRelic(RubberBandBall.RELIC_ID) && object.has("rubberBandBallUsed")) {
                            JsonElement rubberBandBallUsed = object.get("rubberBandBallUsed");
                            RubberBandBall rubberBandBall = ((RubberBandBall)AbstractDungeon.player.getRelic(RubberBandBall.RELIC_ID));
                            rubberBandBall.grayscale = rubberBandBallUsed.getAsBoolean();
                        }
                        if (AbstractDungeon.player.hasRelic(TennisBall.RELIC_ID) && object.has("tennisBallUsed")) {
                            JsonElement tennisBallUsed = object.get("tennisBallUsed");
                            TennisBall tennisBall = ((TennisBall)AbstractDungeon.player.getRelic(TennisBall.RELIC_ID));
                            tennisBall.grayscale = tennisBallUsed.getAsBoolean();
                        }
                    }
                }
            }

            @Override
            public JsonElement onSaveRaw() {
                JsonParser parser = new JsonParser();
                boolean bowlingBallActive = false;
                boolean crystalBallUsed = false;
                boolean dragonBallUsed = false;
                boolean energyBallUsed = false;
                boolean eyeballUsed = false;
                boolean rubberBandBallUsed = false;
                boolean tennisBallUsed = false;

                if(AbstractDungeon.player != null) {
                    if (AbstractDungeon.player.hasRelic(BowlingBall.RELIC_ID)) {
                        bowlingBallActive = ((BowlingBall)AbstractDungeon.player.getRelic(BowlingBall.RELIC_ID)).doubleDamage;
                    }
                    if (AbstractDungeon.player.hasRelic(CrystalBall.RELIC_ID)) {
                        crystalBallUsed = AbstractDungeon.player.getRelic(CrystalBall.RELIC_ID).grayscale;
                    }
                    if (AbstractDungeon.player.hasRelic(DragonBall.RELIC_ID)) {
                        dragonBallUsed = AbstractDungeon.player.getRelic(DragonBall.RELIC_ID).grayscale;
                    }
                    if (AbstractDungeon.player.hasRelic(EnergyBall.RELIC_ID)) {
                        energyBallUsed = AbstractDungeon.player.getRelic(EnergyBall.RELIC_ID).grayscale;
                    }
                    if (AbstractDungeon.player.hasRelic(Eyeball.RELIC_ID)) {
                        eyeballUsed = AbstractDungeon.player.getRelic(Eyeball.RELIC_ID).grayscale;
                    }
                    if (AbstractDungeon.player.hasRelic(RubberBandBall.RELIC_ID)) {
                        rubberBandBallUsed = AbstractDungeon.player.getRelic(RubberBandBall.RELIC_ID).grayscale;
                    }
                    if (AbstractDungeon.player.hasRelic(TennisBall.RELIC_ID)) {
                        tennisBallUsed = AbstractDungeon.player.getRelic(TennisBall.RELIC_ID).grayscale;
                    }
                }

                return parser.parse(
                    "{\"bowlingBallActive\":\""
                    + bowlingBallActive
                    + "\",\"crystalBallUsed\":\""
                    + crystalBallUsed
                    + "\",\"dragonBallUsed\":\""
                    + dragonBallUsed
                    + "\",\"energyBallUsed\":\""
                    + energyBallUsed
                    + "\",\"eyeballUsed\":\""
                    + eyeballUsed
                    + "\",\"rubberBandBallUsed\":\""
                    + rubberBandBallUsed
                    + "\",\"tennisBallUsed\":\""
                    + tennisBallUsed
                    + "\"}"
                );
            }
        });
        // =============== /SAVE FIELDS/ ===================

    }

    // =============== /POST-INITIALIZE/ =================


    // ================ LOAD THE TEXT ===================

    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings for mod with ID: " + getModID());

        pathCheck();

        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
            getModID() + "Resources/localization/eng/Balls-Card-Strings.json");

        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
            getModID() + "Resources/localization/eng/Balls-Power-Strings.json");

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
    public void receiveEditCards() {
        logger.info("Adding cards");

        BaseMod.addCard(new TesticularTorsion());

        logger.info("Done adding cards");
    }

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
        //                 balls.add((AbstractRelic)relic);
        //             } else {
        //                 balls.addToCustomPool((AbstractRelic)relic, ((AbstractBallRelic)relic).cardColor);
        //             }
        //         }
        //         if (info.seen)
        //             UnlockTracker.markRelicAsSeen(relic.relicId);
        //     });

        balls.add(new Baal());
        balls.add(new BallOfYarn());
        balls.add(new BallSack());
        balls.add(new Baseball());
        balls.add(new Basketball());
        balls.add(new BeachBall());
        // balls.add(new BingoBall());
        balls.add(new BowlingBall());
        balls.add(new BruceAvocadoLittleLeviathanSouleater());
        // balls.add(new Butterball());
        // balls.add(new Cannonball());
        balls.add(new CheeseBall());
        // balls.add(new CottonBall());
        balls.add(new CricketBall());
        balls.add(new CrystalBall());
        // balls.add(new DiscoBall()); // TODO: BROKEN
        balls.add(new Dodgeball());
        balls.add(new DragonBall());
        balls.add(new EightBall());
        balls.add(new EnergyBall());
        balls.add(new Eyeball());
        // balls.add(new Fireball());
        balls.add(new Football());
        balls.add(new FudgeBall());
        balls.add(new GolfBall());
        balls.add(new Gumball());
        balls.add(new Handball());
        balls.add(new Hairball());
        balls.add(new Kickball());
        // balls.add(new Meatball());
        balls.add(new MedicineBall());
        balls.add(new Mudball());
        balls.add(new PachinkoBall());
        balls.add(new PaddleBall());
        balls.add(new Pinball());
        balls.add(new PingPongBall());
        balls.add(new Pokeball());
        balls.add(new RiceBall());
        balls.add(new RubberBandBall());
        balls.add(new RubberBouncyBall());
        // balls.add(new RugbyBall());
        balls.add(new SoccerBall());
        // balls.add(new StressBall());
        balls.add(new TennisBall());
        // balls.add(new Tetherball());
        balls.add(new Volleyball());
        balls.add(new WreckingBall());
        // balls.add(new YogaBall());

        for (AbstractBallRelic ball : balls) {
            BaseMod.addRelic(ball, RelicType.SHARED);
        }

        Marble marble = new Marble();
        BaseMod.addRelic(marble, RelicType.RED);
        balls.add(marble);

        Snowball snowball = new Snowball();
        BaseMod.addRelic(snowball, RelicType.BLUE);
        balls.add(snowball);

        WaterPoloBall wpb = new WaterPoloBall();
        BaseMod.addRelic(wpb, RelicType.PURPLE);
        balls.add(wpb);

        WhiffleBall wf = new WhiffleBall();;
        BaseMod.addRelic(wf, RelicType.GREEN);
        balls.add(wf);

        logger.info ("Done adding relics");
    }

    // ================ /ADD RELICS/ ===================


    // ================ COMPLIMENT BALLS ===================

    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
        if (disableBallCompliments)
            return;
        int ballCount = 0;
        for (AbstractRelic relic : AbstractDungeon.player.relics)
            if (relic instanceof AbstractBallRelic)
                ballCount++;
        if (ballCount > 0) {
            for (AbstractMonster monster : room.monsters.monsters) {
                String compliment = "";

                if (monster.id.equals(SphericGuardian.ID)) {
                    compliment = ComplimentHelper.getSphericGuardianPhrase();
                } else if (ballCount == 1 && !AbstractDungeon.player.hasRelic(BruceAvocadoLittleLeviathanSouleater.RELIC_ID)) {
                    compliment = ComplimentHelper.getInsult();
                } else if (AbstractDungeon.player.hasRelic(BruceAvocadoLittleLeviathanSouleater.RELIC_ID) && AbstractDungeon.miscRng.random(1, 5) == 1) {
                    compliment = ComplimentHelper.getBrucePhrase();
                } else if (AbstractDungeon.player.hasRelic(Eyeball.RELIC_ID) && AbstractDungeon.miscRng.random(1, 5) == 1) {
                    compliment = ComplimentHelper.getEyeballPhrase();
                } else if (AbstractDungeon.player.hasRelic(EnergyBall.RELIC_ID) && AbstractDungeon.miscRng.random(1, 5) == 1) {
                    compliment = ComplimentHelper.getEnergyBallPhrase();
                } else {
                    compliment = ComplimentHelper.getCompliment();
                }
                AbstractDungeon.actionManager.addToBottom(
                    new TalkAction(monster, compliment, 3.0F, 3.0F));
            }
        }
    }

    // ================ /COMPLIMENT BALLS/ ===================


    // ================ UPDATE & RENDER ===================

    @Override
    public void receivePostUpdate() {
        // if (!disablePreviewRelics) {
        //     EventInformationPanel.panel().update();
        //     CombatInformationPanel.panel().update();
        // }
    }

    @Override
    public void receivePostRender(SpriteBatch sb) {
        // if (!disablePreviewRelics) {
        //     EventInformationPanel.panel().render(sb);
        //     CombatInformationPanel.panel().render(sb);
        // }
    }

    // ================ /UPDATE & RENDER/ ===================


    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }
}
