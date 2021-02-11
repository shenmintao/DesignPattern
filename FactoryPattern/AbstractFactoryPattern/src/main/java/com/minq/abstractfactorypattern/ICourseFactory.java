package com.minq.abstractfactorypattern;

public interface ICourseFactory {
    INote createNote();
    IVideo createVideo();
}
