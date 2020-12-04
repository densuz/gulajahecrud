package com.deni.gulajahe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* Code by Agus Suparno
July 2020
 */
public class ScrollingActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    FloatingActionButton fab;
    public ProgressBar progressBar;
    public ArrayList<DataModel> dataModelArrayList;
    public TextView empty_msg;
    DataAdapter adapter;
    android.app.AlertDialog.Builder dialog;
    public RelativeLayout layout;
    public SwipeRefreshLayout swipeRefreshLayout;
    public Snackbar snackbar;
    public Toolbar toolbar;
    public AlertDialog alertDialog;
    LayoutInflater inflater;
    EditText txt_id_produk,txt_kd_produk, txt_nama_produk, txt_harga, txt_jumlah_produk;
    String id_produk,kd_produk, nama_produk, harga, jumlah_produk;
    View dialogView;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        layout = findViewById(R.id.layout);
        toolbar = findViewById(R.id.toolBar);
        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        empty_msg = findViewById(R.id.txtblanklist);
        progressBar = findViewById(R.id.progressBar);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        recyclerView = findViewById(R.id.recylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ScrollingActivity.this));
        getData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        // tombol melayang untuk menambahkan data produk
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm("", "", "", "", "","SIMPAN");
            }
        });
    }
    private void getData() {
        progressBar.setVisibility(View.VISIBLE);
        if (Utils.isNetworkAvailable(ScrollingActivity.this)) {
            getMainCategoryFromJson();
            invalidateOptionsMenu();
        } else {
            setSnackBar();
            progressBar.setVisibility(View.GONE);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void getMainCategoryFromJson() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dataModelArrayList = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(response);
                            String error = jsonObject.getString(Constant.ERROR);
                            if (error.equalsIgnoreCase("false")) {
                                empty_msg.setVisibility(View.GONE);
                                JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    DataModel dataModel = new DataModel();
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    dataModel.setid_produk(object.getString("id_produk"));
                                    dataModel.setkd_produk(object.getString("kd_produk"));
                                    dataModel.setnama_produk(object.getString("nama_produk"));
                                    dataModel.setharga(object.getString("harga"));
                                    dataModel.setjumlah_produk(object.getString("jumlah_produk"));
                                    dataModelArrayList.add(dataModel);
                                }
                                adapter = new DataAdapter(ScrollingActivity.this, dataModelArrayList);
                                recyclerView.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);
                            } else {
                                empty_msg.setText(getString(R.string.no_data));
                                progressBar.setVisibility(View.GONE);
                                empty_msg.setVisibility(View.VISIBLE);
                                if (adapter != null) {
                                    adapter = new DataAdapter(ScrollingActivity.this, dataModelArrayList);
                                    recyclerView.setAdapter(adapter);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ScrollingActivity.this,"ini err: "+error,Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.accessKey, Constant.accessKeyValue);
                params.put(Constant.getdata_produk, "1");
                return params;
            }
        };

        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ItemRowHolder> {
        private ArrayList<DataModel> dataList;
        private Context mContext;
        public DataAdapter(Context context, ArrayList<DataModel> dataList) {
            this.dataList = dataList;
            this.mContext = context;
        }
        @Override
        public DataAdapter.ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new ItemRowHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull DataAdapter.ItemRowHolder holder, final int position) {
            final DataModel dataModel = dataList.get(position);
            holder.id_produk.setText(dataModel.getid_produk());
            holder.text.setText(dataModel.getnama_produk());
            holder.harga.setText(dataModel.getharga());
            holder.jumlah_produk.setText(dataModel.getjumlah_produk());
            holder.kd_produk.setText(dataModel.getkd_produk());
            holder.image.setDefaultImageResId(R.mipmap.ic_kitchen);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final String id_produkx = dataList.get(position).getid_produk();
                    final String kd_produkx = dataList.get(position).getkd_produk();
                    final String nama_produkx = dataList.get(position).getnama_produk();
                    final String hargax = dataList.get(position).getharga();
                    final String jumlah_produkx = dataList.get(position).getjumlah_produk();

                    final CharSequence[] dialogitem = {"Edit", "Delete"};
                    dialog = new android.app.AlertDialog.Builder(ScrollingActivity.this);
                    dialog.setCancelable(true);
                    dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            switch (which) {
                                case 0:
                                    edit(id_produkx,kd_produkx,nama_produkx,hargax,jumlah_produkx);
                                    break;
                                case 1:
                                    delete(id_produkx);
                                    break;
                            }
                        }
                    }).show();
                    return false;
                }

            });
        }
        @Override
        public int getItemCount() {
            return dataList.size();
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class ItemRowHolder extends RecyclerView.ViewHolder {
            public NetworkImageView image;
            public TextView id_produk,text,nama_produk,kd_produk,harga,jumlah_produk;
            RelativeLayout relativeLayout;
            public ItemRowHolder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.cateImg);
                id_produk=itemView.findViewById(R.id.id_produk);
                kd_produk = itemView.findViewById(R.id.kd_produk);
                text = itemView.findViewById(R.id.nama_produk);
                harga= itemView.findViewById(R.id.harga);
                jumlah_produk=itemView.findViewById(R.id.jumlah_produk);
                relativeLayout = itemView.findViewById(R.id.cat_layout);
            }
        }
    }
    public void setSnackBar() {
        snackbar = Snackbar
                .make(findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getData();
                    }
                });

        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
    private void DialogForm(String id_produkx, String kd_produkx, String nama_produkx, String hargax, String jumlah_produkx, String button) {
        dialog = new android.app.AlertDialog.Builder(ScrollingActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_input, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_kitchen);
        dialog.setTitle("Data Produk");

        txt_id_produk = (EditText) dialogView.findViewById(R.id.txt_id_produk);
        txt_kd_produk = (EditText) dialogView.findViewById(R.id.txt_kd_produk);
        txt_nama_produk = (EditText) dialogView.findViewById(R.id.txt_nama_produk);
        txt_harga = (EditText) dialogView.findViewById(R.id.txt_harga);
        txt_jumlah_produk = (EditText) dialogView.findViewById(R.id.txt_jumlah_produk);

        if (!id_produkx.isEmpty()) {
            txt_id_produk.setText(id_produkx);
            txt_kd_produk.setText(kd_produkx);
            txt_nama_produk.setText(nama_produkx);
            txt_harga.setText(hargax);
            txt_jumlah_produk.setText(jumlah_produkx);
        } else {
            setblank();
        }

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                id_produk = txt_id_produk.getText().toString();
                kd_produk = txt_kd_produk.getText().toString();
                nama_produk = txt_nama_produk.getText().toString();
                harga = txt_harga.getText().toString();
                jumlah_produk = txt_jumlah_produk.getText().toString();
                simpan_data();
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setblank();
            }
        });

        dialog.show();
    }
    private void setblank() {
        txt_id_produk.setText(null);
        txt_nama_produk.setText(null);
        txt_harga.setText(null);
        txt_jumlah_produk.setText(null);
    }
    private void simpan_data() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(ScrollingActivity.this, "ini masuk onrespon", Toast.LENGTH_LONG).show();
                        try {
                            dataModelArrayList = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(response);
                            String error = jsonObject.getString(Constant.ERROR);

                            if (error.equalsIgnoreCase("false")) {
                                empty_msg.setVisibility(View.GONE);
                                //alertDialog.dismiss();
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ScrollingActivity.this, "Data Berhasil disimpan", Toast.LENGTH_LONG).show();
                                getData();
                            } else {
                                Toast.makeText(ScrollingActivity.this, "Data gagal disimpan: "+id_produk, Toast.LENGTH_LONG).show();


                                empty_msg.setText( getString(R.string.no_data));
                                progressBar.setVisibility(View.GONE);
                                empty_msg.setVisibility(View.VISIBLE);

                                if (adapter != null) {
                                    adapter = new DataAdapter(ScrollingActivity.this, dataModelArrayList);
                                    recyclerView.setAdapter(adapter);
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ScrollingActivity.this,"ini err: "+error,Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.accessKey, Constant.accessKeyValue);
                if (id_produk.isEmpty()) {
                    params.put(Constant.setdata_produk, "1");
                }else {
                    params.put(Constant.editdata_produk,"1");
                    params.put(Constant.id_produk,id_produk);
                }
                params.put(Constant.kd_produk,kd_produk);
                params.put(Constant.nama_produk,nama_produk);
                params.put(Constant.harga,harga);
                params.put(Constant.jumlah_produk,jumlah_produk);
                return params;

            }
        };

        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    private void edit(final String id_produkx, String kd_produkx,String nama_produkx, String hargax, String jumlah_produkx) {

        DialogForm(id_produkx,kd_produkx, nama_produkx, hargax, jumlah_produkx, "UPDATE");

    }
    private void delete(final String id_produkx) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(ScrollingActivity.this, "ini masuk onrespon", Toast.LENGTH_LONG).show();
                        try {
                            dataModelArrayList = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(response);
                            String error = jsonObject.getString(Constant.ERROR);

                            if (error.equalsIgnoreCase("false")) {
                                empty_msg.setVisibility(View.GONE);
                                //alertDialog.dismiss();
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ScrollingActivity.this, "Data Berhasil dihapus", Toast.LENGTH_LONG).show();
                                getData();
                            } else {


                                empty_msg.setText(getString(R.string.no_data));
                                progressBar.setVisibility(View.GONE);
                                empty_msg.setVisibility(View.VISIBLE);

                                if (adapter != null) {
                                    adapter = new DataAdapter(ScrollingActivity.this, dataModelArrayList);
                                    recyclerView.setAdapter(adapter);
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ScrollingActivity.this,"ini err: "+error,Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.accessKey, Constant.accessKeyValue);
                params.put(Constant.id_produk,id_produkx);
                params.put(Constant.deletedata_produk,"1");
                return params;

            }
        };

        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
