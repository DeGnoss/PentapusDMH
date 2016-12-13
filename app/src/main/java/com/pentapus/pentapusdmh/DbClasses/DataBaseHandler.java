package com.pentapus.pentapusdmh.DbClasses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Koni on 27.02.2016.
 */
public class DataBaseHandler extends SQLiteAssetHelper {

    private static final String DATABASE_NAME ="pentalite.sqlite";
    // Database Version
    private static final int DATABASE_VERSION = 1;

    public static final int TYPE_MONSTER = 0;
    public static final int TYPE_NPC = 1;
    public static final int TYPE_PC = 2;

    // Database Name

    public static final String TABLE_CAMPAIGN = "campaign";
    public static final String TABLE_SESSION = "session";
    public static final String TABLE_ENCOUNTER = "encounter";

    public static final String TABLE_ENCOUNTER_PREP = "encounterprep";
    public static final String TABLE_MONSTER = "monster";
    public static final String TABLE_NPC = "npc";
    public static final String TABLE_PC = "pc";
    public static final String TABLE_SPELL = "spell";


    //All Tables
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_INFO = "info";
    public static final String KEY_BELONGSTO = "belongsto";
    public static final String KEY_ICON = "icon";
    public static final String KEY_DISABLED = "disabled";



    //Encounter & NPC Table
    public static final String KEY_CR = "cr";
    public static final String KEY_XP = "xp";
    public static final String KEY_INITIATIVEBONUS = "initiativebonus";


    //Monsters
    public static final String KEY_HPROLL = "hproll";
    public static final String KEY_ACTYPE = "actype";
    public static final String KEY_AC2 = "ac2";
    public static final String KEY_AC2TYPE = "ac2type";
    public static final String KEY_SIZE = "size";
    public static final String KEY_SPEED = "speed";
    public static final String KEY_SOURCEPAGE = "sourcepage";
    public static final String KEY_MULTIATTACK = "multiattack";

    public static final String KEY_ATK1NAME = "atk1name";
    public static final String KEY_ATK1DESC = "atk1desc";
    public static final String KEY_ATK1MOD = "atk1mod";
    public static final String KEY_ATK1DMG1ROLL = "atk1dmg1roll";
    public static final String KEY_ATK1DMG1TYPE = "atk1dmg1type";
    public static final String KEY_ATK1DMG2ROLL = "atk1dmg2roll";
    public static final String KEY_ATK1DMG2TYPE = "atk1dmg2type";
    public static final String KEY_ATK1AUTOROLL = "atk1autoroll";
    public static final String KEY_ATK1ADDITIONAL = "atk1additional";

    public static final String KEY_ATK2NAME = "atk2name";
    public static final String KEY_ATK2DESC = "atk2desc";
    public static final String KEY_ATK2MOD = "atk2mod";
    public static final String KEY_ATK2DMG1ROLL = "atk2dmg1roll";
    public static final String KEY_ATK2DMG1TYPE = "atk2dmg1type";
    public static final String KEY_ATK2DMG2ROLL = "atk2dmg2roll";
    public static final String KEY_ATK2DMG2TYPE = "atk2dmg2type";
    public static final String KEY_ATK2AUTOROLL = "atk2autoroll";
    public static final String KEY_ATK2ADDITIONAL = "atk2additional";

    public static final String KEY_ATK3NAME = "atk3name";
    public static final String KEY_ATK3DESC = "atk3desc";
    public static final String KEY_ATK3MOD = "atk3mod";
    public static final String KEY_ATK3DMG1ROLL = "atk3dmg1roll";
    public static final String KEY_ATK3DMG1TYPE = "atk3dmg1type";
    public static final String KEY_ATK3DMG2ROLL = "atk3dmg2roll";
    public static final String KEY_ATK3DMG2TYPE = "atk3dmg2type";
    public static final String KEY_ATK3AUTOROLL = "atk3autoroll";
    public static final String KEY_ATK3ADDITIONAL = "atk3additional";

    public static final String KEY_ATK4NAME = "atk4name";
    public static final String KEY_ATK4DESC = "atk4desc";
    public static final String KEY_ATK4MOD = "atk4mod";
    public static final String KEY_ATK4DMG1ROLL = "atk4dmg1roll";
    public static final String KEY_ATK4DMG1TYPE = "atk4dmg1type";
    public static final String KEY_ATK4DMG2ROLL = "atk4dmg2roll";
    public static final String KEY_ATK4DMG2TYPE = "atk4dmg2type";
    public static final String KEY_ATK4AUTOROLL = "atk4autoroll";
    public static final String KEY_ATK4ADDITIONAL = "atk4additional";

    public static final String KEY_ATK5NAME = "atk5name";
    public static final String KEY_ATK5DESC = "atk5desc";
    public static final String KEY_ATK5MOD = "atk5mod";
    public static final String KEY_ATK5DMG1ROLL = "atk5dmg1roll";
    public static final String KEY_ATK5DMG1TYPE = "atk5dmg1type";
    public static final String KEY_ATK5DMG2ROLL = "atk5dmg2roll";
    public static final String KEY_ATK5DMG2TYPE = "atk5dmg2type";
    public static final String KEY_ATK5AUTOROLL = "atk5autoroll";
    public static final String KEY_ATK5ADDITIONAL = "atk5additional";

    public static final String KEY_LMULTIATTACK = "lmultiattack";


    public static final String KEY_LATK1NAME = "latk1name";
    public static final String KEY_LATK1DESC = "latk1desc";
    public static final String KEY_LATK1MOD = "latk1mod";
    public static final String KEY_LATK1DMG1ROLL = "latk1dmg1roll";
    public static final String KEY_LATK1DMG1TYPE = "latk1dmg1type";
    public static final String KEY_LATK1DMG2ROLL = "latk1dmg2roll";
    public static final String KEY_LATK1DMG2TYPE = "latk1dmg2type";
    public static final String KEY_LATK1AUTOROLL = "latk1autoroll";
    public static final String KEY_LATK1ADDITIONAL = "latk1additional";

