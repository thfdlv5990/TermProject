package org.androidtown.lbs.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * 현재 위치의 지도를 보여주는 방법에 대해 알 수 있습니다.
 *
 * Google Play Services 라이브러리를 링크하여 사용합니다.
 * 구글맵 v2를 사용하기 위한 여러 가지 권한이 있어야 합니다.
 * 매니페스트 파일 안에 있는 키 값을 PC에 맞는 것으로 새로 발급받아서 넣어야 합니다.
 *
 * @author Mike
 */
public class MainActivity extends ActionBarActivity {


    ImageView imgview1;
    private AnimationDrawable mAnimationDrawable_2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b1 = (Button) findViewById(R.id.button);
        imgview1 = (ImageView) findViewById(R.id.imageView);

        BitmapDrawable mBitmapDrawable_1 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice1);
        BitmapDrawable mBitmapDrawable_2 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice2);
        BitmapDrawable mBitmapDrawable_3 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice3);
        BitmapDrawable mBitmapDrawable_4 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice4);
        BitmapDrawable mBitmapDrawable_5 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice5);
        BitmapDrawable mBitmapDrawable_6 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice6);
        BitmapDrawable mBitmapDrawable_7 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice7);
        BitmapDrawable mBitmapDrawable_8 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice8);
        BitmapDrawable mBitmapDrawable_9 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice9);
        BitmapDrawable mBitmapDrawable_10 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice10);
        BitmapDrawable mBitmapDrawable_11 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice11);
        BitmapDrawable mBitmapDrawable_12 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice12);
        BitmapDrawable mBitmapDrawable_13 = (BitmapDrawable)getResources().getDrawable(R.drawable.rice13);

        mAnimationDrawable_2 = new AnimationDrawable();
        mAnimationDrawable_2.setOneShot(false);

        int duration = 140;

        mAnimationDrawable_2.addFrame(mBitmapDrawable_1, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_2, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_3, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_4, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_5, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_6, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_7, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_8, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_9, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_10, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_11, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_12, duration);
        mAnimationDrawable_2.addFrame(mBitmapDrawable_13, duration);

        imgview1.setBackgroundDrawable(mAnimationDrawable_2);





    }

    public void onclick1(View v)
    {
        Intent intent = new Intent(getApplicationContext(), my_food.class);
        startActivity(intent);
    }




    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // 어플에 포커스가 갈때 시작된다
            mAnimationDrawable_2.start();
        } else {
            // 어플에 포커스를 떠나면 종료한다
            mAnimationDrawable_2.stop();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
