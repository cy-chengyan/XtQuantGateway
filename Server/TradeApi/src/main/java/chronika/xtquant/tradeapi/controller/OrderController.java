package chronika.xtquant.tradeapi.controller;

import chronika.xtquant.common.infra.enums.CkError;
import chronika.xtquant.common.order.OrderService;
import chronika.xtquant.common.order.entity.CancelOrder;
import chronika.xtquant.common.order.entity.NewOrder;
import chronika.xtquant.common.order.entity.Order;
import chronika.xtquant.common.order.entity.OrderPlacingResult;
import chronika.xtquant.tradeapi.model.CkApiRequest;
import chronika.xtquant.tradeapi.model.CkApiResponse;
import chronika.xtquant.tradeapi.model.reqData.*;
import chronika.xtquant.tradeapi.model.resData.BatchPlaceOrderResData;
import chronika.xtquant.tradeapi.model.resData.PlaceOrderResData;
import chronika.xtquant.tradeapi.model.resData.QueryOrderResData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/order")
@Tag(name = "Order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Query current orders")
    @ResponseBody
    @RequestMapping(value = "/current", method = {RequestMethod.POST})
    public CkApiResponse<QueryOrderResData> queryCurrentOrders(@RequestBody @Valid CkApiRequest<QueryCurrentOrderReqData> requestBody) {
        QueryCurrentOrderReqData reqData = requestBody.getData();
        if (reqData == null) {
            return CkApiResponse.error(CkError.MISSING_PARAM.code, "Missing request 'data' field");
        }

        String accountId = reqData.getAccountId();
        List<Integer> status = reqData.getStatus();
        List<Order> orders = orderService.queryCurrentOrders(accountId, status);
        QueryOrderResData resData = QueryOrderResData.builder()
            .orders(orders)
            .build();
        return CkApiResponse.ok(resData);
    }

    @Operation(summary = "Place order")
    @ResponseBody
    @RequestMapping(value = "/async-place", method = {RequestMethod.POST})
    public CkApiResponse<PlaceOrderResData> placeOrder(@RequestBody @Valid CkApiRequest<PlaceOrderReqData> requestBody) {
        PlaceOrderReqData reqData = requestBody.getData();
        if (reqData == null) {
            return CkApiResponse.error(CkError.MISSING_PARAM.code, "Missing request 'data' field");
        }

        NewOrder order = reqData.getOrder();
        OrderPlacingResult result = orderService.asyncPlaceOrder(order);
        PlaceOrderResData resData = PlaceOrderResData.builder()
            .result(result)
            .build();
        return CkApiResponse.ok(resData);
    }

    @Operation(summary = "Place orders in bulk")
    @ResponseBody
    @RequestMapping(value = "/async-place-batch", method = {RequestMethod.POST})
    public CkApiResponse<BatchPlaceOrderResData> batchPlaceOrder(@RequestBody @Valid CkApiRequest<BatchPlaceOrderReqData> requestBody) {
        BatchPlaceOrderReqData reqData = requestBody.getData();
        if (reqData == null) {
            return CkApiResponse.error(CkError.MISSING_PARAM.code, "Missing request 'data' field");
        }

        List<NewOrder> orders = reqData.getOrders();
        List<OrderPlacingResult> results = orderService.asyncPlaceOrders(orders);
        BatchPlaceOrderResData resData = BatchPlaceOrderResData.builder()
            .results(results)
            .build();
        return CkApiResponse.ok(resData);
    }


    @Operation(summary = "Cancel order")
    @ResponseBody
    @RequestMapping(value = "/async-cancel", method = {RequestMethod.POST})
    public CkApiResponse<PlaceOrderResData> cancelOrder(@RequestBody @Valid CkApiRequest<CancelOrderReqData> requestBody) {
        CancelOrderReqData reqData = requestBody.getData();
        if (reqData == null) {
            return CkApiResponse.error(CkError.MISSING_PARAM.code, "Missing request 'data' field");
        }

        CancelOrder order = reqData.getOrder();
        OrderPlacingResult result = orderService.asyncCancelOrder(order);
        PlaceOrderResData resData = PlaceOrderResData.builder()
            .result(result)
            .build();
        return CkApiResponse.ok(resData);
    }

    @Operation(summary = "Cancel orders in bulk")
    @ResponseBody
    @RequestMapping(value = "/async-cancel-batch", method = {RequestMethod.POST})
    public CkApiResponse<BatchPlaceOrderResData> batchCancelOrder(@RequestBody @Valid CkApiRequest<BatchCancelOrderReqData> requestBody) {
        BatchCancelOrderReqData reqData = requestBody.getData();
        if (reqData == null) {
            return CkApiResponse.error(CkError.MISSING_PARAM.code, "Missing request 'data' field");
        }

        List<CancelOrder> orders = reqData.getOrders();
        List<OrderPlacingResult> results = orderService.asyncCancelOrders(orders);
        BatchPlaceOrderResData resData = BatchPlaceOrderResData.builder()
            .results(results)
            .build();
        return CkApiResponse.ok(resData);
    }

}
