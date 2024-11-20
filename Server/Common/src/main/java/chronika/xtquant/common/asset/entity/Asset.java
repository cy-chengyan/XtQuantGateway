package chronika.xtquant.common.asset.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import chronika.xtquant.common.infra.util.JsonUtil;

import java.math.BigDecimal;

@Entity
@Table(name = "t_asset")
public class Asset {

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
        return accountId + ';'
            + date + ';'
            + totalAsset + ';'
            + marketValue + ';'
            + cash + ';'
            + withdrawableCash;
    }

}
