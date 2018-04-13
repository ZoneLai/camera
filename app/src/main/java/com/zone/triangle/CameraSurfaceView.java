package com.zone.triangle;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * @authon by 赖忠安
 * Create on 2018/3/24.
 */

public class CameraSurfaceView extends GLSurfaceView {
    private CameraRenderer mCameraRenderer;

    public CameraSurfaceView(Context context) {
        super(context);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        mCameraRenderer = new CameraRenderer(context, attrs);
        setRenderer(mCameraRenderer);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.setRenderMode(RENDERMODE_CONTINUOUSLY);
        runOnGLThread(new Runnable() {
            @Override
            public void run() {
                mCameraRenderer.onResume();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        this.setRenderMode(RENDERMODE_WHEN_DIRTY);
        runOnGLThread(new Runnable() {
            @Override
            public void run() {
                mCameraRenderer.onPause();
            }
        });
    }

    public void onStop() {
        runOnGLThread(new Runnable() {
            @Override
            public void run() {
                mCameraRenderer.onStop();
            }
        });
    }

    public void onDestroy() {
        runOnGLThread(new Runnable() {
            @Override
            public void run() {
                mCameraRenderer.onDestroy();
            }
        });
    }

    public void runOnGLThread(final Runnable pRunnable) {
        this.queueEvent(pRunnable);
    }
}
