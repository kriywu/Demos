//
// Created by ByteDance on 2024/5/30.
//

#ifndef SURFACEVIEWTESTDEMO_UTILS_H
#define SURFACEVIEWTESTDEMO_UTILS_H

#include <jni.h>


class utils {
    static char* convertJStringToCString(JNIEnv *env, jstring jstr);
};


#endif //SURFACEVIEWTESTDEMO_UTILS_H
