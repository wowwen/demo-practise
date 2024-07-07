package com.demo.zk;

import org.apache.zookeeper.ZooKeeper;
import org.aspectj.lang.annotation.Before;

/**
 * @author jiangyw
 * @date 2024/6/20 20:31
 * @description
 */
public class ZookeeperClient {
    private String connectStr = "";
    private int sessionTimeout = 2000;

    private ZooKeeper zkClient;


}
