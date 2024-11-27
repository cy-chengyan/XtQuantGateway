package chronika.xtquant.common.file;

import chronika.xtquant.common.asset.AssetService;
import chronika.xtquant.common.asset.entity.Asset;
import chronika.xtquant.common.order.OrderService;
import chronika.xtquant.common.order.entity.Order;
import chronika.xtquant.common.position.PositionService;
import chronika.xtquant.common.position.entity.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
public class XtQuantOutputFileService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(XtQuantOutputFileService.class);

    // private final Map<String, String> assetDict = new HashMap<>();
    // private final Map<String, String> positionDict = new HashMap<>();
    // private final Map<String, String> orderDict = new HashMap<>();

    private final AssetService assetService;
    private final PositionService positionService;
    private final OrderService orderService;

    private final String xtQuantOutputDir;
    private String assetFeedPath = null, positionFeedPath = null, orderFeedPath = null;

    @Autowired
    public XtQuantOutputFileService(@Value("${xtquant.output-dir}") String xtQuantOutputDir,
                                    AssetService assetService,
                                    PositionService positionService,
                                    OrderService orderService) {
        if (xtQuantOutputDir != null) {
            xtQuantOutputDir = xtQuantOutputDir.trim();
        }
        if (!StringUtils.hasLength(xtQuantOutputDir)) {
            throw new RuntimeException("'xtquant.output-dir' is not set");
        }
        this.xtQuantOutputDir = xtQuantOutputDir;
        this.parseFeedPath();

        this.assetService = assetService;
        this.positionService = positionService;
        this.orderService = orderService;
    }

    // 解析出 feed 文件的路径
    private void parseFeedPath() {
        // 在 xtQuantOutputDir 目录下存放着诸如 8881976116_2_orders.csv 这样的文件
        // 遍历 xtQuantOutputDir 目录下的所有文件, 找出 asset.csv, position.csv, order.csv
        // 作为 feed 文件的路径
        File dir = new File(xtQuantOutputDir);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new RuntimeException("Directory not found: " + xtQuantOutputDir);
        }

        File[] files = dir.listFiles();
        if (files == null) {
            throw new RuntimeException("No files found in directory: " + xtQuantOutputDir);
        }

        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                if (fileName.contains("account.csv")) {
                    assetFeedPath = file.getAbsolutePath();
                } else if (fileName.contains("positions.csv")) {
                    positionFeedPath = file.getAbsolutePath();
                } else if (fileName.contains("orders.csv")) {
                    orderFeedPath = file.getAbsolutePath();
                }
            }
        }
    }

    public void loadFeed() {
        try {
            loadAssetFeed();
            loadPositionFeed();
            loadOrderFeed();
        } catch (Exception e) {
            log.error("Failed to load feed", e);
        }
    }

    private void loadAssetFeed() {
        if (assetFeedPath == null) {
            return;
        }

        XtQuantFeedFileReader reader = new XtQuantFeedFileReader(assetFeedPath);
        int idx = 0;
        for (String[] line : reader.load()) {
            if (idx++ == 0) {
                continue;
            }

            Asset asset = Asset.createByFeedLine(line);
            if (asset == null) {
                continue;
            }

            assetService.replace(asset);
        }
    }

    private void loadPositionFeed() {
        if (positionFeedPath == null) {
            return;
        }

        XtQuantFeedFileReader reader = new XtQuantFeedFileReader(positionFeedPath);
        int idx = 0;
        for (String[] line : reader.load()) {
            if (idx++ == 0) {
                continue;
            }

            Position position = Position.createByFeedLine(line);
            if (position == null) {
                continue;
            }

            positionService.replace(position);
        }
    }

    private void loadOrderFeed() {
        if (orderFeedPath == null) {
            return;
        }

        Map<String, Order> redupMap = new HashMap<>();
        XtQuantFeedFileReader reader = new XtQuantFeedFileReader(orderFeedPath);
        int idx = 0;
        for (String[] line : reader.load()) {
            if (idx++ == 0) {
                continue;
            }

            Order order = Order.createByFeedLine(line);
            if (order == null) {
                continue;
            }

            String orderRemark = order.getOrderRemark();
            if (!StringUtils.hasLength(orderRemark)) { // 过滤掉不是通过api下单的定单
                continue;
            }

            redupMap.put(orderRemark, order); // 原始数据中一个定单会有多条记录, 一个定单只要状态发生变化就会新增一条, 所以这里只取最后一条
        }

        for (Order order : redupMap.values()) {
            orderService.replace(order);
        }
    }

}
