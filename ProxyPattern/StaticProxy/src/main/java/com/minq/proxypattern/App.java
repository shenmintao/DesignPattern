package com.minq.proxypattern;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try{
            Order order = new Order();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date date = sdf.parse("2017/12/11");
            order.setCreateTime(date.getTime());

            IOrderService orderService = new OrderServiceStaticProxy(new OrderService());
            orderService.createOrder(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
