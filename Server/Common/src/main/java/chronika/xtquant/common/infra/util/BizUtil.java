package chronika.xtquant.common.infra.util;

import chronika.xtquant.common.infra.misc.Constants;
import chronika.xtquant.common.infra.param.PageReqData;
import chronika.xtquant.common.infra.param.SortParam;
import kuenlon.quotation.market.MarketCalendarService;
import kuenlon.quotation.market.entity.Market;
import kuenlon.quotation.market.entity.MarketCalendar;
import kuenlon.quotation.market.enums.MarketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BizUtil {

    private final MarketCalendarService marketCalendarService;

    private int previousTradingDayParamCache = 0;
    private MarketCalendar previousTradingDayCache = null;

    private int nextTradingDayParamCache = 0;
    private MarketCalendar nextTradingDayCache = null;

    private int isTradingDayParamCache = 0;
    private MarketCalendar isTradingDayCache = null;

    @Autowired
    public BizUtil(MarketCalendarService marketCalendarService) {
        this.marketCalendarService = marketCalendarService;
    }

    public static String parseAccountId(String accountKey) {
        // accountKey格式可能如下:
        // 2____10075________49____620000308469____
        // 2____10355____10355____49____8883372637____
        // 取出 620000308469 或是 8883372637
        String[] parts = accountKey.split("____");
        if (parts.length < 5) {
            return null;
        }
        return parts[4];
    }

    public static Pageable createPageable(Integer pageSize, Integer pageNum, List<String> sortFields, List<String> sortOrders) {
        if (pageSize == null) {
            pageSize = Constants.defaultPageSize;
        }
        if (pageNum == null) {
            pageNum = 0;
        }

        Sort sort = Sort.unsorted();
        if (sortFields != null && !sortFields.isEmpty()
            && sortOrders != null && !sortOrders.isEmpty()
            && sortFields.size() == sortOrders.size()) {
            List<Sort.Order> orders = new ArrayList<>();
            for (int i = 0; i < sortFields.size(); i++) {
                String field = sortFields.get(i);
                String order = sortOrders.get(i);
                Sort.Direction direction = "descend".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
                orders.add(new Sort.Order(direction, field));
            }
            sort = Sort.by(orders);
        }

        return PageRequest.of(pageNum, pageSize, sort);
    }

    public static Pageable createPageable(Integer pageSize, Integer pageNum) {
        return createPageable(pageSize, pageNum, null, null);
    }

    public static Pageable createPageable(Integer pageSize, Integer pageNum, SortParam sort) {
        return sort == null
            ? createPageable(pageSize, pageNum)
            : createPageable(pageSize, pageNum, sort.getFields(), sort.getOrders());
    }

    public static Pageable createPageable(PageReqData pageReqData) {
        return pageReqData == null
            ? createPageable(null, null)
            : createPageable(pageReqData.getPageSize(), pageReqData.getPageNum(), pageReqData.getSort());
    }

    public Integer findPreviousTradingDay(int date) {
        if (this.previousTradingDayParamCache != date) {
            this.previousTradingDayParamCache = date;
            this.previousTradingDayCache = marketCalendarService.findPreviousOpeningDay(Market.XSHE, this.previousTradingDayParamCache);
        }
        return this.previousTradingDayCache == null
            ? null
            : this.previousTradingDayCache.getDate();
    }

    public Integer findNextTradingDay(int date) {
        if (this.nextTradingDayParamCache != date) {
            this.nextTradingDayParamCache = date;
            this.nextTradingDayCache = marketCalendarService.findNextOpeningDay(Market.XSHE, this.nextTradingDayParamCache);
        }
        return this.nextTradingDayCache == null
            ? null
            : this.nextTradingDayCache.getDate();
    }

    public boolean isTradingDay(int date) {
        if (this.isTradingDayParamCache != date) {
            this.isTradingDayParamCache = date;
            this.isTradingDayCache = marketCalendarService.findByMarketIdAndDate(Market.XSHE, this.isTradingDayParamCache);
        }
        return this.isTradingDayCache != null
            && (this.isTradingDayCache.getStatus() == MarketStatus.OPEN || this.isTradingDayCache.getStatus() == MarketStatus.EARLY_CLOSE);
    }

}
