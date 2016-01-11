package hk.ust.cse.hunkim.questionroom.question;

/**
 * Created by Tsui Ka Wai on 2015/11/23.
 */
public class Reply {
    private String key;
    private String desc;
    private String questionID;
    private int order;
    private long timestamp;

    public Reply() {
    }

    public long getTimestamp() {

        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
