package com.minq.decoratorpattern;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ISignForThirdService signForThirdService = new SignForThirdService(new SignService());

        signForThirdService.loginForQQ("tewwr234");
    }
}
