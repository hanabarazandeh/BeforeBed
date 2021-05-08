package ca.sfu.beforebed;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.widget.Toast;

public class MyDatabase {
    private Context context;
    //this helper object helps us interact with the database object
    private final DatabaseHelper helper;

    public MyDatabase(Context c) {
        context = c;
        helper = new DatabaseHelper(context);
    }


    public long insertData(String date, Bitmap memory, Bitmap doodle, String gratitude, int mood) {

        //this is called inside addToDatabase() of MainActivity
        //memory and doodle bitmap could possibly be null, if user hasnt made an entry for them
        //so only if they are not null, I should create a byte array based on the bitmap
        //and that is done using the utility class that has this method
        //and I put them in contentValues only if the bitmaps werent null
        long id = -1;
        try {
            //create a SQLiteDatabase instance using the helper which is DatabaseHelper
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            if (memory!= null){
                byte[] memoryInBytes = DbBitmapUtility.getBytes(memory);
                contentValues.put(Constants.MEMORY, memoryInBytes);
            }
            if (doodle!= null){
                byte[] doodleInBytes = DbBitmapUtility.getBytes(doodle);
                contentValues.put(Constants.DOODLE, doodleInBytes);
            }
            //no need to check for null for date and gratitude, because its ok if they are null
            contentValues.put(Constants.DATE, date);
            contentValues.put(Constants.GRATITUDE, gratitude);
            contentValues.put(Constants.MOOD, mood);
//            Log.d("MOOD", "insertData: "+mood);

            //and finally pass that contentValues object to the insert() method of db
            //if id is -1, it means it wasnt successful
            id = db.insert(Constants.TABLE_NAME,Constants.MEMORY, contentValues);
            if (id != -1) {
                Toast.makeText(context, "data added to database", Toast.LENGTH_LONG).show();
                db.close();
            } else Toast.makeText(context, "failed to add data", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        //and the method returns the value of id
        return id;

    }

    //this is called in RecyclerActivity, when I want to show either all the data, or just one
    //if showing all the data, userTypeInput would be null, and the selection would be null too
    //
    public Cursor getData(String userTypeInput)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String selection = null;
        //this is for the case that recycleractivity is opened from viewResults
        if (userTypeInput!=null)
//            selection =Constants.UID + "='" +userTypeInput+ "'";
            selection =Constants.UID + "='" +userTypeInput+ "'";

        String[] columns = {Constants.UID, Constants.DATE, Constants.MEMORY, Constants.DOODLE, Constants.GRATITUDE, Constants.MOOD };
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, selection, null, null, null, null);
        return cursor;
    }

    public int deleteRow(String[] whereArgs){
        SQLiteDatabase db = helper.getWritableDatabase();
//        String[] whereArgs = {"herb"};
        int count = db.delete(Constants.TABLE_NAME, Constants.UID + "=?", whereArgs);
        return count;
    }

}
