Volley
======
Volley框架代码的学习，相关代码添加了中文注释，有利于代码阅读

官方git地址：https://android.googlesource.com/platform/frameworks/volley 

Volley提供的功能
简单来说，它提供了如下的便利功能：
JSON，图像等的异步下载；
网络请求的排序（scheduling）
网络请求的优先级处理
缓存
多级别取消请求
和Activity和生命周期的联动（Activity结束时同时取消所有网络请求）

json请求：
mQueue = Volley.newRequestQueue(getApplicationContext());
mQueue.add(new JsonObjectRequest(Method.GET, url, null,
            new Listener() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "response : " + response.toString());
                }
            }, null));
mQueue.start();

获取网络图片：
// imageView是一个ImageView实例
// ImageLoader.getImageListener的第二个参数是默认的图片resource id
// 第三个参数是请求失败时候的资源id，可以指定为0
ImageListener listener = ImageLoader.getImageListener(imageView, android.R.drawable.ic_menu_rotate, android.R.drawable.ic_delete);
mImageLoader.get(url, listener);
或者：
mImageView.setImageUrl(url, imageLoader)


