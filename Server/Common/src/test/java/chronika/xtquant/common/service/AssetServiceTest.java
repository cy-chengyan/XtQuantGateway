package chronika.xtquant.common.service;

import chronika.xtquant.common.asset.AssetService;
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

}
