package e.azzam.pengeluaranku.Data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class CostDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "cost.db";
    public static final int DATABASE_VERSION =2;

    public CostDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CostContract.CostEntry.CREATE_TABLE );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }



}
