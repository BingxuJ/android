package com.example.bingxu.emergencyactivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class SelectPicActivity extends Activity{

    /** Called when the activity is first created. */

    Button but,upload_image;
    ImageView img;

    String file_str = Environment.getExternalStorageDirectory().getPath();
    File mars_file = new File(file_str + "/my_camera");
    File file_go = new File(file_str + "/my_camera/file.jpg");
    private Bitmap photo;
    private Bitmap bm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_photo);
        but = (Button) findViewById(R.id.my_camare_button);
        upload_image=(Button)findViewById(R.id.upload_image);
        img = (ImageView) findViewById(R.id.my_img_view);
        img.setImageResource(R.drawable.tabbar_camera);


        //taking photos
        but.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                // sd card
                if (Environment.MEDIA_MOUNTED.equals(Environment
                        .getExternalStorageState())) {
                    // if there is no parent, create one
//                    if (!mars_file.exists()) {
                        mars_file.mkdirs();
//                    }

     /*//find child set
     if(!file_go.exists())
     {
      try {
       file_go.createNewFile();
      } catch (IOException e) {
      }}
    */
                    // set system photos activity：MediaStore.ACTION_IMAGE_CAPTURE ;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // the method of storing and the route

//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file_go));
                    //jump to the window of taking photo
                    startActivityForResult(intent, 3023);
                } else {
                    Toast.makeText(SelectPicActivity.this, "Pleas insert sd card",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        //upload
        upload_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                if(file_go.exists())
//                {
                    //authorise the photos
                    //URLconnection ，HttpURLconnection.......
                    Toast.makeText(SelectPicActivity.this, "Uploading....",
                            Toast.LENGTH_LONG).show();

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 75, bos);
                    byte[] data = bos.toByteArray();
                    String imageAsString= Base64.encodeToString(data, Base64.DEFAULT);
                    Intent intent = new Intent();
                    intent.putExtra("rp", imageAsString);
                    setResult(RESULT_OK, intent);
                    finish();
            }
        });

    }

    //after taking a photo and show the photo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        // check if the photo is correct
        if (requestCode == 3023 && resultCode == this.RESULT_OK) {

            Bundle bundle = data.getExtras();
            photo = (Bitmap) bundle.get("data");
            img.setImageBitmap(photo);


        } else {
            System.out.println("No photos");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
