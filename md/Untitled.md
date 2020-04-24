# 未定义



[TOC]



##### 事务

```
mArticalFragment = new ArticalFragment();
FragmentManager fragmentManager = getSupportFragmentManager();
FragmentTransaction transaction = fragmentManager.beginTransaction();
transaction.add(R.id.fragment, mArticalFragment);
transaction.commit();
transaction.show(mArticalFragment);
```



##### SharedPreferences

```
  SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
  editor.putString("name", "Tom");
  editor.putInt("age", 28);
  editor.putBoolean("married", false);
  editor.apply();
```



##### SQLite

> 1 继承SQLiteOpenHelper 
>
> 2 重写方法
>
> ```
> public class SqlData extends SQLiteOpenHelper {
> 
> 	public static final String sql = "create table Book ("
>             + "id integer primary key autoincrement, "
>             + "author text, "
>             + "price real, "
>             + "pages integer, "
>             + "name text)";
> 
>     public SqlData(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
>         super(context, name, factory, version);
>     }
> 
>     @Override
>     public void onCreate(SQLiteDatabase db) {
> 		db.execSQL(sql)
>     }
> 
>     @Override
>     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
> 		db.execSQL("drop table if exists Book");
>         db.execSQL("drop table if exists Category");
>         onCreate(db);
>     }
> }
> 
> ```
>
> 3  getReadableDatabase()` 或`getWritableDatabase()都能够过去一个数据库
>
> 4  添加数据
>
> ```
> SQLiteDatabase db = dbHelper.getWritableDatabase();
>                 ContentValues values = new ContentValues();
>                 // 开始组装第一条数据
>                 values.put("name", "The Da Vinci Code");
>                 values.put("author", "Dan Brown");
>                 values.put("pages", 454);
>                 values.put("price", 16.96);
>                 db.insert("Book", null, values); // 插入第一条数据
>                 values.clear();
> ```
>
> 5 更新数据
>
> ```
>  SQLiteDatabase db = dbHelper.getWritableDatabase();
>                 ContentValues values = new ContentValues();
>                 values.put("price", 10.99);
>                 db.update("Book", values, "name = ?", new String[] { "The Da Vinci
>                     Code" });
>          
> ```
>
> 6 删除数据
>
> ```
>  SQLiteDatabase db = dbHelper.getWritableDatabase();
>                 db.delete("Book", "pages > ?", new String[] { "500" });
> ```
>
> 7 查询
>
> ```
> SQLiteDatabase db = dbHelper.getWritableDatabase();
>                 // 查询Book表中所有的数据
>                 Cursor cursor = db.query("Book", null, null, null, null, null, null);
>                 if (cursor.moveToFirst()) {
>                     do {
>                         // 遍历Cursor对象，取出数据并打印
>                         String name = cursor.getString(cursor.getColumnIndex
>                             ("name"));
>                         String author = cursor.getString(cursor.getColumnIndex
>                             ("author"));
>                         int pages = cursor.getInt(cursor.getColumnIndex("pages"));
>                         double price = cursor.getDouble(cursor.getColumnIndex
>                             ("price"));
>                         Log.d("MainActivity", "book name is " + name);
>                         Log.d("MainActivity", "book author is " + author);
>                         Log.d("MainActivity", "book pages is " + pages);
>                         Log.d("MainActivity", "book price is " + price);
>                     } while (cursor.moveToNext());
>                 }
>                 cursor.close();
> ```
>
> 

#####  ContentResolver

> tip :
>
> 访问内容提供器中共享的数据，就一定要借助ContentResolver类
>
> URI可以非常清楚地表达出我们想要访问哪个程序中哪张表里的数据，ContentResolver中的增删改查方法才都接收`Uri` 对象作为参数
>
> 因为如果使用表名的话，系统将无法得知我们期望访问的是哪个应用程序里的表。
>
> 解析地址 `Uri uri = Uri.parse("content://com.example.app.provider/table1")`
>
> ex:
>
> ```
> Cursor cursor = null;
>         try {
>             // 查询联系人数据
>             cursor = getContentResolver().query(ContactsContract.CommonDataKinds.
>                 Phone.CONTENT_URI, null, null, null, null);
>             if (cursor != null) {
>                 while (cursor.moveToNext()) {
>                     // 获取联系人姓名
>                     String displayName = cursor.getString(cursor.getColumnIndex
>                        (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
>                     // 获取联系人手机号
>                     String number = cursor.getString(cursor.getColumnIndex
>                        (ContactsContract.CommonDataKinds.Phone.NUMBER));
>                     contactsList.add(displayName + "\n" + number);
>                 }
>                 adapter.notifyDataSetChanged();
>             }
>         } catch (Exception e) {
>             e.printStackTrace();
>         } finally {
>             if (cursor != null) {
>                 cursor.close();
>             }
>         }
> ```
>
> 

#####  view



######  事件分发

> public boolean dispatchTouchEvent(MotionEvent event){
>
> ​	boolean result=false;
>
> ​	if(onInterceptTouchEvent(event)){
>
> ​		result=super.onTouchEvent(event);
>
> ​	}else{
>
> ​		result=child.dispatchTouchEvent(event);
>
> ​	}
>
> ​	retuen rsult;
>
> }
>
> * 若是一直传递到底层都不处理，那就回传给Activity
> * 



######  view 工作流程

###### 1  MeasureSpec

MeasueSpec是View内部类，其封装了一个View的规格尺寸，包括View的宽高信息，他的作用是在Measure流程中，系统会将View的LayoutParams根据父容器所施加的规则转换成对应的MeasureSpec,然后再onMeasure方法中根据这个MeasureSpec来确定View的宽高



```
public static class MeasureSpec {

    private static final int MODE_SHIFT = 30;
    
    private static final int MODE_MASK  = 0x3 << MODE_SHIFT;

  
    public static final int UNSPECIFIED = 0 << MODE_SHIFT;

   
    public static final int EXACTLY     = 1 << MODE_SHIFT;

   
    public static final int AT_MOST     = 2 << MODE_SHIFT;
    
    ···
    
    
 }
```

