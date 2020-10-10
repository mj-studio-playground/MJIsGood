#include "native.h"
#include <string>

#define KEY_LEN 512

bool is_same_string(const char *lhs, const char *rhs) {
    return strcmp(lhs, rhs) == 0;
}

extern "C" {
JNIEXPORT jstring
JNICALL
Java_happy_mjstudio_sopt27_utils_NativeLib_getConstant(JNIEnv *jEnv, jobject thiz, jstring
key) {
    char c_key[KEY_LEN];

    jEnv->GetStringUTFRegion(key, 0, KEY_LEN, c_key);

    if (is_same_string(c_key, "sample key")) {
        return jEnv->NewStringUTF("sample value");
    }


    return jEnv->NewStringUTF("");
}
}