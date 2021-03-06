package me.limeice.common.base.rx;


import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava线程调度
 * Created by LimeV on 2018/2/28.
 * 实例：<code>
 * <p> Observable.just(1).compose(RxSchedulers.io_main()).subscribe();
 * </code>
 */
public class RxSchedulers {

    /**
     * Io线程切换到主线程
     *
     * @param <T> 泛型约束
     * @return RxJava流
     */
    public static <T> ObservableTransformer<T, T> io_main() {
        return in -> in.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 计算线程切换到主线程
     *
     * @param <T> 泛型约束
     * @return RxJava流
     */
    public static <T> ObservableTransformer<T, T> computation_main() {
        return in -> in.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 主线程切换到IO线程
     *
     * @param <T> 泛型约束
     * @return RxJava流
     */
    public static <T> ObservableTransformer<T, T> main_io() {
        return in -> in.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io());
    }

    /**
     * 主线程切换到计算线程
     *
     * @param <T> 泛型约束
     * @return RxJava流
     */
    public static <T> ObservableTransformer<T, T> main_computation() {
        return in -> in.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.computation());
    }

    /**
     * Io线程切换到主线程,延迟错误相应
     *
     * @param <T> 泛型约束
     * @return RxJava流
     */
    public static <T> ObservableTransformer<T,T> io_mainDelayError(){
        return in -> in.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(),true);
    }
}
