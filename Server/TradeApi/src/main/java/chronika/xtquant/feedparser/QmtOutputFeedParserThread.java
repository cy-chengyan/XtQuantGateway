package chronika.xtquant.feedparser;

import chronika.xtquant.common.infra.util.BizUtil;
import chronika.xtquant.common.infra.util.DateUtil;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class QmtOutputFeedParserThread implements Runnable {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(QmtOutputFeedParserThread.class);

    private final BizUtil bizUtil;
    private final XtQuantOutputFileService xtQuantOutputFileService;
    private final Thread thread;
    private final Long parseInterval;
    private final Integer hsyncAtClock;
    private boolean loopFlag = true;

    @Autowired
    QmtOutputFeedParserThread(@Value("${xtquant.feed-parsing-interval}") long parseInterval,
                              @Value("${xtquant.hsync-at-clock}") Integer hsyncAtClock,
                              BizUtil bizUtil,
                              XtQuantOutputFileService xtQuantOutputFileService) {
        this.bizUtil = bizUtil;
        this.xtQuantOutputFileService = xtQuantOutputFileService;
        this.parseInterval = parseInterval;
        this.hsyncAtClock = hsyncAtClock;
        thread = new Thread(this);
        thread.start();
    }

    @PreDestroy
    public void destroy() {
        loopFlag = false;
        try {
            thread.join();
            log.info("[=== STOPPED ===] QmtOutputFeedParser thread stopped");
        } catch (InterruptedException e) {
            log.error("QmtOutputFeedParser thread join error", e);
        }
    }

    @Override
    public void run() {
        log.info("[=== STARTED ===] QmtOutputFeedParser thread started");

        while (this.loopFlag) {
            try {
                if (bizUtil.isTradingDay(DateUtil.currentYmd())) {
                    xtQuantOutputFileService.loadFeed();
                }
                if (DateUtil.currentLocalHour() < hsyncAtClock) {
                    Thread.sleep(300000); // 5 minute, 60 * 1000 * 5
                } else {
                    Thread.sleep(parseInterval);
                }
                // System.out.println("JqOutputFileLoader thread running");
            } catch (InterruptedException e) {
                log.error("QmtOutputFeedParser thread interrupted", e);
                break;
            } catch (Exception e) {
                log.error("QmtOutputFeedParser thread error", e);
            }
        }
    }

}
