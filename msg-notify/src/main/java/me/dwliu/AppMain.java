package me.dwliu;

import java.util.Date;
import java.util.concurrent.DelayQueue;

import me.dwliu.bean.NotifyRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


/**
 * 通知App
 *
 * @author zws
 */
public class AppMain {

    private static final Log LOG = LogFactory.getLog(AppMain.class);

    public static DelayQueue<NotifyTask> tasks = new DelayQueue<NotifyTask>();

    private static ClassPathXmlApplicationContext context;

    private static ThreadPoolTaskExecutor threadPool;

    private static NotifyQueue notifyQueue;

    public static void main(String[] args) {
        try {
            context = new ClassPathXmlApplicationContext(new String[]{"spring-context.xml"});
            context.start();
            threadPool = (ThreadPoolTaskExecutor) context.getBean("threadPool");
            notifyQueue = (NotifyQueue) context.getBean("notifyQueue");
            startInitFromDB();
            startThread();
            LOG.info("== context start");
        } catch (Exception e) {
            LOG.error("== application start error:", e);
            return;
        }
        synchronized (AppMain.class) {
            while (true) {
                try {
                    AppMain.class.wait();
                } catch (InterruptedException e) {
                    LOG.error("== synchronized error:", e);
                }
            }
        }
    }

    private static void startThread() {
        // Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
        // public void run() {
        // System.out.println(threadPool.getActiveCount() + "-->current active--->" + tasks.size() + "max is--->" + threadPool.getMaxPoolSize());
        // }
        // }, 0, 5, TimeUnit.SECONDS);

        threadPool.execute(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        // 如果当前活动线程等于最大线程，那么不执行
                        if (threadPool.getActiveCount() < threadPool.getMaxPoolSize()) {
                            final NotifyTask task = tasks.poll();
                            if (task != null) {
                                threadPool.execute(new Runnable() {
                                    public void run() {
                                        System.out.println(threadPool.getActiveCount() + "---------");
                                        tasks.remove(task);
                                        task.run();
                                    }
                                });
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 从数据库中取一次数据用来当系统启动时初始化
     */
    private static void startInitFromDB() {
        LOG.info("get data from database");

        for (int i = 0; i < 10; i++) {
            //NotifyRecord notifyRecord = new NotifyRecord(new Date(System.currentTimeMillis() -1000*60*20), 1, 5, "", "no" + i);
            NotifyRecord notifyRecord = new NotifyRecord(new Date(System.currentTimeMillis() +i*1000+1000*60*1), 1, 5, "", "no" + i);
            System.out.println("##"+notifyRecord);
            notifyQueue.addElementToList(notifyRecord);
        }
    }
}
