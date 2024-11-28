package chronika.xtquant.feedparser;

import chronika.xtquant.common.file.XtQuantOutputFileService;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MiniQmtOutputFeedParser implements Runnable {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MiniQmtOutputFeedParser.class);

    private final XtQuantOutputFileService xtQuantOutputFileService;
    private final Thread thread;
    private boolean loopFlag = true;

    @Autowired
    MiniQmtOutputFeedParser(XtQuantOutputFileService xtQuantOutputFileService) {
        this.xtQuantOutputFileService = xtQuantOutputFileService;
        thread = new Thread(this);
        thread.start();
    }

    @PreDestroy
    public void destroy() {
        loopFlag = false;
        try {
            thread.join();
            log.info("[=== STOPPED ===] MiniQmtOutputFeedParser thread stopped");
        } catch (InterruptedException e) {
            log.error("MiniQmtOutputFeedParser thread join error", e);
        }
    }

    @Override
    public void run() {
        log.info("[=== STARTED ===] MiniQmtOutputFeedParser thread started");

        while (this.loopFlag) {
            try {
                xtQuantOutputFileService.loadFeed();
                Thread.sleep(1000L);
                // System.out.println("JqOutputFileLoader thread running");
            } catch (InterruptedException e) {
                log.error("MiniQmtOutputFeedParser thread interrupted", e);
                break;
            } catch (Exception e) {
                log.error("MiniQmtOutputFeedParser thread error", e);
            }
        }
    }

}
