package com.leam.corregirpecs;

import java.io.File;
import java.net.URL;
import java.time.YearMonth;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

public class InitController implements Initializable {
	
	@FXML
	TextField folder;
	@FXML
	Button btFolder;
	@FXML
	TextField onedrive;
	@FXML
	Button btOneDrive;
	@FXML
	TextField year;
	@FXML
	TextField periodo;
	@FXML
	TextField curso;
	
	
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	int y = YearMonth.now().getYear();
    	int m = YearMonth.now().getMonth().getValue();
    	y = (m<8 ? y-1 : y);
    	this.year.setText(y + "-" + ((y-2000)+1));
    }
	
    @FXML
    public void pbFolder(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Escoger carpeta CorregirPECs");
        File def = new File(this.folder.getText()); 
        chooser.setInitialDirectory(def);
        File dir = chooser.showDialog(null);
        if (dir != null) {
        	this.folder.setText(dir.getAbsolutePath());
        }
    }
    
    @FXML
    public void pbOneDrive(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Escoger carpeta OneDrive");
        File def = new File(this.onedrive.getText()); 
        chooser.setInitialDirectory(def);
        File dir = chooser.showDialog(null);
        if (dir != null) {
        	this.onedrive.setText(dir.getAbsolutePath());
        }
    }
}
