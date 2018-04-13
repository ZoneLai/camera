package com.zone.triangle;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLDisplay;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.AttributeSet;
import android.view.WindowManager;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @authon by 赖忠安
 * Create on 2018/3/24.
 */

public class CameraRenderer implements GLSurfaceView.Renderer {
    private static final int MAX_PREVIEW_WIDTH = 1440;
    private static final int MAX_PREVIEW_HEIGHT = 1920;
    private Context mContext;
    private SurfaceTexture mSurfaceTexture;
    private volatile boolean updateSurface = false;
    private int mWidth;
    private int mHeight;
    private CameraPreview mCameraPreview;

    static {
        System.loadLibrary("CameraManager");
    }

    public CameraRenderer(Context context, AttributeSet attributeSet) {
        mContext = context;
        nativeOnCreate();
        mCameraPreview = new CameraPreview();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        EGLDisplay mEGLDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        int[] version = new int[2];
        EGL14.eglInitialize(mEGLDisplay, version, 0, version, 1);
        int[] configsCount = new int[1];
        android.opengl.EGLConfig[] configs = new android.opengl.EGLConfig[1];
        int[] configSpec = new int[]{
                EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                EGL14.EGL_RED_SIZE, 8,
                EGL14.EGL_GREEN_SIZE, 8,
                EGL14.EGL_BLUE_SIZE, 8,
                EGL14.EGL_ALPHA_SIZE, 8,
                EGL14.EGL_DEPTH_SIZE, 0,
                EGL14.EGL_STENCIL_SIZE, 0,
                EGL14.EGL_NONE
        };
        if (!EGL14.eglChooseConfig(mEGLDisplay, configSpec, 0, configs, 0, configs.length, configsCount, 0)) {
            throw new IllegalArgumentException("Failed to choose config: " + GLUtils.getEGLErrorString(EGL14.eglGetError()));
        } else if (configsCount[0] > 0) {
        }
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mWidth = wm.getDefaultDisplay().getWidth();
        mHeight = wm.getDefaultDisplay().getHeight();
        nativeInitGL(mWidth, mHeight);
        mSurfaceTexture = new SurfaceTexture(nativeGetTextureId());
        mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                updateSurface = true;
            }
        });
        synchronized (this) {
            updateSurface = false;
        }
        // openCamera
        mCameraPreview.open(1);
        mCameraPreview.setPreviewTexture(mSurfaceTexture);
        mCameraPreview.preview();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        nativeOnSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        synchronized (this) {
            if (updateSurface) {
                mSurfaceTexture.updateTexImage();
                updateSurface = false;
            }
        }
        nativeDrawFrame();
    }

    public void onResume() {
    }

    public void onPause() {
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
