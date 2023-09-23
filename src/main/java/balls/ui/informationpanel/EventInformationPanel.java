package balls.ui.informationpanel;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.Skeleton;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.WingBoots;
import com.megacrit.cardcrawl.rewards.chests.MediumChest;
import com.megacrit.cardcrawl.rewards.chests.SmallChest;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoom;

import basemod.ReflectionHacks;
import basemod.eventUtil.EventUtils;
import basemod.eventUtil.util.Condition;
import balls.BallsInitializer;
import balls.patches.DontGenerateNewCombatsPatch;
import balls.patches.DontGenerateNewEventsPatch;
import balls.relics.DiscoBall;
import balls.relics.EightBall;

public class EventInformationPanel {

    private static EventInformationPanel panel;

    private int x;
    private int y;

    private static final float X_OFFSET = 60.0F * Settings.scale;
    private static final float Y_OFFSET = 20.0F * Settings.scale;

    private static final float MERCHANT_X_OFFSET = 120.0F * Settings.scale;
    private static final float MERCHANT_Y_OFFSET = 90.0F * Settings.scale;

    private static final float IMG_X_OFFSET = 78.0F * Settings.scale;
    private static final float IMG_Y_OFFSET = 318.0F * Settings.scale;

    private static final float MONSTER_Y_OFFSET = 500.0F * Settings.scale;

    private boolean nodeHovered;

    public ArrayList<AbstractMonster> monsters = new ArrayList<>();
    private int monsterIdx;
    private AbstractMonster monster;
    private static final float ROTATE_TIMER = 1.5F;
    private float timer = 0.0f;
    int floorNum = 0;

    private AnimatedNpc animation = null;

    private String eventName = "";
    private String key = "";
    private Texture eventImg;
    private Texture chestImg;

    private static final float SHADOW_DIST_X = ReflectionHacks.getPrivateStatic(TipHelper.class, "SHADOW_DIST_X");
    private static final float SHADOW_DIST_Y = ReflectionHacks.getPrivateStatic(TipHelper.class, "SHADOW_DIST_Y");
    private static final float BOX_W = ReflectionHacks.getPrivateStatic(TipHelper.class, "BOX_W");
    private static final float BOX_EDGE_H = ReflectionHacks.getPrivateStatic(TipHelper.class, "BOX_EDGE_H");
    private static final float BOX_BODY_H = ReflectionHacks.getPrivateStatic(TipHelper.class, "BOX_BODY_H");
    private static final float TEXT_OFFSET_X = ReflectionHacks.getPrivateStatic(TipHelper.class, "TEXT_OFFSET_X");
    private static final float HEADER_OFFSET_Y = ReflectionHacks.getPrivateStatic(TipHelper.class, "HEADER_OFFSET_Y");

    private Logger logger = LogManager.getLogger(EventInformationPanel.class.getName());

    private EventInformationPanel() {}

    public static EventInformationPanel panel() {
        if (panel == null)
            panel = new EventInformationPanel();
        return panel;
    }

    private String getTitle() { // TODO: localize, also make better
        if (!eventName.equals(""))
            return eventName;
        else if (!monsters.isEmpty())
            return "Combat (" + monsters.size() + " enemies)";
        else if (animation != null)
            return "Shop";
        else if (chestImg != null)
            return "Treasure";
        else {
            logger.info("Couldn't find title for event: " + key);
            return key;
        }
    }

    public void update() {
        nodeHovered = false;
        eventName = "";
        monsters.clear();
        eventImg = null;
        animation = null;
        chestImg = null;

        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MAP || AbstractDungeon.actNum == 4
            || (AbstractDungeon.player != null && !AbstractDungeon.player.hasRelic(EightBall.RELIC_ID))
            || (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMPLETE))
                return;

        if (floorNum < AbstractDungeon.floorNum) {
            floorNum = AbstractDungeon.floorNum;
            monster = null;
            monsterIdx = 0;
        }

        ArrayList<MapRoomNode> nodes = new ArrayList<>();

