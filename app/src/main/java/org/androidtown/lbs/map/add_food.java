package org.androidtown.lbs.map;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/*
음식점에 대한 정보를 입력
 */
public class add_food extends ActionBarActivity {

    private GoogleMap map;
    static DBHelper dbHelper;
    static DBHelper2 dbHelper2;
    double lati;
    double longi;
    int cnt =0;
    File file = null;
    ImageView imageView1;
    public static final int REQUEST_IMAGE_CAPTURE = 1001;
    Button btn4;
    byte[] food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        btn4 = (Button) findViewById(R.id.button4);
        dbHelper2 = new DBHelper2(getApplicationContext(), "FoodImage.db", null, 1);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        try {
            file = createFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //현재 위도경도 받아오기
        Intent intent = getIntent();
        lati = intent.getDoubleExtra("value", -1);
        longi = intent.getDoubleExtra("value2", -1);


        dbHelper = new DBHelper(getApplicationContext(), "MYFood.db", null, 1);

        //데이터베이스 인풋영역
        final EditText title = (EditText) findViewById(R.id.databaseNameInput3);
        final EditText grade = (EditText) findViewById(R.id.databaseNameInput2);

        Button save = (Button) findViewById(R.id.button3);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String input_title = title.getText().toString();
                String input_content = grade.getText().toString();

                dbHelper.insert(input_title, input_content, lati, longi);
                Toast.makeText(getApplicationContext(), "기록되었습니다.", Toast.LENGTH_LONG).show();

                finish();
            }
        });

        // 지도 객체 참조
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        startLocationService();
    }

    public void on4Clicked(View v)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private File createFile() throws IOException {
        String imageFileName = "test.jpg";
        File storageDir = Environment.getExternalStorageDirectory();
        File curFile = new File(storageDir, imageFileName);

        return curFile;
    }

    public byte[] bitmapToByteArray(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50 , stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            if (file != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                food = bitmapToByteArray(bitmap);
                dbHelper2.insert(food);
                imageView1.setImageBitmap(bitmap);
            } else {
                Toast.makeText(getApplicationContext(), "File is null.", Toast.LENGTH_LONG).show();
            }
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

    /**
     * 현재 위치 확인을 위해 정의한 메소드
     */
    private void startLocationService() {
        // 위치 관리자 객체 참조
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 리스너 객체 생성
        add_food.GPSListener gpsListener = new add_food.GPSListener();
        //메소드가 호출되는 최소시간 10초, 최소거리0
        long minTime = 10000;
        float minDistance = 0;

        // GPS 기반 위치 요청
        manager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime,
                minDistance,
                gpsListener);

        // 네트워크 기반 위치 요청
        manager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime,
                minDistance,
                gpsListener);
    }

    /**
     * 리스너 정의
     */
    private class GPSListener implements LocationListener {
        /**
         * 위치 정보가 확인되었을 때 호출되는 메소드
         */
        public void onLocationChanged(Location location) {
            lati = location.getLatitude();
            longi = location.getLongitude();

            String msg = "Latitude : "+ lati + "\nLongitude:"+ longi;
            Log.i("GPSLocationService", msg);

            // 현재 위치의 지도를 보여주기 위해 정의한 메소드 호출
            showCurrentLocation(lati, longi);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }

    /**
     * 현재 위치의 지도를 보여주기 위해 정의한 메소드
     *
     * @param latitude
     * @param longitude
     */
    private void showCurrentLocation(Double latitude, Double longitude) {
        // 현재 위치를 이용해 LatLon 객체 생성
        LatLng curPoint = new LatLng(latitude, longitude);

        if(cnt ==0) {
            //마커객체 생성
            MarkerOptions optSecond = new MarkerOptions();
            optSecond.position(new LatLng(latitude, longitude));// 위도 • 경도
            optSecond.title("현재위치"); // 제목 미리보기
            map.addMarker(optSecond).showInfoWindow();
            cnt++;
        }

        //currentPosition 위치로 카메라 중심을 옮기고 화면 줌을 조정한다. 줌범위는 2~21, 숫자클수록 확대
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 17));
        map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);

        // 지도 유형 설정. 지형도인 경우에는 GoogleMap.MAP_TYPE_TERRAIN, 위성 지도인 경우에는 GoogleMap.MAP_TYPE_SATELLITE
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }
}
