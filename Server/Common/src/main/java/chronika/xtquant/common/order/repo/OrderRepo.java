package chronika.xtquant.common.order.repo;

import chronika.xtquant.common.order.entity.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends CrudRepository<Order, Long> {

    @Query("select o from Order o where o.orderRemark = ?1")
    Order findByRemark(String remark);

    @Query("select count(o) > 0 from Order o where o.orderRemark = ?1")
    boolean existsByRemark(String remark);

    @Query("select o from Order o where o.accountId = ?1 and o.date = ?2 and o.orderStatus in ?3 order by o.id desc")
    List<Order> findOnDayByStatus(String accountId, int day, List<Integer> status);

    @Query("select o from Order o where o.accountId = ?1 and o.date = ?2 order by o.id desc")
    List<Order> findOnDay(String accountId, int day);

    @Transactional
    @Modifying
    @Query("update Order o set o.orderStatus = ?2 where o.orderRemark = ?1")
    void updateStatusByRemark(String orderRemark, int status);

    @Query(value = "select o from Order o where" +
        " (?1 is null or o.accountId = ?1)" +
        " and (?2 is null or o.orderRemark = ?2)" +
        " and (?3 is null or o.date = ?3)" +
        " and (?4 is null or o.orderStatus in ?4)" +
        " and (?5 is null or o.stockCode = ?5)" +
        " order by o.id desc")
    Page<Order> findAll(String accountId, String remark, Integer date, List<Integer> status, String stockCode, Pageable pageable);

}
