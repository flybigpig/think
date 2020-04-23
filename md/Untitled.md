#### 未定义

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









