package chronika.xtquant.common.order.repo;

import chronika.xtquant.common.order.entity.Order;
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

}
