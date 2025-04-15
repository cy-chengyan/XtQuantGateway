package chronika.xtquant.orderplacer;

import chronika.xtquant.common.queue.OrderQueueService;
import chronika.xtquant.common.queue.entity.OrderQueueMsg;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileOrderPlacingThread implements Runnable {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FileOrderPlacingThread.class);

    private final OrderQueueService orderQueueService;
    private final XtQuantFileOrderService xtQuantFileOrderService;
    private final Thread thread;
    private boolean loopFlag = true;

    @Autowired
    FileOrderPlacingThread(OrderQueueService orderQueueService, XtQuantFileOrderService xtQuantFileOrderService) {
        this.orderQueueService = orderQueueService;
        this.xtQuantFileOrderService = xtQuantFileOrderService;
        thread = new Thread(this);
        thread.start();
    }

    @PreDestroy
    public void destroy() {
        loopFlag = false;
        try {
            thread.join();
            log.info("[=== STOPPED ===] FileOrderPlacingService thread stopped");
        } catch (InterruptedException e) {
            log.error("FileOrderPlacingService thread join error", e);
        }
    }

    @Override
    public void run() {
        log.info("[=== STARTED ===] FileOrderPlacingService thread started");

        while (this.loopFlag) {
            try {
                OrderQueueMsg msg = orderQueueService.blockReceiveOrder(100L);
                if (msg != null) {
                    xtQuantFileOrderService.processOrderQueueMsg(msg);
                }
            } catch (Exception e) {
                log.error("FileOrderPlacingService thread error", e);
            }
        }
    }

}
