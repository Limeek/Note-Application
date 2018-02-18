package Controllers;

import Model.Note;
import Model.NoteDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
//контроллер окна с созданием заметки
public class NewNoteController {
    @FXML
    private Label dateLabel;
    @FXML
    private TextArea newNote;
    @FXML
    private Button btnOk;
    @FXML
    private Button btnCancel;

    private Note note;

    public NewNoteController(){
    }

    @FXML
    public void initialize(){
        note = new Note();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        dateLabel.setText(note.getCreationDate().format(formatter));
        setEvents();
    }
    //метод в котором задаются event handler'ы кнопкам
    private void setEvents(){
        //при нажатии создается заметка и закрывается окно
        btnOk.setOnMouseClicked((event -> {
            note.setNote(newNote.getText());
            Runnable insNote = () -> {
                try {
                    NoteDAO.insertNote(note.getNote(),note.getCreationDate());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            };
            new Thread(insNote).start();
            Stage stage = (Stage) btnOk.getScene().getWindow();
            stage.close();
        }));
        //при нажатии закрывается окно
        btnCancel.setOnMouseClicked(event -> {
            note = null;
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        });
        //если длина заметки превышает 100 символов, появляется окно ошибки
        newNote.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newNote.getText().length() > 100) {
                String s = newNote.getText().substring(0, 100);
                newNote.setText(s);
                Parent root = null;
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/newNoteSizeError.fxml"));
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.setTitle("Error");
                stage.setScene(new Scene(root));
                stage.showAndWait();
        }
    });
    }

    public Note getNote() {
        return note;
    }

}