        this.x = InputHelper.mX;
        this.y = InputHelper.mY;

        int rowFound = -1;
        int currRow = -1;
        if (AbstractDungeon.getCurrRoom() == null)
            currRow = 0;
        for (int i = 0; i < AbstractDungeon.map.size(); i++) {
            ArrayList<MapRoomNode> row = AbstractDungeon.map.get(i);
            for (int j = 0; j < row.size(); j++) {
                MapRoomNode node = row.get(j);
                if (node.x == AbstractDungeon.getCurrMapNode().x && node.y == AbstractDungeon.getCurrMapNode().y) {
                    currRow = i;
                    BallsInitializer.logger.info(currRow);
                }
                if (node.room != null && node.room.getMapSymbol() != null && node.room.getMapSymbol().equals("?")
                    && currRow != -1 && currRow + 1 == i
                    && (node.getParents().contains(AbstractDungeon.getCurrMapNode())
                        || (AbstractDungeon.player.hasRelic(DiscoBall.RELIC_ID) && ((DiscoBall)AbstractDungeon.player.getRelic(DiscoBall.RELIC_ID)).canSkip)
                        || (AbstractDungeon.player.hasRelic(WingBoots.ID) && !AbstractDungeon.player.getRelic(WingBoots.ID).usedUp))) {
                    rowFound = i;

                    if (node.room instanceof EventRoom && node.room.event != null) {
                        key = DontGenerateNewEventsPatch.EventFields.eventKey.get(node.room.event);
                        if ((EventUtils.normalEventBonusConditions.containsKey(key) && (!((Condition)EventUtils.normalEventBonusConditions.get(key)).test()))) {
                            node.room.event = null;
                            key = "";
                        }
                    }

                    if (node.room instanceof EventRoom && node.room.event == null) { // Event is null, re-roll room determine if still event
                        Random eventRng = new Random(Settings.seed, AbstractDungeon.eventRng.counter);
                        EventHelper.RoomResult roomResult = EventHelper.roll(eventRng);
                        AbstractDungeon.eventRng = eventRng;
                        node.room = ReflectionHacks.privateMethod(AbstractDungeon.class, "generateRoom", EventHelper.RoomResult.class).invoke(CardCrawlGame.dungeon, roomResult);
                        if (node.room instanceof EventRoom) {
                            MonsterGroup currRoomMonsters = AbstractDungeon.getCurrRoom().monsters; // monsters are placed in current room when generated
                            node.room.event = AbstractDungeon.generateEvent(eventRng);
                            node.room.monsters = AbstractDungeon.getCurrRoom().monsters;
                            monsterIdx = 0;
                            AbstractDungeon.getCurrRoom().monsters = currRoomMonsters;
                            key = DontGenerateNewEventsPatch.EventFields.eventKey.get(node.room.event);
                        }
                    }

                    if (node.room instanceof MonsterRoomElite) {
                        node.room.combatEvent = true;
                        if (node.room.monsters == null) {
                            node.room.monsters = CardCrawlGame.dungeon.getEliteMonsterForRoomCreation();
                            DontGenerateNewCombatsPatch.RoomFields.initMonsters.set(node.room, true);
                            monsterIdx = 0;
                        }
                        key = DontGenerateNewCombatsPatch.MonsterGroupFields.encounterKey.get(node.room.monsters);
                    } else if (node.room instanceof MonsterRoom) {
                        node.room.combatEvent = true;
                        if (node.room.monsters == null) {
                            DontGenerateNewCombatsPatch.RoomFields.initMonsters.set(node.room, true);
                            node.room.monsters = balls.helpers.EventHelper.getPreGeneratedEncounter();
                            monsterIdx = 0;
                        }
                        key = DontGenerateNewCombatsPatch.MonsterGroupFields.encounterKey.get(node.room.monsters);
                    } else if (node.room instanceof ShopRoom) {
                        key = "";
                    } else if (node.room instanceof TreasureRoom) {
                        TreasureRoom treasureRoom = (TreasureRoom)node.room;
                        if (treasureRoom.chest == null) // TODO: prevent generation of chests
                            treasureRoom.chest = AbstractDungeon.getRandomChest();
                    }
                    node.room.setMapSymbol("?");
                    node.room.setMapImg(ImageMaster.MAP_NODE_EVENT, ImageMaster.MAP_NODE_EVENT_OUTLINE);

                    nodes.add(node);
                }
            }
            if (rowFound != -1)
                break;
        }

