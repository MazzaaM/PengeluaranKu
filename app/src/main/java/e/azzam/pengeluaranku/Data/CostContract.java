package e.azzam.pengeluaranku.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class CostContract {

    public static final String CONTENT_AUTHORITY ="e.azzam.pengeluaranku";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String COSTS_PATH = "costs"; //nama tabel database


    public abstract static class CostEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, COSTS_PATH);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + COSTS_PATH;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY +"/" + COSTS_PATH;

        public static final String TABLE_NAME = "costs";
        public static final String ID = BaseColumns._ID;
        public static final String COLUMN_COST_JUDUL = "judul";
        public static final String COLUMN_COST_JUMLAH = "jumlah";
        public static final String COLUMN_COST_KATEGORI = "kategori";
        public static final String COLUMN_COST_TANGGAL = "tanggal";

        public static final int KATEGORI_TRANSPORT = 0;
        public static final int KATEGORI_UMUM = 1;
        public static final int KATEGORI_MAKANAN = 2;

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COST_JUDUL + " TEXT NOT NULL, " +
                COLUMN_COST_KATEGORI + " INTEGER NOT NULL DEFAULT " +
                KATEGORI_TRANSPORT + ", " +
                COLUMN_COST_JUMLAH + " INTEGER NOT NULL DEFAULT 0, " +
                COLUMN_COST_TANGGAL + " TEXT NOT NULL " +
                ");";

        public static boolean isValidKategori(Integer kategori) {
            if (kategori == KATEGORI_TRANSPORT || kategori ==KATEGORI_UMUM || kategori == KATEGORI_MAKANAN){
                return true;
            }
            return false;
        }


    }
}
