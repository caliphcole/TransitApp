package com.android.hermaeum.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Caliph Cole on 06/04/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "routes.db";

    public static final String ROUTES_TABLE_NAME = "routes";
    public static final String ROUTES_TABLE_ID = "id";
    public static final String ROUTE_COLUMN_ROUTE = "route";
    public static final String ROUTE_COLUMN_ORIGIN ="origin";
    public static final String ROUTE_COLUMN_DESTINATION ="destination";
    public static final String ROUTE_COLUMN_VIA = "via";
    public static final String ROUTE_COLUMN_ROUTETYPE ="route_type";


    private String CREATE_TIMETABLE;


    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);

    }




    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ROUTES_TABLE_NAME);


        //create table again
        onCreate(sqLiteDatabase);
    }



/*
Create functions
 */
    public boolean createRouteTable(){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String CREATE_ROUTE_TABLE = "CREATE TABLE " + ROUTES_TABLE_NAME + "(" + ROUTES_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + ROUTE_COLUMN_ROUTE + " TEXT," + ROUTE_COLUMN_ORIGIN + " TEXT," + ROUTE_COLUMN_DESTINATION + " TEXT," + ROUTE_COLUMN_VIA + " TEXT," + ROUTE_COLUMN_ROUTETYPE + " TEXT" + ")";
            //create timetable database table
            db.execSQL(CREATE_ROUTE_TABLE);
        }catch (Exception e){

        }
        return true;
    }
    public boolean createTimeTable(String tablename,ArrayList<String> via){

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            CREATE_TIMETABLE = "CREATE TABLE " + tablename + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ";
            for (int i = 0; i < via.size(); i++) {
                CREATE_TIMETABLE = CREATE_TIMETABLE + via.get(i) + " TEXT, ";
                if (i == via.size() - 1) {
                    CREATE_TIMETABLE = CREATE_TIMETABLE + "tag Text)";
                }

            }

            db.execSQL(CREATE_TIMETABLE);
        }catch (SQLiteException e){
            e.printStackTrace();
        }


        return false;
    }
    public boolean createTripplannerDataFeed(){

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String CREATE_TRIPPLANNER_FEED = "CREATE TABLE tripplannerfeed (route TEXT PRIMARY KEY NOT NULL,stops TEXT) ";
            db.execSQL(CREATE_TRIPPLANNER_FEED);
        }catch(SQLiteException e){
            return false;
        }
        return true;
    }

    public boolean createRouteCoordinatesTable(){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String CREATE_COORDINATES_FEED = "CREATE TABLE routecoordinates (route TEXT PRIMARY KEY NOT NULL,coordinates TEXT) ";
            db.execSQL(CREATE_COORDINATES_FEED);
        }catch(SQLiteException e){
            return false;
        }
        return true;
    }

    /*
Insert functions
 */
    public boolean insertRoute(String route, String origin, String destination, String via,String route_type)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ROUTE_COLUMN_ROUTE,route);
        contentValues.put(ROUTE_COLUMN_ORIGIN, origin);
        contentValues.put(ROUTE_COLUMN_DESTINATION,destination);
        contentValues.put(ROUTE_COLUMN_VIA, via);
        contentValues.put(ROUTE_COLUMN_ROUTETYPE,route_type);

        db.insert(ROUTES_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }
    public  boolean insertTripPlannerFeed(String route, String  via){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "Select * FROM tripplannerfeed";
        Cursor cursor = db.rawQuery(selectQuery, null);
        ContentValues contentValues = new ContentValues();

        contentValues.put(cursor.getColumnName(0), route);
        contentValues.put(cursor.getColumnName(1), via);
        db.insertOrThrow("tripplannerfeed", null, contentValues);
        db.close();

        return true;
    }

    public boolean insertTime(String timetablename,ArrayList<String> timedata,int i){

        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT  * FROM "+ timetablename;

        Cursor cursor = db.rawQuery(selectQuery, null);
        ContentValues contentValues = new ContentValues();

        switch (i){
            
            case 4:
                contentValues.put(cursor.getColumnName(1), timedata.get(0));
                contentValues.put(cursor.getColumnName(2), timedata.get(1));
                contentValues.put(cursor.getColumnName(3), timedata.get(2));
                contentValues.put(cursor.getColumnName(4), timedata.get(3));
                break;
            case 5:
                contentValues.put(cursor.getColumnName(1), timedata.get(0));
                contentValues.put(cursor.getColumnName(2), timedata.get(1));
                contentValues.put(cursor.getColumnName(3), timedata.get(2));
                contentValues.put(cursor.getColumnName(4), timedata.get(3));
                contentValues.put(cursor.getColumnName(5), timedata.get(4));
                break;
            case 6:
                contentValues.put(cursor.getColumnName(1), timedata.get(0));
                contentValues.put(cursor.getColumnName(2), timedata.get(1));
                contentValues.put(cursor.getColumnName(3), timedata.get(2));
                contentValues.put(cursor.getColumnName(4), timedata.get(3));
                contentValues.put(cursor.getColumnName(5), timedata.get(4));
                contentValues.put(cursor.getColumnName(6), timedata.get(5));
                break;
            case 7:
                contentValues.put(cursor.getColumnName(1), timedata.get(0));
                contentValues.put(cursor.getColumnName(2), timedata.get(1));
                contentValues.put(cursor.getColumnName(3), timedata.get(2));
                contentValues.put(cursor.getColumnName(4), timedata.get(3));
                contentValues.put(cursor.getColumnName(5), timedata.get(4));
                contentValues.put(cursor.getColumnName(6), timedata.get(5));
                contentValues.put(cursor.getColumnName(7), timedata.get(6));
                break;
            case 8:
                contentValues.put(cursor.getColumnName(1), timedata.get(0));
                contentValues.put(cursor.getColumnName(2), timedata.get(1));
                contentValues.put(cursor.getColumnName(3), timedata.get(2));
                contentValues.put(cursor.getColumnName(4), timedata.get(3));
                contentValues.put(cursor.getColumnName(5), timedata.get(4));
                contentValues.put(cursor.getColumnName(6), timedata.get(5));
                contentValues.put(cursor.getColumnName(7), timedata.get(6));
                contentValues.put(cursor.getColumnName(8), timedata.get(7));
                break;
            case 9:
                contentValues.put(cursor.getColumnName(1), timedata.get(0));
                contentValues.put(cursor.getColumnName(2), timedata.get(1));
                contentValues.put(cursor.getColumnName(3), timedata.get(2));
                contentValues.put(cursor.getColumnName(4), timedata.get(3));
                contentValues.put(cursor.getColumnName(5), timedata.get(4));
                contentValues.put(cursor.getColumnName(6), timedata.get(5));
                contentValues.put(cursor.getColumnName(7), timedata.get(6));
                contentValues.put(cursor.getColumnName(8), timedata.get(7));
                contentValues.put(cursor.getColumnName(9), timedata.get(8));
                break;
            case 10:
                contentValues.put(cursor.getColumnName(1), timedata.get(0));
                contentValues.put(cursor.getColumnName(2), timedata.get(1));
                contentValues.put(cursor.getColumnName(3), timedata.get(2));
                contentValues.put(cursor.getColumnName(4), timedata.get(3));
                contentValues.put(cursor.getColumnName(5), timedata.get(4));
                contentValues.put(cursor.getColumnName(6), timedata.get(5));
                contentValues.put(cursor.getColumnName(7), timedata.get(6));
                contentValues.put(cursor.getColumnName(8), timedata.get(7));
                contentValues.put(cursor.getColumnName(9), timedata.get(8));
                contentValues.put(cursor.getColumnName(10), timedata.get(9));
                break;
            case 11:
                contentValues.put(cursor.getColumnName(1), timedata.get(0));
                contentValues.put(cursor.getColumnName(2), timedata.get(1));
                contentValues.put(cursor.getColumnName(3), timedata.get(2));
                contentValues.put(cursor.getColumnName(4), timedata.get(3));
                contentValues.put(cursor.getColumnName(5), timedata.get(4));
                contentValues.put(cursor.getColumnName(6), timedata.get(5));
                contentValues.put(cursor.getColumnName(7), timedata.get(6));
                contentValues.put(cursor.getColumnName(8), timedata.get(7));
                contentValues.put(cursor.getColumnName(9), timedata.get(8));
                contentValues.put(cursor.getColumnName(10), timedata.get(9));
                contentValues.put(cursor.getColumnName(11), timedata.get(10));
                
                break;
            case 12:
                contentValues.put(cursor.getColumnName(1), timedata.get(0));
                contentValues.put(cursor.getColumnName(2), timedata.get(1));
                contentValues.put(cursor.getColumnName(3), timedata.get(2));
                contentValues.put(cursor.getColumnName(4), timedata.get(3));
                contentValues.put(cursor.getColumnName(5), timedata.get(4));
                contentValues.put(cursor.getColumnName(6), timedata.get(5));
                contentValues.put(cursor.getColumnName(7), timedata.get(6));
                contentValues.put(cursor.getColumnName(8), timedata.get(7));
                contentValues.put(cursor.getColumnName(9), timedata.get(8));
                contentValues.put(cursor.getColumnName(10), timedata.get(9));
                contentValues.put(cursor.getColumnName(11), timedata.get(10));
                contentValues.put(cursor.getColumnName(12), timedata.get(11));
                break;
            case 13:
                contentValues.put(cursor.getColumnName(1), timedata.get(0));
                contentValues.put(cursor.getColumnName(2), timedata.get(1));
                contentValues.put(cursor.getColumnName(3), timedata.get(2));
                contentValues.put(cursor.getColumnName(4), timedata.get(3));
                contentValues.put(cursor.getColumnName(5), timedata.get(4));
                contentValues.put(cursor.getColumnName(6), timedata.get(5));
                contentValues.put(cursor.getColumnName(7), timedata.get(6));
                contentValues.put(cursor.getColumnName(8), timedata.get(7));
                contentValues.put(cursor.getColumnName(9), timedata.get(8));
                contentValues.put(cursor.getColumnName(10), timedata.get(9));
                contentValues.put(cursor.getColumnName(11), timedata.get(10));
                contentValues.put(cursor.getColumnName(12), timedata.get(11));
                contentValues.put(cursor.getColumnName(13), timedata.get(12));
                
                break;
            case 14:
                contentValues.put(cursor.getColumnName(1), timedata.get(0));
                contentValues.put(cursor.getColumnName(2), timedata.get(1));
                contentValues.put(cursor.getColumnName(3), timedata.get(2));
                contentValues.put(cursor.getColumnName(4), timedata.get(3));
                contentValues.put(cursor.getColumnName(5), timedata.get(4));
                contentValues.put(cursor.getColumnName(6), timedata.get(5));
                contentValues.put(cursor.getColumnName(7), timedata.get(6));
                contentValues.put(cursor.getColumnName(8), timedata.get(7));
                contentValues.put(cursor.getColumnName(9), timedata.get(8));
                contentValues.put(cursor.getColumnName(10), timedata.get(9));
                contentValues.put(cursor.getColumnName(11), timedata.get(10));
                contentValues.put(cursor.getColumnName(12), timedata.get(11));
                contentValues.put(cursor.getColumnName(13), timedata.get(12));
                contentValues.put(cursor.getColumnName(14), timedata.get(13));
                break;
            case 15:
                contentValues.put(cursor.getColumnName(1), timedata.get(0));
                contentValues.put(cursor.getColumnName(2), timedata.get(1));
                contentValues.put(cursor.getColumnName(3), timedata.get(2));
                contentValues.put(cursor.getColumnName(4), timedata.get(3));
                contentValues.put(cursor.getColumnName(5), timedata.get(4));
                contentValues.put(cursor.getColumnName(6), timedata.get(5));
                contentValues.put(cursor.getColumnName(7), timedata.get(6));
                contentValues.put(cursor.getColumnName(8), timedata.get(7));
                contentValues.put(cursor.getColumnName(9), timedata.get(8));
                contentValues.put(cursor.getColumnName(10), timedata.get(9));
                contentValues.put(cursor.getColumnName(11), timedata.get(10));
                contentValues.put(cursor.getColumnName(12), timedata.get(11));
                contentValues.put(cursor.getColumnName(13), timedata.get(12));
                contentValues.put(cursor.getColumnName(14), timedata.get(13));
                contentValues.put(cursor.getColumnName(15), timedata.get(14));
                
                break;
            case 16:
                contentValues.put(cursor.getColumnName(1), timedata.get(0));
                contentValues.put(cursor.getColumnName(2), timedata.get(1));
                contentValues.put(cursor.getColumnName(3), timedata.get(2));
                contentValues.put(cursor.getColumnName(4), timedata.get(3));
                contentValues.put(cursor.getColumnName(5), timedata.get(4));
                contentValues.put(cursor.getColumnName(6), timedata.get(5));
                contentValues.put(cursor.getColumnName(7), timedata.get(6));
                contentValues.put(cursor.getColumnName(8), timedata.get(7));
                contentValues.put(cursor.getColumnName(9), timedata.get(8));
                contentValues.put(cursor.getColumnName(10), timedata.get(9));
                contentValues.put(cursor.getColumnName(11), timedata.get(10));
                contentValues.put(cursor.getColumnName(12), timedata.get(11));
                contentValues.put(cursor.getColumnName(13), timedata.get(12));
                contentValues.put(cursor.getColumnName(14), timedata.get(13));
                contentValues.put(cursor.getColumnName(15), timedata.get(14));
                contentValues.put(cursor.getColumnName(16), timedata.get(15));
                break;
            case 17:
                contentValues.put(cursor.getColumnName(1), timedata.get(0));
                contentValues.put(cursor.getColumnName(2), timedata.get(1));
                contentValues.put(cursor.getColumnName(3), timedata.get(2));
                contentValues.put(cursor.getColumnName(4), timedata.get(3));
                contentValues.put(cursor.getColumnName(5), timedata.get(4));
                contentValues.put(cursor.getColumnName(6), timedata.get(5));
                contentValues.put(cursor.getColumnName(7), timedata.get(6));
                contentValues.put(cursor.getColumnName(8), timedata.get(7));
                contentValues.put(cursor.getColumnName(9), timedata.get(8));
                contentValues.put(cursor.getColumnName(10), timedata.get(9));
                contentValues.put(cursor.getColumnName(11), timedata.get(10));
                contentValues.put(cursor.getColumnName(12), timedata.get(11));
                contentValues.put(cursor.getColumnName(13), timedata.get(12));
                contentValues.put(cursor.getColumnName(14), timedata.get(13));
                contentValues.put(cursor.getColumnName(15), timedata.get(14));
                contentValues.put(cursor.getColumnName(16), timedata.get(15));
                contentValues.put(cursor.getColumnName(17), timedata.get(16));
                
                
                break;
        }
        db.insert(timetablename, null, contentValues);
        db.close();
        return false;
    }

    public boolean insertCoordinates(String routename, String coordinates){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "Select * FROM routecoordinates";
        Cursor cursor = db.rawQuery(selectQuery, null);
        ContentValues contentValues = new ContentValues();

        contentValues.put(cursor.getColumnName(0), routename);
        contentValues.put(cursor.getColumnName(1),coordinates);

        db.insertOrThrow("routecoordinates", null, contentValues);
        db.close();
        return true;
    }
