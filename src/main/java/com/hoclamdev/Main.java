package com.hoclamdev;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        Map<String, String> oldMapping = new HashMap<>();
        List<String> keys = Arrays.asList("apple", "banana", "carrot", "date", "egg");

        List<String> nodes = Arrays.asList("NodeA", "NodeB", "NodeC");
        ConsistentHashing ch = new ConsistentHashing(3, nodes);

        System.out.println("Assigned nodes:");
        for (String key : keys) {
            System.out.println(key + " => " + ch.getNode(key));
        }

        ch.addNode("NodeD");
        System.out.println("\nAfter adding NodeD:");
        for (String key : keys) {
            System.out.println(key + " => " + ch.getNode(key));
        }


        System.out.println("Before removing NodeB:");
        for (String key : keys) {
            String node = ch.getNode(key);
            oldMapping.put(key, node);
            System.out.println(key + " => " + node);
        }

        // Step 2: Xoá node và rebalance
        ch.removeNode("NodeB");

        System.out.println("\nAfter removing NodeB:");
        for (String key : keys) {
            String newNode = ch.getNode(key);
            String oldNode = oldMapping.get(key);
            System.out.printf("%s: %s => %s %s\n", key, oldNode, newNode,
                    oldNode.equals(newNode) ? "(unchanged)" : "(moved)");
        }
    }
}