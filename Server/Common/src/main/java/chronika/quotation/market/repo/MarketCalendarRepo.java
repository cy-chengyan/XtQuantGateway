package chronika.quotation.market.repo;

import chronika.quotation.market.entity.MarketCalendar;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketCalendarRepo extends CrudRepository<MarketCalendar, Long> {

    @Query("select m from MarketCalendar m where m.marketId = ?1 and m.date >= ?2 and m.date <= ?3")
    List<MarketCalendar> findByMarketIdAndDateBetween(String marketId, Integer startAt, Integer endAt);

    @Query("select m from MarketCalendar m where m.marketId = ?1 and m.date = ?2")
    MarketCalendar findByMarketIdAndDate(String marketId, Integer date);

    @Query(value = "select m from MarketCalendar m where m.marketId = ?1 and m.date < ?2 and m.status = 1 order by m.date desc limit 1")
    MarketCalendar findPreviousOpeningDay(String marketId, Integer date);

    @Query(value = "select m from MarketCalendar m where m.marketId = ?1 and m.date > ?2 and m.status = 1 order by m.date asc limit 1")
    MarketCalendar findNextOpeningDay(String marketId, Integer date);

}
