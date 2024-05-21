#!/bin/sh

# lame库目录
libDir="Users/bytedance/proj/SurfaceViewTestDemo/app/src/main/cpp/lame-3.100/"
# ndk目录
ndkDir="/Users/bytedance/Library/Android/sdk/ndk/21.1.6352462"
# 输出的库目录
productRootDir="/Users/bytedance/proj/SurfaceViewTestDemo/app/src/main/jniLibs"
# 架构
#archs="armeabi-v7a arm64-v8a x86 x86_64"
archs="arm64-v8a"
# api等级
androidApiLevel="29"
# --- 输入参数指定 End ---

for arch in ${archs}
do
    echo "comile arch: ${arch}"

    case $arch in
    armeabi-v7a)
        archHost="armv7a-linux-androideabi"
        crossCompile="arm-linux-androideabi"
    ;;

    arm64-v8a)
        archHost="aarch64-linux-android"
        crossCompile=${archHost}
    ;;

    x86)
        archHost="i686-linux-android"
        crossCompile=${archHost}
    ;;

    x86_64)
        archHost="x86_64-linux-android"
        crossCompile=${archHost}
    ;;
    *)  echo "Parameter is not valid."
        exit
    ;;
esac

export CC="${ndkDir}/toolchains/llvm/prebuilt/darwin-x86_64/bin/${archHost}${androidApiLevel}-clang"
export CXX="${ndkDir}/toolchains/llvm/prebuilt/darwin-x86_64/bin/${archHost}${androidApiLevel}-clang++"
prebuilt="${ndkDir}/toolchains/llvm/prebuilt/darwin-x86_64"
export AS=${prebuilt}/bin/${crossCompile}-as
export LD=${prebuilt}/bin/${crossCompile}-ld
export NM=${prebuilt}/bin/${crossCompile}-nm
export STRIP=${prebuilt}/bin/${crossCompile}-strip
export RANLIB=${prebuilt}/bin/${crossCompile}-ranlib
export AR=${prebuilt}/bin/${crossCompile}-ar

productDir="${productRootDir}/${arch}"

cd ${libDir}

# config
./configure \
--host=${archHost} \
--enable-shared \
--disable-static \
--target=android \
--disable-frontend \
--prefix="${productDir}"

# make
make clean
make -j8
make install

done