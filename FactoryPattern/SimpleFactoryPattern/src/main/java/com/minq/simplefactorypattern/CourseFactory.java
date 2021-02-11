package com.minq.simplefactorypattern;

public class CourseFactory {
    public static ICourse createWithString(String className) {
        if ("java".equals(className)) {
            return new JavaCourse();
        } else if ("python".equals(className)) {
            return new PythonCourse();
        } else {
            return null;
        }
    }

    //更好的方法是使用反射来创建对象
    public static ICourse createWithReflect(Class<? extends ICourse> clazz) {
        if (null != clazz) {
            try{
                return clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
