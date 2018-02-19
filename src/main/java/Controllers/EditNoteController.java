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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EditNoteController {
    @FXML
    private Label dateLabel;
    @FXML
    private TextArea noteText;
    @FXML
    private Button btnOk;
    @FXML
    private Button btnCancel;

    private Note note;
    EditNoteController(){}

    EditNoteController(Note note){
        this.note = note;
    }
    
    @FXML
    public void initialize(){
        setEvents();
    }
    private void setEvents(){
        //при нажатии обновляется заметка и закрывается окно
        btnOk.setOnMouseClicked((event -> {
            note.setNote(noteText.getText());
            Runnable updNote = () -> {
                try {
                    NoteDAO.updateNoteById(note.getNoteId(),note.getNote(),note.getCreationDate());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            };
            new Thread(updNote).start();
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
        noteText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (noteText.getText().length() > 100) {
                String s = noteText.getText().substring(0, 100);
                noteText.setText(s);
                Parent root = null;
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/noteTextSizeError.fxml"));
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
    public void configureData(){
        note.setCreationDate(LocalDateTime.now());
        noteText.setText(note.getNote());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        dateLabel.setText(note.getCreationDate().format(formatter));
        setEvents();
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public Note getNote() {
        return note;
    }
}
