package com.demo.practise.common.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;


/**
 * @author jans9
 */
@Slf4j
public class IpUtils {

    // 获取本地ip地址性能太低，首次获取成功后锁定ip
    private static volatile String localHost;

    // 本地回环mac地址
    private static volatile String localhostMac;

    // 本地mac地址
    private static volatile String localMac;

    private static final String UNKNOWN = "unknown";

    private static final String[] CLIENT_IP_HEADERS = new String[]{"x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP", "X-Real-IP"};

    public static void main(String[] args) throws Exception {
        System.out.println(getLocalhostMac());
        System.out.println(getLocalMac());
    }

    /**
     * 获取客户端真实ip
     *
     * @return
     */
    public static String getRequestClientIp() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return Arrays.stream(CLIENT_IP_HEADERS)
                .map(request::getHeader)
                .filter(ip -> !StringUtils.isBlank(ip) && !UNKNOWN.equals(ip))
                .map(ip -> {
                    if (ip.indexOf(",") > 0) {
                        return ip.substring(0, ip.indexOf(","));
                    }
                    return ip;
                })
                .findFirst()
                .orElse(request.getRemoteAddr());
    }

    /**
     * 获取客户端真实ip
     *
     * @return
     */
    public static String getRequestClientIp(HttpServletRequest request) {
        return Arrays.stream(CLIENT_IP_HEADERS)
                .map(request::getHeader)
                .filter(ip -> !StringUtils.isBlank(ip) && !UNKNOWN.equals(ip))
                .map(ip -> {
                    if (ip.indexOf(",") > 0) {
                        return ip.substring(0, ip.indexOf(","));
                    }
                    return ip;
                })
                .findFirst()
                .orElse(request.getRemoteAddr());
    }

    /**
     * 获取本机8位的ip的16进制
     *
     * @return ip16进制
     */
    public static String getHexLocalIp() {
        if (!StringUtils.isEmpty(localHost)) {
            return localHost;
        }
        synchronized (com.demo.practise.common.helper.IpUtils.class) {
            if (!StringUtils.isEmpty(localHost)) {
                return localHost;
            }
            String localIp = com.demo.practise.common.helper.IpUtils.getLocalIp();
            if (StringUtils.isEmpty(localIp)) {
                return null;
            }
            String[] ips = localIp.split("\\.");
            StringBuffer sb = new StringBuffer();
            for (String ip : ips) {
                sb.append(Integer.toHexString(NumberUtils.toInt(ip)));
            }
            localHost = sb.toString();
            log.info("local ip address ->>> {}", localHost);
        }
        return localHost;
    }

    /**
     * 获取本机IP
     *
     * @return 本机IP
     */
    public static String getLocalIp() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip != null
                            && ip instanceof Inet4Address
                            && !ip.isLoopbackAddress()
                            //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                            && !ip.getHostAddress().contains(":")) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }

        return null;
    }

    /**
     * 获取本机mac地址，有可能是回环地址
     *
     * @return
     */
    public static String getLocalhostMac() {
        if (!StringUtils.isEmpty(localhostMac)) {
            log.info("localhost mac address ->>> {}", localhostMac);
            return localhostMac;
        }
        synchronized (com.demo.practise.common.helper.IpUtils.class) {
            if (!StringUtils.isEmpty(localhostMac)) {
                return localhostMac;
            }
            try {
                InetAddress ia = InetAddress.getLocalHost();
                //获取网卡，获取地址
                NetworkInterface networkInterface = NetworkInterface.getByInetAddress(ia);
                localhostMac = getMacAddress(networkInterface);
            } catch (Exception e) {
            }
        }
        log.info("localhost mac address ->>> {}", localhostMac);
        return localhostMac;
    }

    /**
     * 获取本机第一个mac地址
     *
     * @return
     */
    public static String getLocalMac() {
        if (!StringUtils.isEmpty(localMac)) {
            log.info("local real mac address ->>> {}", localMac);
            return localMac;
        }
        synchronized (com.demo.practise.common.helper.IpUtils.class) {
            if (!StringUtils.isEmpty(localMac)) {
                return localMac;
            }
            try {
                List<NetworkInterface> interfaces = new ArrayList<>();
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface network = networkInterfaces.nextElement();
                    if (network.isLoopback() || network.isPointToPoint() || network.isVirtual() || !network.isUp()) {
                        continue;
                    }
                    interfaces.add(network);
                }
                if (CollectionUtils.isEmpty(interfaces)) {
                    return null;
                }
                interfaces.stream()
                        .filter(inter -> inter.getName().startsWith("ens") || inter.getName().startsWith("eth")
                                || inter.getName().startsWith("enp") || inter.getName().startsWith("eno"))
                        .findFirst()
                        .ifPresent(inter -> {
                            localMac = getMacAddress(inter);
                        });
                if (StringUtils.isEmpty(localMac)) {
                    localMac = getMacAddress(interfaces.get(0));
                }
            } catch (Exception e) {
            }
        }
        log.info("local real mac address ->>> {}", localMac);
        return localMac;
    }

    private static String getMacAddress(NetworkInterface networkInterface) {
        try {
            StringBuilder builder = new StringBuilder();
            byte[] mac = networkInterface.getHardwareAddress();
            if (mac != null) {
                for (int i = 0; i < mac.length; i++) {
                    if (i != 0) {
                        builder.append("-");
                    }
                    //字节转换为整数
                    int temp = mac[i] & 0xff;
                    String str = Integer.toHexString(temp);
                    if (str.length() == 1) {
                        builder.append("0").append(str);
                    } else {
                        builder.append(str);
                    }
                }
            }
            return builder.toString();
        } catch (SocketException e) {
            log.error("obtain mac address error:{}", e);
        }
        return null;
    }

}