> 对于每一个View,都持有MeasureSpec 而该MeasureSpec则保存了该View的尺寸规格，
>
> 在View的测量中，通makeMeasueSpec来保存宽和高的信息。通过getMode或者getSize得到模式和宽、高
>
> MeasureSpec是受自身LayoutParams和父容器的MeasureSpec共同影响的。
>
> 作为顶层View的DecorView来说，其实并没有父容器，它的MeasureSpec是由ViewRootImpl的PerformTravels方法。





###### view的measure流程



view

```
@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(),widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(),heightMeasureSpec));
    }

protected final void setMeasuredDimension(int measuredWidth, int measuredHeight) {
        boolean optical = isLayoutModeOptical(this);
        if (optical != isLayoutModeOptical(mParent)) {
            Insets insets = getOpticalInsets();
            int opticalWidth  = insets.left + insets.right;
            int opticalHeight = insets.top  + insets.bottom;

            measuredWidth  += optical ? opticalWidth  : -opticalWidth;
            measuredHeight += optical ? opticalHeight : -opticalHeight;
        }
        setMeasuredDimensionRaw(measuredWidth, measuredHeight);
    }

public static int getDefaultSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
        case MeasureSpec.UNSPECIFIED:
            result = size;
            break;
        case MeasureSpec.AT_MOST:
        case MeasureSpec.EXACTLY:
            result = specSize;
            break;
        }
        return result;
    }
```



viewGroup

> 对于viewgroup 它不止需要测量自身，还要遍历调用子元素的measure的方法
>
> viewgroup中没有定义onMeasure(),但却定义measureChildren();



































































































##### LruCache

##### [内存缓存LruCache实现原理](https://www.cnblogs.com/liuling/p/2015-9-24-1.html)

　　自己项目中一直都是用的开源的xUtils框架，包括BitmapUtils、DbUtils、ViewUtils和HttpUtils四大模块，这四大模块都是项目中比较常用的。最近决定研究一下xUtils的源码，用了这么久总得知道它的实现原理吧。我是先从先从BitmapUtils模块开始的。BitmapUtils和大多数图片加载框架一样，都是基于内存-文件-网络三级缓存。也就是加载图片的时候首先从内存缓存中取，如果没有再从文件缓存中取，如果文件缓存没有取到，就从网络下载图片并且加入内存和文件缓存。

　　这篇帖子先分析内存缓存是如何实现的。好吧开始进入正题。

　　BitmapUtils内存缓存的核心类LruMemoryCache，LruMemoryCache代码和v4包的LruCache一样，只是加了一个存储超期的处理，这里分析LruCache源码。LRU即Least Recently Used，近期最少使用算法。也就是当内存缓存达到设定的最大值时将内存缓存中近期最少使用的对象移除，有效的避免了OOM的出现。

 

​    讲到LruCache不得不提一下LinkedHashMap，因为LruCache中Lru算法的实现就是通过LinkedHashMap来实现的。LinkedHashMap继承于HashMap，它使用了一个双向链表来存储Map中的Entry顺序关系，这种顺序有两种，一种是LRU顺序，一种是插入顺序，这可以由其构造函数public LinkedHashMap(int initialCapacity,float loadFactor, boolean accessOrder)指定。所以，对于get、put、remove等操作，LinkedHashMap除了要做HashMap做的事情，还做些调整Entry顺序链表的工作。LruCache中将LinkedHashMap的顺序设置为LRU顺序来实现LRU缓存，每次调用get(也就是从内存缓存中取图片)，则将该对象移到链表的尾端。调用put插入新的对象也是存储在链表尾端，这样当内存缓存达到设定的最大值时，将链表头部的对象（近期最少用到的）移除。关于LinkedHashMap详解请前往http://www.cnblogs.com/children/archive/2012/10/02/2710624.html。

