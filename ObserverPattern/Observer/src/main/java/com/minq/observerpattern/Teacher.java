package com.minq.observerpattern;

import java.util.Observable;

import java.util.Observer;

public class Teacher implements Observer {
    private String name;
    public Teacher(String name) {
        this.name = name;
    }
    public void update(Observable o, Object args) {
        GPer gper = (GPer) o;
        Question question = (Question) args;
        System.out.println("===================");
        System.out.println(name + "老师，你好!\n" + "您收到了一个来自" + gper.getName() + "的提问，希望您解答，问题如下：\n"
                + question.getContent() + "\n" + "提问者：" + question.getUserName());
    }
}
