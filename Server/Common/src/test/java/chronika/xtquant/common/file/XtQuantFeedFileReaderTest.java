package chronika.xtquant.common.file;

import chronika.xtquant.common.asset.entity.Asset;
import chronika.xtquant.common.order.entity.Order;
import chronika.xtquant.common.position.entity.Position;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class XtQuantFeedFileReaderTest {

   @Autowired
   private XtQuantOutputFileService xtQuantOutputFileService;

    @Test
    public void testReadOrderFeed() {
        String filePath = "E:\\GjQMT\\output\\8881976116_2_orders.csv";
        XtQuantFeedFileReader reader = new XtQuantFeedFileReader(filePath);
        List<String[]> lines = reader.load();
        int idx = 0;
        for (String[] line : lines) {
            if (idx++ == 0) {
                continue;
            }
            Order order = Order.createByFeedLine(line);
            System.out.println(order);
        }
    }

    @Test
    public void testReadPositionFeed() {
        String filePath = "E:\\GjQMT\\output\\8881976116_2_positions.csv";
        XtQuantFeedFileReader reader = new XtQuantFeedFileReader(filePath);
        List<String[]> lines = reader.load();
        int idx = 0;
        for (String[] line : lines) {
            if (idx++ == 0) {
                continue;
            }
            Position position = Position.createByFeedLine(line);
            System.out.println(position);
        }
    }

    @Test
    public void testReadAssetFeed() {
        String filePath = "E:\\GjQMT\\output\\8881976116_2_account.csv";
        XtQuantFeedFileReader reader = new XtQuantFeedFileReader(filePath);
        List<String[]> lines = reader.load();
        int idx = 0;
        for (String[] line : lines) {
            if (idx++ == 0) {
                continue;
            }
            Asset asset = Asset.createByFeedLine(line);
            System.out.println(asset);
        }
    }

    @Test
    public void testLoadFeed() {
        xtQuantOutputFileService.loadFeed();
    }

}
