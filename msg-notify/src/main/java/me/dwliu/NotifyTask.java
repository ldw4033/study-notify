package me.dwliu;


import me.dwliu.bean.NotifyParam;
import me.dwliu.bean.NotifyRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 执行通知的类，以后有业务时添加业务代码即可
 *
 * @author ZhangWensi
 */
public class NotifyTask implements Runnable, Delayed {

    private static final Log LOG = LogFactory.getLog(NotifyTask.class);

    private long executeTime;

    private NotifyRecord notifyRecord;

    private NotifyQueue notifyQueue;

    private NotifyParam notifyParam;

    public NotifyTask() {
    }

    public NotifyTask(NotifyRecord notifyRecord, NotifyQueue notifyQueue, NotifyParam notifyParam) {
        super();
        this.notifyRecord = notifyRecord;
        this.notifyQueue = notifyQueue;
        this.notifyParam = notifyParam;
        this.executeTime = getExecuteTime(notifyRecord);
    }

    private long getExecuteTime(NotifyRecord record) {
        long lastTime = record.getLastNotifyTime().getTime();
        Integer nextNotifyTime = notifyParam.getNotifyParams().get(record.getNotifyTimes());
        return (nextNotifyTime == null ? 0 : nextNotifyTime * 1000) + lastTime;
    }

    public int compareTo(Delayed o) {
        NotifyTask task = (NotifyTask) o;
        return executeTime > task.executeTime ? 1 : (executeTime < task.executeTime ? -1 : 0);


    }

    public long getDelay(TimeUnit unit) {
        return unit.convert(executeTime - System.currentTimeMillis(), TimeUnit.SECONDS);
    }

    public void run() {
        // 得到当前通知对象的通知次数
        Integer notifyTimes = notifyRecord.getNotifyTimes();
        // 去通知
        try {
            LOG.info("Notify Url " + notifyRecord.getUrl());
            // 得到返回状态，如果是200，也就是通知成功
            if (true) {

                LOG.info("到时间通知:"+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) +":"+ notifyRecord.getMerchantNo());
            } else {
                notifyQueue.addElementToList(notifyRecord);

            }


        } catch (Exception e) {
            LOG.error("NotifyTask", e);
            notifyQueue.addElementToList(notifyRecord);

        }

    }
}
