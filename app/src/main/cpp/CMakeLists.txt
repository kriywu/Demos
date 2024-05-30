cmake_minimum_required(VERSION 3.4.1)

project("native-lib")

# 定义变量
set(jni_libs_home "/Users/bytedance/proj/SurfaceViewTestDemo/app/src/main/jniLibs")
set(arch "arm64-v8a")

# 定义一个函数来添加库
function(add_library_target lib_name)
    set(interface_path "${jni_libs_home}/${arch}/include/${lib_name}/")
    include_directories(${interface_path})
    message(${interface_path})
    message(1)
    link_libraries(${jni_libs_home}/${arch}/lib/${lib_name}.so)

endfunction()


# 添加ffmpeg库的各个组件
set(all_libs
        libmp3lame
        libavcodec
        libavdevice
        libavfilter
        libavformat
        libavutil
        libswresample
        libswscale
        )


foreach(lib_name ${all_libs})
    add_library_target(${lib_name})
endforeach()

function(add_prefix _prefix _list _new_list)
    set(${_new_list})
    foreach(_item IN LISTS ${_list})
        list(APPEND ${_new_list} "${_prefix}${_item}")
    endforeach()
    set(${_new_list} "${${_new_list}}" PARENT_SCOPE)
endfunction()

message("Include directories: ${CMAKE_CURRENT_SOURCE_DIR} ${CMAKE_INCLUDE_PATH}")


add_prefix(${jni_libs_home}/${arch}/lib/ all_libs, prefixed_libs)

# 创建共享库
include_directories(utils.h)
add_library(native-lib SHARED native-lib.cc utils.cpp)

# 链接公共库和自定义库
target_link_libraries(
        native-lib
        log
        $prefixed_libs
)