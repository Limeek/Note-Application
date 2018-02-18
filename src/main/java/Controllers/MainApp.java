package Controllers;

import Model.Note;
import Model.NoteDAO;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.SQLException;


public class MainApp extends Application {

    private ObservableList<Note> noteData = FXCollections.observableArrayList();

    public MainApp(){
        Runnable selAllNotes = () -> {
            try {
                NoteDB.dbCreateDB();
                NoteDB.dbCreateTable();
                this.noteData = NoteDAO.findAllNotes();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        new Thread(selAllNotes).start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/notes.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("NoteApp");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
        NoteController controller= loader.getController();
        controller.setMainApp(this);
    }

    public static void main(String args[]) {
        launch(args);
    }

    public ObservableList<Note> getNoteData() {
        return noteData;
    }
}