    public static final String KEY_LATK2NAME = "latk2name";
    public static final String KEY_LATK2DESC = "latk2desc";
    public static final String KEY_LATK2MOD = "latk2mod";
    public static final String KEY_LATK2DMG1ROLL = "latk2dmg1roll";
    public static final String KEY_LATK2DMG1TYPE = "latk2dmg1type";
    public static final String KEY_LATK2DMG2ROLL = "latk2dmg2roll";
    public static final String KEY_LATK2DMG2TYPE = "latk2dmg2type";
    public static final String KEY_LATK2AUTOROLL = "latk2autoroll";
    public static final String KEY_LATK2ADDITIONAL = "latk2additional";

    public static final String KEY_LATK3NAME = "latk3name";
    public static final String KEY_LATK3DESC = "latk3desc";
    public static final String KEY_LATK3MOD = "latk3mod";
    public static final String KEY_LATK3DMG1ROLL = "latk3dmg1roll";
    public static final String KEY_LATK3DMG1TYPE = "latk3dmg1type";
    public static final String KEY_LATK3DMG2ROLL = "latk3dmg2roll";
    public static final String KEY_LATK3DMG2TYPE = "latk3dmg2type";
    public static final String KEY_LATK3AUTOROLL = "latk3autoroll";
    public static final String KEY_LATK3ADDITIONAL = "latk3additional";

    public static final String KEY_LATK4NAME = "latk4name";
    public static final String KEY_LATK4DESC = "latk4desc";
    public static final String KEY_LATK4MOD = "latk4mod";
    public static final String KEY_LATK4DMG1ROLL = "latk4dmg1roll";
    public static final String KEY_LATK4DMG1TYPE = "latk4dmg1type";
    public static final String KEY_LATK4DMG2ROLL = "latk4dmg2roll";
    public static final String KEY_LATK4DMG2TYPE = "latk4dmg2type";
    public static final String KEY_LATK4AUTOROLL = "latk4autoroll";
    public static final String KEY_LATK4ADDITIONAL = "latk4additional";

    public static final String KEY_LATK5NAME = "latk5name";
    public static final String KEY_LATK5DESC = "latk5desc";
    public static final String KEY_LATK5MOD = "latk5mod";
    public static final String KEY_LATK5DMG1ROLL = "latk5dmg1roll";
    public static final String KEY_LATK5DMG1TYPE = "latk5dmg1type";
    public static final String KEY_LATK5DMG2ROLL = "latk5dmg2roll";
    public static final String KEY_LATK5DMG2TYPE = "latk5dmg2type";
    public static final String KEY_LATK5AUTOROLL = "latk5autoroll";
    public static final String KEY_LATK5ADDITIONAL = "latk5additional";

    public static final String KEY_SPELLCASTER = "spellcaster";
    public static final String KEY_SCLEVEL = "sclevel";
    public static final String KEY_SCABILITY = "scability";
    public static final String KEY_SCMOD = "scmod";
    public static final String KEY_SCDC = "scdc";
    public static final String KEY_SCCLASS = "scclass";
    public static final String KEY_SCSLOTS = "scslots";
    public static final String KEY_SCSPELLS = "scspells";


    public static final String KEY_INNATE = "innate";
    public static final String KEY_INABILITY = "inability";
    public static final String KEY_INMOD = "inmod";
    public static final String KEY_INDC = "indc";
    public static final String KEY_INPSIONICS = "inpsionics";
    public static final String KEY_INSPELLS = "inspells";


    public static final String KEY_REACTION1NAME = "reaction1name";
    public static final String KEY_REACTION1DESC = "reaction1desc";
    public static final String KEY_REACTION2NAME = "reaction2name";
    public static final String KEY_REACTION2DESC = "reaction2desc";
    public static final String KEY_REACTION3NAME = "reaction3name";
    public static final String KEY_REACTION3DESC = "reaction3desc";
    public static final String KEY_REACTION4NAME = "reaction4name";
    public static final String KEY_REACTION4DESC = "reaction4desc";
    public static final String KEY_REACTION5NAME = "reaction5name";
    public static final String KEY_REACTION5DESC = "reaction5desc";

    public static final String KEY_ABILITY1NAME = "ability1name";
    public static final String KEY_ABILITY1DESC = "ability1desc";
    public static final String KEY_ABILITY2NAME = "ability2name";
    public static final String KEY_ABILITY2DESC = "ability2desc";
    public static final String KEY_ABILITY3NAME = "ability3name";
    public static final String KEY_ABILITY3DESC = "ability3desc";
    public static final String KEY_ABILITY4NAME = "ability4name";
    public static final String KEY_ABILITY4DESC = "ability4desc";
    public static final String KEY_ABILITY5NAME = "ability5name";
    public static final String KEY_ABILITY5DESC = "ability5desc";

    public static final String KEY_ACROBATICS = "acrobatics";
    public static final String KEY_ANIMALHANDLING = "animalhandling";
    public static final String KEY_ARCANA = "arcana";
    public static final String KEY_ATHLETICS = "athletics";
    public static final String KEY_DECEPTION = "deception";
    public static final String KEY_HISTORY = "history";
    public static final String KEY_INSIGHT = "insight";
    public static final String KEY_INTIMIDATION = "intimidation";
    public static final String KEY_INVESTIGATION = "investigation";
    public static final String KEY_MEDICINE = "medicine";
    public static final String KEY_NATURE = "nature";
    public static final String KEY_PERCEPTION = "perception";
    public static final String KEY_PERFORMANCE = "performance";
    public static final String KEY_PERSUASION = "persuasion";
    public static final String KEY_RELIGION = "religion";
    public static final String KEY_SLEIGHTOFHAND = "sleightofhand";
    public static final String KEY_STEALTH = "stealth";
    public static final String KEY_SURVIVAL = "survival";

    public static final String KEY_STSTR = "ststr";
    public static final String KEY_STDEX = "stdex";
    public static final String KEY_STCON = "stcon";
    public static final String KEY_STINT = "stint";
    public static final String KEY_STWIS = "stwis";
    public static final String KEY_STCHA = "stcha";

    public static final String KEY_SENSES = "senses";
    public static final String KEY_ALIGNMENT = "alignment";
    public static final String KEY_LANGUAGES = "languages";

