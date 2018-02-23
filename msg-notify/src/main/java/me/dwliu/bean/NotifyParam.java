package me.dwliu.bean;

import java.util.Map;

/**
 * @author liudw
 * @version 1.0.0
 * @description 通知参数 spring-notify.xml
 * @create 2018-02-23 13:41
 **/
public class NotifyParam {

    private Map<Integer, Integer> notifyParams;// 通知时间次数map
    private String successValue;// 通知后用于判断是否成功的返回值。由HttpResponse获取

    public Map<Integer, Integer> getNotifyParams() {
        return notifyParams;
    }

    public void setNotifyParams(Map<Integer, Integer> notifyParams) {
        this.notifyParams = notifyParams;
    }

    public String getSuccessValue() {
        return successValue;
    }

    public void setSuccessValue(String successValue) {
        this.successValue = successValue;
    }

    public int getMaxNotifyTime() {
        return notifyParams == null ? 0 : notifyParams.size();
    }
}
