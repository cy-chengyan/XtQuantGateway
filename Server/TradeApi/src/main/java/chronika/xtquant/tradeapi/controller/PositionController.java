package chronika.xtquant.tradeapi.controller;

import chronika.xtquant.common.infra.util.BizUtil;
import chronika.xtquant.common.position.PositionService;
import chronika.xtquant.common.position.entity.Position;
import chronika.xtquant.tradeapi.model.CkApiRequest;
import chronika.xtquant.tradeapi.model.CkApiResponse;
import chronika.xtquant.tradeapi.model.reqData.AccountReqData;
import chronika.xtquant.tradeapi.model.reqData.QueryPositionReqData;
import chronika.xtquant.tradeapi.model.resData.QueryPositionResData;
import chronika.xtquant.tradeapi.util.ParamHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/position")
@Tag(name = "Position")
@CrossOrigin
public class PositionController {

    @Autowired
    private PositionService positionService;

    @Operation(summary = "Query current holding positions")
    @ResponseBody
    @RequestMapping(value = "/current", method = {RequestMethod.POST})
    public CkApiResponse<QueryPositionResData> queryCurrentPositions(@RequestBody @Valid CkApiRequest<AccountReqData> requestBody) {
        AccountReqData reqData = ParamHelper.getReqDataSafely(requestBody);
        String accountId = reqData.getAccountId();
        List<Position> positions = positionService.findByAccountId(accountId);
        QueryPositionResData resData = QueryPositionResData.builder()
            .positions(positions)
            .build();
        return CkApiResponse.ok(resData);
    }

    @Operation(summary = "Query holding positions")
    @ResponseBody
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public CkApiResponse<QueryPositionResData> queryPositions(@RequestBody @Validated CkApiRequest<QueryPositionReqData> requestBody) {
        QueryPositionReqData reqData = ParamHelper.getReqDataSafely(requestBody);
        String accountId = reqData.getAccountId();
        Integer date = reqData.getDate();
        String stockCode = reqData.getStockCode();
        Pageable pageable = BizUtil.createPageable(reqData);
        Page<Position> pagePosition = positionService.find(accountId, date, stockCode, pageable);
        QueryPositionResData resData = QueryPositionResData.builder()
            .positions(pagePosition.getContent())
            .totalElements(pagePosition.getTotalElements())
            .totalPages(pagePosition.getTotalPages())
            .pageSize(pagePosition.getSize())
            .pageNum(pagePosition.getNumber())
            .build();
        return CkApiResponse.ok(resData);
    }

}
