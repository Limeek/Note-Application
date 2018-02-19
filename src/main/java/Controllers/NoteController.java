package Controllers;

import Model.Note;
import Model.NoteDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

//контролллер основного окна
public class NoteController {
    @FXML
    private TilePane noteTilePane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button btnNew;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;

    private MainApp mainApp;

    public NoteController(){
    }

    @FXML
    private void initialize(){
        setEvents();
    }
    //метод в котором задаются event handler'ы кнопкам
    private void setEvents(){
        //при нажатии кнопки открывается новое окно с созданием заметки
        btnNew.setOnMouseClicked((event)->  {
            Parent root;
            try{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/noteProperties.fxml"));
                NewNoteController controller = new NewNoteController();
                loader.setController(controller);
                root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("New Note");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                if(controller.getNote() != null){
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                    TextArea textArea = new TextArea(controller.getNote().getCreationDate().format(formatter) +"\n"+ controller.getNote().getNote());
                    textArea.setUserData(controller.getNote());
                    textArea.setWrapText(true);
                    textArea.setEditable(false);
                    textArea.setPrefSize(200,200);
                    noteTilePane.getChildren().add(0,textArea);
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
        });
        //при нажатии открывается окно с изменением заметки
        btnEdit.setOnMouseClicked((event)->{
            TextArea textArea = (TextArea) noteTilePane.getScene().getFocusOwner();
            Note noteToEdit = (Note) textArea.getUserData();
            Parent root;
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/noteProperties.fxml"));
                EditNoteController controller = new EditNoteController(noteToEdit);
                loader.setController(controller);
                root = loader.load();
                controller.configureData();
                Stage stage = new Stage();
                stage.setTitle("Edit Note");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                textArea.setText(controller.getNote().getCreationDate().format(formatter) +"\n"+ controller.getNote().getNote());
                textArea.toBack();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        });
        //при нажатии удаляется заметка
        btnDelete.setOnMouseClicked((event)->{
            TextArea textArea =(TextArea) noteTilePane.getScene().getFocusOwner();
            Note noteToDelete = (Note) textArea.getUserData();
            try {
                NoteDAO.deleteNoteById(noteToDelete.getNoteId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            mainApp.getNoteData().remove(noteToDelete);
            noteTilePane.getChildren().remove(textArea);
            noteToDelete = null;
        });
    }
    //рисуем экран
    public void configureData(){
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setContent(noteTilePane);
        btnNew.setFocusTraversable(false);
        btnEdit.setFocusTraversable(false);
        btnDelete.setFocusTraversable(false);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        for(Note n: mainApp.getNoteData()){
            TextArea textArea = new TextArea(n.getCreationDate().format(formatter) +"\n"+ n.getNote());
            textArea.setUserData(n);
            textArea.setWrapText(true);
            textArea.setEditable(false);
            textArea.setPrefSize(200,200);
            noteTilePane.getChildren().add(textArea);
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        configureData();
    }
}
