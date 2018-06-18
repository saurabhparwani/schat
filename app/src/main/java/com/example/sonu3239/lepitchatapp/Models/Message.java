package com.example.sonu3239.lepitchatapp.Models;

public class Message  {
    String type,content,time,seen,from;

    public Message() {
    }

    public Message(String type, String content, String time, String seen, String from) {
        this.type = type;
        this.content = content;
        this.time = time;
        this.seen = seen;
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
