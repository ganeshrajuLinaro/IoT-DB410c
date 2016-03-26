#include "com_example_matias_iotc2m7ndktutorial_MyNDKActivity.h"
#include <jni.h>
/*
 * Class:     com_example_matias_iotc2m7ndktutorial_MyNDKActivity
 * Method:    getStringFromNDK
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_example_matias_iotc2m7ndktutorial_MyNDKActivity_getStringFromNDK
  (JNIEnv * env, jobject obj){
    return (*env)->NewStringUTF(env,"Hello DragonBoard! from: NDK");
}