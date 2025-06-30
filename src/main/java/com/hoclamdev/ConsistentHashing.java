package com.hoclamdev;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashing {
    private final TreeMap<Integer, String> ring = new TreeMap<>();
    private final int numberOfReplicas;

    public ConsistentHashing(int numberOfReplicas, Collection<String> nodes) {
        this.numberOfReplicas = numberOfReplicas;
        for (String node : nodes) {
            addNode(node);
        }
    }

    // Add node with virtual replicas
    public void addNode(String node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            int hash = hash(node + ":" + i);
            ring.put(hash, node);
        }
    }

    // Remove node
    public void removeNode(String node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            int hash = hash(node + ":" + i);
            ring.remove(hash);
        }
    }

    // Get node for a given key
    public String getNode(String key) {
        if (ring.isEmpty()) return null;
        int hash = hash(key);
        SortedMap<Integer, String> tailMap = ring.tailMap(hash);
        int nodeHash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
        return ring.get(nodeHash);
    }

    // Simple hash function using MD5
    private int hash(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(key.getBytes(StandardCharsets.UTF_8));
            return ((digest[0] & 0xFF) << 24)
                    | ((digest[1] & 0xFF) << 16)
                    | ((digest[2] & 0xFF) << 8)
                    | (digest[3] & 0xFF);
        } catch (Exception e) {
            throw new RuntimeException("Hashing error", e);
        }
    }

    // Print all nodes in ring
    public void printRing() {
        for (Map.Entry<Integer, String> entry : ring.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }
    }
}
