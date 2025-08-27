package kuenlon.quotation.market;

import kuenlon.quotation.market.entity.Market;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketService {

    public List<Market> queryAllMarkets() {
        return Market.ALL_MARKETS;
    }

    public boolean isMarketSupported(String marketId) {
        return Market.MARKET_MAP.containsKey(marketId);
    }

    public Market queryMarketById(String marketId) {
        return Market.MARKET_MAP.get(marketId);
    }

}
