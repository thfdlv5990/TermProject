package org.androidtown.lbs.map;

/*
사진을 관리해줄 데이터베이스 정의
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;

public class DBHelper2 extends SQLiteOpenHelper implements Serializable{


    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper2(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 MONEYBOOK이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */
        db.execSQL("CREATE TABLE FoodImage (_id INTEGER PRIMARY KEY AUTOINCREMENT, food BLOB);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(byte[] food) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();

        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO FoodImage(food) VALUES(?);",new Object[]{food});

        db.close();
    }

    //db 데이터 삭제
    public void delete(String title) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM MYFood WHERE title='" + title + "';");
        db.close();
    }


    public String getResult() {
        // 읽기가 가능하게 DB 열기


        return "데이터저장";
    }

}


