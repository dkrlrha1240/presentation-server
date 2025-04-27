package org.screen;

import javafx.stage.Stage;
import org.client.Client;
import org.client.storage.ClientStorage;
import org.network.netty.packet.Packet;

import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;

public class LayoutManager {
    final private static int SPLIT_SCREEN = 4;
    private static LayoutManager instance;
    final private ConcurrentHashMap<Integer, SplitScreen> screens = new ConcurrentHashMap<>();
    final private ConcurrentHashMap<Integer, Client> owners = new ConcurrentHashMap<>();
    private int layout;

    public LayoutManager(int layout) {
        for (int position = 0; position <= SPLIT_SCREEN; position++)
            screens.put(position, new SplitScreen(position));

        setLayout(layout);
    }

    public static void init(int layout) {
        instance = new LayoutManager(layout);
    }

    public void changePosition(int position, Client owner) {
        // 1. 새로운 오너의 기존 position 화면 숨김
        // 2. 변경될 position의 오너 교체
        // 3. 변경될 position 화면 show
        int beforePosition = owner.getPosition();
        SplitScreen beforeScreen = screens.get(beforePosition);

        if (beforeScreen != null)
            beforeScreen.hide();

        Client beforeOwner = owners.get(beforePosition);
        if (beforeOwner != null)
            beforeOwner.setPosition(-1);

        if (position != -1)
            owners.put(position, owner);
        owner.setPosition(position);

        SplitScreen screen = screens.get(position);
        if (screen != null)
            screen.show();

    }

    public static LayoutManager getInstance() {
        return instance;
    }

    public void show(int position) {
        SplitScreen screen = screens.get(position);
        if (screen == null)
            return;
        screen.show();
    }

    public void hide(int position) {
        SplitScreen screen = screens.get(position);
        if (screen == null)
            return;
        screen.hide();
    }

    public void hideAll() {
        screens.values().forEach(SplitScreen::hide);
    }

    public void setScreen(int position, byte[] data) {
        SplitScreen screen = screens.get(position);
        if (screen == null || position == -1)
            return;

        screen.setImage(data);
    }

    public void setLayout(int layout) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = dimension.getWidth();
        double screenHeight = dimension.getHeight();
        double[][] positions = new double[4][4];
        // 가로, 세로, X, Y
        switch (layout) {
            case 1 -> {
                 positions = new double[][] {
                         {screenWidth, screenHeight, 0, 0}, // 전체화면

                         {screenWidth / 2, screenHeight / 2, 0, 0}, // 1
                         {screenWidth / 2, screenHeight / 2, screenWidth / 2, 0}, // 2
                         {screenWidth / 2, screenHeight / 2, 0, screenHeight / 2}, // 3
                         {screenWidth / 2, screenHeight / 2, screenWidth / 2, screenHeight / 2} // 4
                 };
            }
            case 2 -> {
                double smallWidth = screenWidth / 3;
                double smallHeight = screenHeight / 3;
                positions = new double[][] {
                        {screenWidth, screenHeight, 0, 0}, // 전체화면

                        {2 * smallWidth, screenHeight, 0, 0}, // 1
                        {smallWidth, smallHeight, 2 * smallWidth, 0}, // 2
                        {smallWidth, smallHeight, 2 * smallWidth, smallHeight}, // 3
                        {smallWidth, smallHeight, 2 * smallWidth, 2 * smallHeight} // 4
                };
            }
            case 3 -> {
                double smallWidth = screenWidth / 3;
                double smallHeight = screenHeight / 3;
                positions = new double[][] {
                        {screenWidth, screenHeight, 0, 0}, // 전체화면

                        {2 * smallWidth, screenHeight, smallWidth, 0}, // 1
                        {smallWidth, smallHeight, 0, 0}, // 2
                        {smallWidth, smallHeight, 0, smallHeight}, // 3
                        {smallWidth, smallHeight, 0, 2 * smallHeight} // 4
                };
            }
            case 4 -> {
                double smallWidth = screenWidth / 3;
                double smallHeight = screenHeight / 3;
                positions = new double[][] {
                        {screenWidth, screenHeight, 0, 0}, // 전체화면

                        {screenWidth, 2 * smallHeight, 0, smallHeight}, // 1
                        {smallWidth, smallHeight, 0, 0}, // 2
                        {smallWidth, smallHeight, smallWidth, 0}, // 3
                        {smallWidth, smallHeight, 2 * smallWidth, 0} // 4
                };
            }
            case 5 -> {
                double smallWidth = screenWidth / 3;
                double smallHeight = screenHeight / 3;
                positions = new double[][] {
                        {screenWidth, screenHeight, 0, 0}, // 전체화면

                        {screenWidth, 2 * smallHeight, 0, 0}, // 1
                        {smallWidth, smallHeight, 0, 2 * smallHeight}, // 2
                        {smallWidth, smallHeight, smallWidth, 2 * smallHeight}, // 3
                        {smallWidth, smallHeight, 2 * smallWidth, 2 * smallHeight} // 4
                };
            }
        }

        for (int pos = 0; pos < positions.length; pos++) {
            double width = positions[pos][0];
            double height = positions[pos][1];
            double x = positions[pos][2];
            double y = positions[pos][3];
            SplitScreen screen = screens.get(pos);
            screen.setWidth(width);
            screen.setHeight(height);
            screen.setX(x);
            screen.setY(y);
//                    screen.show();
        }
        this.layout = layout;
        for (Client client : ClientStorage.getClients())
            if (client.isConnected())
                client.writeAndFlush(Packet.changeLayout(layout));
    }

    public int getLayout() {
        return layout;
    }


}
