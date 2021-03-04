package com.minq.delegatepattern;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
         new Boss().command("登录", new Leader());
    }
}