```
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.support.v4.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Static library version of {@link android.util.LruCache}. Used to write apps
 * that run on API levels prior to 12. When running on API level 12 or above,
 * this implementation is still used; it does not try to switch to the
 * framework's implementation. See the framework SDK documentation for a class
 * overview.
 */
public class LruCache<K, V> {
    private final LinkedHashMap<K, V> map;

    /** Size of this cache in units. Not necessarily the number of elements. */
    private int size;    //当前cache的大小
    private int maxSize; //cache最大大小

    private int putCount;       //put的次数
    private int createCount;    //create的次数
    private int evictionCount;  //回收的次数
    private int hitCount;       //命中的次数
    private int missCount;      //未命中次数

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *     the maximum number of entries in the cache. For all other caches,
     *     this is the maximum sum of the sizes of the entries in this cache.
     */
    public LruCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
        //将LinkedHashMap的accessOrder设置为true来实现LRU
        this.map = new LinkedHashMap<K, V>(0, 0.75f, true);  
    }

    /**
     * Returns the value for {@code key} if it exists in the cache or can be
     * created by {@code #create}. If a value was returned, it is moved to the
     * head of the queue. This returns null if a value is not cached and cannot
     * be created.
     * 通过key获取相应的item，或者创建返回相应的item。相应的item会移动到队列的尾部，
     * 如果item的value没有被cache或者不能被创建，则返回null。
     */
    public final V get(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        V mapValue;
        synchronized (this) {
            mapValue = map.get(key);
            if (mapValue != null) {
                //mapValue不为空表示命中，hitCount+1并返回mapValue对象
                hitCount++;
                return mapValue;
            }
            missCount++;  //未命中
        }

        /*
         * Attempt to create a value. This may take a long time, and the map
         * may be different when create() returns. If a conflicting value was
         * added to the map while create() was working, we leave that value in
         * the map and release the created value.
         * 如果未命中，则试图创建一个对象，这里create方法返回null,并没有实现创建对象的方法
         * 如果需要事项创建对象的方法可以重写create方法。因为图片缓存时内存缓存没有命中会去
         * 文件缓存中去取或者从网络下载，所以并不需要创建。
         */
        V createdValue = create(key);
        if (createdValue == null) {
            return null;
        }
        //假如创建了新的对象，则继续往下执行
        synchronized (this) {
            createCount++;  
            //将createdValue加入到map中，并且将原来键为key的对象保存到mapValue
            mapValue = map.put(key, createdValue);   
            if (mapValue != null) {
                // There was a conflict so undo that last put
                //如果mapValue不为空，则撤销上一步的put操作。
                map.put(key, mapValue);
            } else {
                //加入新创建的对象之后需要重新计算size大小
                size += safeSizeOf(key, createdValue);
            }
        }

        if (mapValue != null) {
            entryRemoved(false, key, createdValue, mapValue);
            return mapValue;
        } else {
            //每次新加入对象都需要调用trimToSize方法看是否需要回收
            trimToSize(maxSize);
            return createdValue;
        }
    }

    /**
     * Caches {@code value} for {@code key}. The value is moved to the head of
     * the queue.
     *
     * @return the previous value mapped by {@code key}.
     */
    public final V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }

        V previous;
        synchronized (this) {
            putCount++;
            size += safeSizeOf(key, value);  //size加上预put对象的大小
            previous = map.put(key, value);
            if (previous != null) {
                //如果之前存在键为key的对象，则size应该减去原来对象的大小
                size -= safeSizeOf(key, previous);
            }
        }

        if (previous != null) {
            entryRemoved(false, key, previous, value);
        }
        //每次新加入对象都需要调用trimToSize方法看是否需要回收
        trimToSize(maxSize);
        return previous;
    }

    /**
     * @param maxSize the maximum size of the cache before returning. May be -1
     *     to evict even 0-sized elements.
     * 此方法根据maxSize来调整内存cache的大小，如果maxSize传入-1，则清空缓存中的所有对象
     */
    private void trimToSize(int maxSize) {
        while (true) {
            K key;
            V value;
            synchronized (this) {
                if (size < 0 || (map.isEmpty() && size != 0)) {
                    throw new IllegalStateException(getClass().getName()
                            + ".sizeOf() is reporting inconsistent results!");
                }
                //如果当前size小于maxSize或者map没有任何对象,则结束循环
                if (size <= maxSize || map.isEmpty()) {
                    break;
                }
                //移除链表头部的元素，并进入下一次循环
                Map.Entry<K, V> toEvict = map.entrySet().iterator().next();
                key = toEvict.getKey();
                value = toEvict.getValue();
                map.remove(key);
                size -= safeSizeOf(key, value);
                evictionCount++;  //回收次数+1
            }

            entryRemoved(true, key, value, null);
        }
    }

    /**
     * Removes the entry for {@code key} if it exists.
     *
     * @return the previous value mapped by {@code key}.
     * 从内存缓存中根据key值移除某个对象并返回该对象
     */
    public final V remove(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        V previous;
        synchronized (this) {
            previous = map.remove(key);
            if (previous != null) {
                size -= safeSizeOf(key, previous);
            }
        }

        if (previous != null) {
            entryRemoved(false, key, previous, null);
        }

        return previous;
    }

    /**
     * Called for entries that have been evicted or removed. This method is
     * invoked when a value is evicted to make space, removed by a call to
     * {@link #remove}, or replaced by a call to {@link #put}. The default
     * implementation does nothing.
     *
     * <p>The method is called without synchronization: other threads may
     * access the cache while this method is executing.
     *
     * @param evicted true if the entry is being removed to make space, false
     *     if the removal was caused by a {@link #put} or {@link #remove}.
     * @param newValue the new value for {@code key}, if it exists. If non-null,
     *     this removal was caused by a {@link #put}. Otherwise it was caused by
     *     an eviction or a {@link #remove}.
     */
    protected void entryRemoved(boolean evicted, K key, V oldValue, V newValue) {}

    /**
     * Called after a cache miss to compute a value for the corresponding key.
     * Returns the computed value or null if no value can be computed. The
     * default implementation returns null.
     *
     * <p>The method is called without synchronization: other threads may
     * access the cache while this method is executing.
     *
     * <p>If a value for {@code key} exists in the cache when this method
     * returns, the created value will be released with {@link #entryRemoved}
     * and discarded. This can occur when multiple threads request the same key
     * at the same time (causing multiple values to be created), or when one
     * thread calls {@link #put} while another is creating a value for the same
     * key.
     */
    protected V create(K key) {
        return null;
    }

    private int safeSizeOf(K key, V value) {
        int result = sizeOf(key, value);
        if (result < 0) {
            throw new IllegalStateException("Negative size: " + key + "=" + value);
        }
        return result;
    }

    /**
     * Returns the size of the entry for {@code key} and {@code value} in
     * user-defined units.  The default implementation returns 1 so that size
     * is the number of entries and max size is the maximum number of entries.
     *
     * <p>An entry's size must not change while it is in the cache.
     * 用来计算单个对象的大小，这里默认返回1，一般需要重写该方法来计算对象的大小
     * xUtils中创建LruMemoryCache时就重写了sizeOf方法来计算bitmap的大小
     * mMemoryCache = new LruMemoryCache<MemoryCacheKey, Bitmap>(globalConfig.getMemoryCacheSize()) {
     *       @Override
     *       protected int sizeOf(MemoryCacheKey key, Bitmap bitmap) {
     *           if (bitmap == null) return 0;
     *           return bitmap.getRowBytes() * bitmap.getHeight();
     *       }
     *   };
     *
     */
    protected int sizeOf(K key, V value) {
        return 1;
    }

    /**
     * Clear the cache, calling {@link #entryRemoved} on each removed entry.
     * 清空内存缓存
     */
    public final void evictAll() {
        trimToSize(-1); // -1 will evict 0-sized elements
    }

    /**
     * For caches that do not override {@link #sizeOf}, this returns the number
     * of entries in the cache. For all other caches, this returns the sum of
     * the sizes of the entries in this cache.
     */
    public synchronized final int size() {
        return size;
    }

    /**
     * For caches that do not override {@link #sizeOf}, this returns the maximum
     * number of entries in the cache. For all other caches, this returns the
     * maximum sum of the sizes of the entries in this cache.
     */
    public synchronized final int maxSize() {
        return maxSize;
    }

    /**
     * Returns the number of times {@link #get} returned a value.
     */
    public synchronized final int hitCount() {
        return hitCount;
    }

    /**
     * Returns the number of times {@link #get} returned null or required a new
     * value to be created.
     */
    public synchronized final int missCount() {
        return missCount;
    }

    /**
     * Returns the number of times {@link #create(Object)} returned a value.
     */
    public synchronized final int createCount() {
        return createCount;
    }

    /**
     * Returns the number of times {@link #put} was called.
     */
    public synchronized final int putCount() {
        return putCount;
    }

    /**
     * Returns the number of values that have been evicted.
     */
    public synchronized final int evictionCount() {
        return evictionCount;
    }

    /**
     * Returns a copy of the current contents of the cache, ordered from least
     * recently accessed to most recently accessed.
     */
    public synchronized final Map<K, V> snapshot() {
        return new LinkedHashMap<K, V>(map);
    }

    @Override public synchronized final String toString() {
        int accesses = hitCount + missCount;
        int hitPercent = accesses != 0 ? (100 * hitCount / accesses) : 0;
        return String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]",
                maxSize, hitCount, missCount, hitPercent);
    }
}
```









