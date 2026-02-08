<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerWeightCollect" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerWeightCollectMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerWeightCollect" %>
<%@ page import="com.joainfo.gasmax.biz.BizAppUser" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.Map" %>
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

    private int toIntSafe(String value) {
        if (value == null) return 0;
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
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
        String customerCode = request.getParameter("customerCode");
        String startDate = StringUtil.stringReplace(request.getParameter("startDate"));
        String endDate = StringUtil.stringReplace(request.getParameter("endDate"));
        String uuid = request.getParameter("uuid");
        if (uuid == null) uuid = "";

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
        String areaCode = appUser.getAreaCode();

        CustomerWeightCollectMap customerWeightCollects = BizCustomerWeightCollect.getInstance().getCustomerWeightCollects(
            serverIp, catalogName, areaCode, customerCode, startDate, endDate
        );

        if (customerWeightCollects == null) {
            out.write("{\"session\":\"O\",\"totalRowCount\":0,\"carriedOverAmount\":\"0\",\"items\":[]}");
            return;
        }

        LinkedHashMap<String, CustomerWeightCollect> innerMap = customerWeightCollects.getCustomerWeightCollects();
        if (innerMap == null || innerMap.isEmpty()) {
            out.write("{\"session\":\"O\",\"totalRowCount\":0,\"carriedOverAmount\":\"" + jsonEscape(customerWeightCollects.getCarriedOverAmount()) + "\",\"items\":[]}");
            return;
        }

        int totalRowCount = innerMap.size();
        int remainAmount = toIntSafe(customerWeightCollects.getCarriedOverAmount());

        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"session\":\"O\",");
        json.append("\"totalRowCount\":").append(totalRowCount).append(",");
        json.append("\"carriedOverAmount\":\"").append(jsonEscape(customerWeightCollects.getCarriedOverAmount())).append("\",");
        json.append("\"items\":[");

        int index = 0;
        for (Map.Entry<String, CustomerWeightCollect> entry : innerMap.entrySet()) {
            CustomerWeightCollect item = entry.getValue();
            int totalAmount = toIntSafe(item.getTotalAmount());
            if ("5".equals(item.getTypeCode())) {
                totalAmount = 0;
            }
            int collectAmount = toIntSafe(item.getCollectAmount());
            int discountAmount = toIntSafe(item.getDiscountAmount());
            remainAmount = remainAmount + totalAmount - collectAmount - discountAmount;

            json.append("{");
            appendJsonField(json, "areaCode", item.getAreaCode(), true);
            appendJsonField(json, "customerCode", item.getCustomerCode(), true);
            appendJsonField(json, "typeCode", item.getTypeCode(), true);
            appendJsonField(json, "collectDate", item.getCollectDate(), true);
            appendJsonField(json, "sequenceNumber", item.getSequenceNumber(), true);
            appendJsonField(json, "itemCode", item.getItemCode(), true);
            appendJsonField(json, "itemName", item.getItemName(), true);
            appendJsonField(json, "saleQuantity", item.getSaleQuantity(), true);
            appendJsonField(json, "withdrawQuantity", item.getWithdrawQuantity(), true);
            appendJsonField(json, "price", item.getPrice(), true);
            appendJsonField(json, "amount", item.getAmount(), true);
            appendJsonField(json, "tax", item.getTax(), true);
            appendJsonField(json, "totalAmount", item.getTotalAmount(), true);
            appendJsonField(json, "collectAmount", item.getCollectAmount(), true);
            appendJsonField(json, "discountAmount", item.getDiscountAmount(), true);
            appendJsonField(json, "unpaidAmount", item.getUnpaidAmount(), true);
            appendJsonField(json, "employeeCode", item.getEmployeeCode(), true);
            appendJsonField(json, "employeeName", item.getEmployeeName(), true);
            appendJsonField(json, "remark", item.getRemark(), true);
            appendJsonField(json, "collectType", item.getCollectType(), true);
            appendJsonField(json, "equipmentInOutSequenceNumber", item.getEquipmentInOutSequenceNumber(), true);
            appendJsonField(json, "unpaidSequenceNumber", item.getUnpaidSequenceNumber(), true);
            appendJsonField(json, "remainAmount", Integer.toString(remainAmount), false);
            json.append("}");
            if (index < totalRowCount - 1) json.append(",");
            index++;
        }

        json.append("]}");
        out.write(json.toString());
    } catch (Exception e) {
        System.out.println("❌ Error in customer_book_weight_collect_search_json.jsp: " + e.getMessage());
        e.printStackTrace();
        out.write("{\"session\":\"O\",\"error\":\"" + jsonEscape(String.valueOf(e.getMessage())) + "\"}");
    }
%>
