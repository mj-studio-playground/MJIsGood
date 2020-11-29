#include "native.h"
#include <string>

#define REQRES_BASE_URL "https://reqres.in/api/"
#define KAKAO_BASE_URL "https://dapi.kakao.com/"
#define KAKAO_API_KEY "61404525f2982c9feae6b5e8cff7743d"

bool is_same_string(const char *lhs, const char *rhs) {
    return strcmp(lhs, rhs) == 0;
}

extern "C" {
JNIEXPORT jstring
JNICALL
Java_happy_mjstudio_sopt27_utils_NativeLibImpl_getConstant(JNIEnv *jEnv, jobject thiz,
                                                           jstring key) {
    const char *c_key = jEnv->GetStringUTFChars(key, 0);

    if (is_same_string(c_key, "REQRES_BASE_URL")) {
        return jEnv->NewStringUTF(REQRES_BASE_URL);
    }
    if (is_same_string(c_key, "KAKAO_BASE_URL")) {
        return jEnv->NewStringUTF(KAKAO_BASE_URL);
    }
    if (is_same_string(c_key, "KAKAO_API_KEY")) {
        return jEnv->NewStringUTF(KAKAO_API_KEY);
    }


    return jEnv->NewStringUTF("");
}
}