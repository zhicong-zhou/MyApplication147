package com.jzkl.util.newinfo;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * Created by WangJinRui on 2018/3/15.
 * 事件总线封装，背压版：参考http://blog.csdn.net/u011271348/article/details/69946650
 */

public class RxBus {
    private final FlowableProcessor<Object> mBus;

    private RxBus() {
        // PublishSubject只会把在订阅发生的时间点之后来自原始Flowable的数据发射给观察者
        mBus = PublishProcessor.create().toSerialized();
    }

    public static RxBus get() {
        return Holder.SBUS;
    }

    private static class Holder {
        private static final RxBus SBUS = new RxBus();
    }

    // 发送一个事件
    public void post(Object obj) {
        mBus.onNext(obj);
    }

    // 根据传递的 class 类型返回特定类型(class)的 被观察者
    public <T> Flowable<T> toFlowable(Class<T> tClass) {
        return mBus.ofType(tClass);
    }

}
