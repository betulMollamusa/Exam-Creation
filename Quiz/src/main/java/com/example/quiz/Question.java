package com.example.quiz;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

// Question sınıfı, bir sorunun metnini, seçeneklerini ve cevabını içerir
public class Question extends VBox {

    private TextArea questionTextArea;
    private TextField[] optionFields;
    private TextField answerField;

    // "Question" sınıfının yapıcı metodu, bir soru nesnesi oluşturur.
    public Question(int questionNumber, boolean isTestQuestion) {
        setSpacing(10);

        Label questionLabel = new Label(questionNumber + ". Soru:");
        questionTextArea = new TextArea();
        questionTextArea.setPromptText("Soruyu girin...");
        questionTextArea.setPrefRowCount(3);

        // Soru metnini ve varsa seçenekleri ve cevap alanını ekler.
        getChildren().addAll(questionLabel, questionTextArea);

        if (isTestQuestion) {
            optionFields = new TextField[5];
            for (int i = 0; i < 5; i++) {
                optionFields[i] = new TextField();
                optionFields[i].setPromptText((i + 1) + ". Şık:");
                getChildren().add(optionFields[i]);
            }
            answerField = new TextField();
            answerField.setPromptText("Cevap:");
            getChildren().add(answerField);
        } else {
            answerField = new TextField();
            answerField.setPromptText("Cevap:");
            getChildren().add(answerField);
        }
    }

    // Soru metnini döndürür.
    public String getQuestionText() {
        return questionTextArea.getText();
    }

    // Seçenekleri dizi olarak döndürür.
    public String[] getOptions() {
        if (optionFields != null) {
            String[] options = new String[5];
            for (int i = 0; i < 5; i++) {
                options[i] = optionFields[i].getText();
            }
            return options;
        }
        return null;
    }

    // Cevabı döndürür.
    public String getAnswer() {
        return answerField.getText();
    }
}
