#pragma once
#include <time.h>
#include <math.h>
#include <EGL/egl.h>
#include <GLES/gl.h>
#include <android/log.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include <android/native_window_jni.h>
#include "glm.hpp"
#include "ext.hpp"
#include "JniHelper.h"
#include "Singleton.h"
class CameraManager : public Singleton<CameraManager> {
public:
    CameraManager();
    ~CameraManager();
    void                        onCreate();
    void                        onResume();
    void                        onPause();
    void                        onStop();
    void                        onDestroy();
    void                        initGL(int widgetWidth, int widgetHeight);
    void                        drawFrame();
    void                        onChange(int widgetWidth, int widgetHeight);
    GLint                       getCameraTextureId();

private:
    int                         _widgetWidth;
    int                         _widgetHeight;
    GLuint                      _sProgramPlay;
    GLuint						_vaoId;
    GLuint						_vboBuffer;
    GLuint                      _texture;
    GLint						_positionLoc;
    GLint						_textureLoc;
    GLint						_mvpMatrixLoc;
    GLint                       _samplerExternalOESLoc;
    glm::mat4                   _mvpMatrix;
};