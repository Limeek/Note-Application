package Controllers;

import Model.Note;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
//контролллер основного окна
public class NoteController {
    @FXML
    private TilePane noteTilePane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button btnNew;

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
                loader.setLocation(getClass().getResource("/fxml/newNote.fxml"));
                root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("New Note");
                stage.setScene(new Scene(root));

                NewNoteController controller = loader.getController();

                stage.showAndWait();

                if(controller.getNote() !=null){
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                    TextArea textArea = new TextArea(controller.getNote().getCreationDate().format(formatter) +"\n"+ controller.getNote().getNote());
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
    }
    //рисуем экран
    public void configureData(){
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setContent(noteTilePane);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        for(Note n: mainApp.getNoteData()){
            TextArea textArea = new TextArea(n.getCreationDate().format(formatter) +"\n"+ n.getNote());
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
