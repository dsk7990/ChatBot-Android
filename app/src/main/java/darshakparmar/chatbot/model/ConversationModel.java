package darshakparmar.chatbot.model;

public class ConversationModel {

    private String type;
    private String msg;
    private String msgType;
    private String mobNo;
    private String name;


    public ConversationModel() {
    }

    public ConversationModel(String type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public ConversationModel(String type, String msg, String msgType, String mobNo, String name) {
        this.type = type;
        this.msg = msg;
        this.msgType = msgType;
        this.mobNo = mobNo;
        this.name = name;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
