package chronika.xtquant.tradeapi.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import java.util.Locale;

public class RequestHelper {

    private static final Locale defaultLocale = Locale.US;

    public static boolean isJsonMediaTypeRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (!StringUtils.hasLength(contentType)) {
            return false;
        }
        MediaType mediaType = MediaType.valueOf(contentType);
        return mediaType.equalsTypeAndSubtype(MediaType.APPLICATION_JSON);
    }

    public static boolean isMultiPartMediaTypeRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (!StringUtils.hasLength(contentType)) {
            return false;
        }
        MediaType mediaType = MediaType.valueOf(contentType);
        return mediaType.equalsTypeAndSubtype(MediaType.MULTIPART_FORM_DATA);
    }

}
