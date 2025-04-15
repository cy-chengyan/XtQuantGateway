package chronika.xtquant.common.gateway;

import chronika.xtquant.common.gateway.entity.ServiceStatus;
import chronika.xtquant.common.gateway.repo.ServiceStatusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceStatusService {

    private final ServiceStatusRepo serviceStatusRepo;

    @Autowired
    public ServiceStatusService(ServiceStatusRepo serviceStatusRepo) {
        this.serviceStatusRepo = serviceStatusRepo;
    }

    public ServiceStatus findByServiceId(String id) {
        return serviceStatusRepo.findById(id).orElse(null);
    }

    public ServiceStatus save(ServiceStatus serviceStatus) {
        return serviceStatusRepo.save(serviceStatus);
    }

}
