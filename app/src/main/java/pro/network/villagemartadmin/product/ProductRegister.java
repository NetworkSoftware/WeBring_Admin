package pro.network.villagemartadmin.product;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pro.network.villagemartadmin.R;
import pro.network.villagemartadmin.app.ActivityMediaOnline;
import pro.network.villagemartadmin.app.AndroidMultiPartEntity;
import pro.network.villagemartadmin.app.AppController;
import pro.network.villagemartadmin.app.Appconfig;
import pro.network.villagemartadmin.app.Imageutils;

import static pro.network.villagemartadmin.app.Appconfig.CATEGORY;
import static pro.network.villagemartadmin.app.Appconfig.PRODUCT_CREATE;

/**
 * Created by user_1 on 11-07-2018.
 */

public class ProductRegister extends AppCompatActivity implements Imageutils.ImageAttachmentListener, ImageClick {

    private RecyclerView imagelist;
    private ArrayList<String> samplesList = new ArrayList<>();
    AddImageAdapter maddImageAdapter;

    AutoCompleteTextView brand;
    EditText model;
    EditText price;
    EditText ram;

    EditText rom, description;

    private ProgressDialog pDialog;
    MaterialBetterSpinner category;
    MaterialBetterSpinner stock_update;

    private String[] STOCKUPDATE = new String[]{
            "In Stock", "Currently Unavailable",
    };

    String studentId = null;
    Imageutils imageutils;
    private String imageUrl = "";
    ImageView image_placeholder, image_wallpaper;
    CardView itemsAdd;

    TextView submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_register);

        imageutils = new Imageutils(this);

        getSupportActionBar().setTitle("Stock Register");

        itemsAdd = (CardView) findViewById(R.id.itemsAdd);
        ImageView image_wallpaper = (ImageView) findViewById(R.id.image_wallpaper);
        image_wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageutils.imagepicker(1);
            }
        });
        samplesList = new ArrayList<>();
        imagelist = (RecyclerView) findViewById(R.id.imagelist);
        maddImageAdapter = new AddImageAdapter(this,samplesList,this);
        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        imagelist.setLayoutManager(addManager1);
        imagelist.setAdapter(maddImageAdapter);
        category = (MaterialBetterSpinner) findViewById(R.id.category);

        ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, CATEGORY);
        category.setAdapter(titleAdapter);
        category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(ProductRegister.this,
                        android.R.layout.simple_dropdown_item_1line, Appconfig.stringMap.get(CATEGORY[position]));
                brand.setAdapter(brandAdapter);
                brand.setThreshold(1);
            }
        });




        stock_update = (MaterialBetterSpinner) findViewById(R.id.stock_update);

        ArrayAdapter<String> stockAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, STOCKUPDATE);
        stock_update.setAdapter(stockAdapter);
        stock_update.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        brand =  findViewById(R.id.brand);
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Appconfig.stringMap.get(CATEGORY[0]));
        brand.setAdapter(brandAdapter);
        brand.setThreshold(1);



        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        model = (EditText) findViewById(R.id.model);
        price = (EditText) findViewById(R.id.price);
        ram = (EditText) findViewById(R.id.ram);
        rom = (EditText) findViewById(R.id.rom);
        description = findViewById(R.id.description);




        submit = (TextView) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (category.getText().toString().length() <= 0) {
                    category.setError("Select the Category");
                } else if (brand.getText().toString().length() <= 0) {
                    brand.setError("Select the Brand");
                } else if (model.getText().toString().length() <= 0) {
                    model.setError("Enter the Model");
                } else if (price.getText().toString().length() <= 0) {
                    price.setError("Enter the Price");
                } else if (stock_update.getText().toString().length() <= 0) {
                    stock_update.setError("Select the Sold or Not");
                } else if (samplesList.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "Upload the Images!", Toast.LENGTH_SHORT).show();
                } else {

                    registerUser();
                }

            }
        });


    }

    private void registerUser() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Createing ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                PRODUCT_CREATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response.toString());
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response.split("0000")[1]);
                    boolean success = jsonObject.getBoolean("success");
                    String msg = jsonObject.getString("message");
                    if (success) {
                        final String descrip = description.getText().toString();
                        sendNotification(brand.getText().toString() + " " + model.getText().toString()
                                , descrip.length() > 10 ? descrip.substring(0, 9) + "..." :
                                        descrip);
                    }
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("category", category.getText().toString());
                localHashMap.put("brand", brand.getText().toString());
                localHashMap.put("model", model.getText().toString());
                localHashMap.put("price", price.getText().toString());
                localHashMap.put("ram", ram.getText().toString());
                localHashMap.put("rom", rom.getText().toString());
                localHashMap.put("image", new Gson().toJson(samplesList));
                localHashMap.put("description", description.getText().toString());
                localHashMap.put("stock_update", stock_update.getText().toString());

                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        String path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        imageutils.createImage(file, filename, path, false);
        pDialog.setMessage("Uploading...");
        showDialog();
        new UploadFileToServer().execute(Appconfig.compressImage(imageutils.getPath(uri)));
    }

    private class UploadFileToServer extends AsyncTask<String, Integer, String> {
        String filepath;
        public long totalSize = 0;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero

            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pDialog.setMessage("Uploading..." + (String.valueOf(progress[0])));
        }

        @Override
        protected String doInBackground(String... params) {
            filepath = params[0];
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Appconfig.URL_IMAGE_UPLOAD);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filepath);
                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);

                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;

                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("Response from server: ", result);
            try {
                JSONObject jsonObject = new JSONObject(result.toString());
                if (!jsonObject.getBoolean("error")) {
                    imageUrl = Appconfig.ip + "/images/" + imageutils.getfilename_from_path(filepath);
                    samplesList.add(imageUrl);
                    maddImageAdapter.notifyData(samplesList);
                } else {
                    imageUrl = null;
                }
                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (Error | Exception e) {
                Toast.makeText(getApplicationContext(), "Image not uploaded", Toast.LENGTH_SHORT).show();
            }
            hideDialog();
            // showing the server response in an alert dialog
            //showAlert(result);


            super.onPostExecute(result);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageutils.onActivityResult(requestCode, resultCode, data);
    }

    private void sendNotification(String title, String description) {
        String tag_string_req = "req_register";
        showDialog();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("to", "/topics/allDevices");
            jsonObject.put("priority", "high");
            JSONObject dataObject = new JSONObject();
            dataObject.put("title", title);
            dataObject.put("message", description);
            jsonObject.put("data", dataObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Register Response: ", response.toString());
                hideDialog();
                finish();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                return localHashMap;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("Content-Type", "application/json");
                hashMap.put("Authorization", "key=AAAA3577AcI:APA91bFVktJ7Qoiqzg8ae5-KPC51DhEtUidyuEVCoG5DebTBMyiVIKyAgDngvT8HpXUcMIuMy-IU0JyI7Pslcb698Ngd2i1Ypwuki6Khz5MagW-F7_7dJ1T0rL6ThE--ZWXxjHE0hnZg");
                return hashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    @Override
    public void onImageClick(int position) {
        Intent localIntent = new Intent(ProductRegister.this, ActivityMediaOnline.class);
        localIntent.putExtra("filePath", samplesList.get(position));
        localIntent.putExtra("isImage", true);
        startActivity(localIntent);
    }

    @Override
    public void onDeleteClick(int position) {
        samplesList.remove(position);
        maddImageAdapter.notifyData(samplesList);
    }

}
