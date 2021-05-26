package pro.network.webringadmin.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Appconfig {

    //Key values
    public static final String shopIdKey = "shopIdKey";
    public static final String mypreference = "mypref";

    public static final String ip = "http://thestockbazaar.com/admin/e-commerce/we_bring";


    public static String URL_IMAGE_UPLOAD = ip + "/fileUpload.php";
    public static String BANNER_CREATE = ip + "/fileFeed.php";
    public static String BANNER_GET_ALL = ip + "/get_all_feed.php";
    public static String BANNER_DELETE = ip + "/fileDelete.php";

    //Stack
    public static final String PRODUCT_CREATE = ip + "/create_stock.php";
    public static final String PRODUCT_UPDATE = ip + "/update_stock.php";
    public static final String PRODUCT_GET_ALL = ip + "/dataFetchAll.php";
    public static final String PRODUCT_DELETE = ip + "/delete_stock.php";
    //Categories
    public static final String CATEGORIES_CREATE = ip + "/create_category.php";
    public static final String CATEGORIES_UPDATE = ip + "/update_category.php";
    public static final String CATEGORIES_DELETE = ip + "/delete_category.php";
    public static final String CATEGORIES_GET_ALL = ip + "/get_all_category.php";


    //Banner
    public static final String BANNERS_CREATE = ip + "/create_banner.php";
    public static final String BANNERS_UPDATE = ip + "/update_stock.php";
    public static final String BANNERS_GET_ALL = ip + "/dataFetchAll_banner.php";
    public static final String BANNERS_DELETE = ip + "/delete_banner.php";
    //Order
    public static final String ORDER_GET_ALL = ip + "/dataFetchAll_order.php";
    public static final String ORDER_CHANGE_STATUS = ip + "/order_change_status.php";


    public static final String IMAGE_URL = ip + "/images/";
    public static Glide locationMap;

    public static String getResizedImage(String path, boolean isResized) {
        if (isResized) {
            return IMAGE_URL + "small/" + path.substring(path.lastIndexOf("/") + 1);
        }
        return path;
    }


    public static String compressImage(String filePath) {

        //String filePath = getRealPathFromURI(imageUri, context);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 1000.0f;
        float maxWidth = 1000.0f;
//        float imgRatio = actualWidth / actualHeight;
//        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {

            if (actualWidth > actualHeight) {
                float tempRatio = maxWidth / actualWidth;
                actualHeight = (int) (tempRatio * actualHeight);
                actualWidth = (int) maxWidth;
            }else  if (actualWidth < actualHeight) {
                float tempRatio = maxHeight / actualHeight;
                actualWidth = (int) (tempRatio * actualWidth);
                actualHeight = (int) maxHeight;
            }else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
//            if (imgRatio < maxRatio) {
//                imgRatio = maxHeight / actualHeight;
//                actualWidth = (int) (imgRatio * actualWidth);
//                actualHeight = (int) maxHeight;
//            } else if (imgRatio > maxRatio) {
//                imgRatio = maxWidth / actualWidth;
//                actualHeight = (int) (imgRatio * actualHeight);
//                actualWidth = (int) maxWidth;
//            } else {
//                actualHeight = (int) maxHeight;
//                actualWidth = (int) maxWidth;
//
//            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }
    private static void showDialog(ProgressDialog pDialog) {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private static void hideDialog(ProgressDialog pDialog) {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
     public static String[] getSubCatFromCat(String category) {
        if (stringMap.containsKey(category)) {
            return stringMap.get(category);
        }
        return new String[]{};
    }

    public static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI, Context context) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static String convertTimeToLocal(String time) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(time);
            df.setTimeZone(TimeZone.getDefault());
            return df.format(date);
        } catch (Exception e) {
            return time;
        }
    }

    public static Map<String, String[]> stringMap = new HashMap<String, String[]>() {{
        put("Fashion",new String[]{});
        put("Mobiles and Tablets",new String[]{});
        put("Consumer Electronics",new String[]{});
        put("Books",new String[]{});
        put("Baby Products",new String[]{});
           }};

    public static String[] CATEGORY = new String[]{
            "Fashion", "Mobiles and Tablets","Consumer Electronics","Books","Baby Products",
    };

    public static DefaultRetryPolicy getPolicy() {
        return new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }
}
