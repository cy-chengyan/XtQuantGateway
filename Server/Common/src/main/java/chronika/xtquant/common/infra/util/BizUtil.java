package chronika.xtquant.common.infra.util;

import chronika.xtquant.common.infra.misc.Constants;
import chronika.xtquant.common.infra.param.PageReqData;
import chronika.xtquant.common.infra.param.SortParam;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class BizUtil {

    public static String parseAccountId(String accountKey) {
        // accountKey格式可能如下:
        // 2____10075________49____620000308469____
        // 2____10355____10355____49____8883372637____
        // 取出 620000308469 或是 8883372637
        String[] parts = accountKey.split("____");
        if (parts.length < 5) {
            return null;
        }
        return parts[4];
    }

    public static Pageable createPageable(Integer pageSize, Integer pageNum, List<String> sortFields, List<String> sortOrders) {
        if (pageSize == null) {
            pageSize = Constants.defaultPageSize;
        }
        if (pageNum == null) {
            pageNum = 0;
        }

        Sort sort = Sort.unsorted();
        if (sortFields != null && !sortFields.isEmpty()
            && sortOrders != null && !sortOrders.isEmpty()
            && sortFields.size() == sortOrders.size()) {
            List<Sort.Order> orders = new ArrayList<>();
            for (int i = 0; i < sortFields.size(); i++) {
                String field = sortFields.get(i);
                String order = sortOrders.get(i);
                Sort.Direction direction = "descend".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
                orders.add(new Sort.Order(direction, field));
            }
            sort = Sort.by(orders);
        }

        return PageRequest.of(pageNum, pageSize, sort);
    }

    public static Pageable createPageable(Integer pageSize, Integer pageNum) {
        return createPageable(pageSize, pageNum, null, null);
    }

    public static Pageable createPageable(Integer pageSize, Integer pageNum, SortParam sort) {
        return sort == null
            ? createPageable(pageSize, pageNum)
            : createPageable(pageSize, pageNum, sort.getFields(), sort.getOrders());
    }

    public static Pageable createPageable(PageReqData pageReqData) {
        return pageReqData == null
            ? createPageable(null, null)
            : createPageable(pageReqData.getPageSize(), pageReqData.getPageNum(), pageReqData.getSort());
    }

}
