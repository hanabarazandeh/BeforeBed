package ca.sfu.beforebed;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    public ArrayList<MyDay> myDays;

    //the constructor takes an arrayList of type MyDay, which is an object that holds the 4 values
    //that the user enters and is also saved into database

    public MyAdapter(Context context, ArrayList<MyDay> myDays){

        this.myDays=myDays;
        this.context=context;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
        //puts the data in ViewHolder
        //myDay.date holds the value for the current day object's date
        //and I set the text of dateTextView to that
        //same thing for other ones too

//        holder.setPosition(position);
        MyDay myDay= myDays.get(position);
        holder.dateTextView.setText(myDay.date);
        holder.gratitudeTextView.setText(myDay.gratitude);
        holder.memoryImgView.setImageBitmap(myDay.memoryBitmap);
        holder.doodleImgView.setImageBitmap(myDay.doodleBitmap);
        holder.UIDTextView.setText(myDay.UID);
//        Log.d("MOOD", "onBindViewHolder: "+myDay.mood);
        if (myDay.mood==-1){
            holder.moodImgView.setVisibility(View.GONE);
        }

        if (myDay.memoryBitmap==null){
            holder.memoryCardView.setVisibility(View.GONE);
//            Log.d("MEMORY", String.valueOf("onBindViewHolder: "+myDay.memoryBitmap==null));
        }
        if (myDay.doodleBitmap==null){
            holder.doodleCardView.setVisibility(View.GONE);
        }
        if (myDay.mood==MoodActivity.SAD) {
            //https://stackoverflow.com/questions/8642823/using-setimagedrawable-dynamically-to-set-image-in-an-imageview
            holder.moodImgView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.sad_normal));
        } else if (myDay.mood==MoodActivity.NEUTRAL)
            holder.moodImgView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.neutral_normal));
        else if (myDay.mood==MoodActivity.HAPPY)
            holder.moodImgView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.happy_normal));

    }


    @Override
    public int getItemCount() {
        return myDays.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //the layout of one row
        public CardView memoryCardView, doodleCardView;
        public ImageView memoryImgView, doodleImgView;
        public TextView gratitudeTextView, dateTextView, UIDTextView;
        public LinearLayout myLayout;
        public ImageView moodImgView;

        Context context;
//        private ArrayList<MyDay> myDays;
//        private MyDay myDay;
//        private int position;

        public MyViewHolder(View view) {
            super(view);
//            this.myDays=myDays;
//            myDay= myDays.get(position);

            myLayout = (LinearLayout) itemView;
            memoryImgView=  view.findViewById(R.id.memoryImgView);
            doodleImgView= view.findViewById(R.id.doodleImgView);
            gratitudeTextView=view.findViewById(R.id.gratitudeTextView);
            dateTextView=view.findViewById(R.id.dateTextView);
            UIDTextView=view.findViewById(R.id.UIDTextView);
            moodImgView= view.findViewById(R.id.moodImgView);

            doodleCardView= view.findViewById(R.id.doodleCardView);
            memoryCardView= view.findViewById(R.id.memoryCardView);



//            dateTextView.setText(myDay.date);
//            gratitudeTextView.setText(myDay.gratitude);
//            memoryImgView.setImageBitmap(myDay.memoryBitmap);
//            doodleImgView.setImageBitmap(myDay.doodleBitmap);



            itemView.setOnClickListener(this);
            context = itemView.getContext();

        }
        //when the current view is clicked, it starts recycler activity, but this time
        //it puts extra in the intent, which is the value for the gratitude string
        //this will be used in recycler activity to detect which item of the database was clicked
        //and to only show that in recycler activity

        @Override
        public void onClick(View view) {
            Toast.makeText(context,
                    "You have clicked " + ((TextView)view.findViewById(R.id.dateTextView)).getText().toString(),
                    Toast.LENGTH_SHORT).show();
            Intent i = new Intent(view.getContext(), MyDayActivity.class);
//            Log.d("UID_CHECK", "onClick: "+ myDay.UID);
            //it detects correcly
//            i.putExtra("UID_VAL", myDay.UID);
            i.putExtra("UID_VAL", UIDTextView.getText().toString());
            view.getContext().startActivity(i);

        }

//        public void setMyDay(MyDay myDay) {
//            this.myDay=myDay;
//        }

//        public void setPosition(int position) {
//            this.position=position;
//        }
    }
}