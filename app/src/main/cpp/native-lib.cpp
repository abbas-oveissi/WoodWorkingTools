#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_jnfran92_nativetongue_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";

    double *whatever_big;

    const int magic_number = 1000000;
    whatever_big = new double[magic_number];

    delete[] whatever_big;

    return env->NewStringUTF(hello.c_str());
}