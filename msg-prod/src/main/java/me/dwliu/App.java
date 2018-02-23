package me.dwliu;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;

/**
 * @author liudw
 * @version 1.0.0
 * @description
 * @create 2018-02-23 16:40
 **/
public class App {

    private static final Log LOG = LogFactory.getLog(App.class);


    private static ClassPathXmlApplicationContext context;
    private static JmsTemplate notifyJmsTemplate; // 商户通知队列模板

    public static void main(String[] args) {
        try {
            context = new ClassPathXmlApplicationContext(new String[]{"spring-activemq.xml"});
            context.start();
            notifyJmsTemplate = (JmsTemplate) context.getBean("notifyJmsTemplate");

            startInitFromDB();
            context.close();
            LOG.info("== context start");
        } catch (Exception e) {
            LOG.error("== application start error:", e);
            return;
        }

    }


    /**
     * 从数据库中取一次数据用来当系统启动时初始化
     */
    private static void startInitFromDB() {
        LOG.info("get data from database");


        final NotifyRecord notifyRecord = new NotifyRecord(new Date(System.currentTimeMillis() + 1000 * 10 * 1), 1, 5, "", "no111");
        notifyJmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(JSONObject.toJSONString(notifyRecord));
            }
        });

    }
}
