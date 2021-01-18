package com.iBring_user.app.courier;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.iBring_user.app.Constants.URLHelper;
import com.iBring_user.app.Models.ParcelTypeModel;
import com.iBring_user.app.R;
import com.iBring_user.app.Utils.permission.Permission;
import com.iBring_user.app.Utils.permission.PermissionGranted;
import com.iBring_user.app.retrofit.RetrofitResponse;
import com.iBring_user.app.retrofit.RetrofitService;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddParcelDetails extends AppCompatActivity implements RetrofitResponse, PermissionGranted, View.OnClickListener {
   public static RelativeLayout rlNext;

   public static ImageView imgMenu;

   public static Spinner spFragile;

   public static Spinner spParcelType;

  public static RelativeLayout rl;

   public static ImageView ivParcel;

    public static TextView tvImage;

   public static TextView tvNotes;

    public static String isFragile = "";
    public static  String parcelId = "";
    String[] fragileValues = {"Select Fragile", "Yes", "No"};

    String[] parcelType = {"Select Parcel Type"};

    ArrayList<ParcelTypeModel> parcelTypeList = new ArrayList<>();
    ArrayList<String> parcelTypeListName = new ArrayList<>();


    public void findIds()
    {
        rlNext=(RelativeLayout)findViewById(R.id.rlNext);
        imgMenu=(ImageView) findViewById(R.id.imgMenu);
        spFragile=(Spinner) findViewById(R.id.spFragile);
        spParcelType=(Spinner) findViewById(R.id.spParcelType);
        rl=(RelativeLayout) findViewById(R.id.rl);
        ivParcel=(ImageView) findViewById(R.id.ivParcel);
        tvImage=(TextView) findViewById(R.id.tvImage);
        tvNotes=(TextView) findViewById(R.id.tvNotes);
        etWeight=(EditText) findViewById(R.id.etWeight);

        rlNext.setOnClickListener(this);
        imgMenu.setOnClickListener(this);
        rl.setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_parcel_details);
        findIds();
//        ButterKnife.bind(this);
        Permission.checkPermission(this, this);
        requestStoragePermission();
        setSpinner();
        callParcelType();
    }

    public void ImageSelection()
    {
        CardView cvTakePic, cvGallery, cvCancel;
        TextView takePicture,gallery;

        final Dialog alert = new Dialog(this);
        alert.setContentView(R.layout.camera_options);

        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = WindowManager.LayoutParams.MATCH_PARENT;

        alert.getWindow().setLayout(width, height);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams params = alert.getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;

        alert.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

     /*   cvTakePic = (CardView) alert.findViewById(R.id.cvTakePic);
        cvGallery = (CardView) alert.findViewById(R.id.cvGallery);*/
        cvCancel = (CardView) alert.findViewById(R.id.cvCancel);

        takePicture = (TextView) alert.findViewById(R.id.takePicture);
        gallery = (TextView) alert.findViewById(R.id.gallery);

        gallery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(AddParcelDetails.this);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                alert.dismiss();
            }
        });

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                takePicture();
                try
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 2);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                alert.dismiss();
            }

        });

        cvCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                alert.dismiss();
            }
        });

        alert.show();
    }

    //Requesting permission
    private void requestStoragePermission()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_CODE);
    }

    private static final int STORAGE_PERMISSION_CODE = 123;

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE)
        {
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
                ImageSelection();
            }
            else
            {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void setSpinner()
    {
        ArrayAdapter<String> adapter11 = new ArrayAdapter<String>(this, R.layout.custom_spinner, parcelType);
        spParcelType.setAdapter(adapter11);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_spinner, fragileValues);
        spFragile.setAdapter(adapter);


        spFragile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 0)
                {
                    isFragile = "";
                    Log.e("isFragile ", isFragile);
                }
                else
                {
                    isFragile = spFragile.getSelectedItem() + "";
                    Log.e("isFragileSelect ", isFragile);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void callParcelType()
    {
        new RetrofitService(this, this, URLHelper.GET_PARCEL_TYPE,
                500, 1, "1").callService(true);

    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.imgMenu:
                Log.e("backClick ","backClck");
                onBackPressed();
                break;

            case R.id.rlNext:
                Log.e("createCourier  ","creationClick");

                if (checkValidations())
                {
                    Intent intent=new Intent(AddParcelDetails.this,CourierAddReceiverDetails.class);
                    startActivity(intent);
                }


                break;

            case R.id.rl:
                ImageSelection();
                break;
        }
    }

    String user_pic_path0="";
    public static File file;
    Bitmap bitmap = null;
