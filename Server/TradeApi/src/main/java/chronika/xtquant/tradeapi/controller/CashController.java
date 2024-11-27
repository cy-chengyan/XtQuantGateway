package chronika.xtquant.tradeapi.controller;

import chronika.xtquant.common.asset.AssetService;
import chronika.xtquant.common.asset.entity.Asset;
import chronika.xtquant.common.infra.enums.CkError;
import chronika.xtquant.tradeapi.model.CkApiRequest;
import chronika.xtquant.tradeapi.model.CkApiResponse;
import chronika.xtquant.tradeapi.model.reqData.AccountReqData;
import chronika.xtquant.tradeapi.model.resData.QueryCurrentCashResData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
        AccountReqData reqData = requestBody.getData();
        if (reqData == null) {
            return CkApiResponse.error(CkError.MISSING_PARAM.code, "Missing request 'data' field");
        }

        String accountId = reqData.getAccountId();
        Asset cash = assetService.findLatestByAccountId(accountId);
        QueryCurrentCashResData resData = QueryCurrentCashResData.builder()
            .cash(cash)
            .build();
        return CkApiResponse.ok(resData);
    }

}
