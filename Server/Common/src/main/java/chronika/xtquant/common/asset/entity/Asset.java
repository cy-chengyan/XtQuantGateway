package chronika.xtquant.common.asset.entity;

import chronika.xtquant.common.infra.misc.Constants;
import chronika.xtquant.common.infra.util.BizUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import chronika.xtquant.common.infra.util.JsonUtil;

import java.math.BigDecimal;

@Entity
@Table(name = "t_asset")
public class Asset {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Asset.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    @Schema(description = "账号ID")
    private String accountId;

    @Schema(description = "日期")
    private int date;

    @Schema(description = "总资产")
    private BigDecimal totalAsset;

    @Schema(description = "证券市值")
    private BigDecimal marketValue;

    @Schema(description = "可用资金")
    private BigDecimal cash;

    @Schema(description = "可取资金")
    private BigDecimal withdrawableCash;

    @Schema(description = "盈亏金额")
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

    public BigDecimal getTotalAsset() {
        return totalAsset;
    }

    public void setTotalAsset(BigDecimal totalAsset) {
        this.totalAsset = totalAsset;
    }

    public BigDecimal getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(BigDecimal marketValue) {
        this.marketValue = marketValue;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public BigDecimal getWithdrawableCash() {
        return withdrawableCash;
    }

    public void setWithdrawableCash(BigDecimal withdrawableCash) {
        this.withdrawableCash = withdrawableCash;
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
        return "Cash:" + JsonUtil.toJsonString(this);
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
            + totalAsset + ";"
            + marketValue + ";"
            + cash + ";"
            + withdrawableCash;
    }

    //
    // Constructors
    //

    public Asset() {
    }

    public Asset(String[] feedLineFields) {
        this.accountId = BizUtil.parseAccountId(feedLineFields[0]);
        this.totalAsset = new BigDecimal(feedLineFields[1]).setScale(Constants.AssetDecimalPrecision, Constants.FinDecimalRoundingMode);
        this.cash = new BigDecimal(feedLineFields[2]).setScale(Constants.AssetDecimalPrecision, Constants.FinDecimalRoundingMode);
        this.marketValue = new BigDecimal(feedLineFields[3]).setScale(Constants.AssetDecimalPrecision, Constants.FinDecimalRoundingMode);
        this.profit = new BigDecimal(feedLineFields[6]).setScale(Constants.AssetDecimalPrecision, Constants.FinDecimalRoundingMode);
        this.date = Integer.parseInt(feedLineFields[7]);
        this.withdrawableCash = new BigDecimal(feedLineFields[9]).setScale(Constants.AssetDecimalPrecision, Constants.FinDecimalRoundingMode);
    }

    public static Asset createByFeedLine(String[] lineFields) {
        if (lineFields == null || lineFields.length < 10) {
            return null;
        }

        if (!lineFields[8].equals("登录成功")) {
            log.info("Account status: {}", lineFields[8]);
            return null;
        }

        try {
            return new Asset(lineFields);
        } catch (Exception e) {
            log.error("Failed to create Position by fields: {}", e.getMessage());
            return null;
        }
    }

}
