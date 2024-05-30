#include <jni.h>
#include <string>
#include <sys/mman.h>
#include <fcntl.h>
#include <unistd.h>
#include <cstring>

#include "lame.h"
extern "C" {
#include "ac3_parser.h"
}

/**
 * MP3 to PCM File
 */
extern "C"
void releaseStringUTFChars(JNIEnv *env, jstring jstr, const char *cstr) {
    if (cstr != nullptr) {
        env->ReleaseStringUTFChars(jstr, cstr);
    }
}

extern "C"
long getFileTotalSize(FILE *file) {
    long currentPosition = ftell(file);
    fseek(file, 0, SEEK_END);
    long fileLength = ftell(file);
    fseek(file, currentPosition, SEEK_SET);
    return fileLength;
}

extern "C"
void callbackProgress(JNIEnv *env, jobject iProgress, int progress) {
    jclass cls = env->GetObjectClass(iProgress);
    jmethodID callbackMethod = env->GetMethodID(cls, "onProgress", "(I)V");
    if (callbackMethod == nullptr) return;
    env->CallVoidMethod(iProgress, callbackMethod, progress);
}

extern "C"
void test() {
    av_ac3_parse_header(nullptr, 1, 0, 0);
}



extern "C"
JNIEXPORT jint JNICALL
Java_com_wurengao_surfaceviewtestdemo_utils_NativeUtils_transformMp3ToPCM(JNIEnv *env, jobject thiz,
                                                                          jstring mp3_file,
                                                                          jstring pcm_file,
                                                                          jobject callback) {
    return -1;
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_wurengao_surfaceviewtestdemo_utils_NativeUtils_transformPCMToMp3(JNIEnv *env, jobject thiz,
                                                                          jstring mp3FilePath,
                                                                          jstring pcmFilePath,
                                                                          jobject iProgress) {
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
extern "C"
JNIEXPORT jstring JNICALL
Java_com_wurengao_surfaceviewtestdemo_utils_NativeUtils_nativeReadFromMMap(JNIEnv *env,
                                                                           jobject thiz,
                                                                           jstring path) {
    const char *file_path = env->GetStringUTFChars(path, nullptr);
    if (file_path == nullptr) {
        return env->NewStringUTF("Failed to get file path.");
    }

    int fd = open(file_path, O_RDONLY);
    if (fd == -1) {
        env->ReleaseStringUTFChars(path, file_path);
        return env->NewStringUTF("Failed to open file.");
    }

    off_t file_size = lseek(fd, 0, SEEK_END);
    void *mapped_memory = mmap(nullptr, file_size, PROT_READ, MAP_SHARED, fd, 0);
    close(fd);

    if (mapped_memory == MAP_FAILED) {
        env->ReleaseStringUTFChars(path, file_path);
        return env->NewStringUTF("Failed to mmap file.");
    }

    std::string content(static_cast<const char *>(mapped_memory));
    munmap(mapped_memory, file_size);
    env->ReleaseStringUTFChars(path, file_path);

    return env->NewStringUTF(content.c_str());
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_wurengao_surfaceviewtestdemo_utils_NativeUtils_nativeWriteToMMap(JNIEnv *env, jobject thiz,
                                                                          jstring path,
                                                                          jstring content) {
    const char *file_path = env->GetStringUTFChars(path, nullptr);
    const char *file_content = env->GetStringUTFChars(content, nullptr);

    if (file_path == nullptr || file_content == nullptr) {
        return -1;
    }

//    O_RDWR：以读写模式打开文件。
//    O_CREAT：如果文件不存在，则创建新文件。
//    O_TRUNC：如果文件存在，则将其截断为空文件。
//    S_IRUSR：设置用户（拥有者）对文件的读取权限。
//    S_IWUSR：设置用户（拥有者）对文件的写入权限。
    int fd = open(file_path, O_RDWR | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR);
    if (fd == -1) {
        env->ReleaseStringUTFChars(path, file_path);
        env->ReleaseStringUTFChars(content, file_content);
        return -2;
    }

    off_t file_size = strlen(file_content);
    if (ftruncate(fd, file_size) == -1) {
        close(fd);
        env->ReleaseStringUTFChars(path, file_path);
        env->ReleaseStringUTFChars(content, file_content);
        return -3;
    }

//    `mmap` 函数用于将文件或设备映射到内存中，以便用户空间程序可以直接访问这些资源，而无需通过标准的文件 I/O 接口。以下是 `mmap` 函数的各个参数含义：
//    1. `addr`：指定映射区域的起始地址。通常情况下，你可以将其设置为 `nullptr`，让操作系统选择合适的地址。
//    2. `length`：指定要映射的区域长度，即映射到内存中的文件或设备的大小。
//    3. `prot`：指定映射区域的保护方式。常见的取值包括：
//    - `PROT_NONE`：不允许对映射区域进行访问。
//    - `PROT_READ`：允许读取映射区域的内容。
//    - `PROT_WRITE`：允许写入映射区域的内容。
//    - `PROT_EXEC`：允许执行映射区域的内容。
//    可以通过按位或运算符 `|` 来组合多个保护方式，例如 `PROT_READ | PROT_WRITE` 表示既可读又可写。
//    4. `flags`：指定映射的选项，常见的取值包括：
//    - `MAP_SHARED`：映射区域对其他映射同一文件的进程是可见的，对该区域的写入会反映到底层文件。
//    - `MAP_PRIVATE`：创建一个私有的映射区域，对该区域的写入不会反映到底层文件。
//    - `MAP_ANONYMOUS`：创建一个匿名映射，不关联任何文件，通常用于创建匿名内存区域。
//    - `MAP_FIXED`：强制指定映射的地址，如果无法按照要求的地址映射，则映射失败。
//    同样，可以通过按位或运算符 `|` 来组合多个选项。
//    5. `fd`：已打开文件的文件描述符，用于指定要映射的文件。
//    6. `offset`：指定文件映射的起始偏移量。通常情况下，你可以将其设置为 0，表示从文件的起始位置开始映射。
//    返回值：如果成功，`mmap` 函数返回映射区域的起始地址；如果失败，返回 `MAP_FAILED`（通常为 `(void *)-1`）。
//    `mmap` 函数通常用于对文件进行内存映射，从而使得对文件的读写操作可以直接通过内存地址进行。这种技术在提高文件 I/O 效率、实现进程间通信等方面有广泛的应用。

    void *mapped_memory = mmap(nullptr, file_size, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
    close(fd);

    if (mapped_memory == MAP_FAILED) {
        env->ReleaseStringUTFChars(path, file_path);
        env->ReleaseStringUTFChars(content, file_content);
        return -4;
    }

    memcpy(mapped_memory, file_content, file_size);
    munmap(mapped_memory, file_size);

    env->ReleaseStringUTFChars(path, file_path);
    env->ReleaseStringUTFChars(content, file_content);

    return 0;

}