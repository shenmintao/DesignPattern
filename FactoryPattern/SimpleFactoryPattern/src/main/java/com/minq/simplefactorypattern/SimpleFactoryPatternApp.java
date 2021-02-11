package com.minq.simplefactorypattern;

/**
 * Hello world!
 *
 */
public class SimpleFactoryPatternApp
{
    public static void main( String[] args ) {
        ICourse course = new JavaCourse();
        course.record();

        //使用CourseFactory.createWithString创建对象
        ICourse courseWithString = CourseFactory.createWithString("java");
        courseWithString.record();

        //使用CourseFactory.createWithReflect创建对象
        ICourse courseWithReflect = CourseFactory.createWithReflect(PythonCourse.class);
        courseWithReflect.record();
    }
}
