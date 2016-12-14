package com.luo.project.thread;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.luo.project.R;

/**
 * @author XiaoMaGuo ^_^
 * @version [version-code, 2013-10-22]
 * @TODO [线程池控制 ]
 * @since [Product/module]
 */
public class ThreadActivity extends AppCompatActivity implements OnClickListener {
    /**
     * 任务执行队列
     */
    private ConcurrentLinkedQueue<MyRunnable> taskQueue = null;
    /**
     * 正在等待执行或已经完成的任务队列
     * <p>
     * 备注：Future类，一个用于存储异步任务执行的结果，比如：判断是否取消、是否可以取消、是否正在执行、是否已经完成等
     */
    private ConcurrentMap<Future, MyRunnable> taskMap = null;
    /**
     * 创建一个不限制大小的线程池 此类主要有以下好处 1，以共享的无界队列方式来运行这些线程. 2，执行效率高。 3，在任意点，在大多数 nThreads 线程会处于处理任务的活动状态
     * 4，如果在关闭前的执行期间由于失败而导致任何线程终止，那么一个新线程将代替它执行后续的任务（如果需要）。
     */
    private ExecutorService mES = null;
    /**
     * 在此类中使用同步锁时使用如下lock对象即可，官方推荐的，不推荐直接使用MyRunnableActivity.this类型的,可以详细读一下/framework/app下面的随便一个项目
     */
    private Object lock = new Object();
    /**
     * 唤醒标志，是否唤醒线程池工作
     */
    private boolean isNotify = true;
    /**
     * 线程池是否处于运行状态(即:是否被释放!)
     */
    private boolean isRuning = true;
    /**
     * 任务进度
     */
    private ProgressBar pb = null;
    /**
     * 用此Handler来更新我们的UI
     */
    private Handler mHandler = null;

    /**
     * Overriding methods
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        init();
    }

    public void init() {
        pb = (ProgressBar) findViewById(R.id.progress);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        taskQueue = new ConcurrentLinkedQueue<>();
        taskMap = new ConcurrentHashMap<>();
        if (mES == null) {
            mES = Executors.newCachedThreadPool();
        }

        // 用于更新ProgressBar进度条
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                pb.setProgress(msg.what);
            }
        };
    }

    /**
     * Overriding methods
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                start();
                break;
            case R.id.button2:
                stop();
                break;
            case R.id.button3:
                reload(new MyRunnable(mHandler));
                break;
            case R.id.button4:
                release();
                break;
            case R.id.button5:
                addTask(new MyRunnable(mHandler));
                break;
            default:
                break;
        }
    }

    /**
     * <Summary Description>
     */
    private void addTask(final MyRunnable mr) {
        mHandler.sendEmptyMessage(0);
        if (mES == null) {
            mES = Executors.newCachedThreadPool();
            notifyWork();
        }
        if (taskQueue == null) {
            taskQueue = new ConcurrentLinkedQueue<MyRunnable>();
        }
        if (taskMap == null) {
            taskMap = new ConcurrentHashMap<Future, MyRunnable>();
        }
        mES.execute(new Runnable() {
            @Override
            public void run() {
                /**
                 * 插入一个Runnable到任务队列中 这个地方解释一下,offer跟add方法,试了下,效果都一样,没区别,官方的解释如下: 1 offer : Inserts the specified
                 * element at the tail of this queue. As the queue is unbounded, this method will never return
                 * {@code false}. 2 add: Inserts the specified element at the tail of this queue. As the queue is
                 * unbounded, this method will never throw {@link IllegalStateException} or return {@code false}.
                 *
                 *
                 * */

                taskQueue.offer(mr);
                // taskQueue.add(mr);
                notifyWork();
            }
        });
        Toast.makeText(ThreadActivity.this, "已添加一个新任务到线程池中 ！", Toast.LENGTH_LONG).show();
    }

    /**
     * <Summary Description>
     */
    private void release() {
        Toast.makeText(ThreadActivity.this, "释放所有占用的资源！", Toast.LENGTH_LONG).show();
        /** 将ProgressBar进度置为0 */
        mHandler.sendEmptyMessage(0);
        isRuning = false;
        Iterator iter = taskMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Future, MyRunnable> entry = (Map.Entry<Future, MyRunnable>) iter.next();
            Future result = entry.getKey();
            if (result == null) {
                continue;
            }
            result.cancel(true);
            taskMap.remove(result);
        }
        if (null != mES) {
            mES.shutdown();
        }
        mES = null;
        taskMap = null;
        taskQueue = null;
    }

    /**
     * <Summary Description>
     */
    private void reload(final MyRunnable mr) {
        mHandler.sendEmptyMessage(0);
        if (mES == null) {
            mES = Executors.newCachedThreadPool();
            notifyWork();
        }
        if (taskQueue == null) {
            taskQueue = new ConcurrentLinkedQueue<>();
        }
        if (taskMap == null) {
            taskMap = new ConcurrentHashMap<>();
        }
        mES.execute(new Runnable() {
            @Override
            public void run() {
                /** 插入一个Runnable到任务队列中 */
                taskQueue.offer(mr);
                // taskQueue.add(mr);
                notifyWork();
            }
        });
        mES.execute(new Runnable() {
            @Override
            public void run() {
                if (isRuning) {
                    MyRunnable myRunnable = null;
                    synchronized (lock) {
                        // 从线程队列中取出一个Runnable对象来执行，如果此队列为空，则调用poll()方法会返回null
                        myRunnable = taskQueue.poll();
                        if (myRunnable == null) {
                            isNotify = true;
                        }
                    }
                    if (myRunnable != null) {
                        taskMap.put(mES.submit(myRunnable), myRunnable);
                    }
                }
            }
        });
    }

    /**
     * <Summary Description>
     */
    private void stop() {
        Toast.makeText(ThreadActivity.this, "任务已被取消！", Toast.LENGTH_LONG).show();
        for (MyRunnable runnable : taskMap.values()) {
            runnable.setCancleTaskUnit(true);
        }
    }

    /**
     * <Summary Description>
     */
    private void start() {
        if (mES == null || taskQueue == null || taskMap == null) {
            Log.i("KKK", "某资源是不是已经被释放了？");
            return;
        }
        mES.execute(new Runnable() {
            @Override
            public void run() {
                if (isRuning) {
                    MyRunnable myRunnable = null;
                    synchronized (lock) {
                        // 从线程队列中取出一个Runnable对象来执行，如果此队列为空，则调用poll()方法会返回null
                        myRunnable = taskQueue.poll();
                        if (myRunnable == null) {
                            isNotify = true;
                            // try
                            // {
                            // myRunnable.wait(500);
                            // }
                            // catch (InterruptedException e)
                            // {
                            // e.printStackTrace();
                            // }
                        }
                    }
                    if (myRunnable != null) {
                        taskMap.put(mES.submit(myRunnable), myRunnable);
                    }
                }
            }
        });
    }

    private void notifyWork() {
        synchronized (lock) {
            if (isNotify) {
                lock.notifyAll();
                isNotify = !isNotify;
            }
        }
    }
}