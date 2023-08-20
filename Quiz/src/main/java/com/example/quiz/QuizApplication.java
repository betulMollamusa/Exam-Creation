package com.example.quiz;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// QuizApplication sınıfı, sınav sorularını oluşturmayı ve dosyaya kaydetmeyi sağlayar
public class QuizApplication extends Application {

    private ComboBox<String> questionTypeComboBox; // Soru türünün seçildiği açılır menü.
    private TextField questionCountField; // Oluşturulacak soru sayısının girildiği alan.
    private ScrollPane questionFieldsScrollPane; // ekranı kaydırabilmek için .
    private VBox questionFieldsContainer; // Soru alanlarının içeren düzen.

    // Uygulama başladığında çalışan metot.
    @Override
    public void start(Stage primaryStage) {
        questionTypeComboBox = new ComboBox<>();
        questionTypeComboBox.getItems().addAll("Klasik Soru", "Test Sorusu"); // Soru türü seçenekleri.

        questionCountField = new TextField();
        questionCountField.setPromptText("Soru Sayısı"); // Alanın içinde görüntülenen örnek metin.

        Button createButton = new Button("Oluştur"); // Soru alanlarını oluşturan buton.
        createButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        createButton.setOnAction(e -> createQuestionFields()); // butona tıklandığında soru alanları oluşturulur.

        HBox inputContainer = new HBox(10, questionTypeComboBox, questionCountField, createButton); // Girdi bileşenlerini içeren yatay grup yapısı.
        inputContainer.setPadding(new Insets(10));

        questionFieldsContainer = new VBox(10);
        questionFieldsScrollPane = new ScrollPane(questionFieldsContainer); // Soru alanlarını kaydırmak için kaydırma bileşeni.
        questionFieldsScrollPane.setFitToHeight(true);
        questionFieldsScrollPane.setFitToWidth(true);

        Button saveButton = new Button("Kaydet"); // Soruları dosyaya kaydetmek için buton.
        saveButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        saveButton.setOnAction(e -> saveQuestionsToFile()); // butona tıklandığında sorular dosyaya kaydedilir.

        VBox root = new VBox(10, inputContainer, questionFieldsScrollPane, saveButton); // Uygulama arayüzünü oluşturan dikey grup.
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 800, 600); // Uygulama penceresinin boyutu.
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sınav Soru Oluşturma Uygulaması"); // Pencere başlığı.
        primaryStage.show();
    }

    // Soru alanlarını oluşturan metot.
    private void createQuestionFields() {
        questionFieldsContainer.getChildren().clear(); // Önceki soru alanlarını temizler.
        int questionCount = Integer.parseInt(questionCountField.getText()); // Kullanıcının girdiği soru sayısını alır.
        String selectedType = questionTypeComboBox.getValue(); // Seçilen soru türünü alır.

        for (int i = 1; i <= questionCount; i++) {
            boolean isTestQuestion = "Test Sorusu".equals(selectedType);
            Question question = new Question(i, isTestQuestion); // Soru nesnesi oluşturulur.
            questionFieldsContainer.getChildren().add(question); // Soru alanları içine eklenir.
        }
    }

    // Soruları dosyaya kaydeden metot.
    private void saveQuestionsToFile() {
        FileChooser fileChooser = new FileChooser(); // Dosya kaydetme iletişim kutusu.
        fileChooser.setTitle("Dosyayı Kaydet");
        fileChooser.setInitialFileName("sorular.txt"); // Varsayılan dosya adı.
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt")); // Sadece txt dosyalarını filtreler.
        File file = fileChooser.showSaveDialog(new Stage()); // Dosya kaydetme iletişim kutusu açılır.

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) { // Dosyayı yazmak için FileWriter kullanılır.
                int questionCount = questionFieldsContainer.getChildren().size(); // Oluşturulan soru sayısı.
                writer.write("Soru Sayısı: " + questionCount + "\n\n");

                for (int questionNumber = 0; questionNumber < questionCount; questionNumber++) {
                    Node questionBox = questionFieldsContainer.getChildren().get(questionNumber);
                    if (questionBox instanceof Question) { // Eğer içerik bir soru nesnesi ise...
                        Question question = (Question) questionBox;

                        writer.write("Soru " + (questionNumber + 1) + ": ");
                        writer.write(question.getQuestionText() + "\n"); // Soru metni dosyaya yazılır.

                        String[] options = question.getOptions();
                        if (options != null) {
                            for (int i = 0; i < options.length; i++) {
                                writer.write("Şık " + (i + 1) + ": " + options[i] + "\n"); // Seçenekler dosyaya yazılır.
                            }
                        }

                        String answer = question.getAnswer();
                        if (answer != null && !answer.isEmpty()) {
                            writer.write("Cevap: " + answer + "\n"); // Cevap dosyaya yazılır.
                        }

                        writer.write("\n");
                    }
                }

                showSavedAlert(); // Kaydedildi bildirimi gösterilir.
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Kaydedildi bildirimi gösteren metot.
    private void showSavedAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Kaydedildi");
        alert.setHeaderText("Sorular başarıyla kaydedildi.");
        alert.show();
    }

    // Uygulamayı başlatan main metodu.
}
