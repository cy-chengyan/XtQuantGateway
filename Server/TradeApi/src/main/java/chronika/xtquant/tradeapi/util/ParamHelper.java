package chronika.xtquant.tradeapi.util;

import chronika.xtquant.common.infra.enums.CkError;
import chronika.xtquant.common.infra.exception.CkException;
import chronika.xtquant.tradeapi.model.CkApiRequest;
import org.springframework.web.multipart.MultipartFile;

public class ParamHelper {

    public static String getFileName(MultipartFile file) {
        return file.getOriginalFilename();
    }

    public static String getFileContentType(MultipartFile file) {
        return file.getContentType();
    }

    public static byte[] getFileBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static  <T> T getReqDataSafely(CkApiRequest<T> request) {
        T ret = request.getData();
        if (ret == null) {
            throw new CkException(CkError.MISSING_PARAM, "Missing request 'data' field");
        }
        return ret;
    }

}
