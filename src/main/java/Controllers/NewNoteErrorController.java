package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
//контроллер окошка с ошибкой о превышении длины заметки
public class NewNoteErrorController {
    @FXML
    Button btnOk;
    @FXML
    private void initialize(){
        setEvents();
    }

    public void setEvents(){
        btnOk.setOnMouseClicked(event -> {
            Stage stage = (Stage) btnOk.getScene().getWindow();
            stage.close();
        });
    }

}