    public static final String KEY_DMGRES = "dmgres";
    public static final String KEY_DMGIM = "dmgim";
    public static final String KEY_DMGVUL = "dmgvul";
    public static final String KEY_CONIM = "conim";




    //PC & NPC Table
    public static final String KEY_MM = "mm";
    public static final String KEY_MAXHP = "maxhp";
    public static final String KEY_HP = "hp";
    public static final String KEY_AC = "ac";
    public static final String KEY_IDENTIFIER = "identifier";
    public static final String KEY_TYPE = "type";
    public static final String KEY_ACTIONS = "actions";
    public static final String KEY_ABILITIES = "abilities";


    //PC Table
    public static final String KEY_CLASS = "class";
    public static final String KEY_LEVEL = "level";
    public static final String KEY_PLAYER = "player";
    public static final String KEY_ISSPELLCASTER = "isspellcaster";

    //Stats
    public static final String KEY_STRENGTH = "strength";
    public static final String KEY_DEXTERITY = "dexterity";
    public static final String KEY_CONSTITUTION = "constitution";
    public static final String KEY_INTELLIGENCE = "intelligence";
    public static final String KEY_WISDOM = "wisdom";
    public static final String KEY_CHARISMA = "charisma";
    //Status effects
    private static final String KEY_BLINDED = "blinded";
    private static final String KEY_CHARMED = "charmed";
    private static final String KEY_DEAFENED = "deafened";
    private static final String KEY_FRIGHTENED = "frightened";
    private static final String KEY_GRAPPLED = "grappled";
    private static final String KEY_INCAPACITATED = "incapacitated";
    private static final String KEY_INVISIBLE = "invisible";
    private static final String KEY_PARALYZED = "paralyzed";
    private static final String KEY_PETRIFIED = "petrified";
    private static final String KEY_POISONED = "poisoned";
    private static final String KEY_PRONE = "prone";
    private static final String KEY_RESTRAINED = "restrained";
    private static final String KEY_STUNNED = "stunned";
    private static final String KEY_UNCONSCIOUS = "unconscious";
    private static final String KEY_CUSTOM = "custom";


    public static final String KEY_VOCAL = "vocal";
    public static final String KEY_SOMATIC = "somatic";
    public static final String KEY_MATERIAL = "material";
    public static final String KEY_TIME = "time";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_SCHOOL = "school";
    public static final String KEY_RANGE = "range";
    public static final String KEY_SOURCE = "source";
    public static final String KEY_WIZARD = "wizard";
    public static final String KEY_SORCERER = "sorcerer";
    public static final String KEY_BARD = "bard";
    public static final String KEY_DRUID = "druid";
    public static final String KEY_CLERIC = "cleric";
    public static final String KEY_PALADIN = "paladin";
    public static final String KEY_RANGER = "ranger";
    public static final String KEY_WARLOCK = "warlock";
    public static final String KEY_ISRITUAL = "isritual";
    public static final String KEY_MATERIALS = "materials";





