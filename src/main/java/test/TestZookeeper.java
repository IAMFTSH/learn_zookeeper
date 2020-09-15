package test;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;


import java.io.IOException;
import java.util.List;

public class TestZookeeper {
    private String connectString="39.108.11.96:1111,39.108.11.96:2222,39.108.11.96:3333,39.108.11.96:4444";
    //这个时间要大，尤其上面的连接越多，就越大
    private int sessionTimeout=300000;
    private ZooKeeper zooKeeper;
    @Before
    public void init() throws IOException, KeeperException, InterruptedException {

        zooKeeper=new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
               /* try {
                    List<String> nodes=zooKeeper.getChildren("/",true);
                    System.out.println("节点变动");
                    for(String node:nodes){
                        System.out.println(node);
                    }
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        });
        System.out.println("链接情况："+zooKeeper.getState());
    }
    /**
     * ZooKeeper ZooDefs.Ids权限类型说明：
     * OPEN_ACL_UNSAFE：完全开放的ACL，任何连接的客户端都可以操作该属性znode
     * CREATOR_ALL_ACL：只有创建者才有ACL权限
     * READ_ACL_UNSAFE：只能读取ACL
     *
     * CreateMode 创建方法
     * PERSISTENT  持续节点 后面有sq的是带自增序号
     * EPHEMERAL  临时节点 后面有sq的是带自增序号
     */
    //把上面的@Test改成Before，那么上面的会先运行，那么就zooKeeper就有value
    @Test
    public void createNode() throws KeeperException, InterruptedException {
        String path=zooKeeper.create("/zkPro","JavaData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("新建节点的路径："+path);
    }

    @Test
    public void getNode() throws KeeperException, InterruptedException {
        /*第二参数是Watch或者true都可以监听*/
        List<String> nodeData=zooKeeper.getChildren("/", true);
        System.out.println("节点："+nodeData);
        /*有这个会一直监听该路径的变动,但是只有一次有反应*/
        Thread.sleep(Long.MAX_VALUE);
    }
    @Test
    public void isExist() throws KeeperException, InterruptedException {
        Stat stat=zooKeeper.exists("/zn02", false);
        System.out.println(stat==null?"not exist":"exist");
    }
}
