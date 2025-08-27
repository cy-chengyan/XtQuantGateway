package chronika.xtquant.common.asset;

import chronika.xtquant.common.asset.entity.Asset;
import chronika.xtquant.common.asset.repo.AssetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class AssetService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AssetService.class);

    private final AssetRepo assetRepo;

    @Autowired
    public AssetService(AssetRepo assetRepo) {
        this.assetRepo = assetRepo;
    }

    public Asset replace(Asset asset) {
        Asset ret;
        Asset existed = assetRepo.findByAccountIdAndDate(asset.getAccountId(), asset.getDate());
        if (existed != null) {
            if (existed.getManualUpdatedAt() != null) {
                ret = existed;
            } else {
                asset.setId(existed.getId());
                if (!asset.equals(existed)) {
                    log.info("[资产更新]:{} >>> {}", existed, asset);
                    assetRepo.save(asset);
                }
                ret = asset;
            }
        } else {
            log.info("[资产新增]:{}", asset);
            ret = assetRepo.save(asset);
        }

        return ret;
    }

    public Asset findLatestByAccountId(String accountId) {
        return assetRepo.findLatestByAccountId(accountId);
    }

    public Page<Asset> find(String accountId, Integer date, Pageable pageable) {
        return assetRepo.find(accountId, date, pageable);
    }

}
