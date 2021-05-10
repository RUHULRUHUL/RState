package com.house.search_near.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.house.search_near.model.PostModel;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Location.db";
    private static final String TABLE_NAME = "Favourite_House";
    private static final String POST_TABLE= "post";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String IMAGE_ONE = "image_one";
    private static final String IMAGE_TWO = "image_two";
    private static final String IMAGE_THREE = "image_three";
    private static final String BEDROOM = "bedroom";
    private static final String BATHROOM = "bathroom";
    private static final String BELCONI = "belconi";
    private static final String CAR_PARKING = "car_parking";
    private static final String RENT_PER_MONTH = "rent_per_month";
    private static final String RENT_PERSON_NAME = "rent_person_name";
    private static final String RENT_PERSON_EMAIL = "rent_person_email";
    private static final String RENT_PERSON_MOBILE = "rent_person_mobile";
    private static final String RENT_PERSON_LOCATION = "rent_person_location";
    private static final String RENT_PERSON_IMAGE = "rent_person_image";
    private static final String PREFERRED_RENTER = "preferred_renter";
    private static final String DRAWING = "drawing";
    private static final String FLOOR_NO = "floor_no";
    private static final String GENERATOR = "generator";
    private static final String CC_TV = "cctv";
    private static final String GAS_CONNECTION = "gas_connection";
    private static final String FLOOR_TYPE = "floor_type";
    private static final String LIFT = "lift";
    private static final String DESCRIPTION = "description";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+" (id INTEGER PRIMARY KEY AUTOINCREMENT , title TEXT , image_one TEXT, image_two TEXT , " +
                "image_three TEXT ,bedroom TEXT , bathroom TEXT ,  belconi TEXT , car_parking TEXT, rent_per_month TEXT , rent_person_name TEXT ," +
                " rent_person_email TEXT , rent_person_mobile TEXT , rent_person_location TEXT , rent_person_image TEXT , preferred_renter TEXT , " +
                "drawing TEXT , floor_no TEXT , generator TEXT , cctv TEXT , gas_connection TEXT , floor_type TEXT , lift TEXT , description TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(int id , String title , String image_one , String image_two , String image_three , String bedroom , String bathroom
                                , String belconi , String car_parking , String rent_per_month , String rent_person_name , String rent_person_email
                                , String mobile , String location , String user_image , String preferred_renter , String drawing , String floor_no
                                , String generator , String cc_tv , String gas_connection , String floor_type , String lift , String description){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE , title);
        contentValues.put(IMAGE_ONE , image_one);
        contentValues.put(IMAGE_TWO , image_two);
        contentValues.put(IMAGE_THREE , image_three);
        contentValues.put(BEDROOM , bedroom);
        contentValues.put(BATHROOM , bathroom);
        contentValues.put(BELCONI , belconi);
        contentValues.put(CAR_PARKING , car_parking);
        contentValues.put(RENT_PER_MONTH , rent_per_month);
        contentValues.put(RENT_PERSON_NAME , rent_person_name);
        contentValues.put(RENT_PERSON_EMAIL , rent_person_email);
        contentValues.put(RENT_PERSON_MOBILE , mobile);
        contentValues.put(RENT_PERSON_LOCATION , location);
        contentValues.put(RENT_PERSON_IMAGE , user_image);
        contentValues.put(PREFERRED_RENTER , preferred_renter);
        contentValues.put(DRAWING , drawing);
        contentValues.put(FLOOR_NO , floor_no);
        contentValues.put(GENERATOR , generator);
        contentValues.put(CC_TV , cc_tv);
        contentValues.put(GAS_CONNECTION , gas_connection);
        contentValues.put(FLOOR_TYPE , floor_type);
        contentValues.put(LIFT , lift);
        contentValues.put(DESCRIPTION , description);

        long result = db.insert(TABLE_NAME , null , contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public ArrayList<PostModel> list(){
        ArrayList<PostModel> pm = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        if(db != null){
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    PostModel post = new PostModel();
                    post.setId(cursor.getInt(0));
                    post.setTitle(cursor.getString(1));
                    post.setImg_one(cursor.getString(2));
                    post.setImg_two(cursor.getString(3));
                    post.setImg_three(cursor.getString(4));
                    post.setBedroom(cursor.getString(5));
                    post.setBathroom(cursor.getString(6));
                    post.setBelconi(cursor.getString(7));
                    post.setCar_parking(cursor.getString(8));
                    post.setRent_per_month(cursor.getString(9));
                    post.setName(cursor.getString(10));
                    post.setEmail(cursor.getString(11));
                    post.setMobile(cursor.getString(12));
                    post.setLocation(cursor.getString(13));
                    post.setImage(cursor.getString(14));
                    post.setRenter_type(cursor.getString(15));
                    post.setDrawing(cursor.getString(16));
                    post.setFloor_no(cursor.getString(17));
                    post.setGenerator(cursor.getString(18));
                    post.setCctv(cursor.getString(19));
                    post.setGas_connection(cursor.getString(20));
                    post.setFloor_type(cursor.getString(21));
                    post.setLift(cursor.getString(22));
                    post.setDescription(cursor.getString(23));
                    // Adding contact to list
                    pm.add(post);
                } while (cursor.moveToNext());
            }
        }
        return pm;

    }

    public int Delete(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "id=?";
        String whereArgs[] = {id.toString()};
        return db.delete(TABLE_NAME , whereClause , whereArgs);
    }

}
