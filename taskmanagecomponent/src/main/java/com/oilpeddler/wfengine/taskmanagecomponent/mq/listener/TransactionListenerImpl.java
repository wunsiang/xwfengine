package com.oilpeddler.wfengine.taskmanagecomponent.mq.listener;

import com.oilpeddler.wfengine.common.api.taskmanagservice.WfTaskInstanceService;
import com.oilpeddler.wfengine.common.dto.WfTaskInstanceDTO;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;

@RocketMQTransactionListener(txProducerGroup = "task-transaction-producer-group")
public class TransactionListenerImpl implements RocketMQLocalTransactionListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private WfTaskInstanceService wfTaskInstanceService;


    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        stringRedisTemplate.opsForValue().set(message.getHeaders().getId().toString(),RocketMQLocalTransactionState.UNKNOWN.toString());
        try{
            wfTaskInstanceService.ending((WfTaskInstanceDTO) o);
            //不知道这个enum究竟怎么序列化好，先这么搞
            stringRedisTemplate.opsForValue().set(message.getHeaders().getId().toString(),RocketMQLocalTransactionState.COMMIT.toString());
            return RocketMQLocalTransactionState.COMMIT;
        }catch (Exception e)
        {
            stringRedisTemplate.opsForValue().set(message.getHeaders().getId().toString(),RocketMQLocalTransactionState.ROLLBACK.toString());
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        String state = stringRedisTemplate.opsForValue().get(message.getHeaders().getId().toString());
        if (state.equals(RocketMQLocalTransactionState.COMMIT.toString())) {
            return RocketMQLocalTransactionState.COMMIT;
        }else if(state.equals(RocketMQLocalTransactionState.UNKNOWN.toString())){
            return RocketMQLocalTransactionState.UNKNOWN;
        }else
            return RocketMQLocalTransactionState.ROLLBACK;
    }
}