    public static final String[] PROJECTION_ENCOUNTERPREP = new String[]{
            DataBaseHandler.KEY_ROWID,
            DataBaseHandler.KEY_NAME,
            DataBaseHandler.KEY_INITIATIVEBONUS,
            DataBaseHandler.KEY_MAXHP,
            DataBaseHandler.KEY_AC,
            DataBaseHandler.KEY_AC2,
            DataBaseHandler.KEY_STRENGTH,
            DataBaseHandler.KEY_DEXTERITY,
            DataBaseHandler.KEY_CONSTITUTION,
            DataBaseHandler.KEY_INTELLIGENCE,
            DataBaseHandler.KEY_WISDOM,
            DataBaseHandler.KEY_CHARISMA,
            DataBaseHandler.KEY_ICON,
            DataBaseHandler.KEY_IDENTIFIER,
            DataBaseHandler.KEY_TYPE,
            DataBaseHandler.KEY_ACTYPE,
            DataBaseHandler.KEY_AC2TYPE,
            DataBaseHandler.KEY_XP,
            DataBaseHandler.KEY_SIZE,
            DataBaseHandler.KEY_SPEED,
            DataBaseHandler.KEY_SOURCE,
            DataBaseHandler.KEY_SOURCEPAGE,
            DataBaseHandler.KEY_MULTIATTACK,
            DataBaseHandler.KEY_ATK1NAME,
            DataBaseHandler.KEY_ATK1DESC,
            DataBaseHandler.KEY_ATK1MOD,
            DataBaseHandler.KEY_ATK1DMG1ROLL,
            DataBaseHandler.KEY_ATK1DMG1TYPE,
            DataBaseHandler.KEY_ATK1DMG2ROLL,
            DataBaseHandler.KEY_ATK1DMG2TYPE,
            DataBaseHandler.KEY_ATK1AUTOROLL,
            DataBaseHandler.KEY_ATK1ADDITIONAL,
            DataBaseHandler.KEY_ATK2NAME,
            DataBaseHandler.KEY_ATK2DESC,
            DataBaseHandler.KEY_ATK2MOD,
            DataBaseHandler.KEY_ATK2DMG1ROLL,
            DataBaseHandler.KEY_ATK2DMG1TYPE,
            DataBaseHandler.KEY_ATK2DMG2ROLL,
            DataBaseHandler.KEY_ATK2DMG2TYPE,
            DataBaseHandler.KEY_ATK2AUTOROLL,
            DataBaseHandler.KEY_ATK2ADDITIONAL,
            DataBaseHandler.KEY_ATK3NAME,
            DataBaseHandler.KEY_ATK3DESC,
            DataBaseHandler.KEY_ATK3MOD,
            DataBaseHandler.KEY_ATK3DMG1ROLL,
            DataBaseHandler.KEY_ATK3DMG1TYPE,
            DataBaseHandler.KEY_ATK3DMG2ROLL,
            DataBaseHandler.KEY_ATK3DMG2TYPE,
            DataBaseHandler.KEY_ATK3AUTOROLL,
            DataBaseHandler.KEY_ATK3ADDITIONAL,
            DataBaseHandler.KEY_ATK4NAME,
            DataBaseHandler.KEY_ATK4DESC,
            DataBaseHandler.KEY_ATK4MOD,
            DataBaseHandler.KEY_ATK4DMG1ROLL,
            DataBaseHandler.KEY_ATK4DMG1TYPE,
            DataBaseHandler.KEY_ATK4DMG2ROLL,
            DataBaseHandler.KEY_ATK4DMG2TYPE,
            DataBaseHandler.KEY_ATK4AUTOROLL,
            DataBaseHandler.KEY_ATK4ADDITIONAL,
            DataBaseHandler.KEY_ATK5NAME,
            DataBaseHandler.KEY_ATK5DESC,
            DataBaseHandler.KEY_ATK5MOD,
            DataBaseHandler.KEY_ATK5DMG1ROLL,
            DataBaseHandler.KEY_ATK5DMG1TYPE,
            DataBaseHandler.KEY_ATK5DMG2ROLL,
            DataBaseHandler.KEY_ATK5DMG2TYPE,
            DataBaseHandler.KEY_ATK5AUTOROLL,
            DataBaseHandler.KEY_ATK5ADDITIONAL,
            DataBaseHandler.KEY_LMULTIATTACK,
            DataBaseHandler.KEY_LATK1NAME,
            DataBaseHandler.KEY_LATK1DESC,
            DataBaseHandler.KEY_LATK1MOD,
            DataBaseHandler.KEY_LATK1DMG1ROLL,
            DataBaseHandler.KEY_LATK1DMG1TYPE,
            DataBaseHandler.KEY_LATK1DMG2ROLL,
            DataBaseHandler.KEY_LATK1DMG2TYPE,
            DataBaseHandler.KEY_LATK1AUTOROLL,
            DataBaseHandler.KEY_LATK1ADDITIONAL,
            DataBaseHandler.KEY_LATK2NAME,
            DataBaseHandler.KEY_LATK2DESC,
            DataBaseHandler.KEY_LATK2MOD,
            DataBaseHandler.KEY_LATK2DMG1ROLL,
            DataBaseHandler.KEY_LATK2DMG1TYPE,
            DataBaseHandler.KEY_LATK2DMG2ROLL,
            DataBaseHandler.KEY_LATK2DMG2TYPE,
            DataBaseHandler.KEY_LATK2AUTOROLL,
            DataBaseHandler.KEY_LATK2ADDITIONAL,
            DataBaseHandler.KEY_LATK3NAME,
            DataBaseHandler.KEY_LATK3DESC,
            DataBaseHandler.KEY_LATK3MOD,
            DataBaseHandler.KEY_LATK3DMG1ROLL,
            DataBaseHandler.KEY_LATK3DMG1TYPE,
            DataBaseHandler.KEY_LATK3DMG2ROLL,
            DataBaseHandler.KEY_LATK3DMG2TYPE,
            DataBaseHandler.KEY_LATK3AUTOROLL,
            DataBaseHandler.KEY_LATK3ADDITIONAL,
            DataBaseHandler.KEY_LATK4NAME,
            DataBaseHandler.KEY_LATK4DESC,
            DataBaseHandler.KEY_LATK4MOD,
            DataBaseHandler.KEY_LATK4DMG1ROLL,
            DataBaseHandler.KEY_LATK4DMG1TYPE,
            DataBaseHandler.KEY_LATK4DMG2ROLL,
            DataBaseHandler.KEY_LATK4DMG2TYPE,
            DataBaseHandler.KEY_LATK4AUTOROLL,
            DataBaseHandler.KEY_LATK4ADDITIONAL,
            DataBaseHandler.KEY_LATK5NAME,
            DataBaseHandler.KEY_LATK5DESC,
            DataBaseHandler.KEY_LATK5MOD,
            DataBaseHandler.KEY_LATK5DMG1ROLL,
            DataBaseHandler.KEY_LATK5DMG1TYPE,
            DataBaseHandler.KEY_LATK5DMG2ROLL,
            DataBaseHandler.KEY_LATK5DMG2TYPE,
            DataBaseHandler.KEY_LATK5AUTOROLL,
            DataBaseHandler.KEY_LATK5ADDITIONAL,
            DataBaseHandler.KEY_REACTION1NAME,
            DataBaseHandler.KEY_REACTION1DESC,
            DataBaseHandler.KEY_ABILITY1NAME,
            DataBaseHandler.KEY_ABILITY1DESC,
            DataBaseHandler.KEY_ABILITY2NAME,
            DataBaseHandler.KEY_ABILITY2DESC,
            DataBaseHandler.KEY_ABILITY3NAME,
            DataBaseHandler.KEY_ABILITY3DESC,
            DataBaseHandler.KEY_ABILITY4NAME,
            DataBaseHandler.KEY_ABILITY4DESC,
            DataBaseHandler.KEY_ABILITY5NAME,
            DataBaseHandler.KEY_ABILITY5DESC,
            DataBaseHandler.KEY_ACROBATICS,
            DataBaseHandler.KEY_ANIMALHANDLING,
            DataBaseHandler.KEY_ARCANA,
            DataBaseHandler.KEY_ATHLETICS,
            DataBaseHandler.KEY_DECEPTION,
            DataBaseHandler.KEY_HISTORY,
            DataBaseHandler.KEY_INSIGHT,
            DataBaseHandler.KEY_INTIMIDATION,
            DataBaseHandler.KEY_INVESTIGATION,
            DataBaseHandler.KEY_MEDICINE,
            DataBaseHandler.KEY_NATURE,
            DataBaseHandler.KEY_PERCEPTION,
            DataBaseHandler.KEY_PERFORMANCE,
            DataBaseHandler.KEY_PERSUASION,
            DataBaseHandler.KEY_RELIGION,
            DataBaseHandler.KEY_SLEIGHTOFHAND,
            DataBaseHandler.KEY_STEALTH,
            DataBaseHandler.KEY_SURVIVAL,
            DataBaseHandler.KEY_STSTR,
            DataBaseHandler.KEY_STDEX,
            DataBaseHandler.KEY_STCON,
            DataBaseHandler.KEY_STINT,
            DataBaseHandler.KEY_STWIS,
            DataBaseHandler.KEY_STCHA,
            DataBaseHandler.KEY_SENSES,
            DataBaseHandler.KEY_ALIGNMENT,
            DataBaseHandler.KEY_LANGUAGES,
            DataBaseHandler.KEY_DMGRES,
            DataBaseHandler.KEY_DMGIM,
            DataBaseHandler.KEY_DMGVUL,
            DataBaseHandler.KEY_CONIM,
            DataBaseHandler.KEY_SPELLCASTER,
            DataBaseHandler.KEY_SCLEVEL,
            DataBaseHandler.KEY_SCABILITY,
            DataBaseHandler.KEY_SCMOD,
            DataBaseHandler.KEY_SCDC,
            DataBaseHandler.KEY_SCCLASS,
            DataBaseHandler.KEY_SCSLOTS,
            DataBaseHandler.KEY_SCSPELLS,
            DataBaseHandler.KEY_INNATE,
            DataBaseHandler.KEY_INABILITY,
            DataBaseHandler.KEY_INMOD,
            DataBaseHandler.KEY_INDC,
            DataBaseHandler.KEY_INPSIONICS,
            DataBaseHandler.KEY_INSPELLS
    };



