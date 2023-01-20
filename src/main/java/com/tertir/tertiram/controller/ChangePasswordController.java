package com.tertir.tertiram.controller;

import com.tertir.tertiram.service.JavaFxHandling;
import com.tertir.tertiram.service.TertirService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChangePasswordController {

    @FXML
    private Button changePasswordButton;

    @FXML
    private TextField newPassword;

    @FXML
    private TextField oldPassword;

    @Autowired
    private JavaFxHandling javaFxHandling;

    @Autowired
    private TertirService tertirService;

    @FXML
    void initialize(String authorizationToken) {

        this.changePasswordButton.setOnAction(event -> {
            if (newPassword.getText() == null || newPassword.getText().equals("")) {
                javaFxHandling.throwException("Մուտքագրեք նոր գաղտնաբառը:");
                return;
            }
            if (oldPassword.getText() == null || oldPassword.getText().equals("")) {
                javaFxHandling.throwException("Մուտքագրեք հին գաղտնաբառը:");
                return;
            }

            try {
                tertirService.changePassword(oldPassword.getText(), newPassword.getText(), authorizationToken);
                javaFxHandling.showAlert("Գաղտնաբառը հաջողությամբ փոփոխված է։");
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }

        });

    }

}
