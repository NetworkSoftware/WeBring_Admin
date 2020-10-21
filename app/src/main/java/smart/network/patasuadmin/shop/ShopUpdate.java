package smart.network.patasuadmin.shop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

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
import java.util.HashMap;
import java.util.Map;

import smart.network.patasuadmin.R;
import smart.network.patasuadmin.app.AndroidMultiPartEntity;
import smart.network.patasuadmin.app.AppController;
import smart.network.patasuadmin.app.Appconfig;
import smart.network.patasuadmin.app.GlideApp;
import smart.network.patasuadmin.app.Imageutils;
import de.hdodenhof.circleimageview.CircleImageView;


import static smart.network.patasuadmin.app.Appconfig.mypreference;

/**
 * Created by user_1 on 11-07-2018.
 */

public class ShopUpdate extends AppCompatActivity implements Imageutils.ImageAttachmentListener
        , GoogleApiClient.OnConnectionFailedListener {


    String[] areas = {
            "Ganthipuram",
            "Ariyalur",
            "Karur",
            "Nagapattinam",
            "Perambalur",
            "Pudukkottai",
            "Thanjavur",
            "Tiruchirappalli",
            "Tiruvarur",
            "Dharmapuri",
            "Coimbatore",
            "Erode",
            "Krishnagiri",
            "Namakkal",
            "The Nilgiris",
            "Salem",
            "Tiruppur",
            "Dindigul",
            "Kanyakumari",
            "Madurai",
            "Ramanathapuram",
            "Sivaganga",
            "Theni",
            "Thoothukudi",
            "Tirunelveli",
            "Virudhunagar",
            "Chennai",
            "Cuddalore",
            "Kanchipuram",
            "Tiruvallur",
            "Tiruvannamalai",
            "Vellore",
            "Viluppuram",
            "Kallakurichi",
    };


    EditText name;
    EditText contact;
    EditText shopname;
    EditText password;
    EditText confirmPass;
    ImageView placeSearch;
    EditText address;
    String latLon = "";
    private TextView submit;
    private ProgressDialog pDialog;
    SharedPreferences sharedpreferences;
    Imageutils imageutils;
    private CircleImageView profiletImage;
    private String imageUrl = null;
    AutoCompleteTextView actv;
    private Shop shop;
    String shopId = null;

    String oldName;
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_register);

        imageutils = new Imageutils(this);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        profiletImage = (CircleImageView) findViewById(R.id.profiletImage);
        profiletImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageutils.imagepicker(1);
            }
        });

        name = (EditText) findViewById(R.id.name);
        contact = (EditText) findViewById(R.id.contact);
        shopname = (EditText) findViewById(R.id.shopName);
        address = (EditText) findViewById(R.id.address);
        password = (EditText) findViewById(R.id.password);
        confirmPass = (EditText) findViewById(R.id.confirmPass);




        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        placeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(ShopUpdate.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        submit = (TextView) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().length() > 0 &&
                        contact.getText().toString().length() > 0 &&
                        shopname.getText().toString().length() > 0 &&
                        password.getText().toString().length() > 0 &&
                        confirmPass.getText().toString().length() > 0 &&
                        address.getText().toString().length()>0
                ) {

                    if (!password.getText().toString().equalsIgnoreCase(confirmPass.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Password & Confirm password not matched", Toast.LENGTH_SHORT).show();
                    } else if (contact.getText().toString().length() != 10 || contact.getText().toString().matches(".*[a-zA-Z]+.*")) {
                        Toast.makeText(getApplicationContext(), "Enter a valid Contact", Toast.LENGTH_SHORT).show();
                    }  else {
                        Shop shop = new Shop(name.getText().toString(),
                                contact.getText().toString(),
                                shopname.getText().toString(),
                                password.getText().toString(),
                                confirmPass.getText().toString(),
                                address.getText().toString());

                        registerUser(shop);
                    }
                }
            }
        });


        try {
            shop = (Shop) getIntent().getSerializableExtra("object");
            shopId = shop.id;
            contact.setText(shop.contact);
            shopname.setText(shop.shopname);
            oldName=shop.shopname;
            password.setText(shop.password);
            confirmPass.setText(shop.confirmPass);
            address.setText(shop.address);

            submit.setText("Update");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "No Shop found", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void registerUser(final Shop shop) {
        String tag_string_req = "req_register";
        pDialog.setMessage("Processing ...");
        showDialog();
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Appconfig.UPDATE_SHOP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response.substring(response.indexOf("{"), response.length()));
                    int success = jObj.getInt("success");
                    String msg = jObj.getString("message");
                    if (success == 1) {
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
                localHashMap.put("id", shopId);
                localHashMap.put("shopname", shopname.getText().toString());
                localHashMap.put("password", password.getText().toString());
                localHashMap.put("name", name.getText().toString());
                localHashMap.put("contact", contact.getText().toString());
                localHashMap.put("address", address.getText().toString());
                localHashMap.put("oldName", oldName);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        String path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        imageutils.createImage(file, filename, path, false);
        pDialog.setMessage("Uploading...");
        showDialog();
        new UploadFileToServer().execute(imageutils.getPath(uri));
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
                    GlideApp.with(getApplicationContext())
                            .load(filepath)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .skipMemoryCache(false)
                            .placeholder(R.drawable.profile)
                            .into(profiletImage);
                    imageUrl = Appconfig.ip + "/admin/uploads/" + imageutils.getfilename_from_path(filepath);
                } else {
                    imageUrl = null;
                }
                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Image not uploaded", Toast.LENGTH_SHORT).show();
            }
            hideDialog();
            // showing the server response in an alert dialog
            //showAlert(result);


            super.onPostExecute(result);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        hideDialog();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), connectionResult.getErrorMessage()
                + "", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();

                String placename = String.format("%s", place.getName());
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                String addressVal = String.format("%s", place.getAddress());
                latLon = latitude + "," + longitude;
                shopname.setText(placename);
                address.setText(addressVal);

            }
        } else {
            imageutils.onActivityResult(requestCode, resultCode, data);

        }
    }

}
