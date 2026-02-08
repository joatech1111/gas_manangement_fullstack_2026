<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="java.util.List" %>
<%!
    private String jsonEscape(String value) {
        if (value == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '\\': sb.append("\\\\"); break;
                case '"': sb.append("\\\""); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    private void appendJsonField(StringBuilder sb, String name, String value, boolean trailingComma) {
        sb.append("\"").append(name).append("\":\"").append(jsonEscape(value)).append("\"");
        if (trailingComma) sb.append(",");
    }
%>
<%
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=UTF-8");
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    if ("HTTP/1.1".equals(request.getProtocol())) {
        response.setHeader("Cache-Control", "no-cache");
    }
    request.setCharacterEncoding("UTF-8");

    try { out.clearBuffer(); } catch (Exception ignore) {}

    try {
        String pageNumberParam = request.getParameter("pageNumber");
        int pageNumber = 1;
        try {
            pageNumber = Integer.parseInt(pageNumberParam);
        } catch (Exception ignore) {
            pageNumber = 1;
        }

        @SuppressWarnings("unchecked")
        List<List<CustomerSearch>> pages = (List<List<CustomerSearch>>) session.getAttribute("CUSTOMER_SEARCH_PAGING_JSON");

        if (pages == null) {
            out.write("{\"session\":\"X\"}");
            return;
        }

        if (pageNumber < 1 || pageNumber > pages.size()) {
            out.write("{\"session\":\"O\",\"page\":" + pageNumber + ",\"hasMore\":false,\"items\":[]}");
            return;
        }

        List<CustomerSearch> page = pages.get(pageNumber - 1);
        boolean hasMore = pageNumber < pages.size();

        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"session\":\"O\",");
        json.append("\"page\":").append(pageNumber).append(",");
        json.append("\"hasMore\":").append(hasMore).append(",");
        json.append("\"items\":[");

        for (int i = 0; i < page.size(); i++) {
            CustomerSearch item = page.get(i);
            json.append("{");
            appendJsonField(json, "areaCode", item.getAreaCode(), true);
            appendJsonField(json, "customerCode", item.getCustomerCode(), true);
            appendJsonField(json, "customerType", item.getCustomerType(), true);
            appendJsonField(json, "customerStatusCode", item.getCustomerStatusCode(), true);
            appendJsonField(json, "customerStatusName", item.getCustomerStatusName(), true);
            appendJsonField(json, "customerName", item.getCustomerName(), true);
            appendJsonField(json, "phoneNumber", item.getPhoneNumber(), true);
            appendJsonField(json, "mobileNumber", item.getMobileNumber(), true);
            appendJsonField(json, "address1", item.getAddress1(), true);
            appendJsonField(json, "address2", item.getAddress2(), false);
            json.append("}");
            if (i < page.size() - 1) json.append(",");
        }

        json.append("]}");
        out.write(json.toString());
    } catch (Exception e) {
        System.out.println("❌ Error in search_customer_paging_json.jsp: " + e.getMessage());
        e.printStackTrace();
        out.write("{\"session\":\"O\",\"error\":\"" + jsonEscape(String.valueOf(e.getMessage())) + "\"}");
    }
%>
