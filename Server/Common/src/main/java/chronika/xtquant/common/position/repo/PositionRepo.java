package chronika.xtquant.common.position.repo;

import chronika.xtquant.common.position.entity.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepo extends CrudRepository<Position, Long> {

    @Query("select p from Position p where p.accountId = ?1 and p.date = ?2 and p.stockCode = ?3")
    Position findByAccountAndDateAndStockCode(String accountId, Integer date, String stockCode);

    @Query("select p from Position p where p.accountId = ?1 and p.date = (select max(p2.date) from Position p2 where p2.accountId = ?1)")
    List<Position> findByAccountIdOnLatestDay(String accountId);

    @Query("select p from Position p where"
        + " (?1 is null or p.accountId = ?1)"
        + " and (?2 is null or p.date = ?2)"
        + " and (?3 is null or p.stockCode = ?3)"
        + " order by p.id desc")
    Page<Position> find(String accountId, Integer date, String stockCode, Pageable pageable);

}
