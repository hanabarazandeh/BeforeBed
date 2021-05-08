package ca.sfu.beforebed;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class MainActivity extends AppCompatActivity{

    private TextView nextTextView;
    private ViewPager viewPager;
    private LinearLayout layoutDots;
    private IntroPref introPref;
    private int[] layouts;

    private TextView[] dots;
    private MyViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.slidesTheme);
        setContentView(R.layout.slides);

        introPref= new IntroPref(this);
        if (!introPref.isFirstTimeLaunch()){

            //if there is password

            SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
            String password = sharedPrefs.getString("password", MeActivity.DEFAULT);
//            Log.d("password", "onCreate: "+password);
            if (password!=MeActivity.DEFAULT && password!=null){
                launchPassCodeActivity();
            }
            //if there is no password
            else{
                launchEntryActivity();
            }

            finish();
            //else show a message
            //noo move everything from here to IntroActivity
            //and MainActivity should be for passcode
            //nooo

        }
        //its first time
        else{
            nextTextView = findViewById(R.id.nextTextView);
            viewPager = findViewById(R.id.viewPager);
            layoutDots = findViewById(R.id.layoutDots);

            layouts= new int[]{
                    R.layout.intro_one,
                    R.layout.intro_two,
                    R.layout.intro_three
            };
            if (nextTextView!=null) {
                nextTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int current = getItem(+1);
                        if (current < layouts.length) {
                            viewPager.setCurrentItem(current);
                        } else {
                            launchEntryActivity();
                        }
                    }
                });

                viewPagerAdapter = new MyViewPagerAdapter();
                viewPager.setAdapter(viewPagerAdapter);
                viewPager.addOnPageChangeListener(onPageChangeListener);
                addBottomDots(0);
            }
            else{

            }
            introPref.setIsFirstTimeLaunch(false);
        }


    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == layouts.length-1){
                nextTextView.setText("start");
            }
            else{
                nextTextView.setText("next");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void addBottomDots(int currentPage){
        dots= new TextView[layouts.length];
        int [] activeColors = getResources().getIntArray(R.array.active);
        int [] inActiveColors = getResources().getIntArray(R.array.inactive);

        layoutDots.removeAllViews();
        for (int i =0; i< dots.length; i++){
            dots[i]= new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(50);
            dots[i].setTextColor(inActiveColors[currentPage]);
            layoutDots.addView(dots[i]);
        }
        if (dots.length>0){
            dots[currentPage].setTextColor(activeColors[currentPage]);
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {

        LayoutInflater layoutInflater;
        @Override
        public int getCount() {
            return layouts.length;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater= (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view= (View) object;
            container.removeView(view);
        }
    }

    private void launchEntryActivity() {
        startActivity(new Intent(MainActivity.this,EntryActivity.class));

    }
    private void launchPassCodeActivity() {
        startActivity(new Intent(MainActivity.this,PasscodeActivity.class));

    }

    private int getItem(int it){
        return viewPager.getCurrentItem()+1;
    }


}