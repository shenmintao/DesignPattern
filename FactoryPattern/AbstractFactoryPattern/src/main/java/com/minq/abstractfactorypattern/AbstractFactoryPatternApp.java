package com.minq.abstractfactorypattern;

/**
 * Hello world!
 *
 */
public class AbstractFactoryPatternApp
{
    public static void main( String[] args ) {
        ICourseFactory factory = new JavaCourseFactory();
        IVideo video = factory.createVideo();
        INote note = factory.createNote();

        video.record();
        note.edit();

        factory = new PythonCourseFactory();
        video = factory.createVideo();
        note = factory.createNote();
        video.record();
        note.edit();
    }
}
