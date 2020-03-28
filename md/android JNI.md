# android JNI

+ Date  2020 0202

####  ndk

> new version has replace with cmake, cmake as the old android.mk file.



```

JNI有如下两种注册native方法的途径：

静态注册：
先由Java得到本地方法的声明，然后再通过JNI实现该声明方法
动态注册：
先通过JNI重载JNI_OnLoad()实现本地方法，然后直接在Java中调用本地方法。

```



### 动态注册

​	 当我们使用`System.loadLibarary()`方法加载so库的时候，Java虚拟机就会找到这个`JNI_OnLoad`函数兵调用该函数，这个函数的作用是告诉Dalvik虚拟机此C库使用的是哪一个JNI版本，如果你的库里面没有写明JNI_OnLoad()函数，VM会默认该库使用最老的JNI 1.1版本。由于最新版本的JNI做了很多扩充，也优化了一些内容，如果需要使用JNI新版本的功能，就必须在`JNI_OnLoad()`函数声明JNI的版本。同时也可以在该函数中做一些初始化的动作，其实这个函数有点类似于`Android`中的`Activity`中的`onCreate()`方法。该函数前面也有三个关键字分别是`JNIEXPORT`，`JNICALL` ，`jint`。其中`JNIEXPORT`和`JNICALL`是两个宏定义，用于指定该函数时JNI函数。jint是JNI定义的数据类型，因为Java层和C/C++的数据类型或者对象不能直接相互的引用或者使用，JNI层定义了自己的数据类型，用于衔接`Java`层和JNI层



`JNI_OnLoad`声明版本，同时进行初始化操作。

`JNI_OnUnload`与上方法相对应



#### 存在 java 调用jni 方法

`System.loadLibarary()` 方法加载so库，定义原生方法native

通过生成.h文件，.cpp文件生成nvative 方法。



```
相关命令

javac *.java    	# 编译java类

javah MyJNI		 # 生成h头文件

set JAVA_HOME=D:\SoftwareDeveloping\jdk32bit_1.6		# 设置javahome

#        生成.cpp， 运行之后生成.o文件
g++ -Wl,--add-stdcall-alias -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" -shared -o MyJNI.dll  
MyJNIImpl.cpp

java MyJNI   #  运行文件，调用jni
```



#### jni 调用java方法

`example`

举例说明，首先是加载so库



```cpp
public class JniDemo1{
       static {
             System.loadLibrary("samplelib_jni");   // 加载so 库
        }
}
```

在jni中的实现



```cpp
jint JNI_OnLoad(JavaVM* vm, void* reserved)
```

并且在这个函数里面去动态的注册native方法，完整的参考代码如下：



```cpp
#include <jni.h>
#include "Log4Android.h"
#include <stdio.h>
#include <stdlib.h>

using namespace std;

#ifdef __cplusplus
extern "C" {
#endif

static const char *className = "com/gebilaolitou/jnidemo/JNIDemo2";

static void sayHello(JNIEnv *env, jobject, jlong handle) {
    LOGI("JNI", "native: say hello ###");
}

static JNINativeMethod gJni_Methods_table[] = {
    {"sayHello", "(J)V", (void*)sayHello},
};

static int jniRegisterNativeMethods(JNIEnv* env, const char* className,
    const JNINativeMethod* gMethods, int numMethods)
{
    jclass clazz;

    LOGI("JNI","Registering %s natives\n", className);
    clazz = (env)->FindClass( className);
    if (clazz == NULL) {
        LOGE("JNI","Native registration unable to find class '%s'\n", className);
        return -1;
    }

    int result = 0;
    if ((env)->RegisterNatives(clazz, gJni_Methods_table, numMethods) < 0) {
        LOGE("JNI","RegisterNatives failed for '%s'\n", className);
        result = -1;
    }

    (env)->DeleteLocalRef(clazz);
    return result;
}

jint JNI_OnLoad(JavaVM* vm, void* reserved){
    LOGI("JNI", "enter jni_onload");

    JNIEnv* env = NULL;
    jint result = -1;

    if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
        return result;
    }

    jniRegisterNativeMethods(env, className, gJni_Methods_table, sizeof(gJni_Methods_table) / sizeof(JNINativeMethod));

    return JNI_VERSION_1_4;
}

#ifdef __cplusplus
}
#endif
```



#### 签名

```
"签名"，即将参数类型和返回值类型的组合
javap 查看签名
```



jclass GetObjectClass(jobject obj)：
 通过对象实例来获取jclass，相当于Java中的getClass()函数

jclass getSuperClass(jclass obj)：
 通过jclass可以获取其父类的jclass对象



#### 构造对象

```
jmethodID mid = (*env)->GetMethodID(env, cls, "<init>", "()V");
obj = (*env)->NewObject(env, cls, mid);
```

