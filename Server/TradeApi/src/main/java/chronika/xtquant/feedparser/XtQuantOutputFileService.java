package chronika.xtquant.feedparser;

import chronika.xtquant.common.asset.AssetService;
import chronika.xtquant.common.asset.entity.Asset;
import chronika.xtquant.common.file.XtQuantFeedFileReader;
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

    private final AssetService assetService;
    private final PositionService positionService;
    private final OrderService orderService;

    private final String xtQuantFeedDir;
    private String assetFeedPath = null, positionFeedPath = null, orderFeedPath = null;
    private String assetFeedFinPath = null, positionFeedFinPath = null, orderFeedFinPath = null;
    private final boolean feedFinishFlag;
    private final String feedRmDir; // 解析完成后, 删除文件前, 先将待删除的文件移动到该目录下, 以防止并发删除时, 其他线程正在取写该文件, 导致文件损坏

    @Autowired
    public XtQuantOutputFileService(@Value("${xtquant.feed-dir}") String xtQuantFeedDir,
                                    @Value("${xtquant.feed-finish-flag}") boolean feedFinishFlag,
                                    @Value("${xtquant.feed-rm-dir}") String feedRmDir,
                                    AssetService assetService,
                                    PositionService positionService,
                                    OrderService orderService) {
        if (xtQuantFeedDir != null) {
            xtQuantFeedDir = xtQuantFeedDir.trim();
        }
        if (!StringUtils.hasLength(xtQuantFeedDir)) {
            throw new RuntimeException("'xtquant.feed-dir' is not set");
        }
        this.xtQuantFeedDir = xtQuantFeedDir;
        this.feedFinishFlag = feedFinishFlag;
        this.parseFeedPath();
        // 当 outputFinishFlag 为 true 时:
        // 1. feed 文件是由另一个 python 程序生成的, 每次生成后, 会额外生成一个 finish 文件, finish 文件以 .fin 文件, 以表示生成完毕;
        //    当有 .fin 文件存在时，该 python 程序将不会重新生成对应的 feed 文件;
        // 2. 此程序解析 feed 文件完成后，会删除所有的 feed 文件以及对应的 .fin 文件, 这样 python 程序会接着生成新数据;
        // 3. 第一次解析之前, 为防止解析到旧的数据, 所以先删除一次所有的 feed 文件以及对应的 .fin 文件, 这样使得 python 程序生成一份新数据后再去解析;
        if (feedFinishFlag) {
            this.feedRmDir = feedRmDir.trim();
            File dir = new File(feedRmDir);
            if (!dir.exists() || !dir.isDirectory()) {
                throw new RuntimeException("Directory(xtquant.feed-rm-dir) not found: " + feedRmDir);
            }
            this.removeAllFeedFiles();
        } else {
            this.feedRmDir = null;
        }

        this.assetService = assetService;
        this.positionService = positionService;
        this.orderService = orderService;
    }

    // 解析出 feed 文件的路径
    private void parseFeedPath() {
        // 在 xtQuantOutputDir 目录下存放着诸如 8881976116_2_orders.csv 这样的文件
        // 遍历 xtQuantOutputDir 目录下的所有文件, 找出 asset.csv, position.csv, order.csv
        // 作为 feed 文件的路径
        File dir = new File(xtQuantFeedDir);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new RuntimeException("Directory not found: " + xtQuantFeedDir);
        }

        File[] files = dir.listFiles();
        if (files == null) {
            throw new RuntimeException("No files found in directory: " + xtQuantFeedDir);
        }

        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                if (fileName.endsWith("account.csv")) {
                    assetFeedPath = file.getAbsolutePath();
                } else if (fileName.endsWith("positions.csv")) {
                    positionFeedPath = file.getAbsolutePath();
                } else if (fileName.endsWith("orders.csv")) {
                    orderFeedPath = file.getAbsolutePath();
                }
            }
        }

        if (assetFeedPath == null) {
            throw new RuntimeException("Asset feed file not found in directory: " + xtQuantFeedDir);
        } else if (feedFinishFlag) {
            assetFeedFinPath = assetFeedPath + ".fin";
        }
        if (positionFeedPath == null) {
            throw new RuntimeException("Position feed file not found in directory: " + xtQuantFeedDir);
        } else if (feedFinishFlag) {
            positionFeedFinPath = positionFeedPath + ".fin";
        }
        if (orderFeedPath == null) {
            throw new RuntimeException("Order feed file not found in directory: " + xtQuantFeedDir);
        } else if (feedFinishFlag) {
            orderFeedFinPath = orderFeedPath + ".fin";
        }
    }

    private void safeDeleteFile(String path) {
        // 先将文件移动到 feedRmDir 目录下, 再删除
        // 为了提升性能, 不判断文件是否存在之类的参数判断
        File srcFile = new File(path);
        File destFile = new File(feedRmDir, srcFile.getName());
        if (!srcFile.renameTo(destFile)) {
            log.error("Failed to rename file: {} to {}", srcFile.getAbsolutePath(), destFile.getAbsolutePath());
        }
        if (!destFile.delete()) {
            log.error("Failed to delete file: {}", destFile.getAbsolutePath());
        }
    }

    // 删除所有的 feed 文件以及 finish 文件
    private void removeAllFeedFiles() {
        safeDeleteFile(assetFeedPath);
        safeDeleteFile(positionFeedPath);
        safeDeleteFile(orderFeedPath);
        safeDeleteFile(assetFeedFinPath);
        safeDeleteFile(positionFeedFinPath);
        safeDeleteFile(orderFeedFinPath);
    }

    public void loadFeed() {
        try {
            if (feedFinishFlag) {
                File assetFeedFinFile = new File(assetFeedFinPath);
                if (assetFeedFinFile.exists()) {
                    loadAssetFeed();
                    safeDeleteFile(assetFeedPath);
                    safeDeleteFile(assetFeedFinPath);
                }
                File positionFeedFinFile = new File(positionFeedFinPath);
                if (positionFeedFinFile.exists()) {
                    loadPositionFeed();
                    safeDeleteFile(positionFeedPath);
                    safeDeleteFile(positionFeedFinPath);
                }
                File orderFeedFinFile = new File(orderFeedFinPath);
                if (orderFeedFinFile.exists()) {
                    loadOrderFeed();
                    safeDeleteFile(orderFeedPath);
                    safeDeleteFile(orderFeedFinPath);
                }
            } else {
                loadAssetFeed();
                loadPositionFeed();
                loadOrderFeed();
            }
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
