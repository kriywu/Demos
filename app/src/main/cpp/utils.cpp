//
// Created by ByteDance on 2024/5/30.
//

#include <malloc.h>
#include <string.h>
#include "utils.h"


char *utils::convertJStringToCString(JNIEnv *env, jstring jstr) {
    char* res = nullptr;
    jclass clazz = env->FindClass("java/lang/String");
    jmethodID mid = env->GetMethodID(clazz, "getBytes", "()[B");
    auto jbyteArray = static_cast<_jbyteArray *>(env->CallObjectMethod(jstr, mid));

    jsize alen = env->GetArrayLength(jbyteArray);
    jbyte* jdata = env->GetByteArrayElements(jbyteArray, nullptr);
    res = (char *)malloc(alen + 1);
    memcpy(res, jdata, alen);
    res[alen] = 0;
    env->ReleaseByteArrayElements(jbyteArray, jdata, 0);
    return res;
}
