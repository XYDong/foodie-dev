package com.joker.service.impl;

import com.joker.service.StuService;
import com.joker.service.TestTransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestTransServiceImpl implements TestTransService {

    /**
     * 事务传播 - Propagation
     *      REQUIRED ：使用当前的事务，如果没有，则创建一个，子方法是必须运行在一个事务中的；
     *                  如果当前存在事务，则加入这个事务，称为一个整体。
     *                  举例：领导没饭吃，我有钱，我会自己买了自己吃；领导有的吃，会分给你吃。
     *      SUPPORTS : 如果当前有事务，则使用事务，如果没有，则不使用事务；
     *      MANDATORY : 如果当前没有事务，则抛出异常：Support a current transaction, throw an exception if none exists.
     *      REQUIRES_NEW ：如果当前有事务，则挂起当前事务，自己重新新建一个事务；如果当前没有事务，则创建一个新的事务。
     *      NOT_SUPPORTED ：不管当前有没有事务，自己都是不在事务中执行。
     *      NEVER ： 如果当前存在事务，则抛出异常
     *      NESTED ： 如果当前有事务，则开启子事务（嵌套），嵌套事务是独立提交或回滚的；
     *                  如果当前没有主事务提交，则会携带子事务一起提交；
     *                  如果主事务回滚，则子事务一起回滚，相反，子事务异常，则父事务可以回滚或不回滚。
     */

    private final StuService stuService;

    public TestTransServiceImpl(StuService stuService) {
        this.stuService = stuService;
    }

//    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void testPropagationTrans() {
        stuService.saveParent();

        stuService.saveChildren();

//        int a = 1/0;
    }
}
