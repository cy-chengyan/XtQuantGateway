package chronika.xtquant.common.queue.repo;

import chronika.xtquant.common.queue.entity.OrderQueueMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class OrderQueueRepo {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(OrderQueueRepo.class);

    private final RedisTemplate<String, String> redisTemplate;
    private final String orderQueue;

    @Autowired
    public OrderQueueRepo(RedisTemplate<String, String> redisTemplate,
                          @Value("${xtquant.order-queue}") String orderQueue) {
        this.redisTemplate = redisTemplate;
        this.orderQueue = orderQueue;
    }

    public boolean push(OrderQueueMsg msg) {
        try {
            String raw = msg.serialize();
            logger.info("Pushing order message: {}", raw);
            redisTemplate.opsForList().leftPush(orderQueue, raw);
            return true;
        } catch (Exception e) {
            logger.error("Failed to push order message: {}", msg, e);
            return false;
        }
    }

    public OrderQueueMsg blockPull(long timeoutMills) {
        try {
            String raw = redisTemplate.opsForList().rightPop(orderQueue, timeoutMills, TimeUnit.MILLISECONDS);
            if (raw == null) {
                return null;
            }
            logger.info("Pulled order message: {}", raw);
            return OrderQueueMsg.deserialize(raw);
        } catch (Exception e) {
            logger.error("Failed to pull order message", e);
            return null;
        }
    }

}
