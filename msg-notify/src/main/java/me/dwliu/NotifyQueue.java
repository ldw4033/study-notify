package me.dwliu;

import me.dwliu.bean.NotifyParam;
import me.dwliu.bean.NotifyRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.Serializable;
import java.util.Date;
import java.util.Map;


@Component
public class NotifyQueue implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Log LOG = LogFactory.getLog(NotifyQueue.class);

    @Autowired
    private NotifyParam notifyParam;

    /**
     * 将传过来的对象进行通知次数判断，之后决定是否放在任务队列中
     *
     * @param notifyRecord
     * @throws Exception
     */
    public void addElementToList(NotifyRecord notifyRecord) {
        if (notifyRecord == null) {
            return;
        }
        // 通知次数
        Integer notifyTimes = notifyRecord.getNotifyTimes();
        Integer maxNotifyTime = 0;
        try {
            maxNotifyTime = notifyParam.getMaxNotifyTime();
        } catch (Exception e) {
            LOG.error(e);
        }
        //if (notifyRecord.getVersion().intValue() == 0) {// 刚刚接收到的数据
        //notifyRecord.setLastNotifyTime(new Date());
        //}
        long time = notifyRecord.getLastNotifyTime().getTime();
        Map<Integer, Integer> timeMap = notifyParam.getNotifyParams();
        if (notifyTimes < maxNotifyTime) {
            Integer nextKey = notifyTimes + 1;
            Integer next = timeMap.get(nextKey);
            if (next != null) {
                System.out.println("=="+next);
                time += 1000 * 60 * next + 1000;
                System.out.println("=="+notifyRecord);
                notifyRecord.setLastNotifyTime(new Date(time));
                System.out.println("--"+notifyRecord);
                App.tasks.put(new NotifyTask(notifyRecord, this, notifyParam));
            }
        }
    }

}
