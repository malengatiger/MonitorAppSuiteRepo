package com.com.boha.monitor.library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.CompanyStaffDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.dto.VideoClipContainer;
import com.com.boha.monitor.library.dto.VideoClipDTO;
import com.com.boha.monitor.library.dto.transfer.PhotoUploadDTO;
import com.com.boha.monitor.library.services.PhotoUploadService;
import com.com.boha.monitor.library.util.CacheVideoUtil;
import com.com.boha.monitor.library.util.GLToolbox;
import com.com.boha.monitor.library.util.ImageUtil;
import com.com.boha.monitor.library.util.PMException;
import com.com.boha.monitor.library.util.TextureRenderer;
import com.com.boha.monitor.library.util.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import org.acra.ACRA;

import java.io.File;
import java.io.IOException;

/**
 * Created by aubreyM on 2014/04/21.
 */
public class PictureActivity extends ActionBarActivity implements
        LocationListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {
    LocationRequest mLocationRequest;
    LocationClient mLocationClient;
    LocationListener activity;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getApplicationContext();
        activity = this;
        setContentView(R.layout.camera);
        setFields();

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(1000);

        mLocationClient = new LocationClient(getApplicationContext(), this,
                this);

        //get objects
        if (savedInstanceState != null) {
            Log.e(LOG,"##### savedInstanceState is LOADED");
            type = savedInstanceState.getInt("type",0);
            projectSite = (ProjectSiteDTO)savedInstanceState.getSerializable("projectSite");
            project = (ProjectDTO)savedInstanceState.getSerializable("project");
            String path = savedInstanceState.getString("photoFile");
            if (path != null) {
                photoFile = new File(path);
            }
        } else {
            type = getIntent().getIntExtra("type", 0);
            project = (ProjectDTO) getIntent().getSerializableExtra("project");
            projectSite = (ProjectSiteDTO) getIntent().getSerializableExtra("projectSite");
            projectSiteTask = (ProjectSiteTaskDTO) getIntent().getSerializableExtra("projectSiteTask");
            companyStaff = (CompanyStaffDTO) getIntent().getSerializableExtra("companyStaff");
        }
        Log.e(LOG, "###### type: " + type);

        if (savedInstanceState != null) {
            String path = savedInstanceState.getString("filePath");
            if (path != null) {
                Log.w(LOG, "###### file path is: " + path);
                currentFullFile = new File(path);
                currentThumbFile = new File(savedInstanceState.getString("thumbPath"));
                try {
                    Bitmap bm = ImageUtil.getBitmapFromUri(ctx, Uri.fromFile(currentFullFile));
                    image.setImageBitmap(bm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                dispatchTakePictureIntent();
            }
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onResume() {
        Log.w(LOG, "***************** onResume - starting pending uploads, if any");
        PhotoUploadService.uploadPendingPhotos(ctx);
        super.onResume();
    }


    ImageView imgCamera;
    private void setFields() {
        image = (ImageView) findViewById(R.id.CAM_image);
        imgCamera = (ImageView) findViewById(R.id.CAM_imgCamera);

        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent data) {
        Log.e(LOG,"##### onActivityResult requestCode: " + requestCode + " resultCode: " + resultCode);
        switch (requestCode) {
            case CAPTURE_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    if (resultCode == Activity.RESULT_OK) {
                        if (photoFile != null)
                        Log.e(LOG,"++++++ hopefully photo file has a length: " + photoFile.length());
                        new PhotoTask().execute();
                    }
                    pictureChanged = true;

                }
                break;
            case REQUEST_VIDEO_CAPTURE:

                Uri videoUri = data.getData();
                new FileTask().execute(videoUri);
                //mVideoView.setVideoURI(videoUri);
                break;
        }
    }

    private void uploadPhotos() {
        Log.e(LOG, "..........starting service to upload photos ........ ");

        switch (type) {
            case PhotoUploadDTO.PROJECT_IMAGE:
                PhotoUploadService.uploadProjectPicture(ctx, project, currentFullFile, currentThumbFile,location);
                break;
            case PhotoUploadDTO.SITE_IMAGE:
                PhotoUploadService.uploadSitePicture(ctx, projectSite, currentFullFile, currentThumbFile,location);
                break;
            case PhotoUploadDTO.STAFF_IMAGE:
                PhotoUploadService.uploadStaffPicture(ctx, companyStaff, currentFullFile, currentThumbFile,location);
                break;
            case PhotoUploadDTO.TASK_IMAGE:
                PhotoUploadService.uploadSiteTaskPicture(ctx, projectSiteTask, currentFullFile, currentThumbFile,location);
                break;
            default:
                PhotoUploadService.uploadPendingPhotos(ctx);
                break;

        }
        isUploaded = true;

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mLocationClient != null) {
            mLocationClient.connect();
            Log.i(LOG,
                    "#################### onStart - locationClient connecting ... ");
        }

    }

    @Override
    public void onStop() {
        Log.d(LOG,
                "#################### onStop");
        if (mLocationClient != null) {
            if (mLocationClient.isConnected()) {
                stopPeriodicUpdates();
            }
            // After disconnect() is called, the client is considered "dead".
            mLocationClient.disconnect();
            Log.e("map", "### onStop - locationClient disconnected: "
                    + mLocationClient.isConnected());
        }
        super.onStop();
    }
    private void stopPeriodicUpdates() {
        mLocationClient.removeLocationUpdates(this);
        Log.e(LOG,
                "#################### stopPeriodicUpdates - removeLocationUpdates");
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.w(LOG,"############## onLocationChanged ...");
        if (this.location == null) {
           this.location = location;
        } else {
            Log.w(LOG, "Old Location lat: " + this.location.getLatitude() +
                    " long: " + this.location.getLongitude() + " - accuracy: " + this.location.getAccuracy());
            if (location.getAccuracy() == ACCURAY_THRESHOLD || location.getAccuracy() < ACCURAY_THRESHOLD) {
                this.location = location;
                mLocationClient.removeLocationUpdates(this);
            }
        }
        Log.e(LOG, "++Location has changed to lat: " + location.getLatitude() +
                " long: " + location.getLongitude() + " - accuracy: " + location.getAccuracy());
    }

    Location location;
    static final float ACCURAY_THRESHOLD = 10;
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(LOG,
                "### ---> LocationClient onConnected() -  >> ");
        location = mLocationClient.getLastLocation();
        mLocationClient.requestLocationUpdates(mLocationRequest,this);
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        ACRA.getErrorReporter().handleSilentException(new PMException(
                "Google LocationClient onConnectionFailed: " + connectionResult.getErrorCode()));
    }


    class FileTask extends AsyncTask<Uri, Void, Integer> {

        @Override
        protected Integer doInBackground(Uri... uris) {
            Uri uri = uris[0];
            VideoClipDTO clip = new VideoClipDTO();
            clip.setUriString(uri.toString());
            File f;
            try {
                f = ImageUtil.getFileFromUri(ctx, uri);
                clip.setFilePath(f.getAbsolutePath());
                clip.setLength(f.length());
//                if (tournament != null) {
//                    clip.setTournamentID(tournament.getTournamentID());
//                    clip.setTournamentName(tournament.getTourneyName());
//                }
//                if (golfGroup != null) {
//                    clip.setGolfGroupID(golfGroup.getGolfGroupID());
//                    clip.setGolfGroupName(golfGroup.getGolfGroupName());
//                }
//                if (vcc == null) {
//                    vcc = new VideoClipContainer();
//                }
//                vcc.getVideoClips().add(0, clip);
//                CacheVideoUtil.cacheVideo(ctx, vcc);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return 0;
        }
    }

    private void dispatchTakeVideoIntent() {
        final Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        CacheVideoUtil.getCachedVideo(ctx, new CacheVideoUtil.CacheVideoListener() {
            @Override
            public void onDataDeserialized(VideoClipContainer x) {
                vcc = x;
                if (vcc != null) {
                    if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                    }
                }
            }
        });


    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(LOG, "Fuck!", ex);
                Util.showErrorToast(ctx,getString(R.string.file_error));
                return;
            }
            if (photoFile != null) {
                Log.w(LOG, "dispatchTakePictureIntent - start pic intent");

                if (mLocationClient.isConnected()) {
                    Log.w(LOG,"## requesting mLocationClient updates before picture taken");
                    mLocationClient.requestLocationUpdates(mLocationRequest, this);
                } else {
                    mLocationClient.connect();
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "pic" + System.currentTimeMillis();
        File storageDir;
        if (Util.hasStorage(true)) {
            Log.i(LOG, "###### get file from getExternalStoragePublicDirectory");
            storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
        } else {
            Log.i(LOG, "###### get file from getDataDirectory");
            storageDir = Environment.getDataDirectory();
        }

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.camera, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_video) {
            dispatchTakeVideoIntent();
            return true;
        }
        if (item.getItemId() == R.id.menu_gallery) {
            Intent i = new Intent(this, ImagePagerActivity.class);
            startActivity(i);
            return true;
        }
        if (item.getItemId() == R.id.menu_camera) {
            dispatchTakePictureIntent();
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (isUploaded) {
            Log.d(LOG, "onBackPressed ... picture uploaded");
            setResult(RESULT_OK);
        } else {
            Log.d(LOG, "onBackPressed ... cancelled");
            setResult(RESULT_CANCELED);
        }
        finish();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onSaveInstanceState(Bundle b) {
        Log.e(LOG,"############################## onSaveInstanceState");
        b.putInt("type",type);
        if (currentFullFile != null) {
            b.putString("filePath", currentFullFile.getAbsolutePath());
            b.putString("thumbPath", currentThumbFile.getAbsolutePath());
        }
        if (photoFile != null) {
            b.putString("photoFile", photoFile.getAbsolutePath());
        }
        if (projectSite != null) {
            b.putSerializable("projectSite", projectSite);
        }
        if (project != null) {
            b.putSerializable("project", project);
        }


        super.onSaveInstanceState(b);
    }


    class PhotoTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            Log.w(LOG, "## PhotoTask starting doInBackground, file length: " + photoFile.length());
            pictureChanged = false;
            ExifInterface exif = null;
            if (photoFile == null || photoFile.length() == 0) {
                Log.e(LOG,"----- photoFile is null or length 0, exiting");
                return 99;
            }
            fileUri = Uri.fromFile(photoFile);
            if (fileUri != null) {
                try {
                    exif = new ExifInterface(photoFile.getAbsolutePath());
                    String orient = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                    Log.i(LOG, "@@@@@@@@@@@@@@@@@@@@@@ Orientation says: " + orient);
                    float rotate = 0f;
                    if (orient.equalsIgnoreCase("6")) {
                        rotate = 90f;
                        Log.i(LOG, "@@@@@ picture, rotate = " + rotate);
                    }
                    try {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2;
                        Bitmap bm = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);
                        if (bm == null) {
                            Log.e(LOG,"---> Bitmap is null, file length: " + photoFile.length());
                        }
                        getLog(bm, "Raw Camera");
                          //get thumbnail for upload
                        Matrix matrixThumbnail = new Matrix();
                        matrixThumbnail.postScale(0.4f, 0.4f);
                        //matrixThumbnail.postRotate(rotate);
                        Bitmap thumb = Bitmap.createBitmap
                                (bm, 0, 0, bm.getWidth(),
                                        bm.getHeight(), matrixThumbnail, true);
                        getLog(thumb, "Thumb");

                        //get resized "full" size for upload
                        Matrix matrixF = new Matrix();
                        matrixF.postScale(0.75f, 0.75f);
                        //matrixF.postRotate(rotate);
                        Bitmap fullBm = Bitmap.createBitmap
                                (bm, 0, 0, bm.getWidth(),
                                        bm.getHeight(), matrixF, true);

                        getLog(fullBm, "Full");
                        //append date and gps coords to bitmap
                        //fullBm = ImageUtil.drawTextToBitmap(ctx,fullBm,location);
                        //thumb = ImageUtil.drawTextToBitmap(ctx,thumb,location);

                        currentFullFile = ImageUtil.getFileFromBitmap(fullBm, "m" + System.currentTimeMillis() + ".jpg");
                        currentThumbFile = ImageUtil.getFileFromBitmap(thumb, "t" + System.currentTimeMillis() + ".jpg");
                        bitmapForScreen = ImageUtil.getBitmapFromUri(ctx,Uri.fromFile(currentFullFile));

                        Log.e(LOG, "## files created from camera bitmap");

                        thumbUri = Uri.fromFile(currentThumbFile);
                        fullUri = Uri.fromFile(currentFullFile);
                        //write exif data
                        Util.writeLocationToExif(currentFullFile.getAbsolutePath(),location);
                        Util.writeLocationToExif(currentThumbFile.getAbsolutePath(),location);
                        fullBm = null;
                        thumb = null;
                        bm = null;
                        getFileLengths();
                    } catch (Exception e) {
                        Log.e("pic", "Fuck it! unable to process bitmap", e);
                        return 9;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return 1;
                }

            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result > 0) {
                Util.showErrorToast(ctx, ctx.getResources().getString(R.string.camera_error));
                return;
            }
            if (thumbUri != null) {
                pictureChanged = true;
                try {
                    image.setImageBitmap(bitmapForScreen);
                    uploadPhotos();
//                    if (bitmapForScreen.getWidth() > bitmapForScreen.getHeight()) {
//                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                    } else {
//                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void getLog(Bitmap bm, String which) {
        if (bm == null) return;
        Log.e(LOG, which + " - bitmap: width: "
                + bm.getWidth() + " height: "
                + bm.getHeight() + " rowBytes: "
                + bm.getRowBytes());
    }

    private void getFileLengths() {
        Log.i(LOG, "Thumbnail file length: " + currentThumbFile.length());
        Log.i(LOG, "Full file length: " + currentFullFile.length());

    }

    private void initEffect() {
        EffectFactory effectFactory = mEffectContext.getFactory();
        if (mEffect != null) {
            mEffect.release();
        }
        /**
         * Initialize the correct effect based on the selected menu/action item
         */
        if (mCurrentEffect == R.id.none) {
        } else if (mCurrentEffect == R.id.autofix) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_AUTOFIX);
            mEffect.setParameter("scale", 0.5f);

        } else if (mCurrentEffect == R.id.bw) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_BLACKWHITE);
            mEffect.setParameter("black", .1f);
            mEffect.setParameter("white", .7f);

        } else if (mCurrentEffect == R.id.brightness) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_BRIGHTNESS);
            mEffect.setParameter("brightness", 2.0f);

        } else if (mCurrentEffect == R.id.contrast) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_CONTRAST);
            mEffect.setParameter("contrast", 1.4f);

        } else if (mCurrentEffect == R.id.crossprocess) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_CROSSPROCESS);

        } else if (mCurrentEffect == R.id.documentary) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_DOCUMENTARY);

        } else if (mCurrentEffect == R.id.duotone) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_DUOTONE);
            mEffect.setParameter("first_color", Color.YELLOW);
            mEffect.setParameter("second_color", Color.DKGRAY);

        } else if (mCurrentEffect == R.id.filllight) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_FILLLIGHT);
            mEffect.setParameter("strength", .8f);

        } else if (mCurrentEffect == R.id.fisheye) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_FISHEYE);
            mEffect.setParameter("scale", .5f);

        } else if (mCurrentEffect == R.id.flipvert) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_FLIP);
            mEffect.setParameter("vertical", true);

        } else if (mCurrentEffect == R.id.fliphor) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_FLIP);
            mEffect.setParameter("horizontal", true);

        } else if (mCurrentEffect == R.id.grain) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_GRAIN);
            mEffect.setParameter("strength", 1.0f);

        } else if (mCurrentEffect == R.id.grayscale) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_GRAYSCALE);

        } else if (mCurrentEffect == R.id.lomoish) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_LOMOISH);

        } else if (mCurrentEffect == R.id.negative) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_NEGATIVE);

        } else if (mCurrentEffect == R.id.posterize) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_POSTERIZE);

        } else if (mCurrentEffect == R.id.rotate) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_ROTATE);
            mEffect.setParameter("angle", 180);

        } else if (mCurrentEffect == R.id.saturate) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_SATURATE);
            mEffect.setParameter("scale", .5f);

        } else if (mCurrentEffect == R.id.sepia) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_SEPIA);

        } else if (mCurrentEffect == R.id.sharpen) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_SHARPEN);

        } else if (mCurrentEffect == R.id.temperature) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_TEMPERATURE);
            mEffect.setParameter("scale", .9f);

        } else if (mCurrentEffect == R.id.tint) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_TINT);
            mEffect.setParameter("tint", Color.MAGENTA);

        } else if (mCurrentEffect == R.id.vignette) {
            mEffect = effectFactory.createEffect(
                    EffectFactory.EFFECT_VIGNETTE);
            mEffect.setParameter("scale", .5f);

        } else {
        }
    }

    private void applyEffect() {
        mEffect.apply(mTextures[0], mImageWidth, mImageHeight, mTextures[1]);
    }

    private void renderResult() {
        if (mCurrentEffect != R.id.none) {
            // if no effect is chosen, just render the original bitmap
            mTexRenderer.renderTexture(mTextures[1]);
        } else {
            // render the result of applyEffect()
            mTexRenderer.renderTexture(mTextures[0]);
        }
    }

    private void loadTextures() {
        // Generate textures
        GLES20.glGenTextures(2, mTextures, 0);
        mImageWidth = bitmapForScreen.getWidth();
        mImageHeight = bitmapForScreen.getHeight();
        mTexRenderer.updateTextureSize(mImageWidth, mImageHeight);

        // Upload to texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmapForScreen, 0);

        // Set texture parameters
        GLToolbox.initTexParams();
    }

    String mCurrentPhotoPath;
    ProjectDTO project;
    ProjectSiteDTO projectSite;
    ProjectSiteTaskDTO projectSiteTask;
    CompanyStaffDTO companyStaff;
    File photoFile;
    private VideoClipContainer vcc;
    boolean isUploaded;
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private GLSurfaceView mEffectView;
    private int[] mTextures = new int[2];
    private EffectContext mEffectContext;
    private Effect mEffect;
    private TextureRenderer mTexRenderer = new TextureRenderer();
    private int mImageWidth;
    private int mImageHeight;
    private boolean mInitialized = false;
    int mCurrentEffect;

    ImageView image;
    File currentThumbFile, currentFullFile;
    Uri thumbUri, fullUri;
    static final String LOG = "PictureActivity";

    Menu mMenu;
    int type;

    boolean pictureChanged;
    Context ctx;
    Uri fileUri;
    public static final int CAPTURE_IMAGE = 9908;

    Bitmap bitmapForScreen;

}