package chronika.xtquant.common.position;

import chronika.xtquant.common.position.entity.Position;
import chronika.xtquant.common.position.repo.PositionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PositionService.class);

    private final PositionRepo positionRepo;

    @Autowired
    public PositionService(PositionRepo positionRepo) {
        this.positionRepo = positionRepo;
    }

    public Position replace(Position position) {
        Position ret;
        Position existed = positionRepo.findByAccountAndDateAndStockCode(position.getAccountId(), position.getDate(), position.getStockCode());
        if (existed != null) {
            if (existed.getManualUpdatedAt() != null) {
                ret = existed;
            } else {
                position.setId(existed.getId());
                if (!position.equals(existed)) {
                    log.info("[持仓更新]:{} >>> {}", existed, position);
                    positionRepo.save(position);
                }
                ret = position;
            }
        } else {
            log.info("[持仓新增]:{}", position);
            ret = positionRepo.save(position);
        }
        return ret;
    }

    public List<Position> findByAccountId(String accountId) {
        return positionRepo.findByAccountIdOnLatestDay(accountId);
    }

}
