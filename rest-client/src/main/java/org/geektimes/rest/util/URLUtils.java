package org.geektimes.rest.util;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import static java.lang.String.valueOf;
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * TODO
 *
 * @author C.HD
 * @since 2021/4/7.
 */
public interface URLUtils {

    String DEFAULT_ENCODING = System.getProperty("org.geektimes.url.encoding", "UTF-8");

    String AND = "&";

    String EQUAL = "=";

    String TEMPLATE_VARIABLE_START = "{";

    String TEMPLATE_VARIABLE_END = "}";

    static String encode(String content) {
        return encode(content, DEFAULT_ENCODING);
    }

    static String encode(String content, String encoding) {
        String encodedContent = null;
        try {
            encodedContent = URLEncoder.encode(content, encoding);
        } catch (UnsupportedEncodingException | NullPointerException e) {
            throw new IllegalArgumentException(e);
        }
        return encodedContent;
    }

    static Map<String, Object> toTemplateVariables(String template, Object... values) {
        if (isBlank(template)) {
            return null;
        }

        int start = 0;
        int end = 0;

        int index = 0;

        final int length = values == null ? 0 : values.length;

        Map<String, Object> templateVariables = new LinkedHashMap<>();

        for (; ; ) {

            start = template.indexOf(TEMPLATE_VARIABLE_START, end);
            end = template.indexOf(TEMPLATE_VARIABLE_END, start);

            if (start == -1 || end == -1) {
                break;
            }

            String variableName = template.substring(start + 1, end);

            if (!templateVariables.containsKey(variableName)) {

                Object variableValue = index < length ? values[index++] : null;

                templateVariables.put(variableName, variableValue);
            }
        }

        return unmodifiableMap(templateVariables);
    }

    static String resolveVariables(String template, Object[] templateValues, boolean encoded) {
        return resolveVariables(template, toTemplateVariables(template, templateValues), encoded);
    }

    static String resolveVariables(String template, Map<String, ?> templateValues, boolean encoded) {
        if (isBlank(template)) {
            return null;
        }

        if (templateValues == null || templateValues.isEmpty()) {
            return template;
        }

        StringBuilder resolvedTemplate = new StringBuilder(template);
        int start = 0;
        int end = 0;
        for (; ; ) {
            start = resolvedTemplate.indexOf(TEMPLATE_VARIABLE_START, start);
            end = resolvedTemplate.indexOf(TEMPLATE_VARIABLE_END, end);

            if (start == -1 || end == -1) {
                break;
            }

            String variableName = resolvedTemplate.substring(start + 1, end);
            Object value = templateValues.get(variableName);
            if (value == null) { // variable not found, go to next
                continue;
            }

            String variableValue = valueOf(value);
            if (encoded) {
                variableValue = encode(variableValue);
            }

            resolvedTemplate.replace(start, end + 1, variableValue);
        }

        return resolvedTemplate.toString();
    }

    static Map<String, List<String>> resolveParameters(String queryString) {
        if (isBlank(queryString)) {
            return emptyMap();
        }

        Map<String, List<String>> parametersMap = new LinkedHashMap<>();
        String[] queryParams = StringUtils.split(queryString, AND);
        if (queryParams == null) {
            return unmodifiableMap(parametersMap);
        }

        for (String queryParam : queryParams) {
            String[] paramNameAndValue = StringUtils.split(queryParam, EQUAL);
            if (paramNameAndValue.length > 0) {
                String paramName = paramNameAndValue[0];
                String paramValue = paramNameAndValue.length > 1 ? paramNameAndValue[1] : StringUtils.EMPTY;
                List<String> paramValueList = parametersMap.get(paramName);
                if (paramValueList == null) {
                    paramValueList = new LinkedList<>();
                    parametersMap.put(paramName, paramValueList);
                }

                paramValueList.add(paramValue);
            }
        }
        return parametersMap;
    }

    static String toQueryString(Map<String, List<String>> queryParams) {
        if (queryParams == null || queryParams.isEmpty()) {
            return null;
        }

        StringBuilder queryStringBuffer = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
            String paramName = entry.getKey();
            for (String paramValue : entry.getValue()) {
                queryStringBuffer.append(paramName).append(EQUAL).append(AND);
            }
        }
        return queryStringBuffer.substring(0, queryStringBuffer.length() - 1);
    }

}
