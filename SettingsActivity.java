package com.example.karup.clockspot;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by karup on 12/23/2016.
 */

public class SettingsActivity extends AppCompatActivity {
    boolean is_logged_in = false;

    Dialog d;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String imageURI;

    private static final String USER_LOGGED_IN = "USER_LOGGED_IN";
    private static final String USERNAME = "USERNAME";
    String uname;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "Clockspot";
    private Uri fileUri;
    ImageView profile;
    Button newImage, chooseImage, removeImage, changePass, changePin, about;
    private static int RESULT_LOAD_IMAGE = 1;
    DatabaseHelperActivity helper = new DatabaseHelperActivity(this);
    Intent intent;
    String picturePath;

    //String sharedUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Intent intent = getIntent();
        uname = intent.getExtras().getString("uname");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(uname.toUpperCase());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        // Sets the Toolbar to act as the ActionBar for this Activity window.
//        // Make sure the toolbar exists in the activity and is not null
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appbarcolor)));

        profile = (ImageView) findViewById(R.id.profileView);
        newImage = (Button) findViewById(R.id.setImageBtn);
        chooseImage = (Button) findViewById(R.id.chooseImageBtn);
        removeImage = (Button) findViewById(R.id.removeImage);
        changePass = (Button) findViewById(R.id.changePassword);
        changePin = (Button) findViewById(R.id.changeSecurituPin);
        about = (Button) findViewById(R.id.about);

        imageURI = helper.getImagePath(uname);
        //Toast.makeText(getApplicationContext(), "" + imageURI, Toast.LENGTH_SHORT).show();

        if (imageURI.equals("") || (imageURI.equals("imageSrc"))) {
            profile.setImageDrawable(getResources().getDrawable(R.drawable.user));
        } else {
            try {
                // bimatp factory
                BitmapFactory.Options options = new BitmapFactory.Options();

                // downsizing image as it throws OutOfMemory Exception for larger
                // images
                options.inSampleSize = 8;

                final Bitmap bitmap = BitmapFactory.decodeFile(imageURI,
                        options);
                profile.setImageBitmap(bitmap);
            } catch (NullPointerException e) {

                e.printStackTrace();
            }

        }


        newImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();
            }
        });

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                } catch (Exception e) {
                    Log.v("Settings", "ChooseImageOnCLick : Caught Exception" + e.getMessage());
                    Toast.makeText(getApplicationContext(), "There was something wrong, Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeImageMethod();
            }
        });


        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, About.class);
                intent.putExtra("uname", uname);
                startActivity(intent);
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d = new Dialog(SettingsActivity.this);
                d.setTitle("Change Password");
                d.setContentView(R.layout.change_password);

                final EditText userD = (EditText) d.findViewById(R.id.userNameDialog);
                final EditText pass1 = (EditText) d.findViewById(R.id.pass1Dialog);
                final EditText pass2 = (EditText) d.findViewById(R.id.pass2Dialog);
                Button submit = (Button) d.findViewById(R.id.submitDialog);
                Button cancel = (Button) d.findViewById(R.id.cancelDialog);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userDialog = userD.getText().toString();
                        String password1 = pass1.getText().toString();
                        String password2 = pass2.getText().toString();
                        if (!userDialog.equals(uname)) {
                            userD.setBackgroundColor(Color.RED);
                            Toast.makeText(getApplicationContext(),
                                    "username do not match with your account, please try again", Toast.LENGTH_SHORT)
                                    .show();
                        } else if (password1.equals("")) {
                            userD.setBackgroundColor(Color.TRANSPARENT);
                            pass1.setBackgroundColor(Color.RED);
                            Toast.makeText(getApplicationContext(),
                                    "Passwords cannot be empty", Toast.LENGTH_SHORT)
                                    .show();

                        } else if (((password1.length() < 4) || (password1.toString().length() > 15))) {
                            pass1.setBackgroundColor(Color.RED);
                            pass2.setBackgroundColor(Color.RED);
                            Toast.makeText(getApplicationContext(), "Passwords length must be between 4 to 15 characters", Toast.LENGTH_SHORT).show();


                        } else if (!password1.equals(password2)) {

                            pass1.setBackgroundColor(Color.RED);
                            pass2.setBackgroundColor(Color.RED);
                            Toast.makeText(getApplicationContext(),
                                    "Passwords do not match , please try again", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            pass1.setBackgroundColor(Color.TRANSPARENT);
                            pass2.setBackgroundColor(Color.TRANSPARENT);

                            try {
                                helper.changePassword(userDialog, password1);
                                dismissDialogBox();
                                Toast.makeText(getApplicationContext(),
                                        "Password changed successfully!!", Toast.LENGTH_SHORT)
                                        .show();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(),
                                        "Password cannot be changed right now, please try again", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissDialogBox();
                    }
                });
                d.show();


            }
        });

        changePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d = new Dialog(SettingsActivity.this);
                d.setTitle("Change Security Pin");
                d.setContentView(R.layout.change_pin);

                final EditText userD = (EditText) d.findViewById(R.id.userNameDialog);
                final EditText pin1 = (EditText) d.findViewById(R.id.pass1Dialog);
                final EditText pin2 = (EditText) d.findViewById(R.id.pass2Dialog);
                Button submit = (Button) d.findViewById(R.id.submitDialog);
                Button cancel = (Button) d.findViewById(R.id.cancelDialog);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userDialog = userD.getText().toString();
                        String securityPin1 = pin1.getText().toString();
                        String securityPin2 = pin2.getText().toString();
                        if (!userDialog.equals(uname)) {
                            userD.setBackgroundColor(Color.RED);
                            Toast.makeText(getApplicationContext(),
                                    "username do not match with your account, please try again", Toast.LENGTH_SHORT)
                                    .show();
                        } else if (securityPin1.equals("")) {
                            userD.setBackgroundColor(Color.TRANSPARENT);
                            pin1.setBackgroundColor(Color.RED);
                            Toast.makeText(getApplicationContext(),
                                    "Security Pin cannot be empty", Toast.LENGTH_SHORT)
                                    .show();

                        } else if (!securityPin1.equals(securityPin2)) {

                            pin1.setBackgroundColor(Color.RED);
                            pin2.setBackgroundColor(Color.RED);
                            Toast.makeText(getApplicationContext(),
                                    "Security Pins do not match , please try again", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            pin1.setBackgroundColor(Color.TRANSPARENT);
                            pin2.setBackgroundColor(Color.TRANSPARENT);

                            try {
                                helper.changePin(userDialog, securityPin1);
                                dismissDialogBox();
                                Toast.makeText(getApplicationContext(),
                                        "Security Pin changed successfully!!", Toast.LENGTH_SHORT)
                                        .show();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(),
                                        "Security Pin cannot be changed right now, please try again", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissDialogBox();
                    }
                });
                d.show();


            }
        });

// Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_SHORT).show();
            // will close the app if the device does't have camera
            finish();
        }

    }

    private void removeImageMethod() {
        new AlertDialog.Builder(this)
                .setTitle("Remove?")
                .setMessage("Are you sure you want to remove profile image?")
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        profile.setImageResource(R.drawable.user);
                        helper.setImagePath(uname, "imageSrc");
                    }
                }).create().show();
    }

    private void dismissDialogBox() {
        if ((d != null) && d.isShowing()) {
            d.dismiss();
        }
    }


    /*
     * Capturing Camera Image will lauch camera app request image capture
	 */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        // Toast.makeText(getApplicationContext(),""+fileUri,Toast.LENGTH_SHORT).show();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }


    /*
     * Here we store the file url as it will be null after returning from camera
	 * app
	 */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }


    /**
     * Checking device has camera hardware or not
     */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            helper.setImagePath(uname, picturePath);

            profile.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        } else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }


    /*
     * Display image from a path to ImageView
	 */
    private void previewCapturedImage() {
        try {
            // hide video preview


            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
            //Toast.makeText(getApplicationContext(),""+fileUri,Toast.LENGTH_SHORT).show();
            picturePath = fileUri.getPath();
            helper.setImagePath(uname, picturePath);
            profile.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    /*
     * Creating file uri to store image/video
	 */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {


        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.homePage:
//                intent = new Intent(SettingsActivity.this, RecyclerViewActivityHome.class);
//                intent.putExtra("uname", uname);
//                startActivity(intent);
//                // showToast("Home Clicked");
                finish();
                return true;
            case R.id.logout:
                // showToast("Logout Clicked");
                sharedPreferences = getApplicationContext().getSharedPreferences(USER_LOGGED_IN, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(USER_LOGGED_IN, is_logged_in);
                editor.putString(USERNAME, uname);
                editor.commit();

                new AlertDialog.Builder(this)
                        .setTitle("Logout?")
                        .setMessage("Are you sure you want to log out?")
                        .setNegativeButton(R.string.no, null)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                intent = new Intent(SettingsActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).create().show();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.only_logout, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Go Back?")
                .setMessage("Are you sure you want to go back?")
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        SettingsActivity.super.onBackPressed();
                    }
                }).create().show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

