package chronika.xtquant.common.position.entity;

import chronika.xtquant.common.asset.entity.Asset;
import chronika.xtquant.common.infra.misc.Constants;
import chronika.xtquant.common.infra.util.DateUtil;
import chronika.xtquant.common.infra.util.JsonUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

@Entity
@Table(name = "t_position")
public class Position {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Position.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    @Schema(description = "账号ID")
    private String accountId;

    @Schema(description = "日期, format: YYYYMMDD")
    private int date;

    @Schema(description = "股票代码")
    private String stockCode;

    @Schema(description = "持仓数量")
    private Long volume;

    @Schema(description = "可用数量")
    private Long canUseVolume;

    @Schema(description = "冻结数量")
    private Long frozenVolume;

    @Schema(description = "在途数量")
    private Long onRoadVolume;

    @Schema(description = "平均建仓成本价")
    private BigDecimal openPrice;

    @Schema(description = "市价")
    private BigDecimal marketValue;

    @Schema(description = "昨日持仓数量")
    private Long yesterdayVolume;

    @Schema(description = "盈亏")
    private BigDecimal profit;

    @Schema(description = "手动更新时间(毫秒)")
    private Long manualUpdatedAt;

    @Schema(description = "备注")
    private String memo;

    //
    // getters and setters
    //

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public Long getCanUseVolume() {
        return canUseVolume;
    }

    public void setCanUseVolume(Long canUseVolume) {
        this.canUseVolume = canUseVolume;
    }

    public Long getFrozenVolume() {
        return frozenVolume;
    }

    public void setFrozenVolume(Long frozenVolume) {
        this.frozenVolume = frozenVolume;
    }

    public Long getOnRoadVolume() {
        return onRoadVolume;
    }

    public void setOnRoadVolume(Long onRoadVolume) {
        this.onRoadVolume = onRoadVolume;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public BigDecimal getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(BigDecimal marketValue) {
        this.marketValue = marketValue;
    }

    public Long getYesterdayVolume() {
        return yesterdayVolume;
    }

    public void setYesterdayVolume(Long yesterdayVolume) {
        this.yesterdayVolume = yesterdayVolume;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public Long getManualUpdatedAt() {
        return manualUpdatedAt;
    }

    public void setManualUpdatedAt(Long manualUpdatedAt) {
        this.manualUpdatedAt = manualUpdatedAt;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    //
    // Other methods
    //

    @Override
    public String toString() {
        return "Position:" + JsonUtil.toJsonString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Asset other)) {
            return false;
        }
        return this.checkSum().equals(other.checkSum());
    }

    public String checkSum () {
        return accountId + ";"
            + date + ";"
            + stockCode + ";"
            + volume + ";"
            + canUseVolume + ";"
            + openPrice;
    }

    //
    // Constructors
    //

    public Position() {
    }

    public Position(String[] feedLineFields) {
        this.accountId = StringUtils.split(feedLineFields[0], "____")[4];
        this.date = DateUtil.currentYmd();
        this.stockCode = feedLineFields[2] + "." + feedLineFields[1];
        this.volume = Long.parseLong(feedLineFields[3]);
        this.canUseVolume = Long.parseLong(feedLineFields[4]);
        this.onRoadVolume = Long.parseLong(feedLineFields[5]);
        this.frozenVolume = Long.parseLong(feedLineFields[6]);
        this.yesterdayVolume = Long.parseLong(feedLineFields[7]);
        this.openPrice = new BigDecimal(feedLineFields[8]).setScale(Constants.PriceDecimalPrecision, Constants.FinDecimalRoundingMode);
        this.marketValue = new BigDecimal(feedLineFields[9]).setScale(Constants.AssetDecimalPrecision, Constants.FinDecimalRoundingMode);
        this.profit = new BigDecimal(feedLineFields[10]).setScale(Constants.AssetDecimalPrecision, Constants.FinDecimalRoundingMode);
    }

    public static Position createByFeedLine(String[] lineFields) {
        if (lineFields == null || lineFields.length < 11) {
            return null;
        }

        try {
            return new Position(lineFields);
        } catch (Exception e) {
            log.error("Failed to create Position by fields: {}", e.getMessage());
            return null;
        }
    }

}
