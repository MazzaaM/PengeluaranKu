package e.azzam.pengeluaranku;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import e.azzam.pengeluaranku.Adapter.CostCatalogAdapter;
import e.azzam.pengeluaranku.Data.CostContract;
import e.azzam.pengeluaranku.Data.CostDBHelper;

public class MainActivity extends AppCompatActivity {

    private CostDBHelper dbHelper;
    private TextView textViewCount;
    private ListView rvCostCatalog;
    private FloatingActionButton fab;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new CostDBHelper(this);
        textViewCount = (TextView) findViewById(R.id.tv_count);
        rvCostCatalog = (ListView) findViewById(R.id.rv_cost_catalog);
        fab = (FloatingActionButton) findViewById(R.id.fabtn);
        emptyView = (View) findViewById(R.id.empty_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo(){
        String[] projection = {CostContract.CostEntry._ID,
                CostContract.CostEntry.COLUMN_COST_JUDUL,
                CostContract.CostEntry.COLUMN_COST_KATEGORI,
                CostContract.CostEntry.COLUMN_COST_JUMLAH,
                CostContract.CostEntry.COLUMN_COST_TANGGAL};


        final Cursor cursor = dbHelper.getReadableDatabase().query(CostContract.CostEntry.TABLE_NAME,
                projection,null,null, null, null, null);

        textViewCount.setText("data table pet consist : " + cursor.getCount() + " rows \n\n");
        textViewCount.append(CostContract.CostEntry._ID + " - " +
                CostContract.CostEntry.COLUMN_COST_JUDUL + " - " +
                CostContract.CostEntry.COLUMN_COST_KATEGORI + " - " +
                CostContract.CostEntry.COLUMN_COST_JUMLAH + "\n");
        final int idColumnIndex = cursor.getColumnIndex(CostContract.CostEntry.ID);
        final int judulColumnIndex = cursor.getColumnIndex(CostContract.CostEntry.COLUMN_COST_JUDUL);
        final int kategoriColumnIndex = cursor.getColumnIndex(CostContract.CostEntry.COLUMN_COST_KATEGORI);
        final int jumlahColumnIndex = cursor.getColumnIndex(CostContract.CostEntry.COLUMN_COST_JUMLAH);
        final int dateColumnIndex = cursor.getColumnIndex(CostContract.CostEntry.COLUMN_COST_TANGGAL);


        CostCatalogAdapter adapter = new CostCatalogAdapter(this,R.layout.item_list,cursor,0);
        rvCostCatalog.setAdapter(adapter);
        rvCostCatalog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra(EditorActivity.EXTRA_ID, cursor.getLong(idColumnIndex));
                intent.putExtra(EditorActivity.EXTRA_JUDUL, cursor.getString(judulColumnIndex));
                intent.putExtra(EditorActivity.EXTRA_KATEGORI, cursor.getInt(kategoriColumnIndex));
                intent.putExtra(EditorActivity.EXTRA_JUMLAH, cursor.getInt(jumlahColumnIndex));
                intent.putExtra(EditorActivity.EXTRA_DATE, cursor.getString(dateColumnIndex));
                intent.putExtra(EditorActivity.EXTRA_IS_EDIT_MODE,true);
                startActivity(intent);
            }

        });

        if (cursor.getCount() > 0){
            rvCostCatalog.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } else {
            rvCostCatalog.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }

        adapter.changeCursor(cursor);

    }

//    }



    public void onAddcost(View view) {
        Intent editorIntent = new Intent(this, EditorActivity.class);
        startActivity(editorIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuactivity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void insert (){
        ContentValues cp = new ContentValues();
        cp.put(CostContract.CostEntry.COLUMN_COST_JUDUL, "Pengeluaran");
        cp.put(CostContract.CostEntry.COLUMN_COST_KATEGORI, CostContract.CostEntry.KATEGORI_UMUM);
        cp.put(CostContract.CostEntry.COLUMN_COST_JUMLAH, 4);
        cp.put(CostContract.CostEntry.COLUMN_COST_TANGGAL, "11-02-2019");
        getContentResolver().insert(CostContract.CostEntry.CONTENT_URI, cp);
//        if (newRowID == -1) {
//            Toast.makeText(this, "Error Inputting Data", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Inserting Data Success", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_tambah:
                insert();
                displayDatabaseInfo();
                break;
            case R.id.action_deletesmua:
                deleteAllCost();
                displayDatabaseInfo();
                break;
            case android.R.id.home:
                onBackPressed();
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllCost(){
        dbHelper.getWritableDatabase().execSQL("delete from " + CostContract.CostEntry.TABLE_NAME);
    }

}
