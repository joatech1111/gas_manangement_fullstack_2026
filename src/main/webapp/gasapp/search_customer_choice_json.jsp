<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerSearchMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerSearch" %>
<%@ page import="com.joainfo.gasmax.biz.BizAppUser" %>
<%!
    private static final String HARDCODED_UUID = "b28618772c95b3a5";

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
        String areaCode = request.getParameter("areaCode");
        String customerCode = request.getParameter("customerCode");
        String uuid = request.getParameter("uuid");
        if (uuid == null) uuid = "";

        CustomerSearch customerSearch = null;
        CustomerSearchMap customerSearches = (CustomerSearchMap) session.getAttribute("CUSTOMER_SEARCH");
        if (customerSearches != null && areaCode != null && customerCode != null) {
            customerSearch = customerSearches.getCustomerSearch(customerSearches.getKey(areaCode, customerCode));
        }

        if (customerSearch == null) {
            AppUser appUser = null;
            if (!uuid.isEmpty()) {
                try {
                    appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, uuid, uuid);
                } catch (Exception ex) {
                    System.out.println("❌ Error getting appUser: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }

            if (appUser == null) {
                appUser = (AppUser) session.getAttribute("USER_INFO");
            }

            if (appUser == null) {
                out.write("{\"session\":\"X\"}");
                return;
            }

            String serverIp = appUser.getIpAddress();
            String catalogName = appUser.getDbCatalogName();
            String keyword = "";
            String customerSearchType = "A";
            String employeeCode = appUser.getEmployeeCode();
            String grantCode = appUser.getMenuPermission();

            customerSearches = BizCustomerSearch.getInstance().getCustomerSearches(
                serverIp, catalogName, areaCode, customerCode, customerSearchType, employeeCode, keyword, grantCode
            );

            if (customerSearches != null) {
                customerSearch = customerSearches.getCustomerSearch(customerSearches.getKey(areaCode, customerCode));
            }
        }

        if (customerSearch == null) {
            out.write("{\"session\":\"O\",\"item\":null}");
            return;
        }

        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"session\":\"O\",");
        json.append("\"item\":{");
        appendJsonField(json, "areaCode", customerSearch.getAreaCode(), true);
        appendJsonField(json, "customerCode", customerSearch.getCustomerCode(), true);
        appendJsonField(json, "customerType", customerSearch.getCustomerType(), true);
        appendJsonField(json, "customerTypeName", customerSearch.getCustomerTypeName(), true);
        appendJsonField(json, "customerStatusCode", customerSearch.getCustomerStatusCode(), true);
        appendJsonField(json, "customerStatusName", customerSearch.getCustomerStatusName(), true);
        appendJsonField(json, "customerName", customerSearch.getCustomerName(), true);
        appendJsonField(json, "buildingName", customerSearch.getBuildingName(), true);
        appendJsonField(json, "userName", customerSearch.getUserName(), true);
        appendJsonField(json, "phoneNumber", customerSearch.getPhoneNumber(), true);
        appendJsonField(json, "mobileNumber", customerSearch.getMobileNumber(), true);
        appendJsonField(json, "address1", customerSearch.getAddress1(), true);
        appendJsonField(json, "address2", customerSearch.getAddress2(), false);
        json.append("}}");

        out.write(json.toString());
    } catch (Exception e) {
        System.out.println("❌ Error in search_customer_choice_json.jsp: " + e.getMessage());
        e.printStackTrace();
        out.write("{\"session\":\"O\",\"error\":\"" + jsonEscape(String.valueOf(e.getMessage())) + "\"}");
    }
%>
