package chronika.xtquant.tradeapi.util;

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

}
