package su.ezhidze.models;

import java.util.List;

public class ChatModel {

    private Integer id;

    private List<UserResponseModel> users;

    private List<MessageResponseModel> messages;

    private Integer g;

    private Integer p;

    private Integer A;

    private Integer B;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<UserResponseModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserResponseModel> users) {
        this.users = users;
    }

    public List<MessageResponseModel> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageResponseModel> messages) {
        this.messages = messages;
    }

    public Integer getG() {
        return g;
    }

    public void setG(Integer g) {
        this.g = g;
    }

    public Integer getP() {
        return p;
    }

    public void setP(Integer p) {
        this.p = p;
    }

    public Integer getA() {
        return A;
    }

    public void setA(Integer a) {
        A = a;
    }

    public Integer getB() {
        return B;
    }

    public void setB(Integer b) {
        B = b;
    }
}
