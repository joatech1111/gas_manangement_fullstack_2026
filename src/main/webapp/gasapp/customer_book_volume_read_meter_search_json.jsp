<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerVolumeReadMeter" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerVolumeReadMeterMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerVolumeReadMeter" %>
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

        CustomerVolumeReadMeterMap meters = BizCustomerVolumeReadMeter.getInstance().getCustomerVolumeReadMeters(
            serverIp, catalogName, areaCode, customerCode, startDate, endDate
        );

        if (meters == null) {
            out.write("{\"session\":\"O\",\"totalRowCount\":0,\"totalNowMonthAmount\":\"0\",\"items\":[]}");
            return;
        }

        LinkedHashMap<String, CustomerVolumeReadMeter> innerMap = meters.getCustomerVolumeReadMeters();
        if (innerMap == null || innerMap.isEmpty()) {
            out.write("{\"session\":\"O\",\"totalRowCount\":0,\"totalNowMonthAmount\":\"0\",\"items\":[]}");
            return;
        }

        int totalRowCount = innerMap.size();
        int totalNowMonthAmount = 0;

        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"session\":\"O\",");
        json.append("\"totalRowCount\":").append(totalRowCount).append(",");
        json.append("\"totalNowMonthAmount\":\"");

        StringBuilder items = new StringBuilder();
        int index = 0;
        for (Map.Entry<String, CustomerVolumeReadMeter> entry : innerMap.entrySet()) {
            CustomerVolumeReadMeter item = entry.getValue();
            totalNowMonthAmount += toIntSafe(item.getNowMonthAmount());

            items.append("{");
            appendJsonField(items, "areaCode", item.getAreaCode(), true);
            appendJsonField(items, "customerCode", item.getCustomerCode(), true);
            appendJsonField(items, "sequenceNumber", item.getSequenceNumber(), true);
            appendJsonField(items, "readMeterDate", item.getReadMeterDate(), true);
            appendJsonField(items, "preMonthReadMeter", item.getPreMonthReadMeter(), true);
            appendJsonField(items, "nowMonthReadMeter", item.getNowMonthReadMeter(), true);
            appendJsonField(items, "useQuantity", item.getUseQuantity(), true);
            appendJsonField(items, "price", item.getPrice(), true);
            appendJsonField(items, "useAmount", item.getUseAmount(), true);
            appendJsonField(items, "manageAmount", item.getManageAmount(), true);
            appendJsonField(items, "discountAmount", item.getDiscountAmount(), true);
            appendJsonField(items, "delayAmount", item.getDelayAmount(), true);
            appendJsonField(items, "nowMonthAmount", item.getNowMonthAmount(), true);
            appendJsonField(items, "remark", item.getRemark(), true);
            appendJsonField(items, "remainQuantity", item.getRemainQuantity(), true);
            appendJsonField(items, "collectDate", item.getCollectDate(), true);
            appendJsonField(items, "unpaidAmount", item.getUnpaidAmount(), false);
            items.append("}");
            if (index < totalRowCount - 1) items.append(",");
            index++;
        }

        json.append(jsonEscape(Integer.toString(totalNowMonthAmount))).append("\",");
        json.append("\"items\":[").append(items.toString()).append("]}");

        out.write(json.toString());
    } catch (Exception e) {
        System.out.println("❌ Error in customer_book_volume_read_meter_search_json.jsp: " + e.getMessage());
        e.printStackTrace();
        out.write("{\"session\":\"O\",\"error\":\"" + jsonEscape(String.valueOf(e.getMessage())) + "\"}");
    }
%>
