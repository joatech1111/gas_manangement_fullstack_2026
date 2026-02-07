<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.net.InetAddress" %>
<%
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    request.setCharacterEncoding("UTF-8");
    
    // 서버 정보 수집
    Date now = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String serverTime = sdf.format(now);
    String serverName = request.getServerName();
    int serverPort = request.getServerPort();
    String contextPath = request.getContextPath();
    String requestURI = request.getRequestURI();
    String protocol = request.getProtocol();
    String remoteAddr = request.getRemoteAddr();
    String userAgent = request.getHeader("User-Agent");
    
    // 서버 호스트명 가져오기
    String hostName = "Unknown";
    try {
        hostName = InetAddress.getLocalHost().getHostName();
    } catch (Exception e) {
        hostName = "Unable to resolve";
    }
    
    // JVM 정보
    String javaVersion = System.getProperty("java.version");
    String javaVendor = System.getProperty("java.vendor");
    String osName = System.getProperty("os.name");
    String osVersion = System.getProperty("os.version");
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>서버 접속 확인 - Gas Management Server</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Courier New', monospace;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        .container {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 15px;
            padding: 40px;
            max-width: 900px;
            width: 100%;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
        }
        .ascii-art {
            font-family: 'Courier New', monospace;
            font-size: 12px;
            line-height: 1.2;
            color: #333;
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            border: 2px solid #667eea;
            margin: 20px 0;
            white-space: pre;
            overflow-x: auto;
            text-align: center;
        }
        .status-badge {
            display: inline-block;
            padding: 8px 16px;
            background: #28a745;
            color: white;
            border-radius: 20px;
            font-weight: bold;
            font-size: 14px;
            margin: 10px 0;
            animation: pulse 2s infinite;
        }
        @keyframes pulse {
            0%, 100% { opacity: 1; }
            50% { opacity: 0.7; }
        }
        .info-section {
            margin: 30px 0;
            padding: 20px;
            background: #f8f9fa;
            border-radius: 10px;
            border-left: 4px solid #667eea;
        }
        .info-section h2 {
            color: #667eea;
            margin-bottom: 15px;
            font-size: 20px;
        }
        .info-row {
            display: flex;
            padding: 10px 0;
            border-bottom: 1px solid #e9ecef;
        }
        .info-row:last-child {
            border-bottom: none;
        }
        .info-label {
            font-weight: bold;
            color: #495057;
            width: 200px;
            flex-shrink: 0;
        }
        .info-value {
            color: #212529;
            word-break: break-all;
        }
        .footer {
            text-align: center;
            margin-top: 30px;
            color: #6c757d;
            font-size: 14px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="ascii-art">
    ╔══════════════════════════════════════════════════════════════╗
    ║                                                              ║
    ║                                                              ║
    ║        ██████╗  █████╗ ███████╗                              ║
    ║        ██╔══██╗██╔══██╗██╔════╝                              ║
    ║        ██████╔╝███████║███████╗                              ║
    ║        ██╔══██╗██╔══██║╚════██║                              ║
    ║        ██║  ██║██║  ██║███████║                              ║
    ║        ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝                              ║
    ║                                                              ║
    ║        ██╗                                                      ║
    ║                                                              ║
    ║        ███╗   ███╗ █████╗ ███╗   ██╗ █████╗  ██████╗         ║
    ║        ████╗ ████║██╔══██╗████╗  ██║██╔══██╗██╔════╝         ║
    ║        ██╔████╔██║███████║██╔██╗ ██║███████║██║              ║
    ║        ██║╚██╔╝██║██╔══██║██║╚██╗██║██╔══██║██║              ║
    ║        ██║ ╚═╝ ██║██║  ██║██║ ╚████║██║  ██║╚██████╗         ║
    ║        ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝ ╚═════╝         ║
    ║                                                              ║
    ║                                                              ║
    ╚══════════════════════════════════════════════════════════════╝
        </div>
        
        <div style="text-align: center;">
            <div class="status-badge">✓ 서버 정상 작동 중</div>
        </div>
        
        <div class="info-section">
            <h2>📊 서버 상태 정보</h2>
            <div class="info-row">
                <div class="info-label">서버 상태:</div>
                <div class="info-value">
                    <span style="color: #28a745; font-weight: bold;">● ONLINE</span>
                </div>
            </div>
            <div class="info-row">
                <div class="info-label">서버 시간:</div>
                <div class="info-value"><%= serverTime %></div>
            </div>
            <div class="info-row">
                <div class="info-label">서버 호스트:</div>
                <div class="info-value"><%= hostName %></div>
            </div>
            <div class="info-row">
                <div class="info-label">프로토콜:</div>
                <div class="info-value"><%= protocol %></div>
            </div>
        </div>
        
        <div class="info-section">
            <h2>🌐 연결 정보</h2>
            <div class="info-row">
                <div class="info-label">서버 주소:</div>
                <div class="info-value"><%= serverName %>:<%= serverPort %></div>
            </div>
            <div class="info-row">
                <div class="info-label">컨텍스트 경로:</div>
                <div class="info-value"><%= contextPath %></div>
            </div>
            <div class="info-row">
                <div class="info-label">요청 URI:</div>
                <div class="info-value"><%= requestURI %></div>
            </div>
            <div class="info-row">
                <div class="info-label">클라이언트 IP:</div>
                <div class="info-value"><%= remoteAddr %></div>
            </div>
            <div class="info-row">
                <div class="info-label">User Agent:</div>
                <div class="info-value"><%= userAgent != null ? userAgent : "N/A" %></div>
            </div>
        </div>
        
        <div class="info-section">
            <h2>⚙️ 시스템 정보</h2>
            <div class="info-row">
                <div class="info-label">Java 버전:</div>
                <div class="info-value"><%= javaVersion %></div>
            </div>
            <div class="info-row">
                <div class="info-label">Java 벤더:</div>
                <div class="info-value"><%= javaVendor %></div>
            </div>
            <div class="info-row">
                <div class="info-label">운영체제:</div>
                <div class="info-value"><%= osName %> <%= osVersion %></div>
            </div>
            <div class="info-row">
                <div class="info-label">서버 엔진:</div>
                <div class="info-value">Apache Tomcat/9.0.105</div>
            </div>
        </div>
        
        <div class="footer">
            <p>━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━</p>
            <p style="margin-top: 10px;">
                <strong>Gas Management Server</strong> | 서버 접속 확인 페이지
            </p>
            <p style="margin-top: 5px; font-size: 12px;">
                페이지 생성 시간: <%= serverTime %>
            </p>
        </div>
    </div>
</body>
</html>