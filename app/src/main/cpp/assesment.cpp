// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("assesment");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("assesment")
//      }
//    }

#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_catastrophic_app_di_GlobalVariable_getBaseUrl(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("https://api.thecatapi.com/");
}