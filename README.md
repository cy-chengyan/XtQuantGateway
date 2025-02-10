# XtQuantGateway

XtQuantGateway 是对 XtQuant 的 二次封装，成为一个独立的交易网关微服务。使得：

1. 解决了 XtQuant 只能在 Windows 本机使用的局限。
2. 方便查看和统计历史数据。

## 系统结构

![summary.drawio.png](Document/summary.drawio.png)

1. Trade API

   RESTful接口服务，接收来自外部的下单和查询请求。

2. Order Placer

   将 Trade API 收到的下单请求，通过调用 MiniQMT 提供的 API，提交到 MiniQMT。

3. File Order Placer

   将 Trade API 收到的下单请求，通过使用 QMT 提供的 文件单 功能，提交到 QMT。

4. Feed Parser

   MiniQMT 或 QMT 输出文件的数据解析，包括资金，持仓，定单等数据。

要根据实际的情况，

1. 如果使用的是 MiniQMT，则选择 Trade API + Order Placer 的组合；
2. 如果使用的是 QMT，则选择 Trade API + File Order Placer 的组合。

Trade API 收到下单请求后，

1. 先在MySQL保存定单记录；
2. 将定单记录保存到 Redis 的队列中，使下单请求串行化。

Order Placer 模块监听 Redis 队列，依次取出定单，提交到 MiniQMT。

要么：

File Order Placer 模块也会监听 Redis 队列，依次取出定单，通过文件单的方式提交到 QMT。

## 目录结构

* /Document - 文档

* /Server

  包含了 TradeApi, FeedParser, FileOrderPlacer 模块，使用 Java(SpringBoot) 开发。

  * /Common
  
    公共模块。

  * /TradeApi

    RESTful接口服务，接收来自外部的下单和查询请求。 

  * /ApiOrderPlacer

    使用 MiniQMT 才会有的 API 方式的定单处理模块，由于语言的限制，使用 Python 开发。

  * /FileOrderPlacer
  
    文件单方式的定单处理模块。

* /PyLib

  Python客户端库，用于调用 Trade API。
