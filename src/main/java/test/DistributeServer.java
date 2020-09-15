package test;


import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * @author 邝明山
 * zookeeper服务端
 */
public class DistributeServer {

    private String connectString="39.108.11.96:1111,39.108.11.96:2222,39.108.11.96:3333,39.108.11.96:4444";
    //这个时间要大，尤其上面的连接越多，就越大
    private int sessionTimeout=300000;
    private ZooKeeper zooKeeper;
    public static void main(String[] args) {
        DistributeServer distributeServer=new DistributeServer();
        //1.连接zk集群
        distributeServer.getConnect();
        //2注册节点
        String registerPath=distributeServer.register("JAVA");
        System.out.println(registerPath);
        //3业务逻辑处理
        distributeServer.business();
    }

    private void business() {
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String register(String hostName){
        String path=null;
        try {
            //创建带序号的临时节点   注意，如何是在linux增加临时节点，需要给数据加上双引号，持久节点不用
            path=zooKeeper.create("/Servers/Server1",hostName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return path;
    }

    public void getConnect() {
        try {
            zooKeeper=new ZooKeeper(connectString, sessionTimeout, new Watcher() {
                public void process(WatchedEvent watchedEvent) {

                }
            });
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }
    }
}
