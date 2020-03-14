package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author : wxj
 **/
@RestController
@RequestMapping("/test")
@Api(tags = "生产者", description = "OrderProducerController")
public class OrderProducerController {

    @Autowired
    private RedissonClient redisson;

    @PostMapping("/producer")
    @ResponseBody
    @ApiOperation(value = "生产")
    public String Producer() {
        RBlockingDeque<String> blockingDeque = redisson.getBlockingDeque("jiuyang_order_message"); //自动完成订单
        RDelayedQueue<String> delayedQueue = redisson.getDelayedQueue(blockingDeque);
        for (long i = 1; i < 5; i++) {
            try {
                User user = new User();
                user.setId(i);
                user.setNamme("123");
                delayedQueue.offer(JSON.toJSONString(user), 20, TimeUnit.SECONDS);  //秒 这里可以换单位 小时  分钟 天
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println("发送条数  " + i + "  参数为   " + user.toString() + "   时间    " + sdf.format(new Date()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "success";
    }

    @PostMapping("/consumer")
    @ResponseBody
    @ApiOperation(value = "消费")
    public String Consumer() {
        RBlockingDeque<String> blockingDeque = redisson.getBlockingDeque("jiuyang_order_message"); //自动完成订单
        RDelayedQueue<String> delayedQueue = redisson.getDelayedQueue(blockingDeque);
        User user = new User();
        long i =3;
        user.setId(i);
        user.setNamme("123");
        delayedQueue.remove(JSON.toJSONString(user));
        return "success";
    }
}
