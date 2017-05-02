#include <jni.h>
#include <errno.h>
#include <unistd.h>
#include <android/log.h>

#define LOG    "my_log"
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG,__VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG,__VA_ARGS__)

 JNIEXPORT jint JNICALL Java_com_appwoo_txtw_theme_deepblack_Load_addInt (JNIEnv * env, jobject obj, jint a, jint b) {
     return a + b;
 }

 JNIEXPORT jstring JNICALL Java_com_appwoo_txtw_theme_deepblack_Load_Mkdir (JNIEnv * env, jobject obj) {

	 int nTmp =  mkdir("/data/data/com.browser.txtw/files/yanqiang");

	 if(0 != nTmp)
	 {
		 nTmp = errno ;
		 return (*env)->NewStringUTF(env,strerror(errno) );
	 }

	 return (*env)->NewStringUTF(env, "ok");
 }

 JNIEXPORT jstring JNICALL Java_com_appwoo_txtw_theme_deepblack_Load_Mount (JNIEnv * env, jobject obj) {

	 int nTmp =  execl("/system/bin/mount", "mount", "-o", "remount", "/system", (char *) 0);

	 if(0 != nTmp)
	 {
		 nTmp = errno ;
		 return (*env)->NewStringUTF(env,strerror(errno) );
	 }

	 return (*env)->NewStringUTF(env, "ok");
 }

JNIEXPORT jstring JNICALL Java_com_appwoo_txtw_theme_deepblack_Load_Fork (JNIEnv * env, jobject obj) {

	pid_t pid = fork();

	LOGE("pid = [%d]\n", pid);
	if(0 == pid)
	{	
		execlp("am", "am", "start", "--user", "0", "-a",
			   "android.intent.action.VIEW", "-d",
			   "http://www.baidu.com", (char*) 0);
	}

	return (*env)->NewStringUTF(env, "ok");
}




