package com.tertir.tertiram.controller;

import com.tertir.tertiram.service.JavaFxHandling;
import com.tertir.tertiram.service.TertirService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class GeneratePDFController {

    @FXML
    private Button generateButton;

    @FXML
    private TextField number;

    private boolean isGenerated;

    @FXML
    void initialize(String authorizationToken, TertirService tertirService, JavaFxHandling javaFxHandling) {

        isGenerated = false;

        this.number.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                number.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        this.generateButton.setOnAction(event -> {
            if (number.getText() == null || number.getText().equals("")) {
                javaFxHandling.showAlert("Լրացրեք քանակը");
            } else {
                try {
                    int count = Integer.parseInt(number.getText());
                    tertirService.generateQrCodes(authorizationToken, count);
                    isGenerated = true;
                    javaFxHandling.showAlert(String.format("Հաջողությամբ գեներացվել են %d հատ Qr կոդեր", count));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public boolean isGenerated() {
        return isGenerated;
    }

    public void setGenerated(boolean isGenerated) {
        this.isGenerated = isGenerated;
    }
}
