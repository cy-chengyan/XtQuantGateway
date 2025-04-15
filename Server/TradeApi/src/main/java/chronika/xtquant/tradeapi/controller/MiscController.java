package chronika.xtquant.tradeapi.controller;

import chronika.xtquant.common.gateway.ServiceStatusService;
import chronika.xtquant.common.gateway.entity.ServiceStatus;
import chronika.xtquant.common.infra.misc.CkApp;
import chronika.xtquant.tradeapi.model.CkApiRequest;
import chronika.xtquant.tradeapi.model.CkApiResponse;
import chronika.xtquant.tradeapi.model.resData.QueryServiceStatusResData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/misc")
@Tag(name = "Miscellaneous")
@CrossOrigin
public class MiscController {

    @Autowired
    private ServiceStatusService serviceStatusService;
    @Autowired
    private CkApp ckApp;

    @Operation(summary = "Query service status")
    @ResponseBody
    @RequestMapping(value = "/service/status", method = {RequestMethod.POST})
    public CkApiResponse<QueryServiceStatusResData> queryCurrentCash(@RequestBody @Valid CkApiRequest<?> requestBody) {
        ServiceStatus status = serviceStatusService.findByServiceId(ckApp.getId());
        QueryServiceStatusResData resData = QueryServiceStatusResData.builder()
            .status(status)
            .build();
        return CkApiResponse.ok(resData);
    }

}
