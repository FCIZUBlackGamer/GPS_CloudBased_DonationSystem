package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.audiofx.Equalizer;
import android.widget.Toast;

/**
 * Created by fci on 11/03/17.
 */

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Donation_System";

    private static final String TABLE_NAME = "TABLE_FCI";

    private static final String UID = "id";

    private static final String EMAIL = "email";

    private static final String LOGIN = "login";

    private static final String TYPE = "type";

    private static final String DISTANCE = "distanse";

    private static final String ENABLE_NOTIFICATION = "enable_notification";

    private static final String LAST_NUM_ROWS = "last_id";

    private static final int DATABASE_VERSION = 4;
    Context cont;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table " +TABLE_NAME +
            "( "+UID+" integer primary key , "+EMAIL+" varchar(255) not null, "+LOGIN+" varchar(2) not null, "+TYPE+" varchar(255) not null, "+DISTANCE+" varchar(255) not null, "+ENABLE_NOTIFICATION+" varchar(255) not null, "+LAST_NUM_ROWS+" varchar(255) not null);";

    // Database Deletion
    private static final String DATABASE_DROP = "drop table if exists "+TABLE_NAME+";";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.cont = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DATABASE_CREATE);
            db.execSQL("insert into "+TABLE_NAME+" ( "+UID+", "+EMAIL+", "+LOGIN+", "+TYPE+", "+DISTANCE+", "+ENABLE_NOTIFICATION+", "+LAST_NUM_ROWS+" ) values ( '1', 'e', '0', 't', 'e', '0', 't');");
            Toast.makeText(cont,"database created", Toast.LENGTH_SHORT).show();
        }catch (SQLException e)
        {
            Toast.makeText(cont,"database doesn't created " +e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            db.execSQL(DATABASE_DROP);
            onCreate(db);
            Toast.makeText(cont,"database upgraded", Toast.LENGTH_SHORT).show();
        }catch (SQLException e)
        {
            Toast.makeText(cont,"database doesn't upgraded " +e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean InsertData (String email, String state , String type )
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EMAIL,email);
        contentValues.put(LOGIN,state);
        contentValues.put(TYPE,type);
        contentValues.put(DISTANCE,"0");
        contentValues.put(ENABLE_NOTIFICATION,"yes");
        contentValues.put(LAST_NUM_ROWS,"0");
        long result = sqLiteDatabase.insert(TABLE_NAME,null,contentValues);

        return result==-1?false:true;
    }

    public Cursor ShowData ()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+TABLE_NAME+" ;",null);
        return cursor;
    }

    public boolean UpdateData (String id, String email, String state, String type, String dis, String enable, String last )
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UID,id);
        contentValues.put(EMAIL,email);
        contentValues.put(LOGIN,state);
        contentValues.put(TYPE,type);
        contentValues.put(DISTANCE,dis);
        contentValues.put(ENABLE_NOTIFICATION,enable);
        contentValues.put(LAST_NUM_ROWS,last);
        sqLiteDatabase.update(TABLE_NAME,contentValues,"id = "+Integer.parseInt( id ),null);

        return true;
    }

    public boolean UpdateNotification (String id, String word )
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UID,id);
        contentValues.put(ENABLE_NOTIFICATION,word);
        sqLiteDatabase.update(TABLE_NAME,contentValues,"id = ?",new String[]{id});

        return true;
    }

    public boolean UpdateLastNumRows (String id, String word )
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UID,id);
        contentValues.put(LAST_NUM_ROWS,word);
        sqLiteDatabase.update(TABLE_NAME,contentValues,"id = ?",new String[]{id});

        return true;
    }

    public boolean UpdateDistance (String id, String word )
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UID,id);
        contentValues.put(DISTANCE,word);
        sqLiteDatabase.update(TABLE_NAME,contentValues,"id = ?",new String[]{id});

        return true;
    }

    public int DeleteData (String id)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME,"ID = ?",new String[] {id});
    }

}
