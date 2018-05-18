package com.example.pavanshah.consumesafe.bus;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pavanshah.consumesafe.R;
import com.example.pavanshah.consumesafe.support.MyPagerAdapter;

public class FeatureActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private MyPagerAdapter myPagerAdapter;
    private LinearLayout Dots_layout;
    private ImageView[] dots;
    private Button nextbtn,skipbtn;
    private int[] layouts={R.layout.first_slide,R.layout.second_slide,R.layout.third_slide,R.layout.fourth_slide,R.layout.fifth_slide};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.container);
        myPagerAdapter=new MyPagerAdapter(layouts,this);
        mViewPager.setAdapter(myPagerAdapter);
        Dots_layout=(LinearLayout)findViewById(R.id.dot_layout);
        nextbtn=(Button)findViewById(R.id.next_btn);
        skipbtn=(Button)findViewById(R.id.skip_btn);
        nextbtn.setOnClickListener(this);
        skipbtn.setOnClickListener(this);
        createDots(0);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                createDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


        private void createDots(int current_postion){
                if(Dots_layout!=null){
                Dots_layout.removeAllViews();
                }
                dots= new ImageView[layouts.length];
                for(int i=0;i<layouts.length;i++){
                dots[i]=new ImageView(this);
                if(i==current_postion){
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dots));
                }
                else{
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.default_dots));
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(4,0,4,0);
                Dots_layout.addView(dots[i],params);
                }
            }


    @Override
    public void onClick(View v) {

            switch (v.getId()){
                case R.id.next_btn:
                    moveForward();
                break;

                case R.id.skip_btn:
                    loadHome();
                break;
            }
    }

    private void moveForward() {
        if (mViewPager != null ) {

            Log.d("slide", "current "+mViewPager.getCurrentItem());
            Log.d("slide", "page "+mViewPager.getAdapter().getCount());

            if(mViewPager.getCurrentItem() < 4)
            {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, false);
            }
            else
            {
                loadHome();
            }
        }
    }

    private void loadHome()
    {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
