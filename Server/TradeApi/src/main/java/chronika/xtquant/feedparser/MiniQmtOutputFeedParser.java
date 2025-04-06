package chronika.xtquant.feedparser;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MiniQmtOutputFeedParser implements Runnable {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MiniQmtOutputFeedParser.class);

    private final XtQuantOutputFileService xtQuantOutputFileService;
    private final Thread thread;
    private final Long parseInterval;
    private boolean loopFlag = true;

    @Autowired
    MiniQmtOutputFeedParser(@Value("${xtquant.feed-parsing-interval}") long parseInterval,
                            XtQuantOutputFileService xtQuantOutputFileService) {
        this.xtQuantOutputFileService = xtQuantOutputFileService;
        this.parseInterval = parseInterval;
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
                Thread.sleep(parseInterval);
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
