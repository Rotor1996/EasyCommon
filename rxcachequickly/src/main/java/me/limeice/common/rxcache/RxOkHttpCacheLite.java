package me.limeice.common.rxcache;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import me.limeice.common.base.rx.cache.RxCache;
import me.limeice.common.function.CloseUtils;
import me.limeice.common.function.Objects;
import me.limeice.common.function.cache.MemCache;
import me.limeice.common.function.cache.StorageCache;
import me.limeice.common.function.helper.ReaderSource;
import me.limeice.common.function.helper.StorageReaderHelper;
import me.limeice.common.function.helper.WriterSource;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RxOkHttpCacheLite<V> extends RxCache<V, String> {

    private OkHttpClient mOkHttpClient;

    public static class Builder<V> {

        private Context context;
        private RxOkHttpCacheLite<V> rxCache;
        private String cachePath;
        private MemCache<V> memCache;

        public Builder(@NonNull final Context context,
                       @NonNull final StorageReaderHelper<V, String> rxCacheHelper) {
            this.context = context.getApplicationContext();
            rxCache = new RxOkHttpCacheLite<>();
            rxCache.setRxHelper(new RxCacheHelper<V, String>() {
                @Override
                public void download(@NonNull String key, @Nullable String url, @NonNull WriterSource writer) throws IOException {
                    Objects.requireNonNull(url, "Url is null.");
                    Request request = new Request.Builder().url(url).build();
                    Response response = (rxCache).mOkHttpClient.newCall(request).execute();
                    InputStream in = null;
                    try {
                        ResponseBody body = response.body();
                        Objects.requireNonNull(body);
                        in = body.byteStream();
                        OutputStream out = writer.getOutStream();
                        Utils.InputSteamToOutputStream(in, out);
                    } finally {
                        CloseUtils.closeIOQuietly(response, in);
                    }
                }

                @Nullable
                @Override
                public V read(@NonNull String key, @Nullable String url, @NonNull ReaderSource reader) throws IOException {
                    return rxCacheHelper.read(key, url, reader);
                }
            });
        }

        /**
         * 这是过期期限，最大生命周期
         *
         * @param duration 生命周期，秒
         * @return 链式编程
         */
        public Builder<V> setDuration(int duration) {
            rxCache.duration = duration;
            return this;
        }

        /**
         * 自定义缓存路径
         *
         * @param path 文件夹路径
         * @return 链式编程
         */
        public Builder<V> setStorageCachePath(@NonNull String path) {
            this.cachePath = Objects.requireNonNull(path);
            return this;
        }

        /**
         * 创建磁盘缓存
         *
         * @return 链式编程
         */
        public RxOkHttpCacheLite<V> create() {
            if (cachePath == null)
                rxCache.initCache(new File(context.getCacheDir(), StorageCache.CACHE_DIR), memCache);
            else {
                rxCache.initCache(new File(cachePath), memCache);
            }
            rxCache.getCache().setDuration(rxCache.duration);
            if (rxCache.mOkHttpClient == null)
                useCustomOkHttp(new OkHttpClient());
            return rxCache;
        }

        /**
         * 使用内存缓存
         *
         * @param memCache 内存缓存
         * @return 链式编程
         */
        public Builder<V> useMemCache(@NonNull MemCache<V> memCache) {
            this.memCache = Objects.requireNonNull(memCache);
            return this;
        }

        /**
         * 自定义OkHttp
         *
         * @param client OkHttp3
         * @return 链式编程
         */
        public Builder<V> useCustomOkHttp(@NonNull OkHttpClient client) {
            rxCache.mOkHttpClient = Objects.requireNonNull(client);
            return this;
        }
    }
}
