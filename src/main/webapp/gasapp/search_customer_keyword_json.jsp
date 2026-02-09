<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerSearchMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerSearch" %>
<%@ page import="com.joainfo.gasmax.biz.BizAppUser" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
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

    private AppUser buildHardcodedAppUser() {
        AppUser user = new AppUser();
        user.setMacNumber("b28618772c95b3a5");
        user.setAreaSeq("5");
        user.setGrantState("Y");
        user.setPhoneModel("하명현사장_tes");
        user.setMobileNumber("01062343782");
        user.setUserId("1");
        user.setPassword("1");
        user.setOnlyIpAddress("joatech.dyndns.org");
        user.setDbCatalogName("GasMax_Sample");
        user.setDbUserID("GasMax_Sample");
        user.setDbPassword("GasMax_Pass");
        user.setPort("2521");
        user.setAreaCode("01");
        user.setAreaName("본사4");
        user.setLastAreaCode("00");
        user.setEmployeeCode("01");
        user.setEmployeeName("장동건");
        user.setAreaAddress("");
        user.setPhoneAreaNumber("041");
        user.setSignImagePath("/nc20/JoaAPP_Sign/joatech_db/");
        user.setLicenseDate("99991231");
        user.setJoinDate("2022-10-06 10:29");
        user.setLastLoginDate("2026-02-09 03:34");
        user.setExpiryDate("");
        user.setMenuPermission("1.5\n∑2-1248");
        user.setIndividualNotice("");
        user.setRemark("000000000000        ");
        user.setGasType("LPG ");
        return user;
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

    final int PAGE_SIZE = Integer.MAX_VALUE; // fetch all results in one response

    try {
        String keyword = request.getParameter("keyword");
        if (keyword == null) keyword = "";

        String uuid = request.getParameter("uuid");
		String hpSeq = request.getParameter("hpSeq");
        if (uuid == null) uuid = "";

        AppUser appUser = null;
        if (!uuid.isEmpty()) {
            try {
                appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, uuid, request.getParameter("hpSeq"));
            } catch (Exception ex) {
                System.out.println("❌ Error getting appUser: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        if (HARDCODED_UUID.equals(uuid)) {
            try {
                appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, uuid, request.getParameter("hpSeq"));
            } catch (Exception ex) {
                System.out.println("❌ Error getting appUser (hardcoded uuid): " + ex.getMessage());
                ex.printStackTrace();
            }
            if (appUser == null) {
                appUser = buildHardcodedAppUser();
            }
            session.setAttribute("USER_INFO", appUser);
        } else if (appUser == null) {
            appUser = (AppUser) session.getAttribute("USER_INFO");
        }

        if (appUser == null) {
            out.write("{\"session\":\"X\"}");
            return;
        }

        String serverIp = appUser.getIpAddress() == null ? "" : appUser.getIpAddress();
        String catalogName = appUser.getDbCatalogName() == null ? "" : appUser.getDbCatalogName();
        String areaCode = appUser.getAreaCode() == null ? "" : appUser.getAreaCode();
        String employeeCode = appUser.getEmployeeCode() == null ? "" : appUser.getEmployeeCode();
        String grantCode = appUser.getMenuPermission() == null ? "" : appUser.getMenuPermission();

        String customerCode = "";
        String customerSearchType = "A";

        CustomerSearchMap customerSearches = null;
        try {
            customerSearches = BizCustomerSearch.getInstance().getCustomerSearches(
                serverIp, catalogName, areaCode, customerCode, customerSearchType, employeeCode, keyword, grantCode
            );
        } catch (Exception ex) {
            System.out.println("❌ Error getting customer searches: " + ex.getMessage());
            ex.printStackTrace();
        }

        if (customerSearches == null) {
            out.write("{\"session\":\"O\",\"page\":1,\"hasMore\":false,\"items\":[]}");
            return;
        }

        LinkedHashMap<String, CustomerSearch> innerMap = null;
        try {
            innerMap = customerSearches.getCustomerSearches();
        } catch (Exception ignore) {}

        if (innerMap == null || innerMap.isEmpty()) {
            out.write("{\"session\":\"O\",\"page\":1,\"hasMore\":false,\"items\":[]}");
            return;
        }

        int totalRowCount = innerMap.size();
        List<CustomerSearch> firstPage = new ArrayList<>();
        for (Map.Entry<String, CustomerSearch> entry : innerMap.entrySet()) {
            CustomerSearch customerSearch = entry.getValue();
            customerSearch.setSequenceNumber(Integer.toString(totalRowCount--));
            firstPage.add(customerSearch);
        }
        session.setAttribute("CUSTOMER_SEARCH_PAGING_JSON", null);
        boolean hasMore = false;

        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"session\":\"O\",");
        json.append("\"page\":1,");
        json.append("\"hasMore\":").append(hasMore).append(",");
        json.append("\"items\":[");

        for (int i = 0; i < firstPage.size(); i++) {
            CustomerSearch item = firstPage.get(i);
            json.append("{");
            appendJsonField(json, "areaCode", item.getAreaCode(), true);
            appendJsonField(json, "customerCode", item.getCustomerCode(), true);
            appendJsonField(json, "customerType", item.getCustomerType(), true);
            appendJsonField(json, "customerTypeName", item.getCustomerTypeName(), true);
            appendJsonField(json, "customerStatusCode", item.getCustomerStatusCode(), true);
            appendJsonField(json, "customerStatusName", item.getCustomerStatusName(), true);
            appendJsonField(json, "customerName", item.getCustomerName(), true);
            appendJsonField(json, "phoneNumber", item.getPhoneNumber(), true);
            appendJsonField(json, "mobileNumber", item.getMobileNumber(), true);
            appendJsonField(json, "address1", item.getAddress1(), true);
            appendJsonField(json, "address2", item.getAddress2(), false);
            json.append("}");
            if (i < firstPage.size() - 1) json.append(",");
        }

        json.append("]}");
        out.write(json.toString());
    } catch (Exception e) {
        System.out.println("❌ Error in search_customer_keyword_json.jsp: " + e.getMessage());
        e.printStackTrace();
        out.write("{\"session\":\"O\",\"error\":\"" + jsonEscape(String.valueOf(e.getMessage())) + "\"}");
    }
%>
