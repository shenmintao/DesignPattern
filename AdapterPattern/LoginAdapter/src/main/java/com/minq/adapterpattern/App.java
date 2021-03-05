package com.minq.adapterpattern;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        IPassportForThird passportForThird = new PassportForThirdAdapter();
        passportForThird.loginForQQ("");
    }
}
