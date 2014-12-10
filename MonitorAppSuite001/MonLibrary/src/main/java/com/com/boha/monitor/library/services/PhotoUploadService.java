package com.com.boha.monitor.library.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.com.boha.monitor.library.dto.CompanyStaffDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.dto.transfer.PhotoUploadDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.PhotoCache;
import com.com.boha.monitor.library.util.PictureUtil;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.WebCheck;
import com.com.boha.monitor.library.util.WebCheckResult;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class PhotoUploadService extends IntentService {
    /**
     * Upload staff picture
     * @param context
     * @param staff
     * @param fullPicture
     * @param thumb
     */
    public static void uploadStaffPicture(final Context context, final CompanyStaffDTO staff,
                                         final File fullPicture, final File thumb,
                                         Location location) {
        final PhotoUploadDTO dto = getObject(context,fullPicture,thumb,location);
        dto.setCompanyStaffID(staff.getCompanyStaffID());
        dto.setPictureType(PhotoUploadDTO.STAFF_IMAGE);
        addPhotoToCache(context, dto);
    }
    /**
     * Upload site picture
     *
     * @param context
     * @param site
     * @param fullPicture
     * @param thumb
     */
    public static void uploadSitePicture(final Context context, final ProjectSiteDTO site,
                                         final File fullPicture, final File thumb,
                                         Location location) {
        Log.w(LOG,"**** uploadSitePicture .........");
        final PhotoUploadDTO dto = getObject(context,fullPicture,thumb,location);
        dto.setProjectID(site.getProjectID());
        dto.setProjectSiteID(site.getProjectSiteID());
        dto.setPictureType(PhotoUploadDTO.SITE_IMAGE);
        dto.setAccuracy(location.getAccuracy());
        addPhotoToCache(context, dto);
    }
    public static void uploadSiteTaskPicture(final Context context, final ProjectSiteTaskDTO siteTask,
                                         final File fullPicture, final File thumb,
                                         Location location) {
        final PhotoUploadDTO dto = getObject(context,fullPicture,thumb,location);
        dto.setProjectID(siteTask.getProjectID());
        dto.setProjectSiteID(siteTask.getProjectSiteID());
        dto.setProjectSiteTaskID(siteTask.getProjectSiteTaskID());
        dto.setPictureType(PhotoUploadDTO.TASK_IMAGE);
        dto.setAccuracy(location.getAccuracy());
        addPhotoToCache(context, dto);

    }
    public static void uploadProjectPicture(final Context context,
                 final ProjectDTO project, final File fullPicture, final File thumb,
                 Location location) {
        final PhotoUploadDTO dto = getObject(context,fullPicture,thumb, location);
        dto.setProjectID(project.getProjectID());
        dto.setPictureType(PhotoUploadDTO.PROJECT_IMAGE);

       addPhotoToCache(context, dto);
    }
    private static PhotoUploadDTO getObject(Context context,final File fullPicture, final File thumb,
                                     Location location) {
        PhotoUploadDTO dto = new PhotoUploadDTO();
        dto.setCompanyID(SharedUtil.getCompany(context).getCompanyID());
        dto.setThumbFilePath(thumb.getAbsolutePath());
        dto.setImageFilePath(fullPicture.getAbsolutePath());
        dto.setDateTaken(new Date());
        dto.setLatitude(location.getLatitude());
        dto.setLongitude(location.getLongitude());
        dto.setAccuracy(location.getAccuracy());
        dto.setTime(new Date().getTime());
        return dto;
    }
    private static void addPhotoToCache(final Context context, final PhotoUploadDTO dto) {
        Log.w(LOG,"**** addPhotoToCache starting ...");
        CacheUtil.getCachedData(context, CacheUtil.CACHE_PHOTOS, new CacheUtil.CacheUtilListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO response) {
                if (response != null) {
                    if (response.getPhotoCache() != null) {
                        list = response.getPhotoCache().getPhotoUploadList();
                        list.add(0,dto);
                    }

                } else {
                    response = new ResponseDTO();
                    list = new ArrayList<>();
                    list.add(dto);
                }
                Log.w(LOG,"**** setting cache...about to cache: " + list.size());
                response.setPhotoCache(new PhotoCache());
                response.getPhotoCache().setPhotoUploadList(list);
                CacheUtil.cacheData(context, response, CacheUtil.CACHE_PHOTOS, new CacheUtil.CacheUtilListener() {
                    @Override
                    public void onFileDataDeserialized(ResponseDTO response) {

                    }

                    @Override
                    public void onDataCached() {
                        Log.e(LOG, "=======> photos have been cached: " +list.size());
                    }

                    @Override
                    public void onError() {

                    }
                });
                //start service to upload images in cache
                Intent intent = new Intent(context, PhotoUploadService.class);
                context.startService(intent);
            }

            @Override
            public void onDataCached() {

            }

            @Override
            public void onError() {
                Log.e(LOG,"******* error in service");
            }
        });
    }
    public static void uploadPendingPhotos(final Context context) {
        Log.e(LOG, "############### uploadPendingPhotos, will start service");
        CacheUtil.getCachedData(context,CacheUtil.CACHE_PHOTOS,new CacheUtil.CacheUtilListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO response) {
                Log.e(LOG, "##### cached photo list returned - may start service if needed");
                if (response != null) {
                    if (response.getPhotoCache() != null) {
                        list = response.getPhotoCache().getPhotoUploadList();
                        Intent intent = new Intent(context, PhotoUploadService.class);
                        context.startService(intent);
                        return;
                    }

                } else {
                    Log.w(LOG, "##### no photos found in cache for upload, response is NULL");
                }
            }

            @Override
            public void onDataCached() {

            }

            @Override
            public void onError() {

            }
        });
    }

    public PhotoUploadService() {
        super("PhotoUploadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (list != null) {
            Log.w(LOG, "###### starting service, onHandleIntent");
            webCheckResult = WebCheck.checkNetworkAvailability(getApplicationContext());
            if (webCheckResult.isWifiConnected() || webCheckResult.isMobileConnected()) {
                Log.w(LOG,"####### starting thumb upload loop");
                controlThumbUploads();
            } else {
                Log.e(LOG,"----- no network available for upload, will try later!");
            }

        }
    }

    static List<PhotoUploadDTO> list;
    int index;
    WebCheckResult webCheckResult;
    private void controlThumbUploads() {
        //Log.d(LOG,"*** controlThumbUploads, index: " + index + " list: " + list.size());
        if (index < list.size()) {
            if (list.get(index).getDateThumbUploaded() == null) {
                executeThumbUpload(list.get(index));
            } else {
                index++;
                controlThumbUploads();
            }
        }
        if (index == list.size()) {
            //webCheckResult = WebCheck.checkNetworkAvailability(getApplicationContext());
            if (webCheckResult.isWifiConnected()) {
                index = 0;
                controlFullPictureUploads();
            }
        }


    }
    private void controlFullPictureUploads() {

        if (index < list.size()) {
            if (list.get(index).getDateFullPictureUploaded() == null) {
                executeFullPictureUpload(list.get(index));
            } else {
                index++;
                controlFullPictureUploads();
            }
        }
    //
        Log.w(LOG,"*** check and remove photos uploaded from cache");
        List<PhotoUploadDTO> pendingList = new ArrayList<>();
        for (PhotoUploadDTO dto: list) {
            if (dto.getDateThumbUploaded() == null || dto.getDateFullPictureUploaded() == null) {
                pendingList.add(dto);
            }
        }
        list = pendingList;
        saveCache();

    }
    private void executeThumbUpload(final PhotoUploadDTO dto) {
        Log.d(LOG,"*** executeThumbUpload, file: " + dto.getThumbFilePath());
        dto.setFullPicture(false);
        //TODO - remove
        if (dto.getPictureType() == 0) dto.setPictureType(PhotoUploadDTO.PROJECT_IMAGE);
        final long start = System.currentTimeMillis();
        PictureUtil.uploadImage(dto, false, getApplicationContext(), new PhotoUploadDTO.PhotoUploadedListener() {
            @Override
            public void onPhotoUploaded() {
                long end = System.currentTimeMillis();
                Log.i(LOG, "---- thumbnail uploaded, elapsed: " + (end - start) + " ms");
                dto.setDateThumbUploaded(new Date());
                saveCache();
                index++;
                controlThumbUploads();
            }

            @Override
            public void onPhotoUploadFailed() {
                Log.e(LOG,"------<< onPhotoUploadFailed - check and tell someone");
            }
        });
    }
    private void executeFullPictureUpload(final PhotoUploadDTO dto) {
        final long start = System.currentTimeMillis();
        Log.d(LOG,"*** executeFullPictureUpload, path: " + dto.getImageFilePath());
        dto.setFullPicture(true);
        PictureUtil.uploadImage(dto, true, getApplicationContext(), new PhotoUploadDTO.PhotoUploadedListener() {
            @Override
            public void onPhotoUploaded() {
                long end = System.currentTimeMillis();
                Log.i(LOG, "---- full picture uploaded, elapsed: " + (end - start) + " ms");
                dto.setDateFullPictureUploaded(new Date());
                saveCache();
                index++;
                controlFullPictureUploads();
            }

            @Override
            public void onPhotoUploadFailed() {
                Log.e(LOG,"------<< onPhotoUploadFailed - check and tell someone");
            }
        });
    }
    private void saveCache() {
        Log.d(LOG,"*** saveCache starting............");

        ResponseDTO r = new ResponseDTO();
        PhotoCache pc = new PhotoCache();
        pc.setPhotoUploadList(list);
        r.setPhotoCache(pc);
        CacheUtil.cacheData(getApplicationContext(),r,CacheUtil.CACHE_PHOTOS,new CacheUtil.CacheUtilListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO response) {

            }

            @Override
            public void onDataCached() {

            }

            @Override
            public void onError() {

            }
        });
    }
    static final String LOG = PhotoUploadService.class.getSimpleName();
}