# 中高级

#####  [Hashmap](C:\Users\czdxn\Desktop\md\think\think\md\HashMap.md)

HashMap其实就是ArrayList和LinkedList的数据结构加上hashCode和equals方法的思想设计出来的。

> `HashMap`是基于`Map`接口实现的一种键-值对``的存储结构，允许`null`值，同时非有序，非同步(即线程不安全)。`HashMap`的底层实现是数组 + 链表 + 红黑树（JDK1.8增加了红黑树部分）。它存储和查找数据时，是根据键`key`的`hashCode`的值计算出具体的存储位置。`HashMap`最多只允许一条记录的键`key`为`null`，`HashMap`增删改查等常规操作都有不错的执行效率，是`ArrayList`和`LinkedList`等数据结构的一种折中实现。

负载因子 默认0.75

threshold 下次扩容阈值 ，达到阈值会resize

Node<K,V>[] table  底层数组，充当哈希表的作用，用于存储对应的hash未知元素Node<K,V>

此数组的长度总是2的N次幂

<img src="C:\Users\czdxn\Desktop\md\think\think\md\pic\hashmap.webp" style="zoom:50%;" />





HashCrash



`hash`冲突： 当我们调用`put(K key, V value)`操作添加`key-value`键值对，这个`key-value`键值对存放在的位置是通过扰动函数`(key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16)`计算键`key`的`hash`值。随后将 这个`hash`值 % 模上 哈希表`Node[] table`的长度 得到具体的存放位置。所以`put(K key, V value)`多个元素，是有可能计算出相同的存放位置。此现象就是`hash`冲突或者叫`hash`碰撞。

例子如下：
 元素 A 的`hash`值 为 9，元素 B 的`hash`值 为 17。哈希表`Node[] table`的长度为8。则元素 A 的存放位置为`9 % 8 = 1`，元素 B 的存放位置为`17 % 8 = 1`。两个元素的存放位置均为`table[1]`，发生了`hash`冲突。

`hash`冲突的避免：既然会发生`hash`冲突，我们就应该想办法避免此现象的发生，解决这个问题最关键就是如果生成元素的`hash`值。Java是使用“扰动函数”生成元素的`hash`值。



```dart
  /**
    * JDK 7 的 hash方法
    */
    final int hash(int h) {

        h ^= k.hashCode();

        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

   /**
    * JDK 8 的 hash方法
    */
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
```



右位移16位，正好是32bit的一半，自己的高半区和低半区做异或，就是为了混合原始哈希码的高位和低位，以此来加大低位的随机性。而且混合后的低位掺杂了高位的部分特征，这样高位的信息也被变相保留下来。



