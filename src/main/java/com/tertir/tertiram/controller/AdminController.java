package com.tertir.tertiram.controller;

import com.tertir.tertiram.rest.ApplicationUserModel;
import com.tertir.tertiram.service.JavaFxHandling;
import com.tertir.tertiram.service.TertirService;
import eu.europa.esig.dss.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class AdminController {

    @FXML
    private TableColumn<TableObject, Label> id;
    @FXML
    private TableColumn<TableObject, Label> username;
    @FXML
    private TableColumn<TableObject, Label> idram;
    @FXML
    private TableColumn<TableObject, Label> otherBank;
    @FXML
    private TableColumn<TableObject, Label> randomCode;
    @FXML
    private TableColumn<TableObject, Label> first;
    @FXML
    private TableColumn<TableObject, Label> second;
    @FXML
    private TableColumn<TableObject, Label> third;
    @FXML
    private TableColumn<TableObject, Label> fourth;
    @FXML
    private TableColumn<TableObject, Label> fifth;
    @FXML
    private TableColumn<TableObject, Label> sum;
    @FXML
    private TableView<TableObject> userTableView;
    @FXML
    private BorderPane globalPane;
    @FXML
    private Button purchaseUser;
    @FXML
    private MenuItem generateMenuItem;
    @FXML
    private MenuItem refresh;
    @FXML
    private MenuItem scannedCountMenuItem;
    @FXML
    private MenuItem logout;
    @FXML
    private MenuItem changePassword;

    @Autowired
    private TertirService tertirService;

    @Autowired
    private JavaFxHandling javaFxHandling;

    @Value("classpath:/fxml/GeneratePDFControllerPage.fxml")
    private Resource generatePDFControllerResource;

    @Value("classpath:/fxml/MainControllerPage.fxml")
    private Resource mainResource;

    @Value("classpath:/fxml/ChangePasswordControllerPage.fxml")
    private Resource changePasswordControllerResource;

    @FXML
    void initialize(ApplicationContext applicationContext, String authorizationToken) {

        this.userTableView.getItems().clear();
        initializeTable();

        this.refresh.setOnAction(event -> {
            try {
                this.userTableView.getItems().clear();

                List<ApplicationUserModel> allUsers = tertirService.getAllUsers(authorizationToken);
                for (ApplicationUserModel user :
                        allUsers) {
                    userTableView.getItems().add(new TableObject(
                            user.getId(),
                            user.getUsername(),
                            user.getIDBankNumber(),
                            user.getOtherBankNumber(),
                            user.getRandomCode().toString(),
                            user.getNumber5().toString(),
                            user.getNumber4().toString(),
                            user.getNumber3().toString(),
                            user.getNumber2().toString(),
                            user.getNumber1().toString(),
                            user.getSum().toString()
                    ));
                }
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.scannedCountMenuItem.setOnAction(event -> {
            try {
                Integer scannedQrCodesCount = tertirService.getScannedQrCodesCount(authorizationToken);
                javaFxHandling.showAlert(String.format("Այս պահի դրությամբ սկանավորվել է %d հատ Qr կոդ:", scannedQrCodesCount));
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.generateMenuItem.setOnAction(event -> {
            try {

                FXMLLoader fxmlLoader = new FXMLLoader(generatePDFControllerResource.getURL());
                fxmlLoader.setControllerFactory(applicationContext::getBean);
                Pane pane = fxmlLoader.load();

                GeneratePDFController generatePDFController = fxmlLoader.getController();
                generatePDFController.initialize(authorizationToken, tertirService, javaFxHandling);

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UTILITY);
                stage.setTitle("Qr կոդերի գեներացում");
                stage.setScene(new Scene(pane));
                stage.getIcons().add(new Image(this.javaFxHandling.getLogoResource().getInputStream()));
                stage.showAndWait();

                if (generatePDFController.isGenerated()) {
                    FileChooser fileChooser = new FileChooser();
                    //Set extension filter for text files
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
                    fileChooser.getExtensionFilters().add(extFilter);

                    byte[] allNotPrintedQrCodes = tertirService.getAllNotPrintedQrCodes(authorizationToken);

                    File file = fileChooser.showSaveDialog(new Stage());

                    if (file != null) {
                        try (FileOutputStream fos = new FileOutputStream(file)) {
                            Utils.copy(new ByteArrayInputStream(allNotPrintedQrCodes), fos);
                            javaFxHandling.showAlert("Ֆայլը պահպանվել է հաջողությամբ:");
                        } catch (IOException e) {
                            throw new IOException("Հնարավոր չէ պահպանել ֆայլը։ Փորձեք ավելի ուշ։");
                        }
                    }

                    generatePDFController.setGenerated(false);

                }

            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });


        this.purchaseUser.disableProperty().bind(userTableView.getSelectionModel().selectedItemProperty().isNull());

        this.purchaseUser.setOnAction(event -> {
            TableObject selectedItem = userTableView.getSelectionModel().getSelectedItem();
            try {
                tertirService.purchaseUser(selectedItem.getId(), authorizationToken);
                javaFxHandling.showAlert(String.format("%s բաժանորդի գումարը վճարվել է հաջողությամբ:", selectedItem.getUsername()));
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.logout.setOnAction(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(mainResource.getURL());
                fxmlLoader.setControllerFactory(applicationContext::getBean);
                BorderPane borderPane = fxmlLoader.load();

                Stage stage = (Stage) globalPane.getScene().getWindow();
                Scene scene = stage.getScene();
                if (scene == null) {
                    scene = new Scene(borderPane, 1000, 600);
                    stage.setScene(scene);
                } else {
                    stage.getScene().setRoot(borderPane);
                }

                MainController mainController = fxmlLoader.getController();
                mainController.initialize(applicationContext);

            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.changePassword.setOnAction(event -> {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(changePasswordControllerResource.getURL());
                fxmlLoader.setControllerFactory(applicationContext::getBean);
                Pane pane = fxmlLoader.load();

                ChangePasswordController changePasswordController = fxmlLoader.getController();
                changePasswordController.initialize(authorizationToken);

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UTILITY);
                stage.setTitle("Գաղտնաբառի փոփոխում");
                stage.setScene(new Scene(pane));
                stage.getIcons().add(new Image(this.javaFxHandling.getLogoResource().getInputStream()));
                stage.showAndWait();

            }catch (Exception e){
                javaFxHandling.throwException(e.getMessage());
            }

        });

    }

    private void initializeTable() {
        this.id.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.id.setSortable(false);

        this.username.setCellValueFactory(new PropertyValueFactory<>("username"));
        this.username.setSortable(false);

        this.idram.setCellValueFactory(new PropertyValueFactory<>("idram"));
        this.idram.setSortable(false);

        this.otherBank.setCellValueFactory(new PropertyValueFactory<>("otherBank"));
        this.otherBank.setSortable(false);

        this.randomCode.setCellValueFactory(new PropertyValueFactory<>("randomCode"));
        this.randomCode.setSortable(false);

        this.fifth.setCellValueFactory(new PropertyValueFactory<>("fifth"));
        this.fifth.setSortable(false);

        this.fourth.setCellValueFactory(new PropertyValueFactory<>("fourth"));
        this.fourth.setSortable(false);

        this.third.setCellValueFactory(new PropertyValueFactory<>("third"));
        this.third.setSortable(false);

        this.second.setCellValueFactory(new PropertyValueFactory<>("second"));
        this.second.setSortable(false);

        this.first.setCellValueFactory(new PropertyValueFactory<>("first"));
        this.first.setSortable(false);

        this.sum.setCellValueFactory(new PropertyValueFactory<>("sum"));
        this.sum.setSortable(false);

        this.userTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }


//    public record TableObject(String id, String username, String idram,
//                              String otherBank, String randomCode,String fifth,
//                              String fourth, String third,String second, String first, String sum){}
//

    public static class TableObject {

        private String id;
        private String username;
        private String idram;
        private String otherBank;
        private String randomCode;
        private String fifth;
        private String fourth;
        private String third;
        private String second;
        private String first;
        private String sum;

        public TableObject(String id,
                           String username,
                           String idram,
                           String otherBank,
                           String randomCode,
                           String fifth,
                           String fourth,
                           String third,
                           String second,
                           String first,
                           String sum) {
            this.id = id;
            this.username = username;
            this.idram = idram;
            this.otherBank = otherBank;
            this.randomCode = randomCode;
            this.fifth = fifth;
            this.fourth = fourth;
            this.third = third;
            this.second = second;
            this.first = first;
            this.sum = sum;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getIdram() {
            return idram;
        }

        public void setIdram(String idram) {
            this.idram = idram;
        }

        public String getOtherBank() {
            return otherBank;
        }

        public void setOtherBank(String otherBank) {
            this.otherBank = otherBank;
        }

        public String getRandomCode() {
            return randomCode;
        }

        public void setRandomCode(String randomCode) {
            this.randomCode = randomCode;
        }

        public String getFifth() {
            return fifth;
        }

        public void setFifth(String fifth) {
            this.fifth = fifth;
        }

        public String getFourth() {
            return fourth;
        }

        public void setFourth(String fourth) {
            this.fourth = fourth;
        }

        public String getThird() {
            return third;
        }

        public void setThird(String third) {
            this.third = third;
        }

        public String getSecond() {
            return second;
        }

        public void setSecond(String second) {
            this.second = second;
        }

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }
    }


}
