//
// Created by ByteDance on 2024/5/30.
//

#ifndef DEMOS_UTILS_H
#define DEMOS_UTILS_H

#include <jni.h>


class utils {
    static char* convertJStringToCString(JNIEnv *env, jstring jstr);
};


#endif //DEMOS_UTILS_H
