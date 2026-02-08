<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.lang.management.ManagementFactory" %>
<%@ page import="java.lang.management.RuntimeMXBean" %>
<!DOCTYPE html>
<html>
<head>
    <title>Server Status</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 24px; color: #222; }
        h1 { margin: 0 0 12px; font-size: 22px; }
        table { border-collapse: collapse; width: 100%; max-width: 820px; }
        td, th { border: 1px solid #ddd; padding: 8px; }
        th { background: #f5f5f5; text-align: left; }
        .ok { color: #0a7c2f; font-weight: bold; }
        .muted { color: #666; }
    </style>
</head>
<body>
<%
    RuntimeMXBean runtimeMxBean = null;
    long uptimeMs = -1L;
    try {
        runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        uptimeMs = runtimeMxBean.getUptime();
    } catch (Exception ignored) {
    }

    long uptimeSec = uptimeMs >= 0 ? (uptimeMs / 1000) : -1;
    long upDays = uptimeSec >= 0 ? (uptimeSec / 86400) : -1;
    long upHours = uptimeSec >= 0 ? ((uptimeSec % 86400) / 3600) : -1;
    long upMinutes = uptimeSec >= 0 ? ((uptimeSec % 3600) / 60) : -1;
    long upSeconds = uptimeSec >= 0 ? (uptimeSec % 60) : -1;
%>

<h1>Spring Server Status</h1>
<p class="ok">OK - Server is running</p>

<table>
    <tr>
        <th>Item</th>
        <th>Value</th>
    </tr>
    <tr>
        <td>Current Time</td>
        <td><%= new Date() %></td>
    </tr>
    <tr>
        <td>Context Path</td>
        <td><%= request.getContextPath() %></td>
    </tr>
    <tr>
        <td>Server Name</td>
        <td><%= request.getServerName() %></td>
    </tr>
    <tr>
        <td>Server Port</td>
        <td><%= request.getServerPort() %></td>
    </tr>
    <tr>
        <td>Session ID</td>
        <td><%= session.getId() %></td>
    </tr>
    <tr>
        <td>Java Version</td>
        <td><%= System.getProperty("java.version") %></td>
    </tr>
    <tr>
        <td>OS</td>
        <td><%= System.getProperty("os.name") %> <%= System.getProperty("os.version") %></td>
    </tr>
    <tr>
        <td>Uptime</td>
        <td>
            <% if (uptimeSec >= 0) { %>
                <%= upDays %>d <%= upHours %>h <%= upMinutes %>m <%= upSeconds %>s
            <% } else { %>
                <span class="muted">Unavailable</span>
            <% } %>
        </td>
    </tr>
</table>

</body>
</html>
