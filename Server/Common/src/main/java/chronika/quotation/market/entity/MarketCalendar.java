package chronika.quotation.market.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import chronika.xtquant.common.infra.converter.JsonToMapConverter;
import chronika.xtquant.common.infra.util.JsonUtil;
import chronika.quotation.market.enums.MarketStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.Map;

@Entity
@Table(name = "t_market_calendar")
public class MarketCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Schema(description = "Market Id")
    @JsonIgnore
    @Column(name = "market_id", nullable = false)
    private String marketId;

    @Schema(description = "Date")
    private Integer date;

    @Schema(description = "Status on this date")
    @Enumerated
    private MarketStatus status;

    @Schema(description = "Memo")
    private String memo;

    @Schema(description = "Extended information")
    @Column(columnDefinition = "json")
    @Convert(converter = JsonToMapConverter.class)
    private Map<String, Object> ext;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public MarketStatus getStatus() {
        return status;
    }

    public void setStatus(MarketStatus status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Map<String, Object> getExt() {
        return ext;
    }

    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }

}
