package com.luo.project.rx;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.luo.project.R;
import com.luo.project.entity.Girls;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxJavaActivity extends AppCompatActivity {
    private String tag = "RxJavaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);


        init();
    }

    private void init() {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onNext(String s) {
                Log.d(tag, "Item: " + s);
            }

            @Override
            public void onCompleted() {
                Log.d(tag, "Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(tag, "Error!");
            }
        };

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                Log.d(tag, "Item: " + s);
            }

            @Override
            public void onCompleted() {
                Log.d(tag, "Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(tag, "Error!");
            }
        };

        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();
            }
        });

        observable.subscribe(subscriber);

        mLog("-----------observable1-------------");

        Observable observable1 = Observable.just("Hello1", "Hi1", "Aloha1");

        String[] words = {"Hello", "Hi", "Aloha"};
        Observable observable2 = Observable.from(words);

        observable1.subscribe(subscriber);

        mLog("-----------Action-------------");

        Action1<String> onNextAction = new Action1<String>() {
            // onNext()
            @Override
            public void call(String s) {
                Log.d(tag, "onNextAction" + s);
            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            // onError()
            @Override
            public void call(Throwable throwable) {
                // Error handling
            }
        };
        Action0 onCompletedAction = new Action0() {
            // onCompleted()
            @Override
            public void call() {
                Log.d(tag, "onNextAction completed");
            }
        };

        // 自动创建 Subscriber ，并使用 onNextAction 来定义 onNext()
        observable.subscribe(onNextAction);

        // 自动创建 Subscriber ，并使用 onNextAction 和 onErrorAction 来定义 onNext() 和 onError()
        observable.subscribe(onNextAction, onErrorAction);

        // 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);


        mLog("******************************");

        String[] names = new String[]{"李白", "杜甫", "陶渊明", "王维"};
        Observable.from(names)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String name) {
                        Log.d(tag, name);
                    }
                });


        mLog("^^^^^^^^^^^ImageView^^^^^^^^^^^^");

        final int drawableRes = R.mipmap.bg_one;
        final ImageView imageView = (ImageView) findViewById(R.id.iv);

        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getResources().getDrawable(drawableRes);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onNext(Drawable drawable) {
                        mLog("onNext");
                        imageView.setImageDrawable(drawable);
                    }

                    @Override
                    public void onCompleted() {
                        mLog("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLog("Throwable");
                        Toast.makeText(RxJavaActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        mLog("^^^^^^^^^^^Schedulers^^^^^^^^^^^^");

        Observable.just(1, 2, 3, 4).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer number) {
                        Log.d(tag, "number:" + number);
                    }
                });

        mLog("++++++++++++++++ map ++++++++++++++++");

        Girls girls = new Girls();
        girls.setCode(200);
        girls.setMsg("succeed");
        List<Girls.NewslistBean> list = new ArrayList<>();
        list.add(new Girls.NewslistBean("2017-6-1", "the grid", "description", "www.baodu.com", "www.baodu.com"));
        girls.setNewslist(list);

        Girls[] girlses = new Girls[]{girls, girls, girls, girls};

        Subscriber<String> subscriber3 = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String name) {
                Log.e(tag, "map " + name);
            }

        };
        Observable.from(girlses).map(new Func1<Girls, String>() {
            @Override
            public String call(Girls girls1) {
                return girls1.getMsg();
            }
        }).subscribe(subscriber3);

        mLog("~~~~~~~~~~~~~~~~~~~~~~ Test ~~~~~~~~~~~~~~~~~~~~~");

        Observable<String> observable5 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(" subscriber.onNext");
                subscriber.onCompleted();
            }
        });

        Observable.just("http://blog.csdn.net/lzyzsd/article/details/41833541/")
                .observeOn(Schedulers.newThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        try {
                            URL url = new URL(s);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            InputStream stream = connection.getInputStream();
                            byte[] bytes = new byte[1024];
                            while (stream.read(bytes) > 0) {
                                String s1 = new String(bytes, "utf-8");
                                mLog(s1);
                            }

                            connection.disconnect();
                            stream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

        mLog("~~~~~~~~~~~~~~~~~~~~~~ interval ~~~~~~~~~~~~~~~~~~~~~");

        Observable.interval(5, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).map(new Func1() {
            @Override
            public Object call(Object o) {
                //jump and finish
                return null;
            }

        }).subscribe();

        mLog("~~~~~~~~~~~~~~~~~~~~~~ timer ~~~~~~~~~~~~~~~~~~~~~");

        //每隔两秒产生一个数字
        Observable.timer(2, 2, TimeUnit.SECONDS).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                System.out.println("Sequence complete.");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error:" + e.getMessage());
            }

            @Override
            public void onNext(Long aLong) {
                System.out.println("Next:" + aLong.toString());
            }
        });

        Observable.just("roems1", "roems2", "roems3")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return " --- map -- " + s;
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mLog(s);
                    }
                });

        Observable.timer(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        ObjectAnimator
                                .ofFloat(imageView, "alpha", 0.0F, 1.0F, 0.5F)
                                .setDuration(1500)
                                .start();
                    }
                });
    }


    private void mLog(Object msg) {
        Log.i(tag, "" + msg);
    }
}
