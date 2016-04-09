package com.pentapus.pentapusdmh.DbClasses;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;

import java.sql.SQLException;

/**
 * Created by Koni on 27.02.2016.
 */
public class DbContentProvider extends ContentProvider{

    private DataBaseHandler dbHandler;

    private static final int ALL_CAMPAIGNS = 1;
    private static final int SINGLE_CAMPAIGN = 2;
    private static final int ALL_SESSIONS = 3;
    private static final int SINGLE_SESSION = 4;
    private static final int ALL_ENCOUNTERS = 5;
    private static final int SINGLE_ENCOUNTER = 6;
    private static final int ALL_NPCS = 7;
    private static final int SINGLE_NPC = 8;
    private static final int ALL_PCS = 9;
    private static final int SINGLE_PC = 10;
    private static final int COPY = 11;

    // authority is the symbolic name of your provider
    // To avoid conflicts with other providers, you should use
    // Internet domain ownership (in reverse) as the basis of your provider authority.
    private static final String AUTHORITY = "com.pentapus.pentapusdmh.contentprovider";

    // create content URIs from the authority by appending path to database table
    public static final Uri CONTENT_URI_CAMPAIGN =
            Uri.parse("content://" + AUTHORITY + "/campaign");
    public static final Uri CONTENT_URI_SESSION =
            Uri.parse("content://" + AUTHORITY + "/session");
    public static final Uri CONTENT_URI_ENCOUNTER =
            Uri.parse("content://" + AUTHORITY + "/encounter");
    public static final Uri CONTENT_URI_NPC =
            Uri.parse("content://" + AUTHORITY + "/npc");
    public static final Uri CONTENT_URI_PC =
            Uri.parse("content://" + AUTHORITY + "/pc");
    public static final Uri CONTENT_URI_COPY =
            Uri.parse("content://" + AUTHORITY + "/copy");

