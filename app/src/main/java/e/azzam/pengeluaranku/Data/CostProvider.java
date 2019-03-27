package e.azzam.pengeluaranku.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class CostProvider extends ContentProvider {

    public static final int COSTS = 100;

    public static final int COST_ID =101;

    private static final UriMatcher sMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sMatcher.addURI(CostContract.CONTENT_AUTHORITY, CostContract.COSTS_PATH, COSTS);
        sMatcher.addURI(CostContract.CONTENT_AUTHORITY, CostContract.COSTS_PATH + "/#", COST_ID);

    }

    private CostDBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new CostDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        int match = sMatcher.match(uri);
        switch (match) {
            case COSTS:

                return db.query(CostContract.CostEntry.TABLE_NAME,
                        projection,null,null,null,null,null);

            case COST_ID:
                selection = CostContract.CostEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                return db.query(CostContract.CostEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,null);

            default:
                throw new IllegalArgumentException("Cannot Query unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sMatcher.match(uri);
        switch (match){
            case COSTS:
                return CostContract.CostEntry.CONTENT_LIST_TYPE;
            case COST_ID:
                return CostContract.CostEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " With code match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    private Uri insertCost(Uri uri, ContentValues values) {
        String judul = values.getAsString(CostContract.CostEntry.COLUMN_COST_JUDUL);
        if (judul == null || judul.isEmpty()) {
            Toast.makeText(getContext(), "Pet Requires a valid judul", Toast.LENGTH_SHORT).show();
            throw new IllegalArgumentException("Pet Requires a valid judul");
        }

        Integer jumlah = values.getAsInteger(CostContract.CostEntry.COLUMN_COST_JUMLAH);
        if (jumlah < 0) {
            Toast.makeText(getContext(), "Pet Requires a valid jumlah", Toast.LENGTH_SHORT).show();
            throw new IllegalArgumentException("Pet Requires a valid jumlah");
        }

        Integer kategori = values.getAsInteger(CostContract.CostEntry.COLUMN_COST_KATEGORI);
        if (kategori == null || !CostContract.CostEntry.isValidKategori(kategori)) {
            Toast.makeText(getContext(), "Pet Requires a valid kategori", Toast.LENGTH_SHORT).show();
            throw new IllegalArgumentException("Pet Requires a valid kategori");
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(CostContract.CostEntry.TABLE_NAME, null, values);
        if (id== -1) {
            Toast.makeText(getContext(), "Error Inserting Data", Toast.LENGTH_SHORT).show();
            return null;
        }

        return ContentUris.withAppendedId(uri, id);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }


}
