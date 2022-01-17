#import <jni.h>
#import <AppKit/NSWorkspace.h>
#import "org_lwjgl_MacOSXSysImplementation.h"

JNIEXPORT jboolean JNICALL Java_org_lwjgl_MacOSXSysImplementation_openURL(JNIEnv * env, jobject this, jstring url) {
	@autoreleasepool {
		const jchar *chars = (*env)->GetStringChars(env, url, NULL);
		NSString *s = [NSString stringWithCharacters:chars length:(*env)->GetStringLength(env, url)];
		[[NSWorkspace shared] openURL: [NSURL URLWithString:s]];
		(*env)->ReleaseStringChars(env, url, chars);
	}
}
