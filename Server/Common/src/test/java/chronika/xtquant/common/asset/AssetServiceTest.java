package chronika.xtquant.common.asset;

import chronika.xtquant.common.infra.util.BizUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AssetServiceTest {

    @Autowired
    private AssetService assetService;

    @Test
    public void testQueryAsset() {
        System.out.println(assetService.findLatestByAccountId("17631aaf-e823-11ee-bb7c-00163e022aa6"));
    }

    @Test
    public void testAccountIdParsing() {
        System.out.println(BizUtil.parseAccountId("2____10075________49____620000308469____"));
        System.out.println(BizUtil.parseAccountId("2____10355____10355____49____8883372637____"));
    }

}
