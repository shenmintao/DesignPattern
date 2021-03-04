package com.minq.strategypattern;

public class CouponStrategy implements PromotionStrategy{
    @Override
    public void doPromotion(){
        System.out.println("领取优惠券，课程的价格直接减优惠券面值抵扣");
    }
}
