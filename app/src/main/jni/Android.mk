LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := libinvoker
LOCAL_SRC_FILES := Compiled/$(TARGET_ARCH_ABI)/libinvoker.so

include $(PREBUILT_SHARED_LIBRARY)
