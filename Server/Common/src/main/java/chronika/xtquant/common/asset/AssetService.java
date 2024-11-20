package chronika.xtquant.common.asset;

import chronika.xtquant.common.asset.entity.Asset;
import chronika.xtquant.common.asset.repo.AssetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetService {

    private final AssetRepo assetRepo;

    @Autowired
    public AssetService(AssetRepo assetRepo) {
        this.assetRepo = assetRepo;
    }

    public Asset replace(Asset asset) {
        boolean needUpdate = true;
        Asset existed = assetRepo.findByAccountIdAndDate(asset.getAccountId(), asset.getDate());
        if (existed != null) {
            asset.setId(existed.getId());
            if (asset.equals(existed)) {
                needUpdate = false;
                // System.out.println("Asset无变化, 不更新数据库");
            }
        }

        return needUpdate
            ? assetRepo.save(asset)
            : existed;
    }

    public Asset findLatestByAccountId(String accountId) {
        return assetRepo.findLatestByAccountId(accountId);
    }

}
