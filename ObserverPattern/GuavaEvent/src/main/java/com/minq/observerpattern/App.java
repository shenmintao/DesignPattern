package com.minq.observerpattern;

import com.google.common.eventbus.EventBus;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        EventBus eventBus = new EventBus();
        GuavaEvent guavaEvent = new GuavaEvent();
        eventBus.register(guavaEvent);
        eventBus.post("Tom");
    }
}