    public static final String[] PROJECTION_MONSTER_TEMPLATE = new String[]{
            DataBaseHandler.KEY_ROWID,
            DataBaseHandler.KEY_NAME,
            DataBaseHandler.KEY_CR,
            DataBaseHandler.KEY_IDENTIFIER,
            DataBaseHandler.KEY_TYPE,
            DataBaseHandler.KEY_MAXHP,
            DataBaseHandler.KEY_HPROLL,
            DataBaseHandler.KEY_INITIATIVEBONUS,
            DataBaseHandler.KEY_AC,
            DataBaseHandler.KEY_ACTYPE,
            DataBaseHandler.KEY_AC2,
            DataBaseHandler.KEY_AC2TYPE,
            DataBaseHandler.KEY_XP,
            DataBaseHandler.KEY_SIZE,
            DataBaseHandler.KEY_SPEED,
            DataBaseHandler.KEY_STRENGTH,
            DataBaseHandler.KEY_DEXTERITY,
            DataBaseHandler.KEY_CONSTITUTION,
            DataBaseHandler.KEY_INTELLIGENCE,
            DataBaseHandler.KEY_WISDOM,
            DataBaseHandler.KEY_CHARISMA,
            DataBaseHandler.KEY_SOURCE,
            DataBaseHandler.KEY_SOURCEPAGE,
            DataBaseHandler.KEY_MULTIATTACK,
            DataBaseHandler.KEY_ATK1NAME,
            DataBaseHandler.KEY_ATK1DESC,
            DataBaseHandler.KEY_ATK1MOD,
            DataBaseHandler.KEY_ATK1DMG1ROLL,
            DataBaseHandler.KEY_ATK1DMG1TYPE,
            DataBaseHandler.KEY_ATK1DMG2ROLL,
            DataBaseHandler.KEY_ATK1DMG2TYPE,
            DataBaseHandler.KEY_ATK1AUTOROLL,
            DataBaseHandler.KEY_ATK1ADDITIONAL,
            DataBaseHandler.KEY_ATK2NAME,
            DataBaseHandler.KEY_ATK2DESC,
            DataBaseHandler.KEY_ATK2MOD,
            DataBaseHandler.KEY_ATK2DMG1ROLL,
            DataBaseHandler.KEY_ATK2DMG1TYPE,
            DataBaseHandler.KEY_ATK2DMG2ROLL,
            DataBaseHandler.KEY_ATK2DMG2TYPE,
            DataBaseHandler.KEY_ATK2AUTOROLL,
            DataBaseHandler.KEY_ATK2ADDITIONAL,
            DataBaseHandler.KEY_ATK3NAME,
            DataBaseHandler.KEY_ATK3DESC,
            DataBaseHandler.KEY_ATK3MOD,
            DataBaseHandler.KEY_ATK3DMG1ROLL,
            DataBaseHandler.KEY_ATK3DMG1TYPE,
            DataBaseHandler.KEY_ATK3DMG2ROLL,
            DataBaseHandler.KEY_ATK3DMG2TYPE,
            DataBaseHandler.KEY_ATK3AUTOROLL,
            DataBaseHandler.KEY_ATK3ADDITIONAL,
            DataBaseHandler.KEY_ATK4NAME,
            DataBaseHandler.KEY_ATK4DESC,
            DataBaseHandler.KEY_ATK4MOD,
            DataBaseHandler.KEY_ATK4DMG1ROLL,
            DataBaseHandler.KEY_ATK4DMG1TYPE,
            DataBaseHandler.KEY_ATK4DMG2ROLL,
            DataBaseHandler.KEY_ATK4DMG2TYPE,
            DataBaseHandler.KEY_ATK4AUTOROLL,
            DataBaseHandler.KEY_ATK4ADDITIONAL,
            DataBaseHandler.KEY_ATK5NAME,
            DataBaseHandler.KEY_ATK5DESC,
            DataBaseHandler.KEY_ATK5MOD,
            DataBaseHandler.KEY_ATK5DMG1ROLL,
            DataBaseHandler.KEY_ATK5DMG1TYPE,
            DataBaseHandler.KEY_ATK5DMG2ROLL,
            DataBaseHandler.KEY_ATK5DMG2TYPE,
            DataBaseHandler.KEY_ATK5AUTOROLL,
            DataBaseHandler.KEY_ATK5ADDITIONAL,
            DataBaseHandler.KEY_LMULTIATTACK,
            DataBaseHandler.KEY_LATK1NAME,
            DataBaseHandler.KEY_LATK1DESC,
            DataBaseHandler.KEY_LATK1MOD,
            DataBaseHandler.KEY_LATK1DMG1ROLL,
            DataBaseHandler.KEY_LATK1DMG1TYPE,
            DataBaseHandler.KEY_LATK1DMG2ROLL,
            DataBaseHandler.KEY_LATK1DMG2TYPE,
            DataBaseHandler.KEY_LATK1AUTOROLL,
            DataBaseHandler.KEY_LATK1ADDITIONAL,
            DataBaseHandler.KEY_LATK2NAME,
            DataBaseHandler.KEY_LATK2DESC,
            DataBaseHandler.KEY_LATK2MOD,
            DataBaseHandler.KEY_LATK2DMG1ROLL,
            DataBaseHandler.KEY_LATK2DMG1TYPE,
            DataBaseHandler.KEY_LATK2DMG2ROLL,
            DataBaseHandler.KEY_LATK2DMG2TYPE,
            DataBaseHandler.KEY_LATK2AUTOROLL,
            DataBaseHandler.KEY_LATK2ADDITIONAL,
            DataBaseHandler.KEY_LATK3NAME,
            DataBaseHandler.KEY_LATK3DESC,
            DataBaseHandler.KEY_LATK3MOD,
            DataBaseHandler.KEY_LATK3DMG1ROLL,
            DataBaseHandler.KEY_LATK3DMG1TYPE,
            DataBaseHandler.KEY_LATK3DMG2ROLL,
            DataBaseHandler.KEY_LATK3DMG2TYPE,
            DataBaseHandler.KEY_LATK3AUTOROLL,
            DataBaseHandler.KEY_LATK3ADDITIONAL,
            DataBaseHandler.KEY_LATK4NAME,
            DataBaseHandler.KEY_LATK4DESC,
            DataBaseHandler.KEY_LATK4MOD,
            DataBaseHandler.KEY_LATK4DMG1ROLL,
            DataBaseHandler.KEY_LATK4DMG1TYPE,
            DataBaseHandler.KEY_LATK4DMG2ROLL,
            DataBaseHandler.KEY_LATK4DMG2TYPE,
            DataBaseHandler.KEY_LATK4AUTOROLL,
            DataBaseHandler.KEY_LATK4ADDITIONAL,
            DataBaseHandler.KEY_LATK5NAME,
            DataBaseHandler.KEY_LATK5DESC,
            DataBaseHandler.KEY_LATK5MOD,
            DataBaseHandler.KEY_LATK5DMG1ROLL,
            DataBaseHandler.KEY_LATK5DMG1TYPE,
            DataBaseHandler.KEY_LATK5DMG2ROLL,
            DataBaseHandler.KEY_LATK5DMG2TYPE,
            DataBaseHandler.KEY_LATK5AUTOROLL,
            DataBaseHandler.KEY_LATK5ADDITIONAL,
            DataBaseHandler.KEY_REACTION1NAME,
            DataBaseHandler.KEY_REACTION1DESC,
            DataBaseHandler.KEY_REACTION2NAME,
            DataBaseHandler.KEY_REACTION2DESC,
            DataBaseHandler.KEY_REACTION3NAME,
            DataBaseHandler.KEY_REACTION3DESC,
            DataBaseHandler.KEY_REACTION4NAME,
            DataBaseHandler.KEY_REACTION4DESC,
            DataBaseHandler.KEY_REACTION5NAME,
            DataBaseHandler.KEY_REACTION5DESC,
            DataBaseHandler.KEY_ABILITY1NAME,
            DataBaseHandler.KEY_ABILITY1DESC,
            DataBaseHandler.KEY_ABILITY2NAME,
            DataBaseHandler.KEY_ABILITY2DESC,
            DataBaseHandler.KEY_ABILITY3NAME,
            DataBaseHandler.KEY_ABILITY3DESC,
            DataBaseHandler.KEY_ABILITY4NAME,
            DataBaseHandler.KEY_ABILITY4DESC,
            DataBaseHandler.KEY_ABILITY5NAME,
            DataBaseHandler.KEY_ABILITY5DESC,
            DataBaseHandler.KEY_ACROBATICS,
            DataBaseHandler.KEY_ANIMALHANDLING,
            DataBaseHandler.KEY_ARCANA,
            DataBaseHandler.KEY_ATHLETICS,
            DataBaseHandler.KEY_DECEPTION,
            DataBaseHandler.KEY_HISTORY,
            DataBaseHandler.KEY_INSIGHT,
            DataBaseHandler.KEY_INTIMIDATION,
            DataBaseHandler.KEY_INVESTIGATION,
            DataBaseHandler.KEY_MEDICINE,
            DataBaseHandler.KEY_NATURE,
            DataBaseHandler.KEY_PERCEPTION,
            DataBaseHandler.KEY_PERFORMANCE,
            DataBaseHandler.KEY_PERSUASION,
            DataBaseHandler.KEY_RELIGION,
            DataBaseHandler.KEY_SLEIGHTOFHAND,
            DataBaseHandler.KEY_STEALTH,
            DataBaseHandler.KEY_SURVIVAL,
            DataBaseHandler.KEY_STSTR,
            DataBaseHandler.KEY_STDEX,
            DataBaseHandler.KEY_STCON,
            DataBaseHandler.KEY_STINT,
            DataBaseHandler.KEY_STWIS,
            DataBaseHandler.KEY_STCHA,
            DataBaseHandler.KEY_SENSES,
            DataBaseHandler.KEY_ALIGNMENT,
            DataBaseHandler.KEY_LANGUAGES,
            DataBaseHandler.KEY_DMGRES,
            DataBaseHandler.KEY_DMGIM,
            DataBaseHandler.KEY_DMGVUL,
            DataBaseHandler.KEY_CONIM,
            DataBaseHandler.KEY_ICON,
            DataBaseHandler.KEY_MM,
            DataBaseHandler.KEY_SPELLCASTER,
            DataBaseHandler.KEY_SCLEVEL,
            DataBaseHandler.KEY_SCABILITY,
            DataBaseHandler.KEY_SCMOD,
            DataBaseHandler.KEY_SCDC,
            DataBaseHandler.KEY_SCCLASS,
            DataBaseHandler.KEY_SCSLOTS,
            DataBaseHandler.KEY_SCSPELLS,
            DataBaseHandler.KEY_INNATE,
            DataBaseHandler.KEY_INABILITY,
            DataBaseHandler.KEY_INMOD,
            DataBaseHandler.KEY_INDC,
            DataBaseHandler.KEY_INPSIONICS,
            DataBaseHandler.KEY_INSPELLS
    };


