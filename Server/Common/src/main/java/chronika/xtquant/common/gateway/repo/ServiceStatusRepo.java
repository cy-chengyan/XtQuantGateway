package chronika.xtquant.common.gateway.repo;

import chronika.xtquant.common.gateway.entity.ServiceStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceStatusRepo extends CrudRepository<ServiceStatus, String> {
}
