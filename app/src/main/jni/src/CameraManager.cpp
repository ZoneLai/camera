#include "CameraManager.h"
#include "Common.h"
#include "GLUtil.h"
#include "shader/camera.frag"
#include "shader/camera.vert"
/* GL_OES_EGL_image_external */
#ifndef GL_OES_EGL_image_external
#define GL_TEXTURE_EXTERNAL_OES                                 0x8D65
#endif
template<> CameraManager* Singleton<CameraManager>::msSingleton = nullptr;
CameraManager::CameraManager()
    : _widgetWidth(0)
    , _widgetHeight(0)
    , _sProgramPlay(0)
    , _vaoId(0)
    , _vboBuffer(0)
    , _texture(0)
    , _positionLoc(-1)
    , _textureLoc(-1)
    , _mvpMatrixLoc(-1)
    , _samplerExternalOESLoc(-1)
    , _mvpMatrix(glm::mat4(1.0f))
{
}

CameraManager::~CameraManager() {
}

void CameraManager::onCreate() {
}

void CameraManager::onResume() {
}

void CameraManager::onPause() {
}

void CameraManager::onStop() {
}

void CameraManager::onDestroy() {
    glDeleteVertexArrays(1, &_vaoId);
    glDeleteBuffers(1, &_vboBuffer);
    glDeleteTextures(1, &_texture);
    glDeleteProgram(_sProgramPlay);
}

void CameraManager::initGL(int widgetWidth, int widgetHeight) {
    _widgetWidth                = widgetWidth;
    _widgetHeight               = widgetHeight;
    if (CompileShaderProgram(camera_play_vert, camera_play_frag, &_sProgramPlay)) {
        _positionLoc	        = glGetAttribLocation(_sProgramPlay,    "a_Position");
        _textureLoc		        = glGetAttribLocation(_sProgramPlay,    "a_Textcoord");
        _mvpMatrixLoc	        = glGetUniformLocation(_sProgramPlay,   "u_MvpMatrix");
        _samplerExternalOESLoc  = glGetUniformLocation(_sProgramPlay,   "u_CameraTexture");
        GLfloat vertices[]  = {
            -1.0f, -1.0f, 0.0f, 0.0f, 1.0f,
             1.0f, -1.0f, 0.0f, 0.0f, 0.0f,
            -1.0f,  1.0f, 0.0f, 1.0f, 1.0f,
             1.0f,  1.0f, 0.0f, 1.0f, 0.0f,
        };
        glGenVertexArrays(1, &_vaoId);
        glBindVertexArray(_vaoId);
        glGenBuffers(1, &_vboBuffer);
        glBindBuffer(GL_ARRAY_BUFFER, _vboBuffer);
        glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);
        glVertexAttribPointer(
            _positionLoc,
            3,
            GL_FLOAT,
            GL_FALSE,
            5 * sizeof(GL_FLOAT),
            (GLvoid*)0
        );
        glEnableVertexAttribArray(_positionLoc);
        glVertexAttribPointer(
            _textureLoc,
            2,
            GL_FLOAT,
            GL_FALSE,
            5 * sizeof(GL_FLOAT),
            (GLvoid*)(3 * sizeof(GL_FLOAT))
        );
        glEnableVertexAttribArray(_textureLoc);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        // texture
        glGenTextures(1, &_texture);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_EXTERNAL_OES, _texture);
        glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glBindTexture(GL_TEXTURE_EXTERNAL_OES, 0);
    } else {
        LOGE("CompileShaderProgram===================");
    }
}

void CameraManager::drawFrame() {
    glViewport(0, 0, _widgetWidth, _widgetHeight);
    glBindVertexArray(_vaoId);
    glUseProgram(_sProgramPlay);
    glUniformMatrix4fv(_mvpMatrixLoc, 1, GL_FALSE, glm::value_ptr(_mvpMatrix));
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_EXTERNAL_OES, _texture);
    glUniform1i(_samplerExternalOESLoc, 0);
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    glBindVertexArray(0);
    glUseProgram(0);
}

void CameraManager::onChange(int widgetWidth, int widgetHeight) {
    _widgetWidth        = widgetWidth;
    _widgetHeight       = widgetHeight;
   // _modelMatrix	    = glm::scale(glm::mat4(1.0f), glm::vec3(2.0f, 2.0f, 2.0f));
   // _viewMatrix       = glm::lookAt(glm::vec3(0.0f, 0.0f, 6.0f), glm::vec3(0.0f, 0.0f, 0.0f), glm::vec3(0.0f, 1.0f, 0.0f));
   // _projectionMatrix = glm::ortho(-1.0f, 1.0f, -(float) widgetHeight / widgetWidth, (float) widgetHeight / widgetWidth, 5.0f, 7.0f);
   // _mvpMatrix		= _projectionMatrix * _viewMatrix * _modelMatrix;
}

GLint CameraManager::getCameraTextureId() {
    return _texture;
}