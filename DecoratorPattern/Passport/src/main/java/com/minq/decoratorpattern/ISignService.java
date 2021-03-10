package com.minq.decoratorpattern;

public interface ISignService {
    ResultMsg regist(String username, String password);

    ResultMsg login(String username, String password);
}
