package com.aacoin.dig;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.message.internal.HeaderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 记录rest接口日志
 */
@Provider
@PreMatching
public class JerseyLogFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger log = LoggerFactory.getLogger(JerseyLogFilter.class);

    private static final String NOTIFICATION_PREFIX = "* ";
    private static final String SERVER_REQUEST = "> ";
    private static final String SERVER_RESPONSE = "< ";
    private static final String CLIENT_REQUEST = "/ ";
    private static final String CLIENT_RESPONSE = "\\ ";
    private static final AtomicLong logSequence = new AtomicLong(0);

    @Context
    private HttpServletRequest httpServletRequest;

    /**
     * 请求拦截
     *
     * @param requestContext
     * @throws IOException
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            return;
        }
//        log.info(requestContext.getUriInfo().getRequestUri().toASCIIString());

//        String value = requestContext.getHeaderString("content-type");
//        log.info(value);

        //跳过swagger请求
        if ("swagger.json".endsWith(requestContext.getUriInfo().getPath())) {
            return;
        }

        try {
            String id = Utils.generateUUID();
            requestContext.setProperty("log-id", id);

            long sequenceId = logSequence.incrementAndGet();
            StringBuilder b = new StringBuilder();
            StringBuilder requestHeader = new StringBuilder();
            StringBuilder requestContent = new StringBuilder();
            printPrefixedHeaders(SERVER_REQUEST, requestHeader, sequenceId, requestContext.getHeaders());
            printPrefixedRequestBody(SERVER_REQUEST, requestContent, sequenceId, requestContext);
//            log.info(b.toString());
//            log.info(requestContent.toString());
//
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 返回拦截
     *
     * @param requestContext
     * @param responseContext
     * @throws IOException
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            return;
        }

        //跳过swagger请求
        if ("swagger.json".endsWith(requestContext.getUriInfo().getPath())) {
            return;
        }

        try {
            long sequenceId = logSequence.incrementAndGet();
            StringBuilder b = new StringBuilder();
            StringBuilder responseHeader = new StringBuilder();
            StringBuilder responseContent = new StringBuilder();
            printPrefixedHeaders(SERVER_RESPONSE, responseHeader, sequenceId, HeaderUtils.asStringHeaders(responseContext.getHeaders()));
            printPrefixedResponseBody(SERVER_RESPONSE, responseContent, sequenceId, responseContext);
//            log.info(b.toString());
//            log.info(responseContent.toString());
//            rabbitMQHelper.convertAndSend("log-exchange", "log-response-routingKey", apiLogResponseEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private StringBuilder prefixId(StringBuilder b, long id) {
        b.append(Long.toString(id)).append(" ");
        return b;
    }

    /**
     * 请求地址
     *
     * @param prefix
     * @param b
     * @param id
     * @param method
     * @param uri
     */
    private void printRequestLine(final String prefix, StringBuilder b, long id, String method, URI uri) {
//        prefixId(b, id).append(NOTIFICATION_PREFIX)
//                .append("AirLog - Request received on thread ")
//                .append(Thread.currentThread().getName()).append("\n");
//        prefixId(b, id).append(prefix).append(method).append(" ").append(uri.toASCIIString()).append("\n");

        List<String> line = new ArrayList();
//        line.add("AirLog - Request received on thread");
//        line.add(Thread.currentThread().getName());
//        line.add(method);
        line.add(uri.toASCIIString());
        b.append(line);
    }

    /**
     * 返回路径
     *
     * @param prefix
     * @param b
     * @param id
     * @param status
     */
    private void printResponseLine(final String prefix, StringBuilder b, long id, int status) {
//        prefixId(b, id).append(NOTIFICATION_PREFIX)
//                .append("AirLog - Response received on thread ")
//                .append(Thread.currentThread().getName()).append("\n");
//        prefixId(b, id).append(prefix).append(Integer.toString(status)).append("\n");

        b.append(Integer.toString(status));
    }

    /**
     * 请求头信息
     *
     * @param prefix
     * @param b
     * @param id
     * @param headers
     */
    private void printPrefixedHeaders(final String prefix, StringBuilder b, long id, MultivaluedMap<String, String> headers) {
        List<String> line = new ArrayList();
        for (Map.Entry<String, List<String>> e : headers.entrySet()) {
            List<?> val = e.getValue();
            String header = e.getKey();

            if (val.size() == 1) {
//                prefixId(b, id).append(prefix).append(header).append(": ").append(val.get(0)).append("\n");
                line.add(header + ": " + val.get(0));
//                b.append(header+": "+val.get(0));
            } else {
                StringBuilder sb = new StringBuilder();
                boolean add = false;
                for (Object s : val) {
                    if (add) {
                        sb.append(',');
                    }
                    add = true;
                    sb.append(s);
                }
//                prefixId(b, id).append(prefix).append(header).append(": ").append(sb.toString()).append("\n");
                line.add(header + ": " + sb.toString());
//                b.append(header+": "+sb.toString());
            }
        }
        b.append(line);
    }

    /**
     * d
     * 获取请求参数
     *
     * @param prefix
     * @param b
     * @param id
     * @param requestContext
     */
    private void printPrefixedRequestBody(final String prefix, StringBuilder b, long id, ContainerRequestContext requestContext) {
        //判断是否为上传操作
        String value = requestContext.getHeaderString("content-type");
        if (StringUtils.isNotBlank(value) && value.contains("multipart/form-data")) {
            return;
        }

        try {
            HttpServletRequest requestWrapper = new ApiHttpServletRequestHelper(httpServletRequest);
            String data = IOReaderHelper.ioReader(requestWrapper);
            requestContext.setEntityStream(requestWrapper.getInputStream());
            if (StringUtils.isNotBlank(data)) {
//                prefixId(b, id).append(prefix).append("body").append(": ").append("\n");
//                prefixId(b, id).append(prefix).append(data).append("\n");
                b.append(data);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 获取返回参数
     *
     * @param prefix
     * @param b
     * @param id
     * @param responseContext
     */
    private void printPrefixedResponseBody(final String prefix, StringBuilder b, long id, ContainerResponseContext responseContext) {
        Object obj = responseContext.getEntity();
        if (null != obj) {
//            prefixId(b, id).append(prefix).append("body").append(": ").append("\n");
//            prefixId(b, id).append(prefix).append(JSON.toJSON(obj)).append("\n");
            b.append(JSON.toJSON(obj));

//            b.append(obj);
        }
    }

}
