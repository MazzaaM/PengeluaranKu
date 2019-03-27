package e.azzam.pengeluaranku.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import e.azzam.pengeluaranku.Data.CostContract;
import e.azzam.pengeluaranku.R;

public class CostCatalogAdapter extends android.support.v4.widget.ResourceCursorAdapter {

    private TextView tvJudul;

    public CostCatalogAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        final int idColumnIndex = cursor.getColumnIndex(CostContract.CostEntry.ID);
        int nameColumnIndex = cursor.getColumnIndex(CostContract.CostEntry.COLUMN_COST_JUDUL);
        final int kategoriColumnIndex = cursor.getColumnIndex(CostContract.CostEntry.COLUMN_COST_KATEGORI);
        final int jumlahColumnIndex = cursor.getColumnIndex(CostContract.CostEntry.COLUMN_COST_JUMLAH);
        tvJudul = (TextView) view.findViewById(R.id.tv_judul);
        tvJudul.setText(cursor.getString(nameColumnIndex));
    }
}
