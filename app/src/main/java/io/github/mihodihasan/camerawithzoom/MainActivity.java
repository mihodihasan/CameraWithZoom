package io.github.mihodihasan.camerawithzoom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    Matrix matrix=new Matrix();
    Float scale=1f;
    ScaleGestureDetector sgd;

    private static final int CODE = 1;
    Button captureButton;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        captureButton=(Button)findViewById(R.id.captureBtn);
        imageView=(ImageView)findViewById(R.id.imageView);


    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale=scale*detector.getScaleFactor();
            scale=Math.max(1f,Math.min(scale,5f));
            matrix.setScale(scale,scale);
            imageView.setImageMatrix(matrix);
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        sgd.onTouchEvent(event);
        return true;
    }

    public void capture(View view) {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getFile()));
        startActivityForResult(intent,CODE);
    }

    public File getFile(){
        File folder=new File(Environment.getExternalStorageDirectory()+"/cameraPic");
        Log.d("LSNLSN", folder.getAbsolutePath());
        if (!folder.exists()){
            folder.mkdir();
        }

        File file=new File(folder,"img.jpg");
        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path=Environment.getExternalStorageDirectory()+"/cameraPic/img.jpg";
//        imageView.setImageDrawable(Drawable.createFromPath(path));
        Bitmap bitmap=BitmapFactory.decodeFile(path);


        int nh = (int) ( bitmap.getHeight() * (2048.0 / bitmap.getWidth()) );
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 2048, nh, true);
        imageView.setImageBitmap(scaled);
        sgd=new ScaleGestureDetector(this,new ScaleListener());

//        Log.d("RESULTIMAGE", "onActivityResult: " + Drawable.createFromPath(path));
//        Picasso.with(this).load(new File(path)).into(imageView);
    }
}