        for (MapRoomNode node : nodes) {
            node.hb.update();
            if (node.hb.hovered) {
                nodeHovered = true;
                if (node.room.event != null && node.room.event instanceof AbstractImageEvent) {
                    eventName = EventHelper.getEventName(key);
                    eventImg = ReflectionHacks.getPrivate(node.room.event.imageEventText, GenericEventDialog.class, "img");
                    monsterIdx = -1;
                } else if (node.room.event != null && node.room.combatEvent) {
                    if (monsterIdx == -1)
                        monsterIdx = 0;
                    eventName = "Event Combat (" + monsters.size() + " enemies)"; // TODO - localize
                    monsters = new ArrayList<>(node.room.monsters.monsters);
                } else if (node.room instanceof MonsterRoomElite || node.room instanceof MonsterRoom) {
                    if (monsterIdx == -1)
                        monsterIdx = 0;
                    monsters = new ArrayList<>(node.room.monsters.monsters);
                } else if (node.room instanceof ShopRoom) {
                    animation = new AnimatedNpc(x + MERCHANT_X_OFFSET, y - MERCHANT_Y_OFFSET, "images/npcs/merchant/skeleton.atlas", "images/npcs/merchant/skeleton.json", "idle");
                    monsterIdx = -1;
                } else if (node.room instanceof TreasureRoom) {
                    TreasureRoom treasureRoom = (TreasureRoom)node.room;
                    if (treasureRoom.chest == null) {
                        treasureRoom.chest = AbstractDungeon.getRandomChest(); // TODO; verify logic is sound
                    }
                    if (treasureRoom.chest instanceof SmallChest) {
                        chestImg = ImageMaster.S_CHEST;
                    }
                    else if (treasureRoom.chest instanceof MediumChest) {
                        chestImg = ImageMaster.M_CHEST;
                    }
                    else {
                        chestImg = ImageMaster.L_CHEST;
                    }
                    monsterIdx = -1;
                } else {
                    BallsInitializer.logger.info("Unknown room type: " + AbstractDungeon.getCurrRoom().getClass().getSimpleName());
                    monsterIdx = -1;
                }
                break;
            }
        }

