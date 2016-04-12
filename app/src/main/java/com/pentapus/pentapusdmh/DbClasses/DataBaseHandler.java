package com.pentapus.pentapusdmh.DbClasses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Koni on 27.02.2016.
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    public static final int TYPE_NPC = 1;
    public static final int TYPE_PC = 2;

    // Database Name
    private static final String DATABASE_NAME = "ddDatabase";

    public static final String TABLE_CAMPAIGN = "campaign";
    public static final String TABLE_SESSION = "session";
    public static final String TABLE_ENCOUNTER = "encounter";
    public static final String TABLE_NPC = "npc";
    public static final String TABLE_PC = "pc";


    //All Tables
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_INFO = "info";
    public static final String KEY_BELONGSTO = "belongsto";
    public static final String KEY_ICON = "icon";


    //Encounter & NPC Table
    private static final String KEY_CR = "cr";
    private static final String KEY_XP = "xp";
    public static final String KEY_INITIATIVEBONUS = "init";


    //PC & NPC Table
    public static final String KEY_MAXHP = "maxHp";
    private static final String KEY_HP = "hp";
    public static final String KEY_AC = "ac";
    public static final String KEY_TYPE = "type";

    //PC Table
    private static final String KEY_CLASS = "class";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_PLAYER = "player";
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




    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
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

        //Create NPC table
        String CREATE_NPC_TABLE =
                "CREATE TABLE if not exists " + TABLE_NPC + " (" +
                        KEY_ROWID + " integer PRIMARY KEY autoincrement, " +
                        KEY_NAME + " TEXT, " +
                        KEY_INFO + " TEXT, " +
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
                        KEY_TYPE + " INTEGER, " +
                        KEY_ICON + " TEXT, " +
                        KEY_BELONGSTO + " integer NOT NULL, " +
                        "FOREIGN KEY (" + KEY_BELONGSTO + ") REFERENCES " + TABLE_ENCOUNTER + "(" + KEY_ROWID + ") ON DELETE CASCADE);";
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
                        KEY_TYPE + " INTEGER, " +
                        KEY_ICON + " TEXT, " +
                        KEY_BELONGSTO + " integer NOT NULL, " +
                        "FOREIGN KEY (" + KEY_BELONGSTO + ") REFERENCES " + TABLE_CAMPAIGN + "(" + KEY_ROWID + ") ON DELETE CASCADE);";
        db.execSQL(CREATE_PC_TABLE);



    }

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
        // Create tables again
        onCreate(db);
    }
}