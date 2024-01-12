package balls.relics;

import balls.BallsInitializer;
import balls.helpers.CombatHelper;
import basemod.animations.SpineAnimation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonMeshRenderer;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ShaderHelper;

public class BruceAvocadoLittleLeviathanSouleater extends AbstractBallRelic implements ClickableRelic {

    private static final String NAME = BruceAvocadoLittleLeviathanSouleater.class.getSimpleName();
    public static final String RELIC_ID = BallsInitializer.makeID(NAME);

    private static SpineAnimation animation = null;
    private static TextureAtlas atlas = null;
    private static Skeleton skeleton = null;
    private static AnimationStateData stateData = null;
    private static AnimationState state = null;
    private static SkeletonMeshRenderer sr = null;

    public BruceAvocadoLittleLeviathanSouleater() {
        super(RELIC_ID, NAME, RelicTier.RARE, LandingSound.HEAVY);
        counter = 0;
        if (animation == null) {
            animation = new SpineAnimation(
                "ballsResources/images/relics/animations/bruce.atlas",
                "ballsResources/images/relics/animations/bruce.json",
                1.0F);

            atlas = new TextureAtlas(Gdx.files.internal("ballsResources/images/relics/animations/bruce.atlas"));
            SkeletonJson json = new SkeletonJson(atlas);
            json.setScale(0.8F);
            SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("ballsResources/images/relics/animations/bruce.json"));
            skeleton = new Skeleton (skeletonData);
            skeleton.setColor(Color.WHITE);
            stateData = new AnimationStateData(skeletonData);
            state = new AnimationState(stateData);
            sr = new SkeletonMeshRenderer();
            sr.setPremultipliedAlpha(true);

            state.clearTracks();
            AnimationState.TrackEntry e = state.setAnimation(0, "animtion0", true);
            e.setTime(e.getEndTime() * MathUtils.random());
        }
    }

    private void renderAnimation(SpriteBatch sb) {
        if (atlas != null) {
            state.update(Gdx.graphics.getDeltaTime());
            state.apply(skeleton);
            skeleton.updateWorldTransform();
            skeleton.setPosition(this.currentX, this.currentY);
            skeleton.setColor(Color.WHITE);
            sb.end();
            CardCrawlGame.psb.begin();
            sr.draw(CardCrawlGame.psb, skeleton);
            CardCrawlGame.psb.end();
            sb.begin();
        } else {
            sb.setColor(Color.WHITE);
            sb.draw(this.img, this.currentX - 64.0F, this.currentY - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, this.scale, this.scale, 0, 0, 0, 128, 128, false, false);
        }
    }

    @Override
    public void renderInTopPanel(SpriteBatch sb) {
        if (Settings.hideRelics)
            return;
        if (grayscale)
            ShaderHelper.setShader(sb, ShaderHelper.Shader.GRAYSCALE);
        renderAnimation(sb);
        if (grayscale)
            ShaderHelper.setShader(sb, ShaderHelper.Shader.DEFAULT);
        renderCounter(sb, true);
        renderFlash(sb, true);
        hb.render(sb);
    }

    @Override
    public void onRightClick() {
        if (CombatHelper.isInCombat() && counter > 0) {
            addToBot(new GainBlockAction(AbstractDungeon.player, counter));
            flash();
            counter = 0;
        }
    }
}
