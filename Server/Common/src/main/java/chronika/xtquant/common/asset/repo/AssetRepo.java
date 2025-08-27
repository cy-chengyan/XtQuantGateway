package chronika.xtquant.common.asset.repo;

import chronika.xtquant.common.asset.entity.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepo extends CrudRepository<Asset, Long> {

    @Query("select a from Asset a where a.accountId = ?1 and a.date = ?2")
    Asset findByAccountIdAndDate(String accountId, Integer date);

    @Query("select a from Asset a where a.accountId = ?1 order by a.date desc limit 1")
    Asset findLatestByAccountId(String accountId);

    @Query("select a from Asset a where"
        + " (?1 is null or a.accountId = ?1)"
        + " and (?2 is null or a.date = ?2)"
        + " order by a.id desc")
    Page<Asset> find(String accountId, Integer date, Pageable pageable);

}
