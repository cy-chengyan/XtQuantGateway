package chronika.xtquant.tradeapi.controller;

import chronika.xtquant.common.asset.AssetService;
import chronika.xtquant.common.asset.entity.Asset;
import chronika.xtquant.common.infra.util.BizUtil;
import chronika.xtquant.tradeapi.model.CkApiRequest;
import chronika.xtquant.tradeapi.model.CkApiResponse;
import chronika.xtquant.tradeapi.model.reqData.AccountReqData;
import chronika.xtquant.tradeapi.model.reqData.QueryCashReqData;
import chronika.xtquant.tradeapi.model.resData.QueryCashResData;
import chronika.xtquant.tradeapi.model.resData.QueryCurrentCashResData;
import chronika.xtquant.tradeapi.util.ParamHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/cash")
@Tag(name = "Cash")
@CrossOrigin
public class CashController {

    @Autowired
    private AssetService assetService;

    @Operation(summary = "Query current cash")
    @ResponseBody
    @RequestMapping(value = "/current", method = {RequestMethod.POST})
    public CkApiResponse<QueryCurrentCashResData> queryCurrentCash(@RequestBody @Valid CkApiRequest<AccountReqData> requestBody) {
        AccountReqData reqData = ParamHelper.getReqDataSafely(requestBody);
        String accountId = reqData.getAccountId();
        Asset cash = assetService.findLatestByAccountId(accountId);
        QueryCurrentCashResData resData = QueryCurrentCashResData.builder()
            .cash(cash)
            .build();
        return CkApiResponse.ok(resData);
    }

    @Operation(summary = "Query cashes")
    @ResponseBody
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public CkApiResponse<QueryCashResData> queryCashes(@RequestBody @Validated CkApiRequest<QueryCashReqData> requestBody) {
        QueryCashReqData reqData = ParamHelper.getReqDataSafely(requestBody);
        String accountId = reqData.getAccountId();
        Integer date = reqData.getDate();
        Pageable pageable = BizUtil.createPageable(reqData.getPageSize(), reqData.getPageNum());
        Page<Asset> pageCash = assetService.find(accountId, date, pageable);
        QueryCashResData resData = QueryCashResData.builder()
            .cashes(pageCash.getContent())
            .totalElements(pageCash.getTotalElements())
            .totalPages(pageCash.getTotalPages())
            .pageSize(pageCash.getSize())
            .pageNum(pageCash.getNumber())
            .build();
        return CkApiResponse.ok(resData);
    }

}
