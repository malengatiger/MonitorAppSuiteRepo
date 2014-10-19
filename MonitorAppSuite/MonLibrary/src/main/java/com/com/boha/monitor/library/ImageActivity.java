package com.com.boha.monitor.library;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.CompanyStaffDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.transfer.PhotoUploadDTO;
import com.com.boha.monitor.library.util.PhotoCache;
import com.com.boha.monitor.library.util.Util;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageActivity extends Activity {

    TextView txtNumber, txtTitle, txtSubTitle, txtNext, txtPrev;
    ImageView imageView;
    String url;
    PhotoUploadDTO photoUpload;
    ProjectDTO project;
    ProjectSiteDTO projectSite;
    CompanyStaffDTO companyStaff;
    PhotoCache photoCache;
    int index;
    Context ctx;
    float x1, x2;
    float y1, y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ctx = getApplicationContext();

        photoCache = (PhotoCache) getIntent().getSerializableExtra("photoCache");
        project = (ProjectDTO) getIntent().getSerializableExtra("project");
        projectSite = (ProjectSiteDTO) getIntent().getSerializableExtra("projectSite");
        companyStaff = (CompanyStaffDTO) getIntent().getSerializableExtra("companyStaff");
        url = getIntent().getStringExtra("url");
        index = getIntent().getIntExtra("index", 0);
        setFields();

        if (photoCache != null) {
            File f = new File(photoCache.getPhotoUploadList().get(index).getImageFilePath());
            Picasso.with(ctx).load(f).into(imageView);
            txtNumber.setText("" + (index + 1));
        } else {
            Picasso.with(ctx).load(url).into(imageView);
        }
        Util.animateScaleY(imageView, 200);
        setHeader();
    }

    private void setHeader() {

    }

    static final String LOG = ImageActivity.class.getSimpleName();

    private void setFields() {
        txtNumber = (TextView) findViewById(R.id.IMG_number);
        imageView = (ImageView) findViewById(R.id.IMG_image);
        txtTitle = (TextView) findViewById(R.id.IMG_title);
        txtSubTitle = (TextView) findViewById(R.id.IMG_subtitle);
        txtNext = (TextView) findViewById(R.id.IMG_next);
        txtPrev = (TextView) findViewById(R.id.IMG_prev);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.w(LOG,"### MotionEvent action: " + event.getActionMasked());
                // get pointer index from the event object
                int pointerIndex = event.getActionIndex();

                // get pointer ID
                int pointerId = event.getPointerId(pointerIndex);

                // get masked (not specific to a pointer) action
                int maskedAction = event.getActionMasked();

                switch (maskedAction) {

                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN: {
                        // TODO use data
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: { // a pointer was moved
                        Log.e(LOG, "%%%%% moving ...");
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        // TODO use data
                        break;
                    }
                }

                return true;
            }
        });
        txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                if (index == photoCache.getPhotoUploadList().size()) {
                    index = 0;
                }
                File f = new File(photoCache.getPhotoUploadList().get(index).getImageFilePath());
                Picasso.with(ctx).load(f).into(imageView);
                txtNumber.setText("" + (index + 1));
                animate();
                Util.animateRotationY(txtNext,500);
            }
        });
        txtPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == 0) return;
                index--;
                if (index < 0) {
                    index = 0;
                }
                File f = new File(photoCache.getPhotoUploadList().get(index).getImageFilePath());
                Picasso.with(ctx).load(f).into(imageView);
                txtNumber.setText("" + (index + 1));
                animate();
                Util.animateRotationY(txtPrev,500);
            }
        });


    }

    private void animate() {
        Util.animateSlideRight(imageView, 500);
        Util.animateRotationY(txtNumber, 500);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
