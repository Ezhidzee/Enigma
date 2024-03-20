package su.ezhidze.models;


public class InputMessageModel {

    private String type;

    private String senderSubject;

    private Integer chatId;

    private String data;

    public InputMessageModel(String type, String senderSubject, Integer chatId, String data) {
        this.type = type;
        this.senderSubject = senderSubject;
        this.chatId = chatId;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSenderSubject() {
        return senderSubject;
    }

    public void setSenderSubject(String senderSubject) {
        this.senderSubject = senderSubject;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
