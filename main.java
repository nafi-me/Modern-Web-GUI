import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Polished JavaFX GUI");

        // Sidebar
        VBox sidebar = new VBox(12);
        sidebar.setPadding(new Insets(16));
        sidebar.getStyleClass().add("sidebar");

        Label logo = new Label("PG");
        logo.getStyleClass().add("logo");
        Label title = new Label("Polished GUI");
        title.getStyleClass().add("brand-title");
        Label subtitle = new Label("JavaFX + CSS demo");
        subtitle.getStyleClass().add("brand-sub");

        HBox brand = new HBox(12, logo, new VBox(title, subtitle));
        brand.setAlignment(Pos.CENTER_LEFT);

        ToggleGroup navGroup = new ToggleGroup();
        ToggleButton t1 = new ToggleButton("Compose");
        ToggleButton t2 = new ToggleButton("Settings");
        ToggleButton t3 = new ToggleButton("About");
        t1.setSelected(true);
        t1.getStyleClass().add("nav-btn");
        t2.getStyleClass().add("nav-btn");
        t3.getStyleClass().add("nav-btn");
        t1.setToggleGroup(navGroup); t2.setToggleGroup(navGroup); t3.setToggleGroup(navGroup);

        VBox nav = new VBox(8, t1, t2, t3);
        nav.setPadding(new Insets(8,0,0,0));

        Button themeToggle = new Button("Toggle Theme");
        themeToggle.getStyleClass().addAll("small-btn");

        sidebar.getChildren().addAll(brand, nav, new Region(), themeToggle);
        VBox.setVgrow(sidebar.getChildren().get(sidebar.getChildren().size()-2), Priority.ALWAYS);

        // Main content
        VBox main = new VBox(12);
        main.setPadding(new Insets(16));

        Label heading = new Label("Send a friendly message");
        heading.setFont(Font.font(18));
        TextField nameField = new TextField();
        nameField.setPromptText("Your name");
        ComboBox<String> toneBox = new ComboBox<>();
        toneBox.getItems().addAll("Friendly", "Formal", "Funny");
        toneBox.getSelectionModel().select(0);
        TextArea messageArea = new TextArea();
        messageArea.setPromptText("Write your message...");
        messageArea.setWrapText(true);

        HBox row = new HBox(8, nameField, toneBox);
        row.setPrefHeight(40);
        HBox.setHgrow(nameField, Priority.ALWAYS);

        Button send = new Button("Send");
        Button clear = new Button("Clear");
        Button preview = new Button("Preview");
        Button copy = new Button("Copy");

        HBox actions = new HBox(8, send, clear, preview, copy);
        actions.setAlignment(Pos.CENTER_LEFT);

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPromptText("Output preview...");
        outputArea.setMinHeight(120);

        main.getChildren().addAll(heading, row, messageArea, actions, outputArea);

        // Layout
        HBox root = new HBox(sidebar, main);
        root.getStylesheets().add("app.css");
        HBox.setHgrow(main, Priority.ALWAYS);

        // Functional behavior
        send.setOnAction(e -> {
            String nm = nameField.getText().trim();
            String tone = toneBox.getValue().toLowerCase();
            String body = messageArea.getText().trim();
            if (body.isEmpty()) {
                outputArea.setText("Write a message before sending.");
                messageArea.requestFocus();
                return;
            }
            outputArea.setText(formatMessage(nm, tone, body));
        });

        preview.setOnAction(e -> {
            outputArea.setText(formatMessage(nameField.getText(), toneBox.getValue().toLowerCase(), messageArea.getText()));
        });

        clear.setOnAction(e -> {
            nameField.clear();
            messageArea.clear();
            toneBox.getSelectionModel().select(0);
            outputArea.clear();
        });

        copy.setOnAction(e -> {
            String content = outputArea.getText();
            if (!content.isEmpty()) {
                javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
                javafx.scene.input.ClipboardContent cc = new javafx.scene.input.ClipboardContent();
                cc.putString(content);
                clipboard.setContent(cc);
            }
        });

        // Keyboard: send with Ctrl+Enter or Enter in message area
        messageArea.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.ENTER && ev.isControlDown()) {
                send.fire();
            }
        });

        Scene scene = new Scene(root, 900, 520);
        stage.setScene(scene);
        stage.show();

        // Theme toggle (simple)
        themeToggle.setOnAction(evt -> {
            Scene s = stage.getScene();
            if (s.getStylesheets().contains("app.css")) {
                // toggle by re-applying class on root
                root.getStyleClass().toggle("light");
            }
        });
    }

    private String formatMessage(String name, String tone, String body) {
        if (body == null) body = "";
        name = (name == null || name.isBlank()) ? "Friend" : name.trim();
        StringBuilder sb = new StringBuilder();
        switch (tone) {
            case "formal":
                sb.append("Dear ").append(name).append(",\n\n");
                sb.append(body).append("\n\nSincerely,\n").append(name);
                break;
            case "funny":
                sb.append("Yo ").append(name).append("!\n\n");
                sb.append(body).append(" 😂\n\nCheers,\nYour pal");
                break;
            default:
                sb.append("Hi ").append(name).append(" 👋\n\n");
                sb.append(body).append("\n\nTake care,\n").append(name);
                break;
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        launch();
    }
}
