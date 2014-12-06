package com.boha.myapplication;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.com.boha.monitor.library.util.ResizeAnimation;
import com.com.boha.monitor.library.util.Util;


public class MainActivity extends ActionBarActivity {

    View top, middle, bottom, txtNum;
    TextView red, green, amber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        top = findViewById(R.id.top);
        middle = findViewById(R.id.middle);
        bottom = findViewById(R.id.bottom);
        red = (TextView)findViewById(R.id.TRAFF_red);
        amber = (TextView)findViewById(R.id.TRAFF_yellow);
        green = (TextView)findViewById(R.id.TRAFF_green);
        txtNum = (TextView)findViewById(R.id.TRAFF_count);
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.shrink(bottom, 1000, null);
            }
        });
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.explode(bottom, 1000, null);
            }
        });
        amber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashSeveralTimes(amber,200,10);
            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashInfinite(green, 200);
            }
        });
        txtNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(green, 200);
            }
        });
    }

    int count;
    static final int MAX_FLASHES = 30;
    private void animateLights() {

        ResizeAnimation an = new ResizeAnimation(bottom,800);
        an.setDuration(1000);
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Toast.makeText(getApplicationContext(),"Resize animation finished", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        bottom.startAnimation(an);
//        ObjectAnimator an = ObjectAnimator.ofFloat(bottom, "scaleY", 0, 0, 1, 0);
//        an.setDuration(2000);
//        an.start();
//
//        Util.flashTrafficLights(red, amber, green, Util.INFINITE_FLASHES, Util.FLASH_FAST);
//        ScaleAnimation an = new ScaleAnimation(0,0,0,1, Animation.RELATIVE_TO_SELF,Animation.RELATIVE_TO_PARENT);
//        an.setDuration(1000);
//        an.setInterpolator(new AccelerateDecelerateInterpolator());
//        //bottom.startAnimation(an);
//
//        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", 50f);
//        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", 100f);
//        ObjectAnimator.ofPropertyValuesHolder(bottom, pvhX, pvhY).start();
    }

    private void animate() {
        middle.setVisibility(View.VISIBLE);
        ObjectAnimator an = ObjectAnimator.ofFloat(middle,"alpha", 1, 0);
        ObjectAnimator an2 = ObjectAnimator.ofFloat(middle,"scaleX", 0, 1);
        an.setDuration(300);
        an.setInterpolator(new AccelerateDecelerateInterpolator());
        //an.setRepeatCount(1);
        //an.setRepeatMode(ObjectAnimator.REVERSE);
        an.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //middle.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        an.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
