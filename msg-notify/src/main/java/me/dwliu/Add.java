package me.dwliu;

import me.dwliu.bean.NotifyRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Date;

/**
 * @author liudw
 * @version 1.0.0
 * @description
 * @create 2018-02-23 14:06
 **/
public class Add {




    public static void main(String[] args) {
        NotifyQueue notifyQueue= new NotifyQueue();

        NotifyRecord notifyRecord = new NotifyRecord(new Date(), 0, 5, "", "no11111");
        notifyQueue.addElementToList(notifyRecord);
    }
}
