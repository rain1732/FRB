package com.example.frbpro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhotoActivity extends AppCompatActivity {
    public static final String TESS_DATA = "/tessdata";
    public static final int TAKE_PHOTO = 1024;
    public static final int SELECT_PHOTO  = 1023;
    private static final String TAG = PhotoActivity.class.getSimpleName();
    private TessBaseAPI tessBaseAPI;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        //textView = this.findViewById(R.id.textView);
        checkPermission();

        this.findViewById(R.id.button_select).setOnClickListener(v -> selectPictureFromGalleryIntent());
        this.findViewById(R.id.button_take).setOnClickListener(v -> {
            checkPermission();
            dispatchTakePictureIntent();
        });
        Button tutorialButton = findViewById(R.id.button_tutorial);
        tutorialButton.setOnClickListener(new MyClick());


    }

    public class MyClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intent = new Intent(PhotoActivity.this, TutorialActivity.class);
            startActivity(intent);
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 120);
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 121);
        }
    }

    private void selectPictureFromGalleryIntent() {
        Intent selectPictureIntent = new Intent(Intent.ACTION_PICK);
        selectPictureIntent.setType("image/*");
        startActivityForResult(selectPictureIntent, SELECT_PHOTO);
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                prepareTessData();
                startOCR();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Result canceled.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Activity result failed.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == SELECT_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = this.getContentResolver().query(imageUri, filePathColumn,
                        null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                //picturePath就是图片在储存卡所在的位置
                mCurrentPhotoPath = cursor.getString(columnIndex);
                cursor.close();

                prepareTessData();
                startOCR();
            }
        }
    }

    private void prepareTessData() {
        try {
            File dir = getExternalFilesDir(TESS_DATA);
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    Toast.makeText(getApplicationContext(),
                            "The folder " + dir.getPath() + "was not created", Toast.LENGTH_SHORT).show();
                }
            }
            String[] fileList = getAssets().list("");
            for (String fileName : fileList) {
                String pathToDataFile = dir + "/" + fileName;
                if (!(new File(pathToDataFile)).exists()) {
                    InputStream in = getAssets().open(fileName);
                    OutputStream out = new FileOutputStream(pathToDataFile);
                    byte[] buff = new byte[1024];
                    int len;
                    while ((len = in.read(buff)) > 0) {
                        out.write(buff, 0, len);
                    }
                    in.close();
                    out.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void startOCR() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 6;
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
            String result = this.getText(bitmap);
            matchText(result);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private String getText(Bitmap bitmap) {
        try {
            tessBaseAPI = new TessBaseAPI();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        String dataPath = getExternalFilesDir("/").getPath() + "/";
        tessBaseAPI.init(dataPath, "eng+chi_sim");
        tessBaseAPI.setImage(bitmap);
        String retStr = "No result";
        try {
            retStr = tessBaseAPI.getUTF8Text();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        tessBaseAPI.end();
        return retStr;
    }

    private void matchText(String text) {
        Pattern itemPattern = Pattern.compile("(尿酸).*?(\\d+\\.?\\d+)"); //TODO 检测项目名用get方法获取
        Pattern randomPattern = Pattern.compile("\\d+\\.?\\d+");
        Matcher itemMatcher = itemPattern.matcher(text);
        Matcher randomMatcher = randomPattern.matcher(text);
        String itemData;

        if (itemMatcher.find()) {
            Toast.makeText(PhotoActivity.this, "识别项目成功", Toast.LENGTH_SHORT).show();
            itemData = itemMatcher.group(2);
        } else if (randomMatcher.find()) {
            Toast.makeText(PhotoActivity.this, "数据可能有误，请检查", Toast.LENGTH_SHORT).show();
            itemData = randomMatcher.group(0);
        } else {
            Toast.makeText(PhotoActivity.this, "程序君做不到qwq...", Toast.LENGTH_SHORT).show();
            itemData = "";
        }

        EditText content = new EditText(this);
        content.setText(itemData);
        content.setPadding(35,40,30,35);
        AlertDialog dataCheckDialog = new AlertDialog.Builder(this)
                .setTitle("读取数据确认 : 尿酸")    //TODO 检测项目名用get方法获取
                .setView(content)
                .setPositiveButton("确认", (dialogInterface, i) -> {
                    Toast.makeText(PhotoActivity.this, "已确认", Toast.LENGTH_SHORT).show();
                    //TODO
                    // 从content这个EditText中获取数据,http传输
                })
                .setNegativeButton("取消", (dialogInterface, i) ->
                        Toast.makeText(PhotoActivity.this, "已取消", Toast.LENGTH_SHORT).show()
                )
                .create();
        dataCheckDialog.show();
    }
}