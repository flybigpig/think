

# Fragment 相关两个不常见 API



#### FragmentManager.executePendingTransactions()



#### FragmentTransaction.commitNow();



- question

1. 这两个 API 有何作用？
2. 这两个 API 有啥区别？
3. 这两个 API 有什么使用场景么？

- tip
  - 你在 LifeCycle 的源码中可以看到：

```



public class ReportFragment extends Fragment {
    private static final String REPORT_FRAGMENT_TAG = "androidx.lifecycle"
            + ".LifecycleDispatcher.report_fragment_tag";

    public static void injectIfNeededIn(Activity activity) {
        // ProcessLifecycleOwner should always correctly work and some activities may not extend
        // FragmentActivity from support lib, so we use framework fragments for activities
        android.app.FragmentManager manager = activity.getFragmentManager();
        if (manager.findFragmentByTag(REPORT_FRAGMENT_TAG) == null) {
            manager.beginTransaction().add(new ReportFragment(), REPORT_FRAGMENT_TAG).commit();
            // Hopefully, we are the first to make a transaction.
            manager.executePendingTransactions();
        }
    }
    
  
```

	- 你在 RxPermission 可以看到以下使用：

```


private RxPermissionsFragment getRxPermissionsFragment(@NonNull final FragmentManager fragmentManager) {
        RxPermissionsFragment rxPermissionsFragment = findRxPermissionsFragment(fragmentManager);
        boolean isNewInstance = rxPermissionsFragment == null;
        if (isNewInstance) {
            rxPermissionsFragment = new RxPermissionsFragment();
            fragmentManager
                    .beginTransaction()
                    .add(rxPermissionsFragment, TAG)
                    .commitNow();
        }
        return rxPermissionsFragment;
    }

```





- answer