/*
getter functions
 */
    public ArrayList<String> getAllVia(String routenumber){
        ArrayList<String> via = new ArrayList<String>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.query(routenumber , null, null, null, null, null, null);

            for (int i = 1 ; i< c.getColumnCount()-1;i++){

                    String stringtemp ="";
                    for(String s:c.getColumnNames()[i].split("_") ){
                        Log.d("Column name", s);
                        if(s.equalsIgnoreCase("Arrive")|| s.equalsIgnoreCase("Arrive1")|| s.equalsIgnoreCase("Arrive2")||s.equalsIgnoreCase("Depart")||s.equalsIgnoreCase("Depart1")||s.equalsIgnoreCase("Depart2")){

                        }else{

                            if(stringtemp.equals("")){
                                stringtemp = s;
                            }else{
                                stringtemp = stringtemp +" "+ s;
                            }
                        }
                    }
                    via.add(stringtemp);
                }

            return  via;

        }catch (Exception e){
            return  null;
        }
    }
    public ArrayList<String> getschedulevia(String routenumber){
        Log.d("tablename", routenumber);
        Set<String> temp = new LinkedHashSet<String>();
        ArrayList<String> via = new ArrayList<String>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.query(routenumber , null, null, null, null, null, null);


            for (int i = 0 ; i< c.getColumnCount()-1;i++){

                if(i == 0 ){

                    temp.add("Select Location");
                }else if(i == c.getColumnCount()-1){

                }else{
                    String stringtemp ="";
                    for(String s:c.getColumnNames()[i].split("_") ){

                        if(s.equalsIgnoreCase("Arrive")|| s.equalsIgnoreCase("Arrive1")|| s.equalsIgnoreCase("Arrive2")||s.equalsIgnoreCase("Depart")||s.equalsIgnoreCase("Depart1")||s.equalsIgnoreCase("Depart2")){

                        }else{

                            if(stringtemp.equals("")){
                                stringtemp = s;
                            }else{
                                stringtemp = stringtemp +" "+ s;
                            }
                        }
                    }
                   temp.add(stringtemp);
                }
            }

            for(String t : temp){
                via.add(t);
            }
            return  via;

        }catch (Exception e){
            return  null;
        }
    }

    public List<TimeDataItem> getTimeQuery(String week,String route,String timeofday,String via,String deptarrival,String direction){



       if(direction.contains(" to ")) {
            List<TimeDataItem> result = new ArrayList<TimeDataItem>();

            try {
                SQLiteDatabase db = this.getReadableDatabase();
                String arrive = via.replace(" ", "_") + "_" + deptarrival;
                String arrive1 = via.replace(" ", "_") + "_" + deptarrival + 1;
                String depart = via.replace(" ", "_") + "_" + deptarrival;
                String depart1 = via.replace(" ", "_") + "_" + deptarrival + 1;


                if (deptarrival.equals("Arrive")) {
                    try {
                        Cursor arriveres = db.rawQuery("SELECT " + arrive + " FROM " + week + "_" + route, null);
                        Cursor columnnamecursor = db.rawQuery("SELECT * FROM " + week + "_" + route, null);
                        if (arriveres.moveToFirst() && columnnamecursor.moveToFirst()) {
                            do {
                                //Log.d("data", arriveres.getString(0));
                                TimeDataItem time = new TimeDataItem();

                                if(timeofday.equals("5:00am - 11:59am") && arriveres.getString(0).contains("AM")) {
                                    time.setTime(arriveres.getString(0));
                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }else if(timeofday.equals("12:00pm - 5:59pm") && arriveres.getString(0).contains("PM")&&(Integer.parseInt(arriveres.getString(0).split(":")[0])<6||Integer.parseInt(arriveres.getString(0).split(":")[0])==12 )){

                                    time.setTime(arriveres.getString(0));

                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }else if(timeofday.equals("6:00pm - 11:55pm") && arriveres.getString(0).contains("PM")&&(Integer.parseInt(arriveres.getString(0).split(":")[0])>=6&&Integer.parseInt(arriveres.getString(0).split(":")[0])!=12)){
                                    time.setTime(arriveres.getString(0));

                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }


                            } while (arriveres.moveToNext() && columnnamecursor.moveToNext());
                        }
                    } catch (Exception e) {
                        Cursor arrive1res = db.rawQuery("SELECT " + arrive1 + " FROM " + week + "_" + route, null);
                        Cursor columnnamecursor = db.rawQuery("SELECT * FROM " + week + "_" + route, null);
                        if (arrive1res.moveToFirst()&& columnnamecursor.moveToFirst()) {
                            do {

                                TimeDataItem time = new TimeDataItem();
                                if(timeofday.equals("5:00am - 11:59am") && arrive1res.getString(0).contains("AM")) {
                                    time.setTime(arrive1res.getString(0));

                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }else if(timeofday.equals("12:00pm - 5:59pm") && arrive1res.getString(0).contains("PM")&&(Integer.parseInt(arrive1res.getString(0).split(":")[0])<6||Integer.parseInt(arrive1res.getString(0).split(":")[0])==12 )){

                                    time.setTime(arrive1res.getString(0));


                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }else if(timeofday.equals("6:00pm - 11:55pm") && arrive1res.getString(0).contains("PM")&&(Integer.parseInt(arrive1res.getString(0).split(":")[0])>=6&&Integer.parseInt(arrive1res.getString(0).split(":")[0])!=12)){
                                    time.setTime(arrive1res.getString(0));

                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }


                            } while (arrive1res.moveToNext()&& columnnamecursor.moveToNext());

                        }
                    }
                } else {


                    try {
                        Cursor departres = db.rawQuery("SELECT " + depart + " FROM " + week + "_" + route, null);
                        Cursor columnnamecursor = db.rawQuery("SELECT * FROM " + week + "_" + route, null);
                        if (departres.moveToFirst()&& columnnamecursor.moveToFirst()) {
                            do {

                                TimeDataItem time = new TimeDataItem();
                                if(timeofday.equals("5:00am - 11:59am") && departres.getString(0).contains("AM")) {
                                    time.setTime(departres.getString(0));

                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }else if(timeofday.equals("12:00pm - 5:59pm") && departres.getString(0).contains("PM")&&(Integer.parseInt(departres.getString(0).split(":")[0])<6||Integer.parseInt(departres.getString(0).split(":")[0])==12 )){

                                    time.setTime(departres.getString(0));
                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }else if(timeofday.equals("6:00pm - 11:55pm") && departres.getString(0).contains("PM")&&(Integer.parseInt(departres.getString(0).split(":")[0])>=6&&Integer.parseInt(departres.getString(0).split(":")[0])!=12)){
                                    time.setTime(departres.getString(0));
                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }


                            } while (departres.moveToNext()&& columnnamecursor.moveToNext());

                        }
                    } catch (Exception e) {
                        Cursor depart1res = db.rawQuery("SELECT " + depart1 + " FROM " + week + "_" + route, null);
                        Cursor columnnamecursor = db.rawQuery("SELECT * FROM " + week + "_" + route, null);
                        if (depart1res.moveToFirst()&& columnnamecursor.moveToFirst()) {
                            do {

                                TimeDataItem time = new TimeDataItem();
                                if(timeofday.equals("5:00am - 11:59am") && depart1res.getString(0).contains("AM")) {
                                    time.setTime(depart1res.getString(0));
                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }else if(timeofday.equals("12:00pm - 5:59pm") && depart1res.getString(0).contains("PM")&&(Integer.parseInt(depart1res.getString(0).split(":")[0])<6||Integer.parseInt(depart1res.getString(0).split(":")[0])==12 )){

                                    time.setTime(depart1res.getString(0));
                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }else if(timeofday.equals("6:00pm - 11:55pm") && depart1res.getString(0).contains("PM")&&(Integer.parseInt(depart1res.getString(0).split(":")[0])>=6&&Integer.parseInt(depart1res.getString(0).split(":")[0])!=12)){
                                    time.setTime(depart1res.getString(0));
                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }


                            } while (depart1res.moveToNext()&& columnnamecursor.moveToNext());

                        }
                    }
                }

            } catch (Exception e) {

            }


            return result;
        }else{
           List<TimeDataItem> result = new ArrayList<TimeDataItem>();

            try{
                SQLiteDatabase db = this.getReadableDatabase();
                String arrive = via.replace(" ", "_") + "_" + deptarrival;
                String depart = via.replace(" ", "_") + "_" + deptarrival;
                String depart2 = via.replace(" ", "_") + "_" + deptarrival + 2;
                String arrive2 = via.replace(" ", "_") + "_" + deptarrival + 2;
                if (deptarrival.equals("Arrive")) {
                    try {
                        Cursor arriveres = db.rawQuery("SELECT " + arrive + " FROM " + week + "_" + route, null);
                        Cursor columnnamecursor = db.rawQuery("SELECT * FROM " + week + "_" + route, null);
                        if (arriveres.moveToFirst()&& columnnamecursor.moveToFirst()) {
                            do {
                                TimeDataItem time = new TimeDataItem();
                                if(timeofday.equals("5:00am - 11:59am") && arriveres.getString(0).contains("AM")) {
                                   // Log.d("5:00am - 11:59am",arriveres.getString(0));
                                    time.setTime(arriveres.getString(0));

                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }else if(timeofday.equals("12:00pm - 5:59pm") && arriveres.getString(0).contains("PM")&&(Integer.parseInt(arriveres.getString(0).split(":")[0])<6||Integer.parseInt(arriveres.getString(0).split(":")[0])==12 )){
                                              //  Log.d("12:00pm - 5:59pm",arriveres.getString(0));
                                    time.setTime(arriveres.getString(0));
                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }else if(timeofday.equals("6:00pm - 11:55pm") && arriveres.getString(0).contains("PM")&&(Integer.parseInt(arriveres.getString(0).split(":")[0])>=6&&Integer.parseInt(arriveres.getString(0).split(":")[0])!=12)){
                                    //Log.d("6:00pm - 11:55pm",arriveres.getString(0));
                                    time.setTime(arriveres.getString(0));
                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }

                            } while (arriveres.moveToNext()&& columnnamecursor.moveToNext());
                        }
                    } catch (Exception e) {
                        Cursor arrive2res = db.rawQuery("SELECT " + arrive2 + " FROM " + week + "_" + route, null);
                        Cursor columnnamecursor = db.rawQuery("SELECT * FROM " + week + "_" + route, null);
                        if (arrive2res.moveToFirst()&& columnnamecursor.moveToFirst()) {
                            do {

                                TimeDataItem time = new TimeDataItem();
                                if(timeofday.equals("5:00am - 11:59am") && arrive2res.getString(0).contains("AM")) {
                                    time.setTime(arrive2res.getString(0));
                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }else if(timeofday.equals("12:00pm - 5:59pm") && arrive2res.getString(0).contains("PM")&&(Integer.parseInt(arrive2res.getString(0).split(":")[0])<6||Integer.parseInt(arrive2res.getString(0).split(":")[0])==12 )){

                                    time.setTime(arrive2res.getString(0));
                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }else if(timeofday.equals("6:00pm - 11:55pm") && arrive2res.getString(0).contains("PM")&&(Integer.parseInt(arrive2res.getString(0).split(":")[0])>=6&&Integer.parseInt(arrive2res.getString(0).split(":")[0])!=12)){
                                    time.setTime(arrive2res.getString(0));
                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }


                            } while (arrive2res.moveToNext()&& columnnamecursor.moveToNext());

                        }
                    }
                }else {
                    try {
                        Cursor departres = db.rawQuery("SELECT " + depart + " FROM " + week + "_" + route, null);
                        Cursor columnnamecursor = db.rawQuery("SELECT * FROM " + week + "_" + route, null);

                        if (departres.moveToFirst()&& columnnamecursor.moveToFirst()) {
                            do {

                                TimeDataItem time = new TimeDataItem();
                                if(timeofday.equals("5:00am - 11:59am") && departres.getString(0).contains("AM")) {
                                    time.setTime(departres.getString(0));
                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }else if(timeofday.equals("12:00pm - 5:59pm") && departres.getString(0).contains("PM")&&(Integer.parseInt(departres.getString(0).split(":")[0])<6||Integer.parseInt(departres.getString(0).split(":")[0])==12 )){

                                    time.setTime(departres.getString(0));
                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }else if(timeofday.equals("6:00pm - 11:55pm") && departres.getString(0).contains("PM")&&(Integer.parseInt(departres.getString(0).split(":")[0])>=6&&Integer.parseInt(departres.getString(0).split(":")[0])!=12)){
                                    time.setTime(departres.getString(0));
                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }


                            } while (departres.moveToNext()&& columnnamecursor.moveToNext());

                        }
                    } catch (Exception e) {
                        Cursor depart2res  = db.rawQuery("SELECT "+depart2+" FROM "+week+"_"+ route, null);
                        Cursor columnnamecursor = db.rawQuery("SELECT * FROM " + week + "_" + route, null);
                        if (depart2res.moveToFirst() && columnnamecursor.moveToFirst()) {
                            do {

                                TimeDataItem time = new TimeDataItem();
                                if(timeofday.equals("5:00am - 11:59am") && depart2res.getString(0).contains("AM")) {
                                    time.setTime(depart2res.getString(0));
                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }else if(timeofday.equals("12:00pm - 5:59pm") && depart2res.getString(0).contains("PM")&&(Integer.parseInt(depart2res.getString(0).split(":")[0])<6||Integer.parseInt(depart2res.getString(0).split(":")[0])==12 )){

                                    time.setTime(depart2res.getString(0));
                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }else if(timeofday.equals("6:00pm - 11:55pm") && depart2res.getString(0).contains("PM")&&(Integer.parseInt(depart2res.getString(0).split(":")[0])>=6&&Integer.parseInt(depart2res.getString(0).split(":")[0])!=12)){
                                    time.setTime(depart2res.getString(0));
                                    for(int i = 1 ; i<columnnamecursor.getColumnNames().length-1;i++){

                                        time.setNexttimes(columnnamecursor.getString(i));
                                    }
                                    result.add(time);
                                }


                            } while (depart2res.moveToNext()&& columnnamecursor.moveToNext());

                        }
                    }
                }


            }catch (Exception e){

            }
            return result;
        }


    }


    public List<FeedItem> getDataByRoute(String route){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM routes WHERE route LIKE '%" + route + "%'", null);


        List<FeedItem> routeQuery = new ArrayList<FeedItem>();
        if(res.moveToFirst()){
            do{
                FeedItem item = new FeedItem();
                item.setRoute(res.getString(1));
                item.setOrigin(res.getString(2));
                item.setDestination(res.getString(3));
                item.setVia(res.getString(4));
                item.setRouteType(res.getString(5));

                routeQuery.add(item);
                Log.d("routes", "id: " + res.getString(0) + " Route: " + res.getString(1) + " Origin: " + res.getString(2));
            }while(res.moveToNext());
        }

        return routeQuery;
    }
    public ArrayList<String> getAllTimes() {
        ArrayList<String>  timeList = new ArrayList<String>();


        // Select All Query
        String selectQuery = "SELECT  * FROM route75weekday";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        String s0="sixmilesdepart: ";
        String s1="hwtarrive1: ";
        String s2="hwtdepart1: ";
        String s3="papinearrive: ";
        String s4="papinedepart: ";
        String s5="hwtarrive2: ";
        String s6="hwtdepart2: ";
        String s7="sixmilesarrive: ";
        if (cursor.moveToFirst()) {
            do {

                s0+=cursor.getString(1);
                // Log.d("origin",""+cursor.getString(2));
                s1+=cursor.getString(2);
                //Log.d("des",""+cursor.getString(3));
                s2+= cursor.getString(3);
                //Log.d("route",""+cursor.getString(1));
                s3+=cursor.getString(4);
                // Log.d("via",""+cursor.getString(4));
                s4+=cursor.getString(5);
                // Log.d("type",""+cursor.getString(5));
                s5+=cursor.getString(6);
                // Log.d("type",""+cursor.getString(5));
                s6+=cursor.getString(7);
                // Log.d("type",""+cursor.getString(5));
                s7+=cursor.getString(8);
                // Log.d("type",""+cursor.getString(5));

                // Adding contact to list

            } while (cursor.moveToNext());
            timeList.add(s0);
            timeList.add(s1);
            timeList.add(s2);
            timeList.add(s3);
            timeList.add(s4);
            timeList.add(s5);
            timeList.add(s6);
            timeList.add(s7);
        }

        // return contact list
        return timeList;
    }


    /*
    Nearby functionality
     */

    public FeedItem getBusStopData(FeedItem item, Location location){


        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM tripplannerfeed";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                try {
                    if(item.getRoute().contains(cursor.getString(0))){
                        float previous = 0;
                        String [] temparray = cursor.getString(1).split(";");
                        for(int i = 0; i<temparray.length;i++){
                            Location location2= new Location("");
                            location2.setLatitude(Double.parseDouble(temparray[i].split(",")[0]));
                            location2.setLongitude(Double.parseDouble(temparray[i].split(",")[1]));
                            float t  = location.distanceTo(location2);


                            if(t==0){
                                item.setBusstopcord(location2);
                                item.setBusStopName(temparray[i].split(",")[2]);
                                return item;
                            }else if(t< previous && previous>0){

                                item.setBusstopcord(location2);
                                item.setBusStopName(temparray[i].split(",")[2]);
                                previous = t;
                            }else if (previous ==0){

                                previous = t;
                            }
                            }
}
                } catch (Exception e) {

                }
            } while (cursor.moveToNext());
        }


        return item;
    }
      /*
       Route functionality
        */
       // Getting All Routes
    public List<FeedItem> getAllRoutes() {
        List<FeedItem>  routeList = new ArrayList<FeedItem>();


        // Select All Query
        String selectQuery = "SELECT  * FROM " + ROUTES_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                FeedItem item = new FeedItem();
                item.setOrigin(cursor.getString(2));
                // Log.d("origin",""+cursor.getString(2));
                item.setDestination(cursor.getString(3));
                //Log.d("des",""+cursor.getString(3));
                item.setRoute(cursor.getString(1));
                //Log.d("route",""+cursor.getString(1));
                item.setVia( cursor.getString(4));
                // Log.d("via",""+cursor.getString(4));
                item.setRouteType(cursor.getString(5));
                // Log.d("type",""+cursor.getString(5));

                // Adding contact to list
                routeList.add(item);
            } while (cursor.moveToNext());
        }

        // return contact list
        return routeList;
    }
    // Getting All Routes
    public List<FeedItem> getAllRoutesCheck() {
        List<FeedItem>  routeList = new ArrayList<FeedItem>();


        // Select All Query
        String selectQuery = "SELECT  * FROM " + ROUTES_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                FeedItem item = new FeedItem();
                item.setOrigin(cursor.getString(2));
                // Log.d("origin",""+cursor.getString(2));
                item.setDestination(cursor.getString(3));
                //Log.d("des",""+cursor.getString(3));
                item.setRoute(cursor.getString(1));
                //Log.d("route",""+cursor.getString(1));
                item.setVia(cursor.getString(4));
                // Log.d("via",""+cursor.getString(4));
                item.setRouteType(cursor.getString(5));
                // Log.d("type",""+cursor.getString(5));

                // Adding contact to list
                routeList.add(item);
            } while (cursor.moveToNext());
        }

        // return contact list
        return routeList;
    }



   /*
   Trip planner functionality methods helptextquery and tripplannerqueryroute
    */

    public List<TripDataItem> tripPlannerQueryRoute(String org, String des){


       Set<TripDataItem> routeorg = new LinkedHashSet<TripDataItem>();
       Set<TripDataItem> routedes = new LinkedHashSet<TripDataItem>();
       Set<TripDataItem> resulttemp = new LinkedHashSet<TripDataItem>();
       List<TripDataItem> result = new ArrayList<TripDataItem>();
       String query = "SELECT * FROM tripplannerfeed";

       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor= db.rawQuery(query, null);


       if (cursor.moveToFirst()) {
           do {
               try {
                    int count = 0;
                   for(final String a: cursor.getString(1).split(";")){
                       //Log.d("via",a);

                       if(a.split(",")[2].trim().toLowerCase().contains(org.toLowerCase())){
                           TripDataItem data = new TripDataItem();
                           Log.d("routeorg", cursor.getString(0));

                           data.setRoutes(cursor.getString(0));
                           data.setRouteVia(a.split(",")[2].trim());
                           data.setCount(count);
                           routeorg.add(data);

                       }
                       if(a.split(",")[2].trim().toLowerCase().contains(des.toLowerCase())){
                           TripDataItem data = new TripDataItem();
                           Log.d("routedes", cursor.getString(0));

                           data.setRoutes(cursor.getString(0));
                           data.setRouteVia(a.split(",")[2].trim());
                           data.setCount(count);
                           routedes.add(data);

                       }
                       count++;
                   }
               } catch (Exception e) {

               }
           } while (cursor.moveToNext());

       }

       Log.d("routeorg size",""+ routeorg.size());
       Log.d("routedes size",""+ routedes.size());

       int count = 1;
        int max = Math.max(routeorg.size(),routedes.size());
       for (TripDataItem x:routeorg){

           for(TripDataItem y:routedes){
               Log.d("x&y",x.getRoutes()+" and "+ y.getRoutes());
               if(x.getRoutes().equals(y.getRoutes())){

                  result.add(x);

               }

           }
           count++;
       }
        if(!result.isEmpty()) {

            return result;
        }

       for(TripDataItem a: routeorg){

           for(TripDataItem b: routedes){

               String [] temp1 = a.getRouteVia().split(",");
               String [] temp2 = b.getRouteVia().split(",");

               for( int c =0; c<temp1.length;c++){

                   for( int d =0; d<temp2.length;d++){

                       if(temp1[c].toLowerCase().trim().contains(temp2[d].toLowerCase().trim())){
                           TripDataItem temproute = new TripDataItem();
                           temproute.setRoutes(a.getRoutes() + "_" + b.getRoutes());
                           temproute.setContVia(temp1[c]);
                           // a.setContVia(temp1[c].toLowerCase()+"_"+c);
                          // a.setRoutes(a.getRoutes() + "_" + b.getRoutes());
                           Log.d("Routes", a.getRoutes());
                           //Log.d("contViaA", a.getContVia());
                           // b.setContVia(temp2[d].toLowerCase() + "_" + d);
                           //Log.d("contViaB", b.getContVia());
                            resulttemp.add(temproute);
                           //a.setRoutes(a.getRoutes().split("_")[0]);
                            //resulttemp.add(b);
                       }
                   }

               }
           }
       }


       //Log.d("Result route",result.get(0).getRoutes());
      Log.d("Result size",""+resulttemp.size());
       for(TripDataItem q : resulttemp){
           Log.d("item",q.getRoutes());
       }
       for(TripDataItem ti:resulttemp){
           result.add(ti);
       }
       return result;
    }

    public ArrayList<String> helpTextQuery(){
        ArrayList<String> tempStringArray = new ArrayList<String>();// = new String[1];

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT stops FROM tripplannerfeed";

        Cursor cursor= db.rawQuery(query, null);


        if (cursor.moveToFirst()) {
            do {
                try {

                    int a = cursor.getString(0).split(";").length;
                    for(int i = 0 ; i<a;i++){
                        String s =cursor.getString(0).split(";")[i];

                            if(!tempStringArray.contains(s.split(",")[2].trim())) {
                                tempStringArray.add(s.split(",")[2].trim());
                            }



                    }
                } catch (Exception e) {

                }
            } while (cursor.moveToNext());
        }

        return tempStringArray;
    }


    public String findNearestBusStop(LatLng org){
        String temp = "";
        float previous=0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT stops FROM tripplannerfeed";

        Cursor cursor= db.rawQuery(query, null);

        Location location1 = new Location("");
        location1.setLatitude(org.latitude);
        location1.setLongitude(org.longitude);



        if (cursor.moveToFirst()) {
            do {
                try {
                    String [] temparray = cursor.getString(0).split(";");
                    for(int i = 0; i<temparray.length;i++) {
                        Location location2 = new Location("");
                        location2.setLatitude(Double.parseDouble(temparray[i].split(",")[0]));
                        location2.setLongitude(Double.parseDouble(temparray[i].split(",")[1]));
                        float t = location1.distanceTo(location2);
                        if(t==0){

                            return temparray[i].split(",")[2];
                        }else if(t< previous && previous>0){

                            temp = temparray[i].split(",")[2];
                            previous = t;
                        }else if (previous ==0){

                            previous = t;
                        }
                    }

                } catch (Exception e) {

                }
            } while (cursor.moveToNext());
        }

        Log.d("nearby bus stop",temp);
        return  temp;
    }



    /*
    map Functionalities
     */
    public String searchrouteMap(String route_number){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM routecoordinates";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                try {

                    if(route_number.equalsIgnoreCase(cursor.getString(0))){

                        return cursor.getString(1);
                    }
                } catch (Exception e) {

                }
            } while (cursor.moveToNext());
        }


        return "";
    }

    /*
       number of columns, number of tables and rows
        */
    public int tableRowCount(String tablename){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            int numRows = (int) DatabaseUtils.queryNumEntries(db, tablename);
            return numRows;
        }catch(Exception e){
            e.printStackTrace();
            return 99999;
        }

    }
    public int tableColumnCount(String table){

        int i =0;
        String selectQuery = "SELECT  * FROM "+ table;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        i = cursor.getColumnCount()-1;
        return i;
    }
    public String getAllTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        String s="";
        if(c.moveToFirst()){
            do {

                s += "," + c.getString(0);


            }while(c.moveToNext());
        }
        return  s;
    }


    public boolean checkIfTableExist(String tablename){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        ArrayList<String> listOfTables = new ArrayList<String>();
        if(c.moveToFirst()){

            do{
                listOfTables.add(c.getString(0));
            }while(c.moveToNext());
        }

        return !listOfTables.contains(tablename);
    }

}




