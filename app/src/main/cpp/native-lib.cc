#include <jni.h>
#include "lame.h"
extern "C" {
#include "ac3_parser.h"
}

/**
 * MP3 to PCM File
 */
extern "C" {


void releaseStringUTFChars(JNIEnv *env, jstring jstr, const char *cstr) {
    if (cstr != nullptr) {
        env->ReleaseStringUTFChars(jstr, cstr);
    }
}

long getFileTotalSize(FILE *file) {
    long currentPosition = ftell(file);
    fseek(file, 0, SEEK_END);
    long fileLength = ftell(file);
    fseek(file, currentPosition, SEEK_SET);
    return fileLength;
}

void callbackProgress(JNIEnv *env, jobject iProgress, int progress) {
    jclass cls = env->GetObjectClass(iProgress);
    jmethodID callbackMethod = env->GetMethodID(cls, "onProgress", "(I)V");
    if (callbackMethod == nullptr) return;
    env->CallVoidMethod(iProgress, callbackMethod, progress);
}

void test() {
    av_ac3_parse_header(nullptr, 1, 0, 0);
}

JNIEXPORT jint Java_com_wurengao_surfaceviewtestdemo_audio_NativeUtils_transformMp3ToPCM(
        JNIEnv *env,
        jobject obj,
        jstring mp3FilePath,
        jstring pcmFilePath,
        jobject iProgress
) {
    return -1;
}


// VBR - CBR -  ABR
JNIEXPORT jint Java_com_wurengao_surfaceviewtestdemo_audio_NativeUtils_transformPCMToMp3(
        JNIEnv *env,
        jobject obj,
        jstring mp3FilePath,
        jstring pcmFilePath,
        jobject iProgress) {

    test();

    const char *pcmFilePathStr = env->GetStringUTFChars(pcmFilePath, nullptr);
    const char *mp3FilePathStr = env->GetStringUTFChars(mp3FilePath, nullptr);

    FILE *pcmFile = fopen(pcmFilePathStr, "rb");
    FILE *mp3File = fopen(mp3FilePathStr, "wb");

    lame_t lame = lame_init();
    lame_set_in_samplerate(lame, 44100);    // PCM采样率
    lame_set_num_channels(lame, 2);         // 立体声
    lame_set_out_samplerate(lame, 44100);   // MP3采样率
    lame_init_params(lame);                 // 初始化参数

    long fileLength = getFileTotalSize(pcmFile);
    long currentIndex = 0;

    int bufferSize = 1024 * 256;
    auto *buffer = new short[bufferSize / 2];       // 因为这里是short，所以 / 2
    auto *leftBuffer = new short[bufferSize / 4];   // left right 各占 1/2
    auto *rightBuffer = new short[bufferSize / 4];
    auto *mp3_buffer = new unsigned char[bufferSize];
    size_t readBufferSize = 0;
    while ((readBufferSize = fread(buffer, 2, bufferSize / 2, pcmFile)) > 0) {
        for (int i = 0; i < readBufferSize; i++) {
            if (i % 2 == 0) {
                leftBuffer[i / 2] = buffer[i];
            } else {
                rightBuffer[i / 2] = buffer[i];
            }
        }
        size_t wroteSize = lame_encode_buffer(
                lame,
                leftBuffer,
                rightBuffer,
                (int) (readBufferSize / 2),
                mp3_buffer,
                bufferSize);
        fwrite(mp3_buffer, 1, wroteSize, mp3File);
        currentIndex += readBufferSize * 2;
        callbackProgress(env, iProgress, currentIndex * 100 / fileLength);
    }
    delete[] buffer;
    delete[] leftBuffer;
    delete[] rightBuffer;
    delete[] mp3_buffer;
    env->ReleaseStringUTFChars(pcmFilePath, pcmFilePathStr);
    env->ReleaseStringUTFChars(mp3FilePath, mp3FilePathStr);
    return 0;
}

}


