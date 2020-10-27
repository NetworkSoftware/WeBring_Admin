package smart.network.patasuadmin.stock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import smart.network.patasuadmin.R;
import smart.network.patasuadmin.app.AndroidMultiPartEntity;
import smart.network.patasuadmin.app.AppController;
import smart.network.patasuadmin.app.Appconfig;
import smart.network.patasuadmin.app.BaseActivity;
import smart.network.patasuadmin.app.GlideApp;
import smart.network.patasuadmin.app.Imageutils;
import de.hdodenhof.circleimageview.CircleImageView;
import smart.network.patasuadmin.shop.Shop;
import smart.network.patasuadmin.staff.StaffRegister;

import static smart.network.patasuadmin.app.Appconfig.ALL_SHOP;
import static smart.network.patasuadmin.app.Appconfig.STACK_CREATE;
import static smart.network.patasuadmin.app.Appconfig.STACK_GET_ALL;

/**
 * Created by user_1 on 11-07-2018.
 */

public class StockRegister extends BaseActivity {


    MaterialBetterSpinner title;
    EditText items;
    EditText price;
    EditText itemNo;
    MaterialBetterSpinner shopid;


    private ProgressDialog pDialog;

    private String[] STOREID = new String[]{
            "Loading",
    };
    private String[] TITLE = new String[]{
            "Sparklers","Ground Chakkars","Flower Pots","Twinkling Star","Pencil","Atom Bombs","One Sound Crakers","Bijili","Chorsa","Giant",
            "Deluxe","Lar Crackers","Rockets","Fancy Comets","Repeating Shots","Matches","Festival Repeating Shot","New Items","Gift Boxes","Guns",
    };
    TextView submit;
    Map<String, String> storecodeMap = new HashMap<>();
    Map<String, String> storeNameMap = new HashMap<>();


    String studentId = null;
    Contact contact=null;


    @Override
    protected void startDemo() {
        setContentView(R.layout.stock_register);
        getSupportActionBar().setTitle("Add Items");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getSupportActionBar().setTitle("Stock Register");


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        shopid = (MaterialBetterSpinner) findViewById(R.id.storeid);
        title = (MaterialBetterSpinner) findViewById(R.id.title);
        items = (EditText) findViewById(R.id.items);
        price = (EditText) findViewById(R.id.price);
        itemNo = (EditText) findViewById(R.id.itemsNo);


        submit = (TextView) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().toString().length() > 0 &&
                        price.getText().toString().length() > 0 &&
                        items.getText().toString().length() > 0 &&
                        itemNo.getText().toString().length() > 0 &&
                        shopid.getText().toString().length() > 0
                        ) {
                    registerUser();

                }
            }
        });
        shopid=findViewById(R.id.storeid);
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, STOREID);
        shopid.setAdapter(stateAdapter);
        shopid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        fetchstoreid();

        ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, TITLE);
        title.setAdapter(titleAdapter);
        title.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }
    private void registerUser() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Processing ...");
        showDialog();
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                STACK_CREATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response.substring(response.indexOf("{"), response.length()));
                    boolean success = jObj.getBoolean("success");
                    String msg = jObj.getString("message");
                    if (success == true) {
                        finish();
                    }
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("title", title.getText().toString());
                localHashMap.put("items", items.getText().toString());
                localHashMap.put("price", price.getText().toString());
                localHashMap.put("nos", itemNo.getText().toString());
                localHashMap.put("shopid", storecodeMap.get(shopid.getText().toString()));
                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }



    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    @Override
    protected void onPause() {
        super.onPause();
        hideDialog();
    }

    private void fetchstoreid() {
        this.pDialog.setMessage("fetching...");
        showDialog();
        JSONObject jsonObject = new JSONObject();

        JsonObjectRequest local16 = new JsonObjectRequest(1, ALL_SHOP, jsonObject,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject localJSONObject1) {
                        hideDialog();
                        try {
                            if (localJSONObject1.getInt("success") == 1) {
                                storecodeMap = new HashMap<>();
                                storeNameMap = new HashMap<>();
                                JSONArray jsonArray = localJSONObject1.getJSONArray("shops");
                                STOREID = new String[jsonArray.length()];
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    STOREID[i] = jsonObject1.getString("storename");
                                    storecodeMap.put(jsonObject1.getString("storename"), jsonObject1.getString("id"));
                                    storeNameMap.put(jsonObject1.getString("id"), jsonObject1.getString("storename"));
                                }
                                ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(StockRegister.this,
                                        android.R.layout.simple_dropdown_item_1line, STOREID);
                                shopid.setAdapter(districtAdapter);
                                shopid.setText("");


                                return;
                            }
                        } catch (JSONException localJSONException) {
                            localJSONException.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError paramVolleyError) {
                Log.e("tag", "Fetch Error: " + paramVolleyError.getMessage());
                Toast.makeText(getApplicationContext(), paramVolleyError.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {

                HashMap<String, String> localHashMap = new HashMap<String, String>();

                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(local16, "");
    }

/*
    private void fetchtitle() {
        this.pDialog.setMessage("fetching...");
        showDialog();
        JSONObject jsonObject = new JSONObject();

        JsonObjectRequest local16 = new JsonObjectRequest(1, STACK_GET_ALL, jsonObject,
                new Response.Listener<JSONObject>() {

                    public void onResponse(JSONObject localJSONObject1) {
                        hideDialog();
                        try {
                            if (localJSONObject1.getInt("success") == 1) {
                                  JSONArray jsonArray = localJSONObject1.getJSONArray("staff");
                                TITLE = new String[jsonArray.length()];
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    TITLE[i] = jsonObject1.getString("title");

                                }
                                ArrayAdapter<String> Adapter = new ArrayAdapter<String>(StockRegister.this,
                                        android.R.layout.simple_dropdown_item_1line, TITLE);
                                title.setAdapter(Adapter);
                                title.setText("");


                                return;
                            }
                        } catch (JSONException localJSONException) {
                            localJSONException.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError paramVolleyError) {
                Log.e("tag", "Fetch Error: " + paramVolleyError.getMessage());
                Toast.makeText(getApplicationContext(), paramVolleyError.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {

                HashMap<String, String> localHashMap = new HashMap<String, String>();

                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(local16, "");
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
