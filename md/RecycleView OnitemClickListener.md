# RecycleView OnitemClickListener

- 首先在`adapter`中新建一个`回调接口`，然后添加一个`私有属性`并设置上`setter方法`：

```java
public class MytAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

  ...
  //私有属性
  private OnItemClickListener onItemClickListener = null;

  //setter方法
  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
      this.onItemClickListener = onItemClickListener;
  }

  //回调接口
  public interface OnItemClickListener {
      void onItemClick(View v, ViewHolder holedr, int position);
  }

  ...

}
```

- 然后在重写的`onBindViewHolder`方法中添加上点击事件即可：



```java
@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        
    //实现点击效果
    holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, holedr, position);
              }
          }
    });
}
```

- 使用方法：`adapter.setOnItemClickListener`即可添加点击事件。



```java
MyAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(View v, ViewHolder holedr, int position) {
                
      }
});
```