package org.androidtown.lbs.map;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/*
 현재위치와, 마커표시, 리스트뷰 출력
 */

public class my_food extends ActionBarActivity {

    private GoogleMap map;
    static Double latitude;
    static Double longitude;
    static DBHelper dbHelper;
    int cnt=0;
    static ListView listview ;
    static ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_food);

        Button food_add = (Button) findViewById(R.id.button2);
        Button refresh = (Button) findViewById(R.id.refresh);

        dbHelper = new DBHelper(getApplicationContext(), "MYFood.db", null, 1);

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MYFood", null);

        //데이터 베이스 내용 리스트뷰 출력
        while(cursor.moveToNext())
        {
            //리스트뷰 초기화
            String temp_title = cursor.getString(1);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.rice1), temp_title, "non");

        }

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                ListItem item = (ListItem) parent.getItemAtPosition(position) ;
                Intent intent = new Intent(getApplicationContext(), LocalListviewActivity.class);
                String p1 = Integer.toString(position);
                intent.putExtra("itemi", position);
                startActivity(intent);

                // TODO : use item data.
            }
        }) ;

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {//길게 클릭했을 때
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, final long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle("삭제");
                alert.setMessage("이 리스트를 삭제하시겠습니까?");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();

                        //db.delete();
                        adapter.deleteItem(position);
                        adapter.notifyDataSetChanged();
                        //DB delete하는 명령, 리스트뷰 갱신

                    }
                });
                alert.show();
                return false;
            }
        });


        // 지도 객체 참조
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        startLocationService();
    }

    public void onclicked3(View v)
    {
        Intent intent = new Intent(getApplicationContext(), add_food.class);
        intent.putExtra("value", latitude);
        intent.putExtra("value2", longitude);
        startActivity(intent);
    }

    public void refreshclicked(View v)
    {
        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MYFood", null);


        while(cursor.moveToNext())
        {
            //리스트뷰 추가
            String temp_title = cursor.getString(1);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.rice1), temp_title, "non");

            //지도에 마커 추가
            MarkerOptions optSecond = new MarkerOptions();
            optSecond.position(new LatLng(cursor.getDouble(3), cursor.getDouble(4)));
            optSecond.title(cursor.getString(1));
            map.addMarker(optSecond).showInfoWindow();

            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    /**
     * 현재 위치 확인을 위해 정의한 메소드
     */
    private void startLocationService() {
        // 위치 관리자 객체 참조
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 리스너 객체 생성
        GPSListener gpsListener = new GPSListener();
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
