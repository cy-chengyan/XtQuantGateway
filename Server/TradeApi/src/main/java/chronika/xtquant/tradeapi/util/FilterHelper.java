package chronika.xtquant.tradeapi.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.ContentCachingResponseWrapper;

public class FilterHelper {

    public static MutableHttpServletRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof MutableHttpServletRequestWrapper) {
            return (MutableHttpServletRequestWrapper) request;
        } else {
            return new MutableHttpServletRequestWrapper(request);
        }
    }

    public static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }

}
