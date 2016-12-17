package org.androidtown.lbs.map;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/*
목록을 클릭시 그 목록에대한 정보를 보여준다.
 */

public class LocalListviewActivity extends ActionBarActivity {

    int cnt =0;
    private GoogleMap map;
    static Double latitude;
    static Double longitude;
    static DBHelper dbHelper;
    static DBHelper2 dbHelper2;
    static ListView listview ;
    static ListViewAdapter adapter;
    static String current_point;
    static byte[] food_image=null;
    ImageView foodImg;
    Bitmap foodBit=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_listview);
        TextView title = (TextView) findViewById(R.id.textView4);
        TextView grade = (TextView) findViewById(R.id.textView5);

        foodImg = (ImageView) findViewById(R.id.img1);

        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        int position = intent.getExtras().getInt("itemi");

        dbHelper = new DBHelper(getApplicationContext(), "MYFood.db", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MYFood", null);

        dbHelper2 = new DBHelper2(getApplicationContext(), "FoodImage.db", null, 1);
        SQLiteDatabase db2 = dbHelper2.getReadableDatabase();
        Cursor cursor2= db2.rawQuery("SELECT * FROM FoodImage", null);


        //커서를 현재 position이랑 맞춤 db접근해서 현재 listview의 내용을 지정해준다
        while(cursor.moveToNext())
        {
            //cursor2.moveToNext();
            //position+1 이라는거에 주의
            if(cursor.getInt(0)  == position+1)
            {
                current_point = cursor.getString(1);
                title.setText(cursor.getString(1));
                grade.setText(cursor.getString(2));
                latitude = cursor.getDouble(3);
                longitude = cursor.getDouble(4);

                //cursor2.moveToNext();
                //food_image = cursor2.getBlob(1);
                //foodBit = byteArrayToBitmap(food_image);
                //foodImg.setImageBitmap(foodBit);
                //food_image = null;
            }
        }

        for(int i=0;i<position+1;i++)
        {
            cursor2.moveToNext();
            food_image = cursor2.getBlob(1);
            foodBit = byteArrayToBitmap(food_image);
            foodImg.setImageBitmap(foodBit);
        }

        // 지도 객체 참조
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        startLocationService();
    }

    public Bitmap byteArrayToBitmap(byte[] byteArray){
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        byteArray = null;
        return bitmap;
    }

    /**
     * 현재 위치 확인을 위해 정의한 메소드
     */
    private void startLocationService() {
        // 위치 관리자 객체 참조
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 리스너 객체 생성
        LocalListviewActivity.GPSListener gpsListener = new LocalListviewActivity.GPSListener();
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
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            String msg = "Latitude : "+ latitude + "\nLongitude:"+ longitude;
            Log.i("GPSLocationService", msg);

            // 현재 위치의 지도를 보여주기 위해 정의한 메소드 호출
            showCurrentLocation(latitude, longitude);
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
            optSecond.title(current_point); // 제목 미리보기
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
