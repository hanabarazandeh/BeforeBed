package ca.sfu.beforebed;

import android.graphics.Bitmap;

public class MyDay {
    //used in MyAdapter class, to organize the different values of a database row
    String date;
    Bitmap memoryBitmap;
    Bitmap doodleBitmap;
    String gratitude;
    String UID;
    int mood;

    public MyDay(String UID, String date, Bitmap memoryBitmap, Bitmap doodleBitmap, String gratitude, int mood) {
        this.UID = UID;
        this.date = date;
        this.memoryBitmap = memoryBitmap;
        this.doodleBitmap = doodleBitmap;
        this.gratitude = gratitude;
        this.mood= mood;

    }
}
