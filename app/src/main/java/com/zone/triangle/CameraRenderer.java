package com.zone.triangle;

import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @authon by 赖忠安
 * Create on 2018/3/24.
 */

public class CameraRenderer implements GLSurfaceView.Renderer {
    private static final int MAX_PREVIEW_WIDTH = 720;
    private static final int MAX_PREVIEW_HEIGHT = 1280;

    private SurfaceTexture mSurfaceTexture;

    static {
        System.loadLibrary("CameraManager");
    }

    public CameraRenderer() {
        nativeOnCreate();
    }

    public void setViewSize(int width, int height) {
        nativeOnSurfaceChanged(width, height);
    }

    public SurfaceTexture getSurfaceTexture() {
        return mSurfaceTexture;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        nativeInitGL(MAX_PREVIEW_WIDTH, MAX_PREVIEW_HEIGHT);
        mSurfaceTexture = new SurfaceTexture(nativeGetTextureId());
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        nativeOnSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        mSurfaceTexture.updateTexImage();
        nativeDrawFrame();
    }

    public void onStop() {
        nativeOnStop();
    }

    public void onDestroy() {
        nativeOnDestroy();
    }


    private static native void nativeOnCreate();

    private static native void nativeOnResume();

    private static native void nativeOnPause();

    private static native void nativeOnStop();

    private static native void nativeOnDestroy();

    private static native void nativeInitGL(int widgetWidth, int widgetHeight);

    private static native void nativeDrawFrame();

    private static native void nativeOnSurfaceChanged(int widgetWidth, int widgetHeight);

    private static native int nativeGetTextureId();
}