`hash`冲突解决：解决`hash`冲突的方法有很多，常见的有：开发定址法，
 再散列法，链地址法，公共溢出区法（详细说明请查看我的文章[JAVA基础-自问自答学hashCode和equals](https://link.jianshu.com?t=https://juejin.im/post/59b25f825188257e7e11500c)）。`HashMap`是使用链地址法解决`hash`冲突的，当有冲突元素放进来时，会将此元素插入至此位置链表的最后一位，形成单链表。但是由于是单链表的缘故，每当通过`hash % length`找到该位置的元素时，均需要从头遍历链表，通过逐一比较`hash`值，找到对应元素。如果此位置元素过多，造成链表过长，遍历时间会大大增加，最坏情况下的时间复杂度为`O(N)`，造成查找效率过低。所以当存在位置的链表长度 大于等于 8 时，`HashMap`会将链表 转变为 红黑树，红黑树最坏情况下的时间复杂度为`O(logn)`。以此提高查找效率。



容量 == 2^N ?



- 因为调用`put(K key, V value)`操作添加`key-value`键值对时，具体确定此元素的位置是通过  `hash`值 % 模上 哈希表`Node[] table`的长度 `hash % length` 计算的。但是"模"运算的消耗相对较大，通过位运算`h & (length-1)`也可以得到取模后的存放位置，而位运算的运行效率高，但只有`length`的长度是2的n次方时，`h & (length-1)` 才等价于 `h % length`。
- 而且当数组长度为2的n次幂的时候，不同的key算出的index相同的几率较小，那么数据在数组上分布就比较均匀，也就是说碰撞的几率小，相对的，查询的时候就不用遍历某个位置上的链表，这样查询效率也就较高了。

例子：

![img](https:////upload-images.jianshu.io/upload_images/4752096-e4ea0052e83dc3e2.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/877/format/webp)

hash &  (length-1)运算过程.jpg

- 上图中，左边两组的数组长度是16（2的4次方），右边两组的数组长度是15。两组的`hash`值均为8和9。
- 当数组长度是15时，当它们和`1110`进行`&`与运算（相同为1，不同为0）时，计算的结果都是`1000`，所以他们都会存放在相同的位置`table[8]`中，这样就发生了`hash`冲突，那么查询时就要遍历链表，逐一比较`hash`值，降低了查询的效率。
- 同时，我们可以发现，当数组长度为15的时候，`hash`值均会与`14（1110）`进行`&`与运算，那么最后一位永远是0，而`0001`，`0011`，`0101`，`1001`，`1011`，`0111`，`1101`这几个位置永远都不能存放元素了，空间浪费相当大，更糟的是这种情况中，数组可以使用的位置比数组长度小了很多，这意味着进一步增加了碰撞的几率，减慢了查询的效率。

- 所以，`HashMap`的容量是2的n次方，有利于提高计算元素存放位置时的效率，也降低了`hash`冲突的几率。因此，我们使用`HashMap`存储大量数据的时候，最好先预先指定容器的大小为2的n次方，即使我们不指定为2的n次方，`HashMap`也会把容器的大小设置成最接近设置数的2的n次方，如，设置`HashMap`的大小为 7 ，则`HashMap`会将容器大小设置成最接近7的一个2的n次方数，此值为 8 。







##### JVM

<img src="C:\Users\czdxn\Desktop\md\think\think\md\pic\jvm.png" style="zoom:75%;" />

方法区 ：存储已被虚拟机加载的类信息、常量、静态变量、即时编译后的代码等数据

运行常量池：是方法区的一部分 。jdk8+ 方法区被元数据区替代

堆 ：jvm中最大的一块内存区域，该区域只是用于存储对象实例和数组，Gc的主要区域，分配内存会报oom

虚拟机栈：每个方法都会在执行时创建一个栈桢，包含局部变量表，返回地址，操作数栈等信息。每个方法之心与完成就是对应栈桢的入栈和出栈过程。局部变量表占用空间的大小在编译器就确定了。如果线程请求的栈深度大于虚拟机所允许的深度，会抛出StackOverFlowError;如果虚拟机栈可以动态扩展，当无法申请到足够内存时，OOM

程序计数器：记录正在执行虚拟机字节码指令地址。native方法，值为空，没有oom

> 而对于 Java 虚拟机栈和本地方法栈，这里要稍微复杂一点。如果我们写一段程序不断的进行递归调用，而且没有退出条件，就会导致不断地进行压栈。类似这种情况，JVM 实际会抛出 StackOverFlowError；当然，如果 JVM 试图去扩展栈空间的的时候失败，则会抛出 OutOfMemoryError。 



##### JVM内存区域，开线程影响哪块内存

概括地说 ，jvm初始运行的时候都会分配好方法区 ，堆 。

**而JVM每遇到一个线程，就为其分配一个程序计数器，虚拟机栈 和本地方法栈，当线程终止时，虚拟机栈 本地方法栈 程序计数器 所占内存空间会被释放掉**

使用Executor架构是十分必要的

jvm并发时通过切换线程并分配时间片来执行的。





##### dalvik  art



在Dalvik下，应用每次运行的时候，字节码都需要通过即时编译器（just in time ，JIT）转换为机器码，这会拖慢应用的运行效率。



而在ART 环境中，应用在第一次安装的时候，字节码就会预先编译成机器码，极大的提高了程序的运行效率，同时减少了手机的耗电量，使其成为真正的本地应用。这个过程叫做预编译（AOT,Ahead-Of-Time）。这样的话，应用的启动(首次)和执行都会变得更加快速。

优点：



- 系统性能的显著提升。
- 应用启动更快、运行更快、体验更流畅、触感反馈更及时。
- 更长的电池续航能力。
- 支持更低的硬件。



缺点：



- 机器码占用的存储空间更大，字节码变为机器码之后，可能会增加10%-20%（不过在应用包中，可执行的代码常常只是一部分。比如最新的 Google+ APK 是 28.3 MB，但是代码只有 6.9 MB。）
- 应用的安装时间会变长。



##### gc









##### 类的加载过程



###### 加载

“加载”是“类加载”（Class Loading）过程的一个阶段，希望读者没有混淆这两个看起来很相似的名词。在加载阶段，虚拟机需要完成以下3件事情：

1）通过一个类的全限定名来获取定义此类的二进制字节流。

2）将这个字节流所代表的静态存储结构转化为方法区的运行时数据结构。

3）在内存中生成一个代表这个类的java.lang.Class对象，作为方法区这个类的各种数据的访问入口。



###### 验证

这一阶段的目的是为了确保Class文件的字节流中包含的信息符合当前虚拟机的要求，并且不会危害虚拟机自身的安全。

- **文件格式验证** ：该验证阶段的主要目的是保证输入的字节流能正确地解析并存储于方法区之内
  - 魔数 0xCAFEBABE
  - 主次版本号是不是在当前虚拟机处理范围内
  - 常量池是否又不支持的常量类型（tag标志）
  - 常量池的常量中是否有不被支持的常量类型或者不符合类型的常量
  - CONSTANT_Utf8_info型的查昂立是否有不符合utf8编码数据
  - Class文件中各个部分及文件本身是否有被删除的或附加的信息
- **元数据验证** ： 是对类的元数据信息进行语义校验，保证不存在不符合Java语言规范的元数据信息。
- **字节码验证**  ：  目的是通过数据流和控制流分析，确定程序语义是合法的、符合逻辑的。
- **符号引用验证** ： 符号引用验证可以看做是对类自身以外（常量池中的各种符号引用）的信息进行匹配性校验



###### 准备

准备阶段是正式为类变量分配内存并设置类变量初始值的阶段，



###### 解析

解析阶段是虚拟机将常量池内的符号引用替换为直接引用的过程

符号引用：符号引用以一组符号来描述引用的目标

直接引用：直接引用可以是直接指向目标的指针，相对偏移量或是一个

能间接定位到目标的句柄

解析动作主要针对类或接口、字段、类方法、接口方法、方法类型、方法句柄和调用点限定符7类符号引用进行，分别对应于常量池的CONSTANT_Class_info、CONSTANT_Fieldref_info、CONSTANT_Methodref_info、CONSTANT_InterfaceMethodref_info、CONSTANT_MethodType_info、CONSTANT_MethodHandle_info和CONSTANT_InvokeDynamic_info 7种常量类型



###### 初始化

在准备阶段，变量已经赋过一次系统要求的初始值，而在初始化阶段，则根据程序员通过程序制定的主观计划去初始化类变量和其他资源



##### 类加载器

比较两个类是否“相等”，只有在这两个类是由同一个类加载器加载的前提下才有意义，否则，即使这两个类来源于同一个Class文件，被同一个虚拟机加载，只要加载它们的类加载器不同，那这两个类就必定不相等。



###### 双亲委派模型



启动类加载器（Bootstrap ClassLoader）：前面已经介绍过，这个类将器负责将存放在＜JAVA_HOME＞\lib目录中的，或者被-Xbootclasspath参数所指定的路径中的，并且是虚拟机识别的（仅按照文件名识别，如rt.jar，名字不符合的类库即使放在lib目录中也不会被加载）类库加载到虚拟机内存中。启动类加载器无法被Java程序直接引用，用户在编写自定义类加载器时，如果需要把加载请求委派给引导类加载器，那直接使用null代替即可，



扩展类加载器（Extension ClassLoader）：这个加载器由sun.misc.Launcher $ExtClassLoader实现，它负责加载＜JAVA_HOME＞\lib\ext目录中的，或者被java.ext.dirs系统变量所指定的路径中的所有类库，开发者可以直接使用扩展类加载器。



应用程序类加载器（Application ClassLoader）：这个类加载器由sun.misc.Launcher $App-ClassLoader实现。由于这个类加载器是ClassLoader中的getSystemClassLoader（）方法的返回值，所以一般也称它为系统类加载器。它负责加载用户类路径（ClassPath）上所指定的类库，开发者可以直接使用这个类加载器，如果应用程序中没有自定义过自己的类加载器，一般情况下这个就是程序中默认的类加载器。



**双亲委派模型的工作过程是：如果一个类加载器收到了类加载的请求，它首先不会自己去尝试加载这个类，而是把这个请求委派给父类加载器去完成，每一个层次的类加载器都是如此，因此所有的加载请求最终都应该传送到顶层的启动类加载器中，只有当父加载器反馈自己无法完成这个加载请求（它的搜索范围中没有找到所需的类）时，子加载器才会尝试自己去加载。**







##### Thread

有5种状态

新建   运行   等待 （Waiting   /  timed Waiting）    阻塞  结束



无限期等待（Waiting）：处于这种状态的线程不会被分配CPU执行时间，它们要等待被其他线程显式地唤醒。以下方法会让线程陷入无限期的等待状态：

●没有设置Timeout参数的Object.wait（）方法。

●没有设置Timeout参数的Thread.join（）方法。

●LockSupport.park（）方法。

限期等待（Timed Waiting）：处于这种状态的线程也不会被分配CPU执行时间，不过无须等待被其他线程显式地唤醒，在一定时间之后它们会由系统自动唤醒。以下方法会让线程进入限期等待状态：

●Thread.sleep（）方法。

●设置了Timeout参数的Object.wait（）方法。

●设置了Timeout参数的Thread.join（）方法。

●LockSupport.parkNanos（）方法。

●LockSupport.parkUntil（）方法。





#####  锁

在读很多并发文章中，会提及各种各样锁如公平锁，乐观锁等等，这篇文章介绍各种锁的分类。介绍的内容如下：

- 公平锁/非公平锁
- 可重入锁
- 独享锁/共享锁
- 互斥锁/读写锁
- 乐观锁/悲观锁
- 分段锁
- 偏向锁/轻量级锁/重量级锁
- 自旋锁

上面是很多锁的名词，这些分类并不是全是指锁的状态，有的指锁的特性，有的指锁的设计，下面总结的内容是对每个锁的名词进行一定的解释。

###### 公平锁/非公平锁

公平锁是指多个线程按照申请锁的顺序来获取锁。
非公平锁是指多个线程获取锁的顺序并不是按照申请锁的顺序，有可能后申请的线程比先申请的线程优先获取锁。有可能，会造成优先级反转或者饥饿现象。
对于Java `ReentrantLock`而言，通过构造函数指定该锁是否是公平锁，默认是非公平锁。非公平锁的优点在于吞吐量比公平锁大。
对于`Synchronized`而言，也是一种非公平锁。由于其并不像`ReentrantLock`是通过AQS的来实现线程调度，所以并没有任何办法使其变成公平锁。

###### 可重入锁

可重入锁又名递归锁，是指在同一个线程在外层方法获取锁的时候，在进入内层方法会自动获取锁。说的有点抽象，下面会有一个代码的示例。
对于Java `ReentrantLock`而言, 他的名字就可以看出是一个可重入锁，其名字是`Re entrant Lock`重新进入锁。
对于`Synchronized`而言,也是一个可重入锁。可重入锁的一个好处是可一定程度避免死锁。

```
synchronized void setA() throws Exception{
    Thread.sleep(1000);
    setB();
}

synchronized void setB() throws Exception{
    Thread.sleep(1000);
}
```

上面的代码就是一个可重入锁的一个特点，如果不是可重入锁的话，setB可能不会被当前线程执行，可能造成死锁。

###### 独享锁/共享锁

独享锁是指该锁一次只能被一个线程所持有。
共享锁是指该锁可被多个线程所持有。

对于Java `ReentrantLock`而言，其是独享锁。但是对于Lock的另一个实现类`ReadWriteLock`，其读锁是共享锁，其写锁是独享锁。
读锁的共享锁可保证并发读是非常高效的，读写，写读 ，写写的过程是互斥的。
独享锁与共享锁也是通过AQS来实现的，通过实现不同的方法，来实现独享或者共享。
对于`Synchronized`而言，当然是独享锁。

###### 互斥锁/读写锁

上面讲的独享锁/共享锁就是一种广义的说法，互斥锁/读写锁就是具体的实现。
互斥锁在Java中的具体实现就是`ReentrantLock`
读写锁在Java中的具体实现就是`ReadWriteLock`

###### 乐观锁/悲观锁

乐观锁与悲观锁不是指具体的什么类型的锁，而是指看待并发同步的角度。
悲观锁认为对于同一个数据的并发操作，一定是会发生修改的，哪怕没有修改，也会认为修改。因此对于同一个数据的并发操作，悲观锁采取加锁的形式。悲观的认为，不加锁的并发操作一定会出问题。
乐观锁则认为对于同一个数据的并发操作，是不会发生修改的。在更新数据的时候，会采用尝试更新，不断重新的方式更新数据。乐观的认为，不加锁的并发操作是没有事情的。

从上面的描述我们可以看出，悲观锁适合写操作非常多的场景，乐观锁适合读操作非常多的场景，不加锁会带来大量的性能提升。
悲观锁在Java中的使用，就是利用各种锁。
乐观锁在Java中的使用，是无锁编程，常常采用的是CAS算法，典型的例子就是原子类，通过CAS自旋实现原子操作的更新。

###### 分段锁

分段锁其实是一种锁的设计，并不是具体的一种锁，对于`ConcurrentHashMap`而言，其并发的实现就是通过分段锁的形式来实现高效的并发操作。
我们以`ConcurrentHashMap`来说一下分段锁的含义以及设计思想，`ConcurrentHashMap`中的分段锁称为Segment，它即类似于HashMap（JDK7与JDK8中HashMap的实现）的结构，即内部拥有一个Entry数组，数组中的每个元素又是一个链表；同时又是一个ReentrantLock（Segment继承了ReentrantLock)。
当需要put元素的时候，并不是对整个hashmap进行加锁，而是先通过hashcode来知道他要放在那一个分段中，然后对这个分段进行加锁，所以当多线程put的时候，只要不是放在一个分段中，就实现了真正的并行的插入。
但是，在统计size的时候，可就是获取hashmap全局信息的时候，就需要获取所有的分段锁才能统计。
分段锁的设计目的是细化锁的粒度，当操作不需要更新整个数组的时候，就仅仅针对数组中的一项进行加锁操作。

###### 偏向锁/轻量级锁/重量级锁

这三种锁是指锁的状态，并且是针对`Synchronized`。在Java 5通过引入锁升级的机制来实现高效`Synchronized`。这三种锁的状态是通过对象监视器在对象头中的字段来表明的。
偏向锁是指一段同步代码一直被一个线程所访问，那么该线程会自动获取锁。降低获取锁的代价。
轻量级锁是指当锁是偏向锁的时候，被另一个线程所访问，偏向锁就会升级为轻量级锁，其他线程会通过自旋的形式尝试获取锁，不会阻塞，提高性能。
重量级锁是指当锁为轻量级锁的时候，另一个线程虽然是自旋，但自旋不会一直持续下去，当自旋一定次数的时候，还没有获取到锁，就会进入阻塞，该锁膨胀为重量级锁。重量级锁会让其他申请的线程进入阻塞，性能降低。

###### 自旋锁

在Java中，自旋锁是指尝试获取锁的线程不会立即阻塞，而是采用循环的方式去尝试获取锁，这样的好处是减少线程上下文切换的消耗，缺点是循环会消耗CPU。
典型的自旋锁实现的例子，可以参考[自旋锁的实现](http://ifeve.com/java_lock_see1/)





###### synchronized 和 volatile 、ReentrantLock 、CAS 的区别

synchronized 

	- 内存可见性
	- 操作原子性
	- 有序性

volatile

- volatile保证内存可见性

- volatile的特殊规则保证了新值能**立即同步到主内存**，以及每次**使用前立即从主内存刷新**。

- volatile禁止指令重排

- volatile不能保证原子性



> 　　一个线程执行临界区代码过程如下： 
> 　　　　1 获得同步锁 
> 　　　　2 清空工作内存 
> 　　　　3 从主存拷贝变量副本到工作内存 
> 　　　　4 对这些变量计算 
> 　　　　5 将变量从工作内存写回到主存 
> 　　　　6 释放锁



```
1）Synchronized保证内存可见性和操作的原子性，Volatile只能保证内存可见性。

2）volatile不需要加锁，比Synchronized更轻量级，并不会阻塞线程（volatile不会造成线程的阻塞；synchronized可能会造成线程的阻塞。）

3）volatile标记的变量不会被编译器优化,而synchronized标记的变量可以被编译器优化（如编译器重排序的优化）

4）volatile是变量修饰符，仅能用于变量，而synchronized是一个方法或块的修饰符
```



ReentrantLock



非公平锁和公平锁的两处不同：
非公平锁在调用 lock 后，首先就会调用 CAS 进行一次抢锁，如果这个时候恰巧锁没有被占用，那么直接就获取到锁返回了。

非公平锁在 CAS 失败后，和公平锁一样都会进入到 tryAcquire 方法，在 tryAcquire 方法中，如果发现锁这个时候被释放了（state == 0），非公平锁会直接 CAS 抢锁，但是公平锁会判断等待队列是否有线程处于等待状态，如果有则不去抢锁，乖乖排到后面。




1、CAS指令
CAS指令需要有3个操作数，分别是内存位置（在Java中可以简单理解为变量的内存地址，用V表示）、旧的预期值（用A表示）和新值（用B表示）。

CAS指令执行时，当且仅当V符合旧预期值A时，处理器用新值B更新V的值，否则它就不执行更新，但是无论是否更新了V的值，都会返回V的旧值，上述的处理过程是一个原子操作。
————————————————
版权声明：本文为CSDN博主「amunamuna」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/amunamuna/article/details/92796982



在Java中，synchronized是用来表示同步的，我们可以synchronized来修饰一个方法。也可以synchronized来修饰方法里面的一个语句块。
在static方法前加synchronizedstatic：静态方法属于类方法，它属于这个类，获取到的锁，是属于类的锁。
在普通方法前加synchronizedstatic：非static方法获取到的锁，是属于当前对象的锁。
结论：类锁和对象锁不同，他们之间不会产生互斥。



sleep()
　　sleep()方法需要指定等待的时间，它可以让当前正在执行的线程在指定的时间内暂停执行，进入阻塞状态，该方法既可以让其他同优先级或者高优先级的线程得到执行的机会，也可以让低优先级的线程得到执行机会。但是sleep()方法不会释放“锁标志”，也就是说如果有synchronized同步块，其他线程仍然不能访问共享数据。
　　
wait()
　　wait()方法需要和notify()及notifyAll()两个方法一起介绍，这三个方法用于协调多个线程对共享数据的存取，所以必须在synchronized语句块内使用，也就是说，调用wait()，notify()和notifyAll()的任务在调用这些方法前必须拥有对象的锁。注意，它们都是Object类的方法，而不是Thread类的方法。
　　wait()方法与sleep()方法的不同之处在于，wait()方法会释放对象的“锁标志”。当调用某一对象的wait()方法后，会使当前线程暂停执行，并将当前线程放入对象等待池中，直到调用了notify()方法后，将从对象等待池中移出任意一个线程并放入锁标志等待池中，只有锁标志等待池中的线程可以获取锁标志，它们随时准备争夺锁的拥有权。当调用了某个对象的notifyAll()方法，会将对象等待池中的所有线程都移动到该对象的锁标志等待池。
　　除了使用notify()和notifyAll()方法，还可以使用带毫秒参数的wait(long timeout)方法，效果是在延迟timeout毫秒后，被暂停的线程将被恢复到锁标志等待池。
　　此外，wait()，notify()及notifyAll()只能在synchronized语句中使用，但是如果使用的是ReenTrantLock实现同步，该如何达到这三个方法的效果呢？解决方法是使用ReenTrantLock.newCondition()获取一个Condition类对象，然后Condition的await()，signal()以及signalAll()分别对应上面的三个方法。

yield()
　　yield()方法和sleep()方法类似，也不会释放“锁标志”，区别在于，它没有参数，即yield()方法只是使当前线程重新回到可执行状态，所以执行yield()的线程有可能在进入到可执行状态后马上又被执行，另外yield()方法只能使同优先级或者高优先级的线程得到执行机会，这也和sleep()方法不同。

join()
　　join()方法会使当前线程等待调用join()方法的线程结束后才能继续执行，





##### Http



**1.物理层**

该层负责比特流在节点间的传输，即负责物理传输。该层的协议既与链路有关，也与传输介质有关。其通俗来讲就是把计算机连接起来的物理手段。

**2.数据链路层**

该层控制网络层与物理层之间的通信，其主要功能是如何在不可靠的物理线路上进行数据的可靠传递。为了保证传输，从网络层接收到的数据被分割成特定的可被物理层传输的帧。帧是用来移动数据的结构包，它不仅包括原始数据，还包括发送方和接收方的物理地址以及纠错和控制信息。其中的地址确定了帧将发送到何处，而纠错和控制信息则确保帧无差错到达。如果在传送数据时，接收点检测到所传数据中有差错，就要通知发送方重发这一帧。

**3.网络层**

该层决定如何将数据从发送方路由到接收方。网络层通过综合考虑发送优先权、网络拥塞程度、服务质量以及可选路由的花费来决定从一个网络中的节点 A 到另一个网络中节点 B 的最佳路径。

**4.传输层**

该层为两台主机上的应用程序提供端到端的通信。相比之下，网络层的功能是建立主机到主机的通信。传输层有两个传输协议：TCP（传输控制协议）和UDP（用户数据报协议）。其中，TCP是一个可靠的面向连接的协议，UDP是不可靠的或者说无连接的协议。

**5.应用层**

应用程序收到传输层的数据后，接下来就要进行解读。解读必须事先规定好格式，而应用层就是规定应用程序的数据格式的。它的主要协议有HTTP、FTP、Telnet、SMTP、POP3等。



######  3次握手四次挥手

<img src="C:\Users\czdxn\Desktop\md\think\think\md\pic\TCP.jpg" style="zoom:50%;" />



*Syn*chronize Sequence Numbers   



TCP三次握手的过程如下。

• 第一次握手：建立连接。客户端发送连接请求报文段，将 SYN 设置为 1、Sequence Number（seq）为x；接下来客户端进入SYN_SENT状态，等待服务端的确认。

• 第二次握手：服务器收到客户端的 SYN 报文段，对 SYN 报文段进行确认，设置Acknowledgment Number（ACK）为 x+1（seq+1）；同时自己还要发送 SYN 请求信息，将SYN设置为1、seq为y。服务端将上述所有信息放到SYN+ACK报文段中，一并发送给客户端，此时服务端进入SYN_RCVD状态。

• 第三次握手：客户端收到服务端的SYN+ACK报文段；然后将ACK设置为y+1，向服务端发送ACK报文段，这个报文段发送完毕后，客户端和服务端都进入ESTABLISHED （TCP连接成功）状态，完成TCP的三次握手。

当客户端和服务端通过三次握手建立了TCP连接以后，当数据传送完毕，断开连接时就需要进行TCP的四次挥手。其四次挥手如下所示。

• 第一次挥手：客户端设置seq和ACK，向服务端发送一个FIN报文段。此时，客户端进入FIN_WAIT_1状态，表示客户端没有数据要发送给服务端了。

• 第二次挥手：服务端收到了客户端发送的FIN报文段，向客户端回了一个ACK报文段。

• 第三次挥手：服务端向客户端发送 FIN 报文段，请求关闭连接，同时服务端进入LAST_ACK状态。

• 第四次挥手：客户端收到服务端发送的FIN报文段，向服务端发送ACK报文段，然后客户端进入TIME_WAIT状态。服务端收到客户端的ACK报文段以后，就关闭连接。此时，客户端等待2MSL（最大报文段生存时间）后依然没有收到回复，则说明服务端已正常关闭，这样客户端也可以关闭连接了





如果有大量的连接，每次在连接、关闭时都要经历三次握手、四次挥手，这很显然会造成性能低下。因此，HTTP有一种叫作keepalive connections的机制，它可以在传输数据后仍然保持连接，当客户端需要再次获取数据时，直接使用刚刚空闲下来的连接而无须再次握手

##### 反射



















