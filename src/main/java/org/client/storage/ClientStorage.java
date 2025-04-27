package org.client.storage;

import org.client.Client;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ClientStorage {
    private final static Logger LOG = Logger.getGlobal();
    final private static ConcurrentHashMap<Integer, Client> clients = new ConcurrentHashMap<>();

    public static void addClient(Client client) {
        clients.put(client.getId(), client);
        LOG.info( "Client ID : " + client.getId() + " 가 스토리지에 추가됐습니다.");
    }

    public static void removeClient(int id) {
        clients.remove(id);
        LOG.info( "Client ID : " + id + " 가 스토리지에서 제거됐습니다.");
    }

    public static Client findById(int id) {
        return clients.get(id);
    }

    public static Client findByIP(String ip) {
        return clients.values().stream()
                .filter(client -> client.getIp().equals(ip))
                .findFirst()
                .orElse(null);
    }

    public static List<Client> getClients() {
        return clients.values().stream().toList();
    }
}
