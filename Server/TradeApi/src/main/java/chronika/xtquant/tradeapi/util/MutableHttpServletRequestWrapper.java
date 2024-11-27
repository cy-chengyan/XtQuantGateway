package chronika.xtquant.tradeapi.util;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

// 使得 HttpServletRequest 可以：
// 1. 可以添加自定义的 header;
// 2. 可多次 getInputStream;
// 3. 对于 multipart/form-data 类型的请求，只有内容被有效消费后 getInputStream 才会返回有效的内容
final public class MutableHttpServletRequestWrapper extends ContentCachingRequestWrapper {

    // holds custom header and value mapping
    private final Map<String, String> customHeaders;

    // holds input stream
    private byte[] buffer;

    // holds input stream flag
    private boolean bufferFlag = false;

    public MutableHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.customHeaders = new HashMap<>();
        try {
            // 对于不是 multipart/form-data 类型的请求, 通过 buffer 实现多次 getInputStream
            if (!RequestHelper.isMultiPartMediaTypeRequest(request)) {
                this.buffer = StreamUtils.copyToByteArray(request.getInputStream());
                this.bufferFlag = true;
            } else { // 对于 multipart/form-data 类型的请求, 通过 ContentCachingRequestWrapper 去处理
                this.buffer = new byte[0];
            }
        } catch (Exception e) {
            this.buffer = new byte[0];
        }
    }

    public void putHeader(String name, String value) {
        this.customHeaders.put(name, value);
    }

    public String getHeader(String name) {
        // check the custom headers first
        String headerValue = customHeaders.get(name);

        if (headerValue != null) {
            return headerValue;
        }
        // else return from into the original wrapped object
        return ((HttpServletRequest) getRequest()).getHeader(name);
    }

    public Enumeration<String> getHeaderNames() {
        // create a set of the custom header names
        Set<String> set = new HashSet<>();

        // now add the headers from the wrapped request object
        Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (e.hasMoreElements()) {
            // add the names of the request headers into the list
            String n = e.nextElement();
            set.add(n);
        }

        // add the custom headers into the list
        set.addAll(customHeaders.keySet());

        // create an enumeration from the set and return
        return Collections.enumeration(set);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public ServletInputStream getInputStream() throws IOException {
        if (!bufferFlag) {
            return super.getInputStream();
        }

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public byte[] getContentAsByteArray() {
        if (!bufferFlag) {
            return super.getContentAsByteArray();
        } else {
            return buffer;
        }
    }

}
