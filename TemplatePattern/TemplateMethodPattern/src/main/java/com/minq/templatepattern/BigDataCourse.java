package com.minq.templatepattern;

public class BigDataCourse extends NetworkCourse{

    private Boolean needHomeworkFlag = false;

    public BigDataCourse(Boolean needHomeworkFlag) {
        this.needHomeworkFlag = needHomeworkFlag;
    }

    void checkHomework() {
        System.out.println("检查大数据的课后作业");
    }

    @Override
    protected boolean needHomework() {
        return this.needHomeworkFlag;
    }
}