        if(monsterIdx >= 0 && !monsters.isEmpty()) {
            timer -= Gdx.graphics.getDeltaTime();
            if (timer <= 0.0F) {
                timer = ROTATE_TIMER;
                monsterIdx++;
                if (monsterIdx >= monsters.size())
                    monsterIdx = 0;
                monster = monsters.get(monsterIdx);
            }
        }
    }

    public void render(SpriteBatch sb) {
        if (nodeHovered) {
            renderTipBox(sb);
            if (eventImg != null) {
                sb.draw(eventImg, this.x + IMG_X_OFFSET, this.y - IMG_Y_OFFSET, eventImg.getWidth() * 0.63F, eventImg.getHeight() * 0.63F);
            } else if (!monsters.isEmpty()) {
                try {
                    monster.state.update(Gdx.graphics.getDeltaTime());
                    Field skeletonField = AbstractCreature.class.getDeclaredField("skeleton");
                    skeletonField.setAccessible(true);
                    Skeleton skeleton = (Skeleton)skeletonField.get((AbstractCreature)monster);
                    monster.state.apply(skeleton);
                    skeleton.updateWorldTransform();
                    skeleton.setPosition((InputHelper.mX < Settings.WIDTH / 2 ? Settings.WIDTH - 325.0F * Settings.scale : 325.0F * Settings.scale), 85.0F);
                    skeleton.setColor(Color.WHITE);
                    sb.end();
                    CardCrawlGame.psb.begin();
                    AbstractCreature.sr.draw(CardCrawlGame.psb, skeleton);
                    CardCrawlGame.psb.end();
                    sb.begin();
                    sb.setBlendFunction(770, 771);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (animation != null) {
                animation.render(sb);
            } else if (chestImg != null) {
                sb.draw(chestImg, this.x + X_OFFSET, this.y + Y_OFFSET);
            }
        }
    }

    private void renderTipBox(SpriteBatch sb) {
        float h = 0.0F;
        if (monster != null && !monsters.isEmpty()) {
            h = 320.0F * Settings.scale;
            float x = (InputHelper.mX < Settings.WIDTH / 2 ? Settings.WIDTH - BOX_W * 2.0F : 0.0F);
            sb.setColor(Settings.TOP_PANEL_SHADOW_COLOR);
            sb.draw(ImageMaster.KEYWORD_TOP, x + SHADOW_DIST_X, MONSTER_Y_OFFSET - SHADOW_DIST_Y, BOX_W * 2.0F, BOX_EDGE_H);
            sb.draw(ImageMaster.KEYWORD_BODY, x + SHADOW_DIST_X, MONSTER_Y_OFFSET - h - BOX_EDGE_H - SHADOW_DIST_Y, BOX_W * 2.0F, h + BOX_EDGE_H);
            sb.draw(ImageMaster.KEYWORD_BOT, x + SHADOW_DIST_X, MONSTER_Y_OFFSET - h - BOX_BODY_H - SHADOW_DIST_Y, BOX_W * 2.0F, BOX_EDGE_H);
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.KEYWORD_TOP, x, MONSTER_Y_OFFSET, BOX_W * 2.0F, BOX_EDGE_H);
            sb.draw(ImageMaster.KEYWORD_BODY, x, MONSTER_Y_OFFSET - h - BOX_EDGE_H, BOX_W * 2.0F, h + BOX_EDGE_H);
            sb.draw(ImageMaster.KEYWORD_BOT, x, MONSTER_Y_OFFSET - h - BOX_BODY_H, BOX_W * 2.0F, BOX_EDGE_H);
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, getTitle(), x + TEXT_OFFSET_X + 10.0F * Settings.scale, MONSTER_Y_OFFSET + HEADER_OFFSET_Y, Settings.GOLD_COLOR);
        } else {
            h = 250.0F * Settings.scale;
            sb.setColor(Settings.TOP_PANEL_SHADOW_COLOR);
            sb.draw(ImageMaster.KEYWORD_TOP, x + X_OFFSET + SHADOW_DIST_X, y - Y_OFFSET - SHADOW_DIST_Y, BOX_W, BOX_EDGE_H);
            sb.draw(ImageMaster.KEYWORD_BODY, x + X_OFFSET + SHADOW_DIST_X, y - Y_OFFSET - h - BOX_EDGE_H - SHADOW_DIST_Y, BOX_W, h + BOX_EDGE_H);
            sb.draw(ImageMaster.KEYWORD_BOT, x + X_OFFSET + SHADOW_DIST_X, y - Y_OFFSET - h - BOX_BODY_H - SHADOW_DIST_Y, BOX_W, BOX_EDGE_H);
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.KEYWORD_TOP, x + X_OFFSET, y - Y_OFFSET, BOX_W, BOX_EDGE_H);
            sb.draw(ImageMaster.KEYWORD_BODY, x + X_OFFSET, y - Y_OFFSET - h - BOX_EDGE_H, BOX_W, h + BOX_EDGE_H);
            sb.draw(ImageMaster.KEYWORD_BOT, x + X_OFFSET, y - Y_OFFSET - h - BOX_BODY_H, BOX_W, BOX_EDGE_H);
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, getTitle(), x + X_OFFSET + TEXT_OFFSET_X, y - Y_OFFSET + HEADER_OFFSET_Y, Settings.GOLD_COLOR);
        }
    }
}
