package com.identity.arx.faculty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.identity.arx.R;
import com.identity.arx.camerafacedetection.FaceTrackerActivity;
import com.identity.arx.db.UserDetailTable;
import com.identity.arx.general.AppSharedPrefrence;
import com.identity.arx.general.WebUrl;
import com.identity.arx.httpasynctask.AsyncResponse;
import com.identity.arx.httpasynctask.HttpAsyncTask;
import com.identity.arx.imagemanupulation.ImageConverter;
import com.identity.arx.imagemanupulation.ResizeImage;
import com.identity.arx.objectclass.LoginResponseObject;
import com.identity.arx.objectclass.UploadProfilePic;

import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements OnClickListener {
    private static final String TEMP_IMAGE_NAME = "tempImage";
    int YOUR_SELECT_PICTURE_REQUEST_CODE = 5;
    TextView aadhaar;
    Bitmap bitmapImg;
    TextView contact;
    TextView deptname;
    TextView design;
    TextView email;
    FloatingActionButton floatingActionButton;
    ImageView imageView;
    private SharedPreferences sharedPreference;
    TextView userid;

    class C07991 implements AsyncResponse {
        C07991() {
        }

        public void asyncResponse(ResponseEntity<?> responseEntity) {
            String data = "imageResponse";
            ProfileFragment.this.loadImageFromStorage(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/IdentityAXS Profile/");
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("My Profile");
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        CollapsingToolbarLayout name_title = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout1);
        this.deptname = (TextView) view.findViewById(R.id.department);
        this.contact = (TextView) view.findViewById(R.id.faculty_contact);
        this.design = (TextView) view.findViewById(R.id.designation);
        this.email = (TextView) view.findViewById(R.id.emailId);
        this.aadhaar = (TextView) view.findViewById(R.id.aadhar);
        this.imageView = (ImageView) view.findViewById(R.id.profileImageview);
        this.floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabuploadImage);
        this.floatingActionButton.setOnClickListener(this);
        LoginResponseObject loginResponseObject = new UserDetailTable(getContext()).getUserDetails();
        name_title.setTitle(loginResponseObject.getName());
        this.deptname.setText(loginResponseObject.getDeptName());
        this.contact.setText(loginResponseObject.getContact());
        this.aadhaar.setText(loginResponseObject.getAdhaarNum());
        this.email.setText(loginResponseObject.getEmail());
        this.design.setText(loginResponseObject.getDesignation());
        this.sharedPreference = AppSharedPrefrence.getSharedPrefrence(getActivity());
        return view;
    }

    public void onClick(View v) {
        startActivityForResult(getPickImageIntent(getActivity()), this.YOUR_SELECT_PICTURE_REQUEST_CODE);
    }

    public Intent getPickImageIntent(Context context) {
        List<Intent> intentList = new ArrayList();
        Intent pickIntent = new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI);
        Intent takePhotoIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        takePhotoIntent.putExtra("return-data", true);
        takePhotoIntent.putExtra("output", Uri.fromFile(getTempFile(context)));
        intentList = addIntentsToList(context, addIntentsToList(context, intentList, pickIntent), takePhotoIntent);
        if (intentList.size() <= 0) {
            return null;
        }
        Intent chooserIntent = Intent.createChooser((Intent) intentList.remove(intentList.size() - 1), "Profile Photo");
        chooserIntent.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) intentList.toArray(new Parcelable[0]));
        return chooserIntent;
    }

    private static File getTempFile(Context context) {
        File imageFile = new File(context.getExternalCacheDir(), TEMP_IMAGE_NAME);
        imageFile.getParentFile().mkdirs();
        return imageFile;
    }

    private static List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentActivities(intent, 0)) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }
        return list;
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        }
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("_data"));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.YOUR_SELECT_PICTURE_REQUEST_CODE) {
            getActivity();
            if (resultCode == -1) {
                boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals("android.media.action.IMAGE_CAPTURE");
                    }
                }
                String filename = null;
                if (isCamera) {
                    try {
                        this.bitmapImg = new ResizeImage().getBitmap(Uri.fromFile(getTempFile(getActivity())));
                        this.bitmapImg = RotateBitmap(this.bitmapImg, 0.0f);
                        this.imageView.setImageBitmap(this.bitmapImg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    this.bitmapImg = null;
                    Uri selectedImageUri = data == null ? null : data.getData();
                    Cursor cur = getActivity().managedQuery(selectedImageUri, new String[]{"orientation"}, null, null, null);
                    try {
                        String imagePath = getRealPathFromURI(selectedImageUri);
                        filename = imagePath.substring(imagePath.lastIndexOf("/") + 1);
                        this.bitmapImg = new ResizeImage().getBitmap(Uri.parse(imagePath));
                        this.bitmapImg = RotateBitmap(this.bitmapImg, 0.0f);
                        this.imageView.setImageBitmap(this.bitmapImg);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                if (this.bitmapImg != null) {
                    new FaceTrackerActivity().storeImage(this.bitmapImg);
                    this.imageView.setImageBitmap(this.bitmapImg);
                    uploadImage(filename);
                }
            }
        }
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void uploadImage(String fName) {
        byte[] bitmapString = ImageConverter.bitmapToByte(this.bitmapImg);
        UploadProfilePic uploadProfilePic = new UploadProfilePic();
        uploadProfilePic.setImage(bitmapString);
        uploadProfilePic.setLoginStatus(Integer.valueOf(1));
        uploadProfilePic.setUserName("a@arxtechaxs.com");
        uploadProfilePic.setRole(Integer.valueOf(Integer.parseInt(this.sharedPreference.getString("LABEL_ID", ""))));
        uploadProfilePic.setMsg(fName);
        new HttpAsyncTask(getActivity(), WebUrl.SERVER_ADDRESS + this.sharedPreference.getString("INTITUTE_ID", "") + "/saveImage", uploadProfilePic, new C07991()).execute(new ResponseEntity[0]);
    }

    private void loadImageFromStorage(String path) {
        File f = new File(path);
        Options options = new Options();
        options.inPreferredConfig = Config.ARGB_8888;
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
