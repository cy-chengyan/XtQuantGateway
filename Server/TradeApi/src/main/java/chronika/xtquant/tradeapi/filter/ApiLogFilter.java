package chronika.xtquant.tradeapi.filter;

import chronika.xtquant.tradeapi.util.FilterHelper;
import chronika.xtquant.tradeapi.util.MutableHttpServletRequestWrapper;
import chronika.xtquant.tradeapi.util.RequestHelper;
import chronika.xtquant.common.infra.param.RestApiLog;
import chronika.xtquant.common.infra.util.JsonUtil;
import com.google.common.collect.Sets;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;


public class ApiLogFilter extends OncePerRequestFilter {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ApiLogFilter.class);
    private static final String tz = TimeZone.getDefault().getID();
    private static final Set<String> SENSITIVE_HEADERS = Sets.newHashSet(
            "authorization"
    );

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(FilterHelper.wrapRequest(request), FilterHelper.wrapResponse(response), filterChain);
        }
    }

    protected void doFilterWrapped(MutableHttpServletRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws ServletException, IOException {
        RestApiLog restApiLog = new RestApiLog();
        Long startTs = System.currentTimeMillis();
        try {
            beforeRequest(request, restApiLog);
            filterChain.doFilter(request, response);
        } finally {
            afterRequest(request, response, restApiLog);
            response.copyBodyToResponse();

            Long endTs = System.currentTimeMillis();
            restApiLog.setReqAt(startTs);
            restApiLog.setResAt(endTs);
            restApiLog.setSpend(endTs - startTs);
            restApiLog.setTz(tz);
            log.info("[API]{}", JsonUtil.toJsonString(restApiLog));
        }
    }

    protected void beforeRequest(MutableHttpServletRequestWrapper request, RestApiLog restApiLog) {
        logRequestHeader(request, restApiLog);
    }

    protected void afterRequest(MutableHttpServletRequestWrapper request, ContentCachingResponseWrapper response, RestApiLog restApiLog) {
        logRequestBody(request, restApiLog);
        logResponse(response, restApiLog);
    }

    private void logRequestHeader(MutableHttpServletRequestWrapper request, RestApiLog restApiLog) {
        String endpoint = request.getRequestURI();
        restApiLog.setEndpoint(endpoint);
        restApiLog.setMethod(request.getMethod());
        restApiLog.setReqContentType(request.getContentType());

        if (RequestHelper.isMultiPartMediaTypeRequest(request)) {
            Map<String, String[]> params = new HashMap<>(request.getParameterMap());
            MultipartResolver multipartResolver = new StandardServletMultipartResolver();
            if (multipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multipartRequest = multipartResolver.resolveMultipart(request);
                multipartRequest.getFileMap().values().forEach((value) -> params.put(value.getName(),
                        new String[]{
                                value.getContentType(),
                                value.getOriginalFilename(),
                                String.valueOf(value.getSize())
                        }));
                multipartRequest.getTrailerFields().forEach((key, value) -> params.put(key, new String[]{value}));
            }
            restApiLog.setReqParams(params);
        } else {
            restApiLog.setReqParams(request.getParameterMap());
        }

        Map<String, String> header = new HashMap<>();
        Collections.list(request.getHeaderNames())
                .forEach(headerName ->
                        Collections.list(request.getHeaders(headerName))
                                .forEach(headerValue -> {
                                    if (isSensitiveHeader(headerName)) {
                                        header.put(headerName, "*");
                                    } else {
                                        header.put(headerName, headerValue);
                                    }
                                }));
        restApiLog.setReqHeader(header);
    }

    private void logRequestBody(MutableHttpServletRequestWrapper request, RestApiLog restApiLog) {
        byte[] content = request.getContentAsByteArray();
        restApiLog.setReqSize(content.length);
        restApiLog.setReqBody(parseContent(content, request.getContentType(), request.getCharacterEncoding()));
    }

    private void logResponse(ContentCachingResponseWrapper response, RestApiLog restApiLog) {
        int status = response.getStatus();
        restApiLog.setResStatus(status);
        restApiLog.setResSize(response.getContentSize());
        Map<String, String> header = new HashMap<>();
        response.getHeaderNames().forEach(headerName ->
                response.getHeaders(headerName).forEach(headerValue -> {
                    if (isSensitiveHeader(headerName)) {
                        header.put(headerName, "*");
                    } else {
                        header.put(headerName, headerValue);
                    }
                }));
        restApiLog.setResHeader(header);

        restApiLog.setResContentType(response.getContentType());
        byte[] content = response.getContentAsByteArray();
        restApiLog.setResBody(parseContent(content, response.getContentType(), response.getCharacterEncoding()));
    }

    private Map<String, Object> parseContent(byte[] content, String contentType, String contentEncoding) {
        if (content.length > 0) {
            if (isJsonMediaType(contentType)) {
                try {
                    String contentString = new String(content, contentEncoding);
                    return JsonUtil.jsonStringToMap(contentString);
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
            } else {
                Map<String, Object> ret = new HashMap<>();
                ret.put("desc", "content is not json");
                if (content.length > 2048) {
                    ret.put("content", "too large");
                } else {
                    ret.put("content", new String(content));
                }
                return ret;
            }
        } else {
            return null;
        }
    }

    private boolean isSensitiveHeader(String headerName) {
        return SENSITIVE_HEADERS.contains(headerName.toLowerCase());
    }

    private boolean isJsonMediaType(String contentType) {
        MediaType mediaType = MediaType.valueOf(contentType);
        return mediaType.equalsTypeAndSubtype(MediaType.APPLICATION_JSON);
    }

}
