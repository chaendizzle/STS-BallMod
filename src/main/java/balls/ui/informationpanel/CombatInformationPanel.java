package balls.ui.informationpanel;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.Skeleton;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.WingBoots;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;

import basemod.ReflectionHacks;
import balls.helpers.EventHelper;
import balls.patches.DontGenerateNewCombatsPatch;
import balls.relics.CrystalBall;
import balls.relics.DiscoBall;

public class CombatInformationPanel {

    private static CombatInformationPanel panel;

    private static final float MONSTER_Y_OFFSET = 500.0F * Settings.scale;

    private static boolean nodeHovered;

    private static ArrayList<AbstractMonster> monsters = new ArrayList<>();
    private int monsterIdx = 0;
    private AbstractMonster monster;
    private static final float ROTATE_TIMER = 1.5F;
    private float timer = 0.0f;

    private static final float SHADOW_DIST_X = ReflectionHacks.getPrivateStatic(TipHelper.class, "SHADOW_DIST_X");
    private static final float SHADOW_DIST_Y = ReflectionHacks.getPrivateStatic(TipHelper.class, "SHADOW_DIST_Y");
    private static final float BOX_W = ReflectionHacks.getPrivateStatic(TipHelper.class, "BOX_W");
    private static final float BOX_EDGE_H = ReflectionHacks.getPrivateStatic(TipHelper.class, "BOX_EDGE_H");
    private static final float BOX_BODY_H = ReflectionHacks.getPrivateStatic(TipHelper.class, "BOX_BODY_H");
    private static final float TEXT_OFFSET_X = ReflectionHacks.getPrivateStatic(TipHelper.class, "TEXT_OFFSET_X");
    private static final float HEADER_OFFSET_Y = ReflectionHacks.getPrivateStatic(TipHelper.class, "HEADER_OFFSET_Y");


    private CombatInformationPanel() {}

    public static CombatInformationPanel panel() {
        if (panel == null)
            panel = new CombatInformationPanel();
        return panel;
    }

    public void update() {
        nodeHovered = false;
        monsters.clear();

        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MAP
            || (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT)
            || AbstractDungeon.player != null && !AbstractDungeon.player.hasRelic(CrystalBall.RELIC_ID))
                return;

        int currRow = -1;
        if (AbstractDungeon.getCurrRoom() == null)
            currRow = 0;
        for (int i = 0; i < AbstractDungeon.map.size(); i++) {
            ArrayList<MapRoomNode> row = AbstractDungeon.map.get(i);
            for (int j = 0; j < row.size(); j++) {
                MapRoomNode node = row.get(j);
                if (node == AbstractDungeon.getCurrMapNode())
                    currRow = i;
                node.hb.update();
                if (node.hb.hovered && (node.getParents().contains(AbstractDungeon.getCurrMapNode()) || i == 0
                    || (i == currRow + 1 && (AbstractDungeon.player.hasRelic(DiscoBall.RELIC_ID) || (AbstractDungeon.player.hasRelic(WingBoots.ID) && AbstractDungeon.player.getRelic(WingBoots.ID).counter > 0))))) {
                        nodeHovered = true;
                        if (node.room instanceof MonsterRoom || node.room instanceof MonsterRoomElite) {
                            if (node.room.monsters == null) {
                                if (node.room instanceof MonsterRoomElite) {
                                    DontGenerateNewCombatsPatch.RoomFields.initMonsters.set(node.room, true);
                                    node.room.monsters = CardCrawlGame.dungeon.getEliteMonsterForRoomCreation();
                                } else if (node.room instanceof MonsterRoom) {
                                    DontGenerateNewCombatsPatch.RoomFields.initMonsters.set(node.room, true);
                                    node.room.monsters = EventHelper.getPreGeneratedEncounter();
                                }
                            }
                            monsters = new ArrayList<>(node.room.monsters.monsters);
                        }
                }
                if (nodeHovered)
                    break;
            }
            if (nodeHovered)
                break;
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
        if (nodeHovered && !monsters.isEmpty() && monster != null) {
            renderTipBox(sb);
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
        }
    }

    private void renderTipBox(SpriteBatch sb) {
        float h = 400.0F * Settings.scale;
        float x = (InputHelper.mX < Settings.WIDTH / 2 ? Settings.WIDTH - BOX_W * 2.0F : 0.0F);
        sb.setColor(Settings.TOP_PANEL_SHADOW_COLOR);
        sb.draw(ImageMaster.KEYWORD_TOP, x + SHADOW_DIST_X, MONSTER_Y_OFFSET - SHADOW_DIST_Y, BOX_W * 2.0F, BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BODY, x + SHADOW_DIST_X, MONSTER_Y_OFFSET - h - BOX_EDGE_H - SHADOW_DIST_Y, BOX_W * 2.0F, h + BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BOT, x + SHADOW_DIST_X, MONSTER_Y_OFFSET - h - BOX_BODY_H - SHADOW_DIST_Y, BOX_W * 2.0F, BOX_EDGE_H);
        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.KEYWORD_TOP, x, MONSTER_Y_OFFSET, BOX_W * 2.0F, BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BODY, x, MONSTER_Y_OFFSET - h - BOX_EDGE_H, BOX_W * 2.0F, h + BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BOT, x, MONSTER_Y_OFFSET - h - BOX_BODY_H, BOX_W * 2.0F, BOX_EDGE_H);
        FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, "Combat (" + monsters.size() + " enemies)", x + TEXT_OFFSET_X + 10.0F * Settings.scale, MONSTER_Y_OFFSET + HEADER_OFFSET_Y, Settings.GOLD_COLOR);
    }
}
