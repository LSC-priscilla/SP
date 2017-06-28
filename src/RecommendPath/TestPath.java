package RecommendPath;

import DataStructure.Graph;
import DataStructure.Node;
import DataStructure.Path;
import MainCode.Guider;
import TestCode.InitMap;
import Util.Util;

import java.util.*;

/**
 * Created by Administrator on 2017/6/28 0028.
 */
public class TestPath {
    //N types of product
    static int N = 160;
    static HashMap<Integer, Double> allProducts;
    static int[] shopList;
    static Map<Integer, Set<Integer>> shelf;
    static Map<Integer, Integer> pLocation;
    static Map<Integer, Double> pDis;

    public static void main(String[] args) {
        Random random = new Random();
        //均值为0.方差为1 的高斯分布
        allProducts = new HashMap<Integer, Double>();
        for (int i = 0; i < N; i++) {
            allProducts.put(new Integer(i), Math.abs(Math.sqrt(1) * random.nextGaussian() + 0));
        }
        //对所有的product根据probability进行排序,从大到小
        List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>(allProducts.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> o1,
                               Map.Entry<Integer, Double> o2) {
                return -o1.getValue().compareTo(o2.getValue());
            }
        });
        //k probability distribution
        //k, a customer going to buy k products
        int k = 4;
        shopList = new int[k];
        int count = 0;
        for (Map.Entry<Integer, Double> mapping : list) {
            System.out.println(mapping.getKey() + ":" + mapping.getValue());
            shopList[count] = mapping.getKey();
            count++;
            if (count == k) {
                break;
            }
        }
        //所有商品随机分配给16个点 0-15
        shelf = new HashMap<Integer, Set<Integer>>();
        pLocation = new HashMap<Integer, Integer>();
        for (int i = 0; i < 16; i++) {
            Set<Integer> products = new HashSet<Integer>();
            for (int j = 0; j < 10; j++) {
                while (true) {
                    int productId = random.nextInt(N);
                    if (allProducts.containsKey(productId)) {
                        products.add(productId);
                        pLocation.put(productId, i);
                        allProducts.remove(productId);
                        break;
                    } else {
                        continue;
                    }
                }
            }
            shelf.put(i, products);
        }
        System.out.println();
        System.out.print("输出货架上的商品");
        System.out.println();
        for (Map.Entry e : shelf.entrySet()) {
            System.out.print(e.getKey());
            System.out.println(e.getValue());
        }
        System.out.println();
        System.out.print("输出待购买商品");
        System.out.println();
        for (int toBuy : shopList) {
            System.out.print(toBuy + " ");
        }
        //Get product location
        System.out.println();
        System.out.print("输出购买商品位置");
        System.out.println();
        for (int i : shopList)
            System.out.println(i + "->" + pLocation.get(i));

        //Generating paths;
        // 初始化graph
        Graph graph = InitMap.returnGraph();
        ArrayList<Node> nodes = new ArrayList<Node>();
        for (int i : shopList)
            nodes.add(graph.getNode(pLocation.get(i)));

        Stack<Node> finalPath = new Stack<Node>();
        pDis = new HashMap<Integer, Double>();
        //对所有的product根据离入口距离进行排序,从小到大
        List<Map.Entry<Integer, Double>> pDisSorted = CreateSort(pDis,nodes,graph);
        System.out.println("距离进行排序啦~~");
        for (Map.Entry entry : pDisSorted) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        Node lastNode = graph.getNode(0);
        while (pDis != null) {
            Node des = graph.getNode(pDisSorted.get(0).getKey());
            List<Path> paths = Guider.getSingleDestPath(graph, lastNode, des, null, 0.1);
            if (paths.isEmpty()) {
                //出现了自己去自己,eg:  4->4
                pDis.remove(des.N);
                nodes.remove(graph.getNode(des.N));
                pDisSorted = CreateSort(pDis,nodes,graph);
                System.out.println("重新排序啦_______去除自己");
                for (Map.Entry entry : pDisSorted) {
                    System.out.println(entry.getKey() + ":" + entry.getValue());
                }
                continue;
            }
            Path bestPath = paths.get(0);
            Stack<Node> tempPath = new Stack<Node>();
            for (Node node : bestPath.getNodes()) {
                tempPath.push(node);
                if (pDis.containsKey(node.N)) {
                    pDis.remove(node.N);
                    nodes.remove(graph.getNode(des.N));
                    pDisSorted = CreateSort(pDis,nodes,graph);
                    System.out.println("重新排序啦，去除路径中已经包含");
                    for (Map.Entry entry : pDisSorted) {
                        System.out.println(entry.getKey() + ":" + entry.getValue());
                    }
                    if (pDisSorted.isEmpty()) {
                        System.out.println("gaojuhengfu ~~~~~~~~~~~");
                    }
                }
            }
            while (!tempPath.isEmpty()) {
                if (!finalPath.isEmpty()) {
                    Node topNode = finalPath.peek();
                    if (topNode == tempPath.peek()) {
                        finalPath.pop();
                    }
                }
                finalPath.push(tempPath.pop());
            }
            if (pDisSorted.isEmpty()) {
                break;
            }
            lastNode = graph.getNode(finalPath.peek().N);

        }
        for (Node node : finalPath) {
            System.out.print(node.N + "->");
        }
    }

    private static List<Map.Entry<Integer, Double>> CreateSort(Map<Integer, Double> a, ArrayList<Node> nodes, Graph graph) {
        for (Node target : nodes) {
            double Cost = Util.getDis(graph.getNode(0), target);
            a.put(target.N, Cost);
        }
        List<Map.Entry<Integer, Double>> list1 = new ArrayList<Map.Entry<Integer, Double>>(a.entrySet());
        Collections.sort(list1, new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> o1,
                               Map.Entry<Integer, Double> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        for (Map.Entry entry : list1) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        return list1;
    }
}