//    Camera camera;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if (data!=null)
        {
            switch (requestCode)
            {
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK)
                    {
                        Uri resultUri = result.getUri();

                        String path=resultUri.getPath();
                        user_pic_path0=resultUri.getPath();
                        file=new File(path);

                        Log.e("Paaaath","Paaaath"+path);
                        Log.e("Fileeee","Fileeee"+file);

                        try
                        {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                        ivParcel.setImageBitmap(bitmap);
//                        tvImage.setText(file.getName());
                        tvImage.setText("Image Selected");

                    }
                    else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
                    {
                        Exception error = result.getError();
                    }
                    break;

                  /*  case 4:
                    if(camera!=null)
                    {
                        user_pic_path0 = camera.getCameraBitmapPath();
                        Log.e( "onActivityResult: ImagePath",user_pic_path0);
                        ivParcel.setImageBitmap(camera.getCameraBitmap());
                        file=new File(user_pic_path0);
                        Log.e("ISFile",file.isFile()+"");
                        Log.e("directory ",file.isDirectory()+"");
                        Log.e("NAme",file.getName()+"");
                    }
                    break;
                    */


                case 2:
                    Log.e("2  ","2");
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ivParcel.setImageBitmap(bitmap);

                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "taxiNanny";
                    user_pic_path0=path;
                    file = new File(path);

                    if (!file.exists())
                    {
                        try
                        {
                            file.mkdirs();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                    file = new File(file, System.currentTimeMillis() + ".png");
//                    tvImage.setText(file.getName());
                    tvImage.setText("Image Selected");


                    if (file.exists()) {
                        file.delete();
                    }
                    FileOutputStream fileOutputStream=null;
                    try {
                        fileOutputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 85, fileOutputStream);
                        try {
                            fileOutputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    }

public static     EditText etWeight;


    public boolean checkValidations()
    {
        if (parcelId.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please choose parcel type", Toast.LENGTH_SHORT).show();
            return false;
        }else if (etWeight.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please enter parcel's weight", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (isFragile.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please choose fragile value", Toast.LENGTH_SHORT).show();
            return false;
        }else if (tvNotes.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please enter delivery notes", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (file==null)
        {
            Toast.makeText(this, "Choose courier image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onResponse(int RequestCode, String response)
    {
        switch (RequestCode)
        {
            case 500:
                Log.e("ParcelTypeList ", response);
                try {
                    parcelTypeList.clear();
                    parcelTypeListName.clear();

                    JSONObject jsonObject=new JSONObject(response);

                    if (jsonObject.getString("status").equalsIgnoreCase("success"))
                    {
                        JSONArray result=jsonObject.getJSONArray("result");

                        if (result.length()>0)
                        {


                            parcelTypeListName.add("Select Parcel Type");

                            if (result.length() > 0)
                            {
                                for (int i = 0; i < result.length(); i++)
                                {
                                    JSONObject parcelJson = result.getJSONObject(i);
                                    ParcelTypeModel parcelTypeModel = new ParcelTypeModel();
                                    parcelTypeModel.setId(parcelJson.getString("id"));
                                    parcelTypeModel.setParcelName(parcelJson.getString("parcel_type"));

                                    parcelTypeList.add(parcelTypeModel);
                                    parcelTypeListName.add(parcelJson.getString("parcel_type"));
                                }
                            }
                        }
                    }



                    Log.e("parcelTypeListSize ", parcelTypeList.size() + "");
                    Log.e("parcelTypeListNameSize ", parcelTypeListName.size() + "");

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_spinner, parcelTypeListName);
                    spParcelType.setAdapter(adapter);

                    spParcelType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                        {
                            if (position == 0) {
                                parcelId = "";
                                Log.e("parcelIdd ", parcelId);
                            } else {
                                parcelId = parcelTypeList.get(position - 1).getId() + "";
                                Log.e("parcelId ", parcelId);
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent)
                        {

                        }
                    });
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                break;

        }

    }

    @Override
    public void showPermissionAlert(ArrayList<String> permissionList, int code) {

    }
}
