package me.dwliu.message;

import com.alibaba.fastjson.JSONObject;
import me.dwliu.NotifyQueue;
import me.dwliu.bean.NotifyRecord;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;


import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * @描述: 通知队列监听器.
 * @作者: HuPitao, WuShuicheng.
 * @创建: 2014-5-8,下午3:58:28
 * @版本: V1.0
 */
@Component
public class ConsumerSessionAwareMessageListener implements SessionAwareMessageListener<Message> {

    private static final Log log = LogFactory.getLog(ConsumerSessionAwareMessageListener.class);

    @Autowired
    private JmsTemplate notifyJmsTemplate;
    @Autowired
    private Destination sessionAwareQueue;

    @Autowired
    private NotifyQueue notifyQueue;


    @SuppressWarnings("static-access")
    public synchronized void onMessage(Message message, Session session) {
        try {
            ActiveMQTextMessage msg = (ActiveMQTextMessage) message;
            final String ms = msg.getText();
            log.info("== receive message:" + ms);
            NotifyRecord notifyRecord = JSONObject.parseObject(ms, NotifyRecord.class);// 这里转换成相应的对象还有问题
            if (notifyRecord == null) {
                return;
            }

            // 添加到通知队列
            notifyQueue.addElementToList(notifyRecord);


        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
    }
}
