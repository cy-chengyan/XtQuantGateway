package chronika.quotation.market.entity;

import com.google.common.collect.Lists;
import chronika.quotation.market.enums.CountryCode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Market {

    public static Market of(String id, String shortName, String fullName, CountryCode country, String tz) {
        return new Market(id, shortName, fullName, country, tz);
    }

    // ISO 10383 Market Identifier Code (MIC)
    public static final Market XSHG = Market.of("XSHG", "SSE", "Shanghai Stock Exchange", CountryCode.CHN, "Asia/Shanghai");
    public static final Market XSHE = Market.of("XSHE", "SZSE", "Shanghai Stock Exchange", CountryCode.CHN, "Asia/Shanghai");
    public static final Market BJSE = Market.of("BJSE", "BSE", "Beijing Stock Exchange", CountryCode.CHN, "Asia/Shanghai");
    public static final Market XHKG = Market.of("XHKG", "HKEX", "Hong Kong Stock Exchange", CountryCode.HKG, "Asia/Hong_Kong");
    public static final Market XNYS = Market.of("XNYS", "NYSE", "New York Stock Exchange", CountryCode.USA, "America/New_York");
    public static final Market XNAS = Market.of("XNAS", "NASDAQ", "National Association of Securities Dealers Automated Quotations", CountryCode.USA, "America/New_York");

    public static List<Market> ALL_MARKETS = Lists.newArrayList(XSHG, XSHE, BJSE, XHKG, XNYS, XNAS);
    public static Map<String, Market> MARKET_MAP = ALL_MARKETS.stream().collect(Collectors.toMap(Market::getId, Function.identity()));

    @Schema(description = "Market Id, ISO 10383 Market Identifier Code (MIC)")
    private String id;

    @Schema(description = "Short name")
    private String shortName;

    @Schema(description = "Full name")
    private String fullName;

    @Schema(description = "Country, ISO 3166-1 alpha-3 country code")
    private CountryCode country;

    @Schema(description = "Time zone")
    private String tz;

    //
    // Constructors
    //

    public Market(String id, String shortName, String fullName, CountryCode country, String tz) {
        this.id = id;
        this.shortName = shortName;
        this.fullName = fullName;
        this.country = country;
        this.tz = tz;
    }

    //
    // Getters and Setters
    //

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public CountryCode getCountry() {
        return country;
    }

    public void setCountry(CountryCode country) {
        this.country = country;
    }

    public String getTz() {
        return tz;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }

    //
    // Others
    //

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Market other) {
            return this.id.equals(other.id);
        }
        return false;
    }

}
