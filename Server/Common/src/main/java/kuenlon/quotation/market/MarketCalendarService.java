package kuenlon.quotation.market;

import kuenlon.quotation.market.entity.Market;
import kuenlon.quotation.market.entity.MarketCalendar;
import kuenlon.quotation.market.repo.MarketCalendarRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketCalendarService {

    private final MarketCalendarRepo marketCalendarRepo;

    @Autowired
    public MarketCalendarService(MarketCalendarRepo marketCalendarRepo) {
        this.marketCalendarRepo = marketCalendarRepo;
    }

    public List<MarketCalendar> findByMarketIdAndDateBetween(Market market, String startAt, String endAt) {
        int startAtInt = Integer.parseInt(startAt.replace("-", ""));
        int endAtInt = Integer.parseInt(endAt.replace("-", ""));
        return findByMarketIdAndDateBetween(market, startAtInt, endAtInt);
    }

    public List<MarketCalendar> findByMarketIdAndDateBetween(Market market, Integer startAt, Integer endAt) {
        return marketCalendarRepo.findByMarketIdAndDateBetween(market.getId(), startAt, endAt);
    }

    public MarketCalendar findByMarketIdAndDate(Market market, Integer date) {
        return marketCalendarRepo.findByMarketIdAndDate(market.getId(), date);
    }

    public MarketCalendar findPreviousOpeningDay(Market market, Integer date) {
        return marketCalendarRepo.findPreviousOpeningDay(market.getId(), date);
    }

    public MarketCalendar findNextOpeningDay(Market market, Integer date) {
        return marketCalendarRepo.findNextOpeningDay(market.getId(), date);
    }

}