    public static final String[] PROJECTION_NPC_TEMPLATE = new String[]{
            DataBaseHandler.KEY_ROWID,
            DataBaseHandler.KEY_NAME,
            DataBaseHandler.KEY_INFO,
            DataBaseHandler.KEY_INITIATIVEBONUS,
            DataBaseHandler.KEY_MAXHP,
            DataBaseHandler.KEY_AC,
            DataBaseHandler.KEY_STRENGTH,
            DataBaseHandler.KEY_DEXTERITY,
            DataBaseHandler.KEY_CONSTITUTION,
            DataBaseHandler.KEY_INTELLIGENCE,
            DataBaseHandler.KEY_WISDOM,
            DataBaseHandler.KEY_CHARISMA,
            DataBaseHandler.KEY_ICON,
            DataBaseHandler.KEY_IDENTIFIER,
            DataBaseHandler.KEY_ACTIONS,
            DataBaseHandler.KEY_ABILITIES,
            DataBaseHandler.KEY_BELONGSTO
    };

    public static final String[] PROJECTION_ENCOUNTER = new String[]{
            DataBaseHandler.KEY_ROWID,
            DataBaseHandler.KEY_NAME,
            DataBaseHandler.KEY_INFO
    };

    public static final String[] PROJECTION_SESSION = new String[]{
            DataBaseHandler.KEY_ROWID,
            DataBaseHandler.KEY_NAME,
            DataBaseHandler.KEY_INFO
    };

