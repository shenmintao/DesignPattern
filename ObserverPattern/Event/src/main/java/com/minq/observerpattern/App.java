package com.minq.observerpattern;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            MouseEventCallback callback = new MouseEventCallback();

            //注册事件
            Mouse mouse = new Mouse();
            mouse.addListener(MouseEventType.ON_CLICK, callback);
            mouse.addListener(MouseEventType.ON_MOVE, callback);
            mouse.addListener(MouseEventType.ON_WHEEL, callback);
            mouse.addListener(MouseEventType.ON_OVER, callback);

            //调用方法
            mouse.click();

            //失焦事件
            mouse.blur();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
