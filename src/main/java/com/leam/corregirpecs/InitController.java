package com.leam.corregirpecs;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class InitController implements Initializable {
	
	@FXML
	TextField stata;
	@FXML
	Button btStata;
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
	
	private static final String ST1 = "ST1";
	private static final String ST2 = "ST2";
	private static final String PEC0 = "PEC0";
	private static final String PEC1 = "PEC1";
	private static final String PEC2 = "PEC2";
	private static final String corregidas = "corregidas";
	private static final String comprimidas = "comprimidas";
	private static final String descomprimidas = "descomprimidas";
	private static final String originales = "originales";
	private static final String sintaxis = "sintaxis";
	private static final String pdf = "pdf";
	private static final String xlsx = "xlsx";
	private static final String temp_do = "temp.do";
	
	private static final String STATA17 = "Stata17";
	private static final String STATASE = "StataSE-64.exe";
	
	private static final String TRABAJO_NO_EXISTE = "La carpeta de trabajo no existe";
	private static final String ONEDRIVE_NO_EXISTE = "La carpeta de OneDrive no existe";
	private static final String INDIQUE_CURSO = "Indique el curso";
	private static final String INDIQUE_PERIODO = "Indique el período";
	
	
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	int y = YearMonth.now().getYear();
    	int m = YearMonth.now().getMonth().getValue();
    	y = (m<8 ? y-1 : y);
    	this.year.setText(y + "-" + ((y-2000)+1));
    	
    	Path p = Paths.get(System.getenv("ProgramFiles")).resolve(STATA17).resolve(STATASE);
    	this.stata.setText((Files.exists(p) ? p.toString() : ""));
    }
    
    @FXML
    void mnuCarpetas(ActionEvent event) {
    	String e = "";
    	if (Files.exists(Paths.get(this.folder.getText()))) {
    		String curso = this.curso.getText();
    		if (curso.length()>0) {
    			Path p = Paths.get(this.folder.getText()).resolve(this.curso.getText());
    			if (curso.equals(ST1)) {
    				p.resolve(PEC0).resolve(comprimidas).toFile().mkdirs();
    				p.resolve(PEC0).resolve(corregidas).toFile().mkdirs();
    				p.resolve(PEC0).resolve(descomprimidas).toFile().mkdirs();
    				p.resolve(PEC1).resolve(pdf).toFile().mkdirs();
    				p.resolve(PEC1).resolve(xlsx).toFile().mkdirs();
    				p.resolve(PEC2).resolve(corregidas).toFile().mkdirs();
    				p.resolve(PEC2).resolve(originales).toFile().mkdirs();
    				p.resolve(PEC2).resolve(sintaxis).toFile().mkdirs();
    			}
    			if (curso.equals(ST2)) {
    				p.resolve(PEC1).resolve(corregidas).toFile().mkdirs();
    				p.resolve(PEC1).resolve(originales).toFile().mkdirs();
    				p.resolve(PEC1).resolve(sintaxis).toFile().mkdirs();    				
    			}
        		new Alert(AlertType.INFORMATION,"Carpetas creadas").showAndWait();
    		} else { e = INDIQUE_CURSO;}
    	} else { e = TRABAJO_NO_EXISTE;}
    	
    	if (e.length() > 0) {
    		new Alert(AlertType.ERROR,e).showAndWait();
    	}
    }
    
    @FXML
    void mnuAlumnos(ActionEvent event) {
    	try {
        	if (Files.exists(Paths.get(this.folder.getText()))) {
        		if (Files.exists(Paths.get(this.onedrive.getText()))) {
	        		if (this.periodo.getText().length()>0) {
	        			if (this.curso.getText().length()>0) {
	        		        DirectoryChooser chooser = new DirectoryChooser();
	        		        chooser.setTitle("Escoger carpeta Excels");
	        		        File def = new File(System.getProperty("user.home"));
	        		        chooser.setInitialDirectory(def);
	        		        File dir = chooser.showDialog(null);
	        		        if (dir != null) {
	                    		Path p = Paths.get(this.folder.getText()).resolve(this.curso.getText()).resolve(temp_do);
	                    		PrintWriter out = new PrintWriter(p.toString());
	                    		out.println("pecs getdta, p(" + this.periodo.getText() + ") curso(" + this.curso.getText() + 
	                    				") folder(" + dir.getAbsolutePath() + ") online " +
	                    				"dir(" + this.folder.getText() + ") onedrive(" + this.onedrive.getText() + ")");
	                    		out.close();
	                    		
	                    		String[] exec = {this.stata.getText(), "pecs getdta, p(" + this.periodo.getText() + ") curso(" + this.curso.getText() + 
	                    				") folder(" + dir.getAbsolutePath() + ") online " +
	                    				"dir(" + this.folder.getText() + ") onedrive(" + this.onedrive.getText() + ")"};
	                    		Runtime.getRuntime().exec(exec);
	        		        }
	        			} else { new Alert(AlertType.ERROR,INDIQUE_CURSO).showAndWait();}
	        		} else { new Alert(AlertType.ERROR,INDIQUE_PERIODO).showAndWait();}
        		} else { new Alert(AlertType.ERROR,ONEDRIVE_NO_EXISTE).showAndWait();}
        	} else { new Alert(AlertType.ERROR,TRABAJO_NO_EXISTE).showAndWait();}
        } catch(Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void pbStata(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Seleccionar Stata ejecutable");
        File def = new File(System.getenv("ProgramFiles"));
        chooser.setInitialDirectory(def);
        File file = chooser.showOpenDialog(null);
        if (file != null) {
        	this.stata.setText(file.getAbsolutePath());
        }
    }
    
    @FXML
    public void pbFolder(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Escoger carpeta CorregirPECs");
        File def = new File((Files.exists(Paths.get(this.folder.getText())) ? 
        					this.folder.getText() : System.getProperty("user.home")));
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
        File def = new File((Files.exists(Paths.get(this.onedrive.getText())) ? 
				this.onedrive.getText() : System.getProperty("user.home")));
        chooser.setInitialDirectory(def);
        File dir = chooser.showDialog(null);
        if (dir != null) {
        	this.onedrive.setText(dir.getAbsolutePath());
        }
    }
}