    public static final String[] PROJECTION_PC = new String[]{
            DataBaseHandler.KEY_ROWID,
            DataBaseHandler.KEY_NAME,
            DataBaseHandler.KEY_INFO,
            DataBaseHandler.KEY_CLASS,
            DataBaseHandler.KEY_LEVEL,
            DataBaseHandler.KEY_PLAYER,
            DataBaseHandler.KEY_INITIATIVEBONUS,
            DataBaseHandler.KEY_HP,
            DataBaseHandler.KEY_MAXHP,
            DataBaseHandler.KEY_IDENTIFIER,
            DataBaseHandler.KEY_AC,
            DataBaseHandler.KEY_ICON,
            DataBaseHandler.KEY_DISABLED
    };

    public static final String[] PROJECTION_SPELL = new String[]{
            DataBaseHandler.KEY_ROWID,
            DataBaseHandler.KEY_NAME,
            DataBaseHandler.KEY_LEVEL,
            DataBaseHandler.KEY_VOCAL,
            DataBaseHandler.KEY_SOMATIC,
            DataBaseHandler.KEY_MATERIAL,
            DataBaseHandler.KEY_TIME,
            DataBaseHandler.KEY_DURATION,
            DataBaseHandler.KEY_SCHOOL,
            DataBaseHandler.KEY_RANGE,
            DataBaseHandler.KEY_INFO,
            DataBaseHandler.KEY_SOURCE,
            DataBaseHandler.KEY_WIZARD,
            DataBaseHandler.KEY_SORCERER,
            DataBaseHandler.KEY_BARD,
            DataBaseHandler.KEY_DRUID,
            DataBaseHandler.KEY_CLERIC,
            DataBaseHandler.KEY_PALADIN,
            DataBaseHandler.KEY_RANGER,
            DataBaseHandler.KEY_WARLOCK,
            DataBaseHandler.KEY_ISRITUAL,
            DataBaseHandler.KEY_MATERIALS
    };




    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*@Override
    public void onCreate(SQLiteDatabase db) {
        //Create campaign table
        String CREATE_CAMPAIGN_TABLE =
                "CREATE TABLE if not exists " + TABLE_CAMPAIGN + " (" +
                        KEY_ROWID + " integer PRIMARY KEY autoincrement, " +
                        KEY_NAME + " TEXT, " +
                        KEY_INFO + " TEXT);";
        db.execSQL(CREATE_CAMPAIGN_TABLE);

        //Create session table
        String CREATE_SESSION_TABLE =
                "CREATE TABLE if not exists " + TABLE_SESSION + " (" +
                        KEY_ROWID + " integer PRIMARY KEY autoincrement, " +
                        KEY_NAME + " TEXT, " +
                        KEY_INFO + " TEXT, " +
                        KEY_BELONGSTO + " integer NOT NULL, " +
                        "FOREIGN KEY (" + KEY_BELONGSTO + ") REFERENCES " + TABLE_CAMPAIGN + "(" + KEY_ROWID + ") ON DELETE CASCADE);";
        db.execSQL(CREATE_SESSION_TABLE);

        //Create encounter table
        String CREATE_ENCOUNTER_TABLE =
                "CREATE TABLE if not exists " + TABLE_ENCOUNTER + " (" +
                        KEY_ROWID + " integer PRIMARY KEY autoincrement, " +
                        KEY_NAME + " TEXT, " +
                        KEY_INFO + " TEXT, " +
                        KEY_CR + " INTEGER, " +
                        KEY_XP + " DOUBLE, " +
                        KEY_HP + " INTEGER, " +
                        KEY_AC + " INTEGER, " +
                        KEY_BELONGSTO + " integer NOT NULL, " +
                        "FOREIGN KEY (" + KEY_BELONGSTO + ") REFERENCES " + TABLE_SESSION + "(" + KEY_ROWID + ") ON DELETE CASCADE);";
        db.execSQL(CREATE_ENCOUNTER_TABLE);


        String CREATE_ENCOUNTERPREP_LINKER_TABLE =
                "CREATE TABLE if not exists " + TABLE_ENCOUNTER_PREP_LINKER + " (" +
                        KEY_ROWID + " integer PRIMARY KEY autoincrement, " +
                        KEY_IDENTIFIER + " INTEGER, " +
                        KEY_BELONGSTO + " integer NOT NULL);";
        db.execSQL(CREATE_ENCOUNTERPREP_LINKER_TABLE);


        String CREATE_ENCOUNTERPREP_TABLE =
                "CREATE TABLE if not exists " + TABLE_ENCOUNTER_PREP + " (" +
                        KEY_ROWID + " integer PRIMARY KEY autoincrement, " +
                        KEY_NAME + " TEXT, " +
                        KEY_INFO + " TEXT, " +
                        KEY_HP + " INTEGER, " +
                        KEY_MAXHP + " INTEGER, " +
                        KEY_AC + " INTEGER, " +
                        KEY_CR + " INTEGER, " +
                        KEY_XP + " DOUBLE, " +
                        KEY_STRENGTH + " INTEGER, " +
                        KEY_DEXTERITY + " INTEGER, " +
                        KEY_CONSTITUTION + " INTEGER, " +
                        KEY_INTELLIGENCE + " INTEGER, " +
                        KEY_WISDOM + " INTEGER, " +
                        KEY_CHARISMA + " INTEGER, " +
                        KEY_INITIATIVEBONUS + " INTEGER, " +
                        KEY_IDENTIFIER + " INTEGER, " +
                        KEY_ICON + " TEXT, " +
                        KEY_BELONGSTO + " integer NOT NULL, " +
                        "FOREIGN KEY (" + KEY_BELONGSTO + ") REFERENCES " + TABLE_ENCOUNTER + "(" + KEY_ROWID + ") ON DELETE CASCADE);";
        db.execSQL(CREATE_ENCOUNTERPREP_TABLE);


        //Create Monster table
        String CREATE_MONSTER_TABLE =
                "CREATE TABLE if not exists " + TABLE_MONSTER + " (" +
                        KEY_ROWID + " integer PRIMARY KEY autoincrement, " +
                        KEY_NAME + " TEXT, " +
                        KEY_INFO + " TEXT, " +
                        KEY_HP + " INTEGER, " +
                        KEY_MAXHP + " INTEGER, " +
                        KEY_AC + " INTEGER, " +
                        KEY_CR + " INTEGER, " +
                        KEY_XP + " DOUBLE, " +
                        KEY_STRENGTH + " INTEGER, " +
                        KEY_DEXTERITY + " INTEGER, " +
                        KEY_CONSTITUTION + " INTEGER, " +
                        KEY_INTELLIGENCE + " INTEGER, " +
                        KEY_WISDOM + " INTEGER, " +
                        KEY_CHARISMA + " INTEGER, " +
                        KEY_INITIATIVEBONUS + " INTEGER, " +
                        KEY_IDENTIFIER + " INTEGER, " +
                        KEY_ICON + " TEXT, " +
                        KEY_MM + " INTEGER);";
        db.execSQL(CREATE_MONSTER_TABLE);


        //Create NPC table
        String CREATE_NPC_TABLE =
                "CREATE TABLE if not exists " + TABLE_NPC + " (" +
                        KEY_ROWID + " integer PRIMARY KEY autoincrement, " +
                        KEY_NAME + " TEXT, " +
                        KEY_INFO + " TEXT, " +
                        KEY_HP + " INTEGER, " +
                        KEY_MAXHP + " INTEGER, " +
                        KEY_AC + " INTEGER, " +
                        KEY_CR + " INTEGER, " +
                        KEY_XP + " DOUBLE, " +
                        KEY_STRENGTH + " INTEGER, " +
                        KEY_DEXTERITY + " INTEGER, " +
                        KEY_CONSTITUTION + " INTEGER, " +
                        KEY_INTELLIGENCE + " INTEGER, " +
                        KEY_WISDOM + " INTEGER, " +
                        KEY_CHARISMA + " INTEGER, " +
                        KEY_INITIATIVEBONUS + " INTEGER, " +
                        KEY_IDENTIFIER + " INTEGER, " +
                        KEY_ICON + " TEXT, " +
                        KEY_BELONGSTO + " integer NOT NULL, " +
                        "FOREIGN KEY (" + KEY_BELONGSTO + ") REFERENCES " + TABLE_CAMPAIGN + "(" + KEY_ROWID + ") ON DELETE CASCADE);";
        db.execSQL(CREATE_NPC_TABLE);


        //Create PC table
        String CREATE_PC_TABLE =
                "CREATE TABLE if not exists " + TABLE_PC + " (" +
                        KEY_ROWID + " integer PRIMARY KEY autoincrement, " +
                        KEY_NAME + " TEXT, " +
                        KEY_CLASS + " TEXT, " +
                        KEY_LEVEL + " INTEGER, " +
                        KEY_PLAYER + " TEXT, " +
                        KEY_HP + " INTEGER, " +
                        KEY_MAXHP + " INTEGER, " +
                        KEY_AC + " INTEGER, " +
                        KEY_STRENGTH + " INTEGER, " +
                        KEY_DEXTERITY + " INTEGER, " +
                        KEY_CONSTITUTION + " INTEGER, " +
                        KEY_INTELLIGENCE + " INTEGER, " +
                        KEY_WISDOM + " INTEGER, " +
                        KEY_CHARISMA + " INTEGER, " +
                        KEY_BLINDED + " INTEGER, " +
                        KEY_CHARMED + " INTEGER, " +
                        KEY_DEAFENED + " INTEGER, " +
                        KEY_FRIGHTENED + " INTEGER, " +
                        KEY_GRAPPLED + " INTEGER, " +
                        KEY_INCAPACITATED + " INTEGER, " +
                        KEY_INVISIBLE + " INTEGER, " +
                        KEY_PARALYZED + " INTEGER, " +
                        KEY_PETRIFIED + " INTEGER, " +
                        KEY_POISONED + " INTEGER, " +
                        KEY_PRONE + " INTEGER, " +
                        KEY_RESTRAINED + " INTEGER, " +
                        KEY_STUNNED + " INTEGER, " +
                        KEY_UNCONSCIOUS + " INTEGER, " +
                        KEY_CUSTOM + " INTEGER, " +
                        KEY_INFO + " TEXT, " +
                        KEY_INITIATIVEBONUS + " INTEGER, " +
                        KEY_IDENTIFIER + " INTEGER, " +
                        KEY_ICON + " TEXT, " +
                        KEY_DISABLED + " INTEGER, " +
                        KEY_BELONGSTO + " integer NOT NULL, " +
                        "FOREIGN KEY (" + KEY_BELONGSTO + ") REFERENCES " + TABLE_CAMPAIGN + "(" + KEY_ROWID + ") ON DELETE CASCADE);";
        db.execSQL(CREATE_PC_TABLE);



    } */

    @Override
    public void onOpen(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
        //db.execSQL(" PRAGMA foreign_keys = ON ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAMPAIGN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENCOUNTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NPC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONSTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENCOUNTER_PREP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPELL);

        // Create tables again
        onCreate(db);
    }
}
