package com.zone.triangle;

import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @authon by 赖忠安
 * Create on 2018/3/24.
 */

public class CameraPreview implements CameraViewImpl, Camera.PreviewCallback {
    private Config mConfig;
    private android.hardware.Camera mCamera;
    private CameraSizeComparator sizeComparator;

    private android.hardware.Camera.Size picSize;
    private android.hardware.Camera.Size preSize;

    private Point mPicSize;
    private Point mPreSize;

    public CameraPreview() {
        mConfig = new Config();
        mConfig.minPreviewWidth = 720;
        mConfig.minPictureWidth = 720;
        mConfig.rate = 1.778f;
        sizeComparator = new CameraSizeComparator();
    }

    @Override
    public boolean open(int cameraId) {
        mCamera = android.hardware.Camera.open(cameraId);
        if (mCamera != null) {
            android.hardware.Camera.Parameters param = mCamera.getParameters();
            picSize = getPropPictureSize(param.getSupportedPictureSizes(), mConfig.rate, mConfig.minPictureWidth);
            preSize = getPropPreviewSize(param.getSupportedPreviewSizes(), mConfig.rate, mConfig.minPreviewWidth);
            param.setPictureSize(picSize.width, picSize.height);
            param.setPreviewSize(preSize.width, preSize.height);
            mCamera.setParameters(param);
            android.hardware.Camera.Size pre = param.getPreviewSize();
            android.hardware.Camera.Size pic = param.getPictureSize();
            mPicSize = new Point(pic.height, pic.width);
            mPreSize = new Point(pre.height, pre.width);
            mCamera.setPreviewCallback(this);
            return true;
        }
        return false;
    }

    @Override
    public void setConfig(Config config) {
        this.mConfig = config;
    }

    @Override
    public boolean preview() {
        if (mCamera != null) {
            mCamera.startPreview();
        }
        return false;
    }

    @Override
    public boolean switchTo(int cameraId) {
        close();
        open(cameraId);
        return false;
    }

    @Override
    public void takePhoto(TakePhotoCallback callback) {

    }

    @Override
    public boolean close() {
        if (mCamera != null) {
            try {
                mCamera.stopPreview();
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void setPreviewTexture(SurfaceTexture texture) {
        if (mCamera != null) {
            try {
                mCamera.setPreviewTexture(texture);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Point getPreviewSize() {
        return mPreSize;
    }

    @Override
    public Point getPictureSize() {
        return mPicSize;
    }

    @Override
    public void setOnPreviewFrameCallback(final PreviewFrameCallback callback) {

    }

    private android.hardware.Camera.Size getPropPreviewSize(List<android.hardware.Camera.Size> list, float th, int minWidth) {
        Collections.sort(list, sizeComparator);
        int i = 0;
        for (android.hardware.Camera.Size s : list) {
            if ((s.height >= minWidth) && equalRate(s, th)) {
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = 0;
        }
        return list.get(i);
    }

    private android.hardware.Camera.Size getPropPictureSize(List<android.hardware.Camera.Size> list, float th, int minWidth) {
        Collections.sort(list, sizeComparator);
        int i = 0;
        for (android.hardware.Camera.Size s : list) {
            if ((s.height >= minWidth) && equalRate(s, th)) {
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = 0;
        }
        return list.get(i);
    }

    private boolean equalRate(android.hardware.Camera.Size s, float rate) {
        float r = (float) (s.width) / (float) (s.height);
        if (Math.abs(r - rate) <= 0.03) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Log.e("xx", "onPreviewFrame: " + data.length);
    }

    private class CameraSizeComparator implements Comparator<android.hardware.Camera.Size> {
        public int compare(android.hardware.Camera.Size lhs, android.hardware.Camera.Size rhs) {
            // TODO Auto-generated method stub
            if (lhs.height == rhs.height) {
                return 0;
            } else if (lhs.height > rhs.height) {
                return 1;
            } else {
                return -1;
            }
        }

    }
}
