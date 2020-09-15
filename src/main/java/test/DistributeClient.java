package test;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * zooKeeper客户端
 * @author 邝明山
 * on 2020/9/15 18:25
 */
public class DistributeClient {
    private String connectString="39.108.11.96:1111,39.108.11.96:2222,39.108.11.96:3333,39.108.11.96:4444";
    //这个时间要大，尤其上面的连接越多，就越大
    private int sessionTimeout=300000;
    private ZooKeeper zooKeeper;
    public static void main(String[] args) {
        DistributeClient distributeClient=new DistributeClient();
        //1.连接zk集群
        distributeClient.getConnect();
        //2注册监听
        //3业务逻辑处理
        distributeClient.business();
    }

    private  void getChildren() {
        List<String> children= null;
        //储存服务器节点名称集合
        ArrayList<String> hosts=new ArrayList<String>();
        try {
            children = zooKeeper.getChildren("/Servers",true);
            for(String child:children){
                System.out.println(child);
                byte[] data=zooKeeper.getData("/Servers/"+child,false,null);
                hosts.add(new String(data));
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(hosts);
    }

    public void getConnect() {
        try {
            zooKeeper=new ZooKeeper(connectString, sessionTimeout, new Watcher() {
                public void process(WatchedEvent watchedEvent) {
                    //这里有个getChildren是为了重复监听
                    getChildren();
                }
            });
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }
    }
    private void business() {
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
