package org.screen.form;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.client.Client;
import org.client.storage.ClientStorage;
import org.network.netty.NettyServer;
import org.screen.LayoutManager;

import java.util.Map;


public class FormController {
    private static FormController instance;

    @FXML
    private Button oneButton;
    @FXML
    private Button twoButton;
    @FXML
    private Button threeButton;
    @FXML
    private Button fourButton;
    @FXML
    private Button fullButton;
    @FXML
    private Button hideButton;
    @FXML
    private Button allHideButton;
    @FXML
    private Button disconnectButton;
    @FXML
    private RadioButton optionOne;
    @FXML
    private RadioButton optionTwo;
    @FXML
    private RadioButton optionThree;
    @FXML
    private RadioButton optionFour;
    @FXML
    private RadioButton optionFive;
    @FXML
    private TableView<ClientDTO> tableView;
    @FXML
    private TableColumn<ClientDTO, Integer> idColumn;
    @FXML
    private TableColumn<ClientDTO, String> statusColumn;
    @FXML
    private TableColumn<ClientDTO, String> ipColumn;
    @FXML
    private TableColumn<ClientDTO, String> jobColumn;
    @FXML
    private TableColumn<ClientDTO, String> nameColumn;
    @FXML
    private TableColumn<ClientDTO, String> positionColumn;
    private final ObservableList<ClientDTO> data = FXCollections.observableArrayList();


    Map<Integer, Button> changePositionButton;

    public FormController() {
        instance = this;
    }

    public static FormController getInstance() {
        return instance;
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        ipColumn.setCellValueFactory(new PropertyValueFactory<>("ip"));
        jobColumn.setCellValueFactory(new PropertyValueFactory<>("job"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        tableView.setItems(data);

        Platform.runLater(() -> {
            changePositionButton = Map.of(
                    1, oneButton,
                    2, twoButton,
                    3, threeButton,
                    4, fourButton
            );
            // 요소들이 모두 로딩 된 후 실행할 코드
            LayoutManager.init(1);
            new NettyServer(8484).start();
        });
    }

    public void setLayout(int layout) {
        switch (layout) {
            case 1:
                optionOne.setSelected(true);
                break;
            case 2:
                optionTwo.setSelected(true);
                break;
            case 3:
                optionThree.setSelected(true);
                break;
            case 4:
                optionFour.setSelected(true);
                break;
            case 5:
                optionFive.setSelected(true);
                break;
            default:
                optionOne.setSelected(true);
                break;
        }
        changeLayout(null);
    }

    public void changeLayout(ActionEvent actionEvent) {
        // [레이아웃 번호][위치 번호][위치 크기 정보]
        // 위치 크기 : x, y, w, h
        int[][][] position = {
                { // 1번 레이아웃
                        {0, 0, 102, 63},
                        {102, 0, 102, 63},
                        {0, 63, 102, 63},
                        {102, 63, 102, 63}
                },
                { // 2번 레이아웃
                        {0, 0, 136, 126},
                        {136, 0, 68, 42},
                        {136, 42, 68, 42},
                        {136, 84, 68, 42}
                },
                { // 3번 레이아웃
                        {68, 0, 136, 126},
                        {0, 0, 68, 42},
                        {0, 42, 68, 42},
                        {0, 84, 68, 42}
                },
                { // 4번 레이아웃
                        {0, 42, 204, 84},
                        {0, 0, 68, 42},
                        {68, 0, 68, 42},
                        {136, 0, 68, 42}
                },
                { // 5번 레이아웃
                        {0, 0, 204, 84},
                        {0, 84, 68, 42},
                        {68, 84, 68, 42},
                        {136, 84, 68, 42}
                }
        };
        int layout = optionOne.isSelected() ? 1 :
                optionTwo.isSelected() ? 2 :
                        optionThree.isSelected() ? 3 :
                                optionFour.isSelected() ? 4 : 5;

        int[][] positions = position[layout - 1];
        for (int num = 0; num < positions.length; num++) {
            int[] dimension = positions[num];
            int x = dimension[0], y = dimension[1], width = dimension[2], height = dimension[3];
            Button button = changePositionButton.get(num + 1);
            if (button == null) {
                System.out.println((num + 1) + "번 위치 변경 버튼이 없습니다.");
                continue;
            }

            button.setLayoutX(x);
            button.setLayoutY(y);
            button.setPrefWidth(width);
            button.setPrefHeight(height);
        }

        LayoutManager.getInstance().setLayout(layout);
    }

    public void clickChangePosition(ActionEvent actionEvent) {
        // 위치 변경 클릭 시
        Button button = (Button) actionEvent.getSource();
        int position = -1; // 기본 값 (숨김)
        if (button.equals(fullButton))
            position = 0;
        else if (button.equals(hideButton))
            position = -1;
        else
            position = Integer.parseInt(button.getText());

        ClientDTO selectClient = getSelectedClient();
        if (selectClient == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("알림");
            alert.setHeaderText("클라이언트를 선택하세요.");
            alert.setContentText("위치를 변경할 클라이언트를 선택하세요.");
            alert.showAndWait();
            return;
        }
        Client client = ClientStorage.findById(selectClient.getId());
        LayoutManager.getInstance().changePosition(position, client);
    }

    public void clickHideAllButton(ActionEvent actionEvent) {
        // 모두 숨김 버튼 클릭 시
        LayoutManager.getInstance().hideAll();
    }

    public void clickDisconnectButton(ActionEvent actionEvent) {
        // 연결해제 버튼 클릭 시
        ClientDTO clientDTO = getSelectedClient();
        if (clientDTO == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("알림");
            alert.setHeaderText("클라이언트를 선택하세요.");
            alert.setContentText("연결을 해제할 클라이언트를 선택하세요.");
            alert.showAndWait();
            return;
        }

        removeRowById(clientDTO.getId());
    }

    public ClientDTO getSelectedClient() {
        return tableView.getSelectionModel().getSelectedItem();
    }

    public void updateRowByClient(ClientDTO clientDTO) {
        ClientDTO targetClientDTO = findRowById(clientDTO.getId());
        if (targetClientDTO == null)
            return;

        targetClientDTO.setId(clientDTO.getId());
        targetClientDTO.setStatus(clientDTO.getStatus());
        targetClientDTO.setIP(clientDTO.getIP());
        targetClientDTO.setJob(clientDTO.getJob());
        targetClientDTO.setName(clientDTO.getName());
        targetClientDTO.setPosition(clientDTO.getPosition());
        tableView.refresh();

    }

    public ClientDTO findRowById(int id) {
        return data.stream()
                .filter(clientDTO -> clientDTO.getId() == id)
                .findFirst()
                .orElse(null);
    }


    public void removeRowById(int id) {
        data.removeIf(clientDTO -> clientDTO.getId() == id);
    }

    public void addRow(ClientDTO clientDTO) {
        data.add(clientDTO);
    }



}
