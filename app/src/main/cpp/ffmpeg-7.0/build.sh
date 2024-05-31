#!/bin/bash

NDK_PATH=/Users/bytedance/Library/Android/sdk/ndk/21.1.6352462                  # Android NDK 路径
TOOLCHAIN=$NDK_PATH/toolchains/llvm/prebuilt/darwin-x86_64                      # 工具链路径
SYSROOT=$TOOLCHAIN/sysroot
OUTPUT_PREFIX=/Users/bytedance/proj/Demos/app/src/main/jniLibs    # 编译输出目录
ANDROID_API_VERSION=29

#archs="armeabi-v7a arm64-v8a x86 x86_64"

for ARCH in arm64-v8a; do
  if [[ "$ARCH" == "arm" ]]; then
    CROSS_PREFIX=armv7a-linux-androideabi${ANDROID_API_VERSION}-
  else
    CROSS_PREFIX=aarch64-linux-android${ANDROID_API_VERSION}-
  fi

  CC="$TOOLCHAIN/bin/${CROSS_PREFIX}clang"
  CXX="$TOOLCHAIN/bin/${CROSS_PREFIX}clang++"
  OUTPUT=$OUTPUT_PREFIX/$ARCH
  echo $CC
  echo $CXX
  echo $OUTPUT

  ./configure \
    --prefix=$OUTPUT \
    --target-os=android \
    --arch=$ARCH  \
    --disable-asm \
    --enable-neon \
    --enable-cross-compile \
    --enable-shared \
    --disable-static \
    --disable-doc \
    --disable-ffplay \
    --disable-ffprobe \
    --disable-symver \
    --disable-ffmpeg \
    --cc=$CC \
    --cxx=$CXX \
    --sysroot=$SYSROOT \
    --extra-cflags="-Os -fpic $OPTIMIZE_CFLAGS" \

  make clean all
  make -j8
  make install
done
