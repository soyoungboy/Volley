/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.android.volley.toolbox;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.http.AndroidHttpClient;
import android.os.Build;

import com.android.volley.Network;
import com.android.volley.RequestQueue;

import java.io.File;

@TargetApi(Build.VERSION_CODES.DONUT)
public class Volley {

	/** Default on-disk cache directory. */
	private static final String DEFAULT_CACHE_DIR = "volley";

	/**
	 * Creates a default instance of the worker pool and calls {@link RequestQueue#start()} on it.
	 *获取Volley对象
	 * @param context A {@link Context} to use for creating the cache dir.
	 * @param stack An {@link HttpStack} to use for the network, or null for default.
	 * @return A started {@link RequestQueue} instance.
	 */
	@SuppressLint("NewApi")
	public static RequestQueue newRequestQueue(Context context, HttpStack stack) {
		File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);

		String userAgent = "volley/0";
		try {
			String packageName = context.getPackageName();
			PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
			userAgent = packageName + "/" + info.versionCode;
		} catch (NameNotFoundException e) {
		}

		if (stack == null) {
			if (Build.VERSION.SDK_INT >= 9) {
				stack = new HurlStack();
			} else {
				// Prior to Gingerbread, HttpUrlConnection was unreliable.
				// See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
				stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
			}
		}

		Network network = new BasicNetwork(stack);

		RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), network);
		queue.start();

		return queue;
		/*
		 * 实例化一个RequestQueue，其中start()主要完成相关工作线程的开启，
		 * 比如开启缓存线程CacheDispatcher先完成缓存文件的扫描， 还包括开启多个NetworkDispatcher访问网络线程，
		 * 该多个网络线程将从 同一个 网络阻塞队列中读取消息
		 * 
		 * 此处可见，start()已经开启，所有我们不用手动的去调用该方法，在start()方法中如果存在工作线程应该首先终止，并重新实例化工作线程并开启
		 * 在访问网络很频繁，而又重复调用start()，势必会导致性能的消耗；但是如果在访问网络很少时，调用stop()方法，停止多个线程，然后调用start(),反而又可以提高性能，具体可折中选择
		 */
	}

	/**
	 * Creates a default instance of the worker pool and calls {@link RequestQueue#start()} on it.
	 *
	 * @param context A {@link Context} to use for creating the cache dir.
	 * @return A started {@link RequestQueue} instance.
	 */
	public static RequestQueue newRequestQueue(Context context) {
		return newRequestQueue(context, null);
	}
	

}
