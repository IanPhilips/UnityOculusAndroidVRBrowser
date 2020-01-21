## For more information about using CMake with Android Studio, read the
## documentation: https://d.android.com/studio/projects/add-native-code.html
#
## Sets the minimum version of CMake required to build the native library.
#
#cmake_minimum_required(VERSION 3.4.1)
## set targetPlatform, will be passed in from gradle when this sample is completed
## openGL Supportability
## platform         status
##   (0 12)           ES2/ES3 not supported
##   [12, 18)         ES2 only; for ES3, app do dynamic load/detection
##                    this applies to the situations that:
##                        - minimum API is set to less than 18. In this case
##                          there is no ES3 header/lib support inside NDK
##                        - the built APK might be running on newer API phones
##                    with dynamic loading of ES3, the same APK would still be able
##                    to use ES3. Otherwise, app would stuck with ES2 even phone is
##                    is newer than the minimum API level (for example, Android-27 etc).
##
##   [18, 24)         ES2 & ES3
##                    If app is built to only support API-18 or later,
##                    set minimum api level to 18 is good enough, NDK supprts ES3
##                    with the right header and lib files. No need to use ES3 dynamic
##                    detection.
##   [24, infinite)   ES2 & ES3 & Vulkan
#
#set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall")
#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -fno-rtti -fno-exceptions -Wall")
#if (${ANDROID_PLATFORM_LEVEL} LESS 12)
#    message(FATAL_ERROR "OpenGL 2 is not supported before API level 11 \
#                      (currently using ${ANDROID_PLATFORM_LEVEL}).")
#    return()
#elseif (${ANDROID_PLATFORM_LEVEL} LESS 18)
#    add_definitions("-DDYNAMIC_ES3")
#    set(GL3STUB_SRC  src/main/cpp/gl3stub.c)
#    set(OPENGL_LIB GLESv2)
#else ()
#    set(OPENGL_LIB GLESv3)
#endif (${ANDROID_PLATFORM_LEVEL} LESS 12)
#
#add_library(gles3jni SHARED
#        ${GL3STUB_SRC}
#         src/main/cpp/gles3jni.cpp
#        src/main/cpp/RendererES2.cpp
#        src/main/cpp/RendererES3.cpp)
#
## Include libraries needed for gles3jni lib
#target_link_libraries(gles3jni
#        ${OPENGL_LIB}
#        android
#        EGL
#        log
#        m
#        )
#
## Creates and names a library, sets it as either STATIC
## or SHARED, and provides the relative paths to its source code.
## You can define multiple libraries, and CMake builds them for you.
## Gradle automatically packages shared libraries with your APK.
##
#add_library( # Sets the name of the library.
#             native-lib
#
#             # Sets the library as a shared library.
#             SHARED
#
#             # Provides a relative path to your source file(s).
#             src/main/cpp/NativeTexturePlugin.cpp )
#
## Searches for a specified prebuilt library and stores the path as a
## variable. Because CMake includes system libraries in the search path by
## default, you only need to specify the name of the public NDK library
## you want to add. CMake verifies that the library exists before
## completing its build.
#
##find_library( # Sets the name of the path variable.
##              log-lib
##
##              # Specifies the name of the NDK library that
##              # you want CMake to locate.
##              log )
#
## Specifies libraries CMake should link to your target library. You
## can link multiple libraries, such as libraries you define in this
## build script, prebuilt third-party libraries, or system libraries.
#
#target_link_libraries( # Specifies the target library.
#                   native-lib
#
#                   # Links the target library to the log library
#                   # included in the NDK.
#                   ${log-lib}
#                   ${OPENGL_LIB}
#                   gles3jni
#                   EGL
#                   )