/*
Usage:

        CameraBackground camera = new CameraBackground(getApplicationContext());
        camera.type = CameraBackground.MEDIA_TYPE_IMAGE;
        camera.takePicture();

 */



package com.fd.mobilelocatornotifier;


import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by frog on 08/01/15.
 */
public class CameraBackground {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final String TAG = "MyCamera";
    private static String TITLE = "";
    private static String fileDir;
    private Context context;
    private Camera camera;
    public static int Type;
    private int widthPicture = 0;
    private int heightPicture = 0;
    private boolean cameraConfigured = false;

    public CameraBackground(Context context, String appName) {
        this.context = context;
        TITLE = appName;
    }

    private void getCameraInstance() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            if (cameraExist()) {
                for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                    Camera.getCameraInfo(i, info);
                    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        camera = Camera.open(i);
                    } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        camera = Camera.open(i);
                    }
                }
            }
        }
    }

    private boolean cameraExist() {
        return Camera.getNumberOfCameras() < 0;
    }


    private void configureToPicture() {
        if (widthPicture == 0 && heightPicture == 0) {
            widthPicture = 0;
            heightPicture = 0;
        }
        if (camera != null) {
            if (!cameraConfigured) {
                Camera.Parameters parameters = camera.getParameters();
                Camera.Size size = getSmallestPictureSize(parameters);
                Camera.Size pictureSize = getSmallestPictureSize(parameters);
                if (size != null && pictureSize != null) {
                    parameters.setPictureSize(pictureSize.width,
                            pictureSize.height);
                    parameters.setPictureFormat(ImageFormat.JPEG);
                    camera.setParameters(parameters);
                    cameraConfigured = true;
                }
            }
        }
    }

    public String takePicture() {
        try {
            if (cameraExist()) {
                getCameraInstance();
                configureToPicture();
                camera.takePicture(null, null, mPicture);
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileDir;
    }

    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), TITLE);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TITLE, "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

        if (type == MEDIA_TYPE_IMAGE) {
            fileDir = mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg";
            mediaFile = new File(fileDir);
        } else if (type == MEDIA_TYPE_VIDEO) {
            fileDir = mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4";
            mediaFile = new File(fileDir);
        } else {
            return null;
        }

        return mediaFile;
    }

    private Camera.Size getSmallestPictureSize(Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPictureSizes()) {
            if (result == null) {
                result = size;
            } else {
                int resultArea = result.width * result.height;
                int newArea = size.width * size.height;

                if (newArea < resultArea) {
                    result = size;
                }
            }
        }

        return (result);
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile(Type);
            if (pictureFile == null) {
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            } catch (Exception e) {
                Log.d(TAG, "Error creating media file, check storage permissions: " +
                        e.getMessage());
            }
        }
    };
}
