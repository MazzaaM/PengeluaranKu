package e.azzam.pengeluaranku;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import e.azzam.pengeluaranku.Data.CostContract;
import e.azzam.pengeluaranku.Data.CostDBHelper;

public class EditorActivity extends AppCompatActivity {


    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private TextView tvDateResult;
    private EditText btDatePicker;

    private TextInputEditText etJudul;
    private TextInputEditText etJumlah;
    private Spinner spKategori;
    private ImageButton btnDate;

    public static final String EXTRA_ID = "costid";
    public static final String EXTRA_JUDUL = "costjudul";
    public static final String EXTRA_JUMLAH = "costjumlah";
    public static final String EXTRA_KATEGORI = "costkategori";
    public static final String EXTRA_DATE = "costdate";
    public static final String EXTRA_IS_EDIT_MODE = "isEditMode";

    private int mKategori;
    private String mJudul;
    private int mJumlah;
    private String mTanggal;
    private CostDBHelper costDBHelper;
    private boolean isEditMode = false;
    private long id;

    private long newRowId;

    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                tvDateResult.setText(dateFormatter.format(newDate.getTime()));

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        tvDateResult = (TextView) findViewById(R.id.tv_dateresult);
        btDatePicker = (EditText) findViewById(R.id.tv_dateresult);

        etJudul = (TextInputEditText) findViewById(R.id.et_judul);
        etJumlah = (TextInputEditText) findViewById(R.id.et_jumlah);
        spKategori = (Spinner) findViewById(R.id.sp_kategori);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        costDBHelper = new CostDBHelper(this);

        ArrayAdapter genderArrayAdapter = ArrayAdapter
                .createFromResource(this, R.array.array_kategori_option, android.R.layout.simple_spinner_item);
        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spKategori.setAdapter(genderArrayAdapter);
        Intent intent = getIntent();

        if (intent.getExtras()!=null){
            id = intent.getLongExtra(EXTRA_ID, -1);
            etJudul.setText(intent.getStringExtra(EXTRA_JUDUL));
            etJumlah.setText(String.valueOf(intent.getIntExtra(EXTRA_JUMLAH,0)));
            int position = intent.getIntExtra(EXTRA_KATEGORI,0);
            spKategori.setSelection(position);
            isEditMode = intent.getBooleanExtra(EXTRA_IS_EDIT_MODE,false);
            btDatePicker.setText(intent.getStringExtra(EXTRA_DATE));
        }


        spKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selectedItem)) {
                    switch (selectedItem) {
                        case "Umum":
                            mKategori = 1;
                            break;
                        case "Makanan":
                            mKategori = 2;
                            break;
                        default:
                            mKategori = 0;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mKategori = 0;

            }
        });

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        tvDateResult = (TextView) findViewById(R.id.tv_dateresult);
        btDatePicker = (EditText) findViewById(R.id.tv_dateresult);
        btDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                mJudul = etJudul.getText().toString();
                mJumlah = (etJumlah.getText().toString().equals("") ? 0 : Integer.parseInt(etJumlah.getText().toString()));
                mTanggal= tvDateResult.getText().toString();
                if (mJudul.isEmpty()){
                    etJudul.setError("Judul Can't be empty");
                    etJumlah.setError("Jumlah Can't be empty");
                } else {
                    if (!isEditMode){
                        saveCost();
                        Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show();

                    }else {
                        updateCost();
                        Toast.makeText(this,"Data Updated", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
                break;

            case R.id.action_apus:
                deleteCost();
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    private void saveCost() {
        ContentValues values = new ContentValues();
        values.put(CostContract.CostEntry.COLUMN_COST_JUDUL, mJudul);
        values.put(CostContract.CostEntry.COLUMN_COST_JUMLAH, mJumlah);
        values.put(CostContract.CostEntry.COLUMN_COST_KATEGORI, mKategori);
        values.put(CostContract.CostEntry.COLUMN_COST_TANGGAL, mTanggal);
        getContentResolver().insert(CostContract.CostEntry.CONTENT_URI, values);
    }

    private void deleteCost(){
        String[] args = {String.valueOf(id)};
        costDBHelper.getWritableDatabase().delete(CostContract.CostEntry.TABLE_NAME, CostContract.CostEntry._ID+ "=?", args);

    }

    private void updateCost(){
        ContentValues values = new ContentValues();
        values.put(CostContract.CostEntry.COLUMN_COST_JUDUL, mJudul);
        values.put(CostContract.CostEntry.COLUMN_COST_JUMLAH, mJumlah);
        values.put(CostContract.CostEntry.COLUMN_COST_KATEGORI, mKategori);
        costDBHelper.getWritableDatabase()
                .update(CostContract.CostEntry.TABLE_NAME, values,
                        CostContract.CostEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public void onDateClick(View view){
        showDateDialog();
    }
}
