package chronika.xtquant.tradeapi.controller;

import chronika.xtquant.common.infra.enums.CkError;
import chronika.xtquant.common.position.PositionService;
import chronika.xtquant.common.position.entity.Position;
import chronika.xtquant.tradeapi.model.CkApiRequest;
import chronika.xtquant.tradeapi.model.CkApiResponse;
import chronika.xtquant.tradeapi.model.reqData.AccountReqData;
import chronika.xtquant.tradeapi.model.resData.QueryPositionResData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
        AccountReqData reqData = requestBody.getData();
        if (reqData == null) {
            return CkApiResponse.error(CkError.MISSING_PARAM.code, "Missing request 'data' field");
        }

        String accountId = reqData.getAccountId();
        List<Position> positions = positionService.findByAccountId(accountId);
        QueryPositionResData resData = QueryPositionResData.builder()
            .positions(positions)
            .build();
        return CkApiResponse.ok(resData);
    }

}
