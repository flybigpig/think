# Instrumentation

## 说明：本文是基于Android6.0源码来分析的

1. Instrumentation这个类，我的理解是：Instrumentation是一个用来监视Activity的监测类，Activity的生命周期的函数也是Instrumentation来调用的，那么他是再什么时候初始化的呢？
2. 下面我们就来分析一下Instrumentation这个类的初始化时机和在什么时候会初始化

------

- 我们启动一个应用的时候系统就会给我们准备一个Instrumentation的实例；开启一个app进程，会掉用ActivityThread的mian方法



```csharp
public static void main(String[] args) {
       ...
    
        Process.setArgV0("<pre-initialized>");

        Looper.prepareMainLooper();

        ActivityThread thread = new ActivityThread();
        thread.attach(false);

        if (sMainThreadHandler == null) {
            sMainThreadHandler = thread.getHandler();
        }
      ...
}
```

接着是ActivityThread#attach方法,由于我们传进来的是false，所以走if里面的逻辑。IActivityManager是一个AIDL的接口，这里涉及到进程间通信，就不详细讲解了，网上已经有大量的文章讲解IPC了。IActivityManager的实现类就是大名鼎鼎的ActivityManagerService了，这是一个Android中很重要的类，管理者四大组件，这里就不介绍了，相信大家有所了解。



```java
 private void attach(boolean system) {
        sCurrentActivityThread = this;
        mSystemThread = system;
        if (!system) {
        
            ...
            
            final IActivityManager mgr = ActivityManagerNative.getDefault();
            try {
                mgr.attachApplication(mAppThread);
            } catch (RemoteException ex) {
                // Ignore
            }
            
        } else {
            
        }

      ...
      
    }
```

接着调用了ActivityManagerService#attachApplication方法



```java
 @Override
    public final void attachApplication(IApplicationThread thread) {
        synchronized (this) {
           ...
            attachApplicationLocked(thread, callingPid);
           ...
        }
    }
    
    private final boolean attachApplicationLocked(IApplicationThread thread,
            int pid) {
        
            bindApplication()
            }
```

这里又涉及到进程间通信了，IApplicationThread的实现类是ApplicationThread，所以会调用ApplicationThread#bindApplication方法。



```cpp
  thread.bindApplication(processName, appInfo, providers, app.instrumentationClass,
                    profilerInfo, app.instrumentationArguments, app.instrumentationWatcher,
                    app.instrumentationUiAutomationConnection, testMode, enableOpenGlTrace,
                    isRestrictedBackupMode || !normalMode, app.persistent,
                    new Configuration(mConfiguration), app.compat,
                    getCommonServicesLocked(app.isolated),
                    mCoreSettingsObserver.getCoreSettingsLocked());
```

然后用handler发送消息给ActivityThread#bindApplicationff



```java
public final void bindApplication(String processName, ApplicationInfo appInfo,
                List<ProviderInfo> providers, ComponentName instrumentationName,
                ProfilerInfo profilerInfo, Bundle instrumentationArgs,
                IInstrumentationWatcher instrumentationWatcher,
                IUiAutomationConnection instrumentationUiConnection, int debugMode,
                boolean enableOpenGlTrace, boolean isRestrictedBackupMode, boolean persistent,
                Configuration config, CompatibilityInfo compatInfo, Map<String, IBinder> services,
                Bundle coreSettings) {
                    ...
                    sendMessage(H.BIND_APPLICATION, data);
                    
                }
```

最后一行代码会用handler发送消息给ActivityThread的H



```cpp
 public void handleMessage(Message msg) {
     ...
     case BIND_APPLICATION:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "bindApplication");
                    AppBindData data = (AppBindData)msg.obj;
                    handleBindApplication(data);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
     ...
 }
```

在handleBindApplication(data)中我们终于看到了mInstrumentation的创建，是通过反射来创建实例的。



```cpp
 private void handleBindApplication(AppBindData data) {
 try {
                java.lang.ClassLoader cl = instrContext.getClassLoader();
                mInstrumentation = (Instrumentation)
                    cl.loadClass(data.instrumentationName.getClassName()).newInstance();
            } catch (Exception e) {
                throw new RuntimeException(
                    "Unable to instantiate instrumentation "
                    + data.instrumentationName + ": " + e.toString(), e);
            }
            　
     ...
 }
```

ok，Instrumentation的初始化就介绍完了，因为这个类和Actiity的关系还是比较密切的，前面说过，Activity的生命周期方法都是通过这个类来掉用的，所以我们把他初始化的流程大概也梳理这讲解一下,相对来说，流程还是比较简单的，这里就不给出流程图来.



作者：AN_9c94
链接：https://www.jianshu.com/p/9ab6bae48ed8
来源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。