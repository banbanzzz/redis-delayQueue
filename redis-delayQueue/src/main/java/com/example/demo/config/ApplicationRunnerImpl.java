package com.example.demo.config;

import com.example.demo.service.RefundOrderServiceImpl;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author : wxj
 **/

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

    @Autowired
    private RefundOrderServiceImpl refundOrderService;

    @Autowired
    private RedissonClient redisson;

    //tomcat启动执行此方法
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("====>自动执行？");

        /**
         * 防止服务挂掉 挂掉之间刚好过期的队列消息没处理，立即处理
        */
        RBlockingDeque<String> blockingDeque = redisson.getBlockingDeque("jiuyang_order_message");
        RDelayedQueue<String> delayedQueue = redisson.getDelayedQueue(blockingDeque);
        refundOrderService.refundOrder();
    }
}
