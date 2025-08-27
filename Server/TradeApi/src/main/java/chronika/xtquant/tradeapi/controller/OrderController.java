package chronika.xtquant.tradeapi.controller;

import chronika.xtquant.common.infra.util.BizUtil;
import chronika.xtquant.common.order.OrderService;
import chronika.xtquant.common.order.entity.*;
import chronika.xtquant.tradeapi.model.CkApiRequest;
import chronika.xtquant.tradeapi.model.CkApiResponse;
import chronika.xtquant.tradeapi.model.reqData.*;
import chronika.xtquant.tradeapi.model.resData.BatchPlaceOrderResData;
import chronika.xtquant.tradeapi.model.resData.ManualUpdateOrderResData;
import chronika.xtquant.tradeapi.model.resData.PlaceOrderResData;
import chronika.xtquant.tradeapi.model.resData.QueryOrderResData;
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
        QueryCurrentOrderReqData reqData = ParamHelper.getReqDataSafely(requestBody);
        String accountId = reqData.getAccountId();
        List<Integer> status = reqData.getStatus();
        List<Order> orders = orderService.queryCurrentOrders(accountId, status);
        QueryOrderResData resData = QueryOrderResData.builder()
            .orders(orders)
            .build();
        return CkApiResponse.ok(resData);
    }

    @Operation(summary = "Query orders")
    @ResponseBody
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public CkApiResponse<QueryOrderResData> queryOrders(@RequestBody @Validated CkApiRequest<QueryOrderReqData> requestBody) {
        QueryOrderReqData reqData = ParamHelper.getReqDataSafely(requestBody);
        String accountId = reqData.getAccountId();
        String sid = reqData.getSid();
        Integer date = reqData.getDate();
        List<Integer> status = reqData.getStatus();
        String stockCode = reqData.getStockCode();
        Pageable pageable = BizUtil.createPageable(reqData.getPageSize(), reqData.getPageNum());
        Page<Order> pageOrder = orderService.queryOrders(accountId, sid, date, status, stockCode, pageable);
        QueryOrderResData resData = QueryOrderResData.builder()
            .orders(pageOrder.getContent())
            .totalElements(pageOrder.getTotalElements())
            .totalPages(pageOrder.getTotalPages())
            .pageSize(pageOrder.getSize())
            .pageNum(pageOrder.getNumber())
            .build();
        return CkApiResponse.ok(resData);
    }

    @Operation(summary = "Place order")
    @ResponseBody
    @RequestMapping(value = "/async-place", method = {RequestMethod.POST})
    public CkApiResponse<PlaceOrderResData> placeOrder(@RequestBody @Valid CkApiRequest<PlaceOrderReqData> requestBody) {
        PlaceOrderReqData reqData = ParamHelper.getReqDataSafely(requestBody);
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
        BatchPlaceOrderReqData reqData = ParamHelper.getReqDataSafely(requestBody);
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
        CancelOrderReqData reqData = ParamHelper.getReqDataSafely(requestBody);
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
        BatchCancelOrderReqData reqData = ParamHelper.getReqDataSafely(requestBody);
        List<CancelOrder> orders = reqData.getOrders();
        List<OrderPlacingResult> results = orderService.asyncCancelOrders(orders);
        BatchPlaceOrderResData resData = BatchPlaceOrderResData.builder()
            .results(results)
            .build();
        return CkApiResponse.ok(resData);
    }

    @Operation(summary = "Manual update order")
    @ResponseBody
    @RequestMapping(value = "/manual-update", method = {RequestMethod.POST})
    public CkApiResponse<ManualUpdateOrderResData> manualUpdateOrder(@RequestBody @Validated CkApiRequest<ManualUpdateOrderReqData> requestBody) {
        ManualUpdateOrderReqData reqData = ParamHelper.getReqDataSafely(requestBody);
        UpdateOrder updateOrder = reqData.getUpdateOrder();
        Order updatedOrder = orderService.manualUpdateOrder(updateOrder);
        ManualUpdateOrderResData resData = ManualUpdateOrderResData.builder()
            .order(updatedOrder)
            .build();
        return CkApiResponse.ok(resData);
    }

}
