package chronika.xtquant.common.queue;

import chronika.xtquant.common.order.entity.Order;
import chronika.xtquant.common.queue.entity.OrderQueueMsg;
import chronika.xtquant.common.queue.repo.OrderQueueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderQueueService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(OrderQueueService.class);

    private final OrderQueueRepo orderQueueRepo;

    @Autowired
    public OrderQueueService(OrderQueueRepo orderQueueRepo) {
        this.orderQueueRepo = orderQueueRepo;
    }

    public boolean sendNewOrder(Order order) {
        OrderQueueMsg msg = new OrderQueueMsg(OrderQueueMsg.MSG_TYPE_PLACE_ORDER, order);
        return orderQueueRepo.push(msg);
    }

    public boolean sendCancelOrder(Order order) {
        OrderQueueMsg msg = new OrderQueueMsg(OrderQueueMsg.MSG_TYPE_CANCEL_ORDER, order);
        return orderQueueRepo.push(msg);
    }

}
