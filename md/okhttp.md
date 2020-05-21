# okhttp

```
private void getNodeList() {
    //1.创建OkHttpClient对象
    OkHttpClient okHttpClient = new OkHttpClient();
    //2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
    Request request = new Request.Builder().url(Api.pingUrl).method("GET", null).build();
    //3.创建一个call对象,参数就是Request请求对象
    Call call = okHttpClient.newCall(request);
    //4.请求加入调度，重写回调方法
    call.enqueue(new Callback() {
        //请求失败执行的方法
        @Override
        public void onFailure(Call call, IOException e) {

        }

        //请求成功执行的方法
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String data = response.body().string();
            Gson gson = new Gson();

            List<NodeBean> NodeBeans = gson.fromJson(data, new TypeToken<List<NodeBean>>() {
            }.getType());

            Log.d("response", NodeBeans.size() + "");
        }
    });
}

public void HttpPost() {
    OkHttpClient okHttpClient = new OkHttpClient();
    //Form表单格式的参数传递
    FormBody formBody = new FormBody
            .Builder()
            //设置参数名称和参数值
            .add("username", "23")
            .add("password", "123")
            .build();
    Request request = new Request
            .Builder()
            //Post请求的参数传递
            .post(formBody)
            .url("http://www.baidu.com")
            .build();
    okHttpClient.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(okhttp3.Call call, IOException e) {
            Log.d("my_Test", e.getMessage());
        }

        @Override
        public void onResponse(okhttp3.Call call, Response response) throws IOException {
            String data = response.body().string();
            Log.d("my_Test", data);
            response.body().close();
        }
    });
}
```