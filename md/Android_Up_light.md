

# Android_Up_light

- 5.0 Lollipop（棒糖）

  - **Material Design**

  - 支持多种设备 

  - 全新的通知中心设计

  - 支持 **64** 位 **ART**虚拟机

    - ###### Dalvik与Art的区别：

      1. Dalvik每次都要编译再运行，Art只会首次启动编译
      2. Art占用空间比Dalvik大（原生代码占用的存储空间更大），就是用“空间换时间”
      3. Art减少编译，减少了CPU使用频率，使用明显改善电池续航
      4. Art应用启动更快、运行更快、体验更流畅、触感反馈更及时

  - **Overview**

    - 卡片

  - 设备识别解锁

  - **Face unlock**面部解锁

  - **RecyclerView**

    - Good: 构提供了一种插拔式的体验，它具有高度的解耦、异常的灵活性和更高的效率，通过设置它提供的不同 LayoutManager、ItemDecoration、ItemAnimator 可实现更加丰富多样的效果。

    - BAD：设置列表的分割线时需要自定义，另外列表的点击事件需要自己去实现

      ```
      public class TestRecycleViewAdapter extends RecyclerView.Adapter<TestRecycleViewAdapter.ViewHolder> {
      
          private Context mContext;
          private List<String> mList;
          private List<Integer> heights;
      
          public TestRecycleViewAdapter(Context context, List<String> list) {
              this.mContext = context;
              this.mList = list;
              randomHeight();
          }
      
          @NonNull
          @Override
          public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
              View view = LayoutInflater.from(parent.getContext())
                      .inflate(R.layout.item_recycleview_test, parent, false);
              ViewHolder holder = new ViewHolder(view);
              return holder;
          }
      
          @Override
          public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      
              // holder 存在复用问题 ，需要防止混乱
      
              ViewGroup.LayoutParams lp = holder.mTextView.getLayoutParams();
      
              lp.height = heights.get(position);
      
              holder.mTextView.setLayoutParams(lp);
      
              holder.mTextView.setText(mList.get(position));
          }
      
      
          @Override
          public int getItemCount() {
              return mList.size();
          }
      
          public class ViewHolder extends RecyclerView.ViewHolder {
      
              TextView mTextView;
      
              public ViewHolder(@NonNull View itemView) {
                  super(itemView);
                  mTextView = (TextView) itemView.findViewById(R.id.textview);
              }
          }
      
          private List randomHeight() {
              heights = new ArrayList<>();
              for (int i = 0; i < mList.size(); i++) {
                  heights.add((int) ((int) 100 + Math.random() * 300));
              }
              return heights;
          }
      
      
      }
      ```

      ```
      forList();
              RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
              LinearLayoutManager layoutManager = new LinearLayoutManager(this);
              recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
              recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
              TestRecycleViewAdapter adapter = new TestRecycleViewAdapter(this, list);
              recyclerView.setAdapter(adapter);
      ```

      

  - **3**种**Notification**

  - **Palette**
  
    - 可以在一张图片里面分析出一些色彩特性：主色调、鲜艳的颜色、柔和颜色等等
    - 设置给actionBar
  
- 6.0 Marshmallow（棉花糖）

  - **应用权限管理**
  - **Android Pay**
  - **指纹支持**
    - 指纹demo
    - 九宫锁demo
  - **Doze电量管理**
  - **App Links**
    - Android 6.0加强了软件间的关联，允许开发者将App和他们的Web域名关联。谷歌大会展示了App Links的应用场景，比如你的手机邮箱里收到一封邮件，内文里有一个Twitter链接，点击该链接可以直接跳转到Twitter应用，而不再是网页。
  - **Now on Tap**

- 7.0 Nougat(牛轧糖)

  - **多窗口模式**
  - **Data Saver**
  - **改进的Java 8语言支持**
  - **自定义壁纸**
  - **快速回复**
  - .Daydream VR支持
  - **快速设置**
  - **Unicode 9支持和全新的emoji表情符号**
  - **Google Assistant**

- [8.0 9.0 10.0 特性](https://www.jianshu.com/p/a6c727cb4af4)



- Material Design
  - **1.底部动作条（Bottom Sheets）**
  - **2.卡片（Cards）**
  - **3.提示框（Dialogs）**
  - **4.菜单（Menus）**
  - **5.选择器**
  - **6.滑块控件（Sliders）**
  - **7.进度和动态**
  - **8.Snackbar与Toast**
  - **Tab**
- 自定义view
- 自定义viewGroup
- 