    // a content URI pattern matches content URIs using wildcard characters:
    // *: Matches a string of any valid characters of any length.
    // #: Matches a string of numeric characters of any length.
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "campaign", ALL_CAMPAIGNS);
        uriMatcher.addURI(AUTHORITY, "campaign/#", SINGLE_CAMPAIGN);
        uriMatcher.addURI(AUTHORITY, "session", ALL_SESSIONS);
        uriMatcher.addURI(AUTHORITY, "session/#", SINGLE_SESSION);
        uriMatcher.addURI(AUTHORITY, "encounter", ALL_ENCOUNTERS);
        uriMatcher.addURI(AUTHORITY, "encounter/#", SINGLE_ENCOUNTER);
        uriMatcher.addURI(AUTHORITY, "npc", ALL_NPCS);
        uriMatcher.addURI(AUTHORITY, "npc/#", SINGLE_NPC);
        uriMatcher.addURI(AUTHORITY, "pc", ALL_PCS);
        uriMatcher.addURI(AUTHORITY, "pc/#", SINGLE_PC);
        uriMatcher.addURI(AUTHORITY, "copy/#", COPY);
    }


    @Override
    public boolean onCreate() {
        dbHandler = new DataBaseHandler(getContext());
        return false;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case SINGLE_CAMPAIGN:
                return "vnd.android.cursor.item/vnd.com.pentapus.contentprovider.campaign";
            case SINGLE_SESSION:
                return "vnd.android.cursor.item/vnd.com.pentapus.contentprovider.session";
            case SINGLE_ENCOUNTER:
                return "vnd.android.cursor.item/vnd.com.pentapus.contentprovider.encounter";
            case SINGLE_NPC:
                return "vnd.android.cursor.item/vnd.com.pentapus.contentprovider.npc";
            case SINGLE_PC:
                return "vnd.android.cursor.item/vnd.com.pentapus.contentprovider.pc";
            case COPY:
                return "vnd.android.cursor.item/vnd.com.pentapus.contentprovider.copy";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri _uri = null;
        long KEY_ROWID_CAMPAIGN, KEY_ROWID_SESSION, KEY_ROWID_ENCOUNTER, KEY_ROWID_NPC, KEY_ROWID_PC;
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case ALL_CAMPAIGNS:
                KEY_ROWID_CAMPAIGN = db.insert(DataBaseHandler.TABLE_CAMPAIGN, null, values);
                //if added successfully
                if(KEY_ROWID_CAMPAIGN > 0){
                    _uri = ContentUris.withAppendedId(CONTENT_URI_CAMPAIGN, KEY_ROWID_CAMPAIGN);
                    //_uri = Uri.parse(CONTENT_URI_SESSION + "/" + KEY_ROWID_SESSION);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            case SINGLE_CAMPAIGN:
                KEY_ROWID_CAMPAIGN = db.insert(DataBaseHandler.TABLE_CAMPAIGN, null, values);
                //if added successfully
                if(KEY_ROWID_CAMPAIGN > 0){
                    _uri = ContentUris.withAppendedId(CONTENT_URI_CAMPAIGN, KEY_ROWID_CAMPAIGN);
                    //_uri = Uri.parse(CONTENT_URI_SESSION + "/" + KEY_ROWID_SESSION);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }

                break;
            case ALL_SESSIONS:
                KEY_ROWID_SESSION = db.insert(DataBaseHandler.TABLE_SESSION, null, values);
                //if added successfully
                if(KEY_ROWID_SESSION > 0){
                    _uri = ContentUris.withAppendedId(CONTENT_URI_SESSION, KEY_ROWID_SESSION);
                    //_uri = Uri.parse(CONTENT_URI_SESSION + "/" + KEY_ROWID_SESSION);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            case SINGLE_SESSION:
                KEY_ROWID_SESSION = db.insert(DataBaseHandler.TABLE_SESSION, null, values);
                //if added successfully
                if(KEY_ROWID_SESSION > 0){
                    _uri = ContentUris.withAppendedId(CONTENT_URI_SESSION, KEY_ROWID_SESSION);
                    //_uri = Uri.parse(CONTENT_URI_SESSION + "/" + KEY_ROWID_SESSION);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }

                break;
            case ALL_ENCOUNTERS:
                KEY_ROWID_ENCOUNTER = db.insert(DataBaseHandler.TABLE_ENCOUNTER, null, values);
                //if added successfully
                if(KEY_ROWID_ENCOUNTER > 0){
                    _uri = ContentUris.withAppendedId(CONTENT_URI_ENCOUNTER, KEY_ROWID_ENCOUNTER);
                    //_uri = Uri.parse(CONTENT_URI_SESSION + "/" + KEY_ROWID_SESSION);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            case SINGLE_ENCOUNTER:
                KEY_ROWID_ENCOUNTER = db.insert(DataBaseHandler.TABLE_ENCOUNTER, null, values);
                if(KEY_ROWID_ENCOUNTER > 0){
                    _uri = ContentUris.withAppendedId(CONTENT_URI_ENCOUNTER, KEY_ROWID_ENCOUNTER);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            case ALL_NPCS:
                KEY_ROWID_NPC = db.insert(DataBaseHandler.TABLE_NPC, null, values);
                //if added successfully
                if(KEY_ROWID_NPC > 0){
                    _uri = ContentUris.withAppendedId(CONTENT_URI_NPC, KEY_ROWID_NPC);
                    //_uri = Uri.parse(CONTENT_URI_SESSION + "/" + KEY_ROWID_SESSION);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            case SINGLE_NPC:
                KEY_ROWID_NPC = db.insert(DataBaseHandler.TABLE_NPC, "", values);
                if(KEY_ROWID_NPC > 0){
                    _uri = ContentUris.withAppendedId(CONTENT_URI_NPC, KEY_ROWID_NPC);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            case ALL_PCS:
                KEY_ROWID_PC = db.insert(DataBaseHandler.TABLE_PC, null, values);
                //if added successfully
                if(KEY_ROWID_PC > 0){
                    _uri = ContentUris.withAppendedId(CONTENT_URI_PC, KEY_ROWID_PC);
                    //_uri = Uri.parse(CONTENT_URI_SESSION + "/" + KEY_ROWID_SESSION);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            case SINGLE_PC:
                KEY_ROWID_PC = db.insert(DataBaseHandler.TABLE_PC, "", values);
                if(KEY_ROWID_PC > 0){
                    _uri = ContentUris.withAppendedId(CONTENT_URI_PC, KEY_ROWID_PC);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            default:
                try {
                    throw new SQLException("Failed to insert row into " + uri);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        getContext().getContentResolver().notifyChange(_uri, null, false);
        return _uri;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        String id = null;
        switch (uriMatcher.match(uri)) {
            case ALL_CAMPAIGNS:
                queryBuilder.setTables(DataBaseHandler.TABLE_CAMPAIGN);
                break;
            case SINGLE_CAMPAIGN:
                queryBuilder.setTables(DataBaseHandler.TABLE_CAMPAIGN);
                id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(DataBaseHandler.KEY_ROWID + "=" + id);
                break;
            case ALL_SESSIONS:
                queryBuilder.setTables(DataBaseHandler.TABLE_SESSION);
                break;
            case SINGLE_SESSION:
                queryBuilder.setTables(DataBaseHandler.TABLE_SESSION);
                id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(DataBaseHandler.KEY_ROWID + "=" + id);
                break;
            case ALL_ENCOUNTERS:
                queryBuilder.setTables(DataBaseHandler.TABLE_ENCOUNTER);
                break;
            case SINGLE_ENCOUNTER:
                queryBuilder.setTables(DataBaseHandler.TABLE_ENCOUNTER);
                id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(DataBaseHandler.KEY_ROWID + "=" + id);
                break;
            case ALL_NPCS:
                queryBuilder.setTables(DataBaseHandler.TABLE_NPC);
                break;
            case SINGLE_NPC:
                queryBuilder.setTables(DataBaseHandler.TABLE_NPC);
                id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(DataBaseHandler.KEY_ROWID + "=" + id);
                break;
            case ALL_PCS:
                queryBuilder.setTables(DataBaseHandler.TABLE_PC);
                break;
            case SINGLE_PC:
                queryBuilder.setTables(DataBaseHandler.TABLE_PC);
                id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(DataBaseHandler.KEY_ROWID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);

        }
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        String id;
        Uri _uri = null;
        int deleteCount = 0;
        switch (uriMatcher.match(uri)) {
            case SINGLE_CAMPAIGN:
                id = uri.getPathSegments().get(1);
                selection = DataBaseHandler.KEY_ROWID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                deleteCount = db.delete(DataBaseHandler.TABLE_CAMPAIGN, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
            case SINGLE_SESSION:
                id = uri.getPathSegments().get(1);
                selection = DataBaseHandler.KEY_ROWID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                deleteCount = db.delete(DataBaseHandler.TABLE_SESSION, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case SINGLE_ENCOUNTER:
                id = uri.getPathSegments().get(1);
                selection = DataBaseHandler.KEY_ROWID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                deleteCount = db.delete(DataBaseHandler.TABLE_ENCOUNTER, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case SINGLE_NPC:
                id = uri.getPathSegments().get(1);
                selection = DataBaseHandler.KEY_ROWID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                deleteCount = db.delete(DataBaseHandler.TABLE_NPC, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case SINGLE_PC:
                id = uri.getPathSegments().get(1);
                selection = DataBaseHandler.KEY_ROWID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                deleteCount = db.delete(DataBaseHandler.TABLE_PC, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String id;
        int updateCount = 0;
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case SINGLE_CAMPAIGN:
                id = uri.getPathSegments().get(1);
                selection = DataBaseHandler.KEY_ROWID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                updateCount = db.update(DataBaseHandler.TABLE_CAMPAIGN, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case SINGLE_SESSION:
                id = uri.getPathSegments().get(1);
                selection = DataBaseHandler.KEY_ROWID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                updateCount = db.update(DataBaseHandler.TABLE_SESSION, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case SINGLE_ENCOUNTER:
                id = uri.getPathSegments().get(1);
                selection = DataBaseHandler.KEY_ROWID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                updateCount = db.update(DataBaseHandler.TABLE_ENCOUNTER, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case SINGLE_NPC:
                id = uri.getPathSegments().get(1);
                selection = DataBaseHandler.KEY_ROWID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                updateCount = db.update(DataBaseHandler.TABLE_NPC, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case SINGLE_PC:
                id = uri.getPathSegments().get(1);
                selection = DataBaseHandler.KEY_ROWID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                updateCount = db.update(DataBaseHandler.TABLE_PC, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return updateCount;
    }
}
