package balls.helpers;

import java.util.ArrayList;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.GremlinNob;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import com.megacrit.cardcrawl.monsters.exordium.Sentry;

import basemod.BaseMod;

public class EventHelper {

    // TODO: ideally support for more than just 1 combat
    private static MonsterGroup currentEncounter = null;

    public static MonsterGroup getPreGeneratedEncounter() {
        if (currentEncounter == null)
            currentEncounter = CardCrawlGame.dungeon.getMonsterForRoomCreation();
        return currentEncounter;
    }

    public static void generateNextEncounter() {
        currentEncounter = CardCrawlGame.dungeon.getMonsterForRoomCreation();
    }

    public static MonsterGroup eventKeyToEncounter(String key) {
        // hard-code special cases where event key is different than encounter key
        switch(key) {
            case "Mysterious Sphere":
                return MonsterHelper.getEncounter("2 Orb Walkers");
            case "Dead Adventurer":
                ArrayList<AbstractMonster> monsters = new ArrayList<AbstractMonster>();
                monsters.add(new Lagavulin(false));
                monsters.add(new Sentry(0F, 0F));
                monsters.add(new GremlinNob(0F, 0F));
                return new MonsterGroup((AbstractMonster[])monsters.toArray());
            case "Mushrooms":
                return MonsterHelper.getEncounter("The Mushroom Lair");
        }
        // assume event key is the same as the encounter key, otherwise kind of out of luck
        return MonsterHelper.getEncounter(BaseMod.encounterList.get(BaseMod.encounterList.indexOf(key)));
    }
}
