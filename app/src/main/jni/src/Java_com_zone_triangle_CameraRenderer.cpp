#include "CameraManager.h"
#ifndef _Included_com_zone_triangle_CameraRenderer
#define _Included_com_zone_triangle_CameraRenderer
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_com_zone_triangle_CameraRenderer_nativeOnCreate(JNIEnv *env, jclass thiz)
{
	if (CameraManager::getSingletonPtr() == nullptr) {
        new CameraManager();
    }
    CameraManager::getSingleton().onCreate();
}

JNIEXPORT void JNICALL Java_com_zone_triangle_CameraRenderer_nativeOnResume(JNIEnv *env, jclass thiz)
{
	 CameraManager::getSingleton().onResume();
}

JNIEXPORT void JNICALL Java_com_zone_triangle_CameraRenderer_nativeOnPause(JNIEnv *env, jclass thiz)
{
	CameraManager::getSingleton().onPause();
}

JNIEXPORT void JNICALL Java_com_zone_triangle_CameraRenderer_nativeOnStop(JNIEnv *env, jclass thiz)
{
	CameraManager::getSingleton().onStop();
}

JNIEXPORT void JNICALL Java_com_zone_triangle_CameraRenderer_nativeOnDestroy(JNIEnv *env, jclass thiz)
{
	CameraManager::getSingleton().onDestroy();
}

JNIEXPORT void JNICALL Java_com_zone_triangle_CameraRenderer_nativeInitGL(JNIEnv *env, jobject thiz, jint width, jint height)
{
	CameraManager::getSingleton().initGL(width, height);
}

JNIEXPORT void JNICALL Java_com_zone_triangle_CameraRenderer_nativeDrawFrame(JNIEnv *env, jclass thiz)
{
	 CameraManager::getSingleton().drawFrame();
}

JNIEXPORT void JNICALL Java_com_zone_triangle_CameraRenderer_nativeOnSurfaceChanged(JNIEnv *env, jobject thiz, jint width, jint height)
{
	CameraManager::getSingleton().onChange(width, height);
}

JNIEXPORT jint JNICALL Java_com_zone_triangle_CameraRenderer_nativeGetTextureId(JNIEnv *env, jclass thiz)
{
	return CameraManager::getSingleton().getCameraTextureId();
}

#ifdef __cplusplus
}
#endif
#endif
