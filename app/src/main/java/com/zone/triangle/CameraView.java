package com.zone.triangle;

import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @authon by 赖忠安
 * Create on 2018/3/24.
 */

public class CameraView extends GLSurfaceView implements GLSurfaceView.Renderer {
    private Camera1 mCamera1;
    private CameraRenderer mCameraRenderer;
    private int cameraId = 1;

    public CameraView(Context context) {
        super(context);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        setRenderer(this);
        this.setRenderMode(RENDERMODE_CONTINUOUSLY);
        mCamera1 = new Camera1();
        mCameraRenderer = new CameraRenderer();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        mCameraRenderer.onSurfaceCreated(gl10, eglConfig);
        mCamera1.open(cameraId);
        Point point = mCamera1.getPreviewSize();
        mCameraRenderer.setViewSize(point.x, point.y);
        mCamera1.setPreviewTexture(mCameraRenderer.getSurfaceTexture());
        mCameraRenderer.getSurfaceTexture().setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                requestRender();
            }
        });
        mCamera1.preview();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        mCameraRenderer.onSurfaceChanged(gl10, i, i1);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        mCameraRenderer.onDrawFrame(gl10);
    }

    public void onStop() {
        mCameraRenderer.onStop();
    }

    public void onDestroy() {
        mCameraRenderer.onDestroy();
    }
}
