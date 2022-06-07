package com.leam.corregirpecs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
	private static final String Stata = "Stata";
	private static final String CursoStata = "Curso Stata";
	
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
	                    				"dir(" + this.folder.getText() + ") onedrive(" +
	                    				this.GetPathCurso().toString() + ")");
	                    		out.close();
	                    		
	                    		String[] exec = {this.stata.getText(), p.toString()};
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
    void mnuGetXlsxSol(ActionEvent event) {
    	try {
        	if (Files.exists(Paths.get(this.folder.getText()))) {
        		if (this.periodo.getText().length()>0) {
            		Path p = Paths.get(this.folder.getText()).resolve(ST1).resolve(temp_do);
            		PrintWriter out = new PrintWriter(p.toString());
            		Path dta = Paths.get(this.folder.getText()).resolve(this.periodo.getText() + "_ST1.dta");
            		Path pPEC1 = Paths.get(this.folder.getText()).resolve(ST1).resolve(PEC1);
            		Path pxlsx = Paths.get(this.folder.getText()).resolve(ST1).resolve(PEC1).resolve(xlsx);
            		out.println("pecs getxlsx, dta(" + dta.toString() + ") pec1(" + pPEC1.toString() + 
            				") xlsx(" + pxlsx.toString() + ")");
            		out.println("pecs get_results_tuto, dta(" + dta.toString() + ") pec1(" + pPEC1.toString() + ")");
            		out.close();
            		
            		String[] exec = {this.stata.getText(), p.toString()};
            		Runtime.getRuntime().exec(exec);
        		} else { new Alert(AlertType.ERROR,INDIQUE_PERIODO).showAndWait();}
        	} else { new Alert(AlertType.ERROR,TRABAJO_NO_EXISTE).showAndWait();}
        } catch(Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }    	
    }
    
    @FXML
    void mnuCorregirPEC1(ActionEvent event) {
    	try {
        	if (Files.exists(Paths.get(this.folder.getText()))) {
        		if (this.periodo.getText().length()>0) {
	                FileChooser chooser = new FileChooser();
	                chooser.setTitle("Seleccionar archivo comprimido");
	                File def = new File(System.getProperty("user.home") + "\\Downloads");
	                chooser.setInitialDirectory(def);
	                File file = chooser.showOpenDialog(null);
	                if (file != null) {
	                	// unzip PDF files
	            		this.unzip(file.getAbsolutePath(),
	            				Paths.get(this.folder.getText()).resolve(ST1).resolve(PEC1).resolve(pdf).toString());
	            		
	            		// open dta with syntax
	            		Path p = Paths.get(this.folder.getText()).resolve(ST1).resolve(temp_do);
	            		PrintWriter out = new PrintWriter(p.toString());
	            		out.println("pecs PEC1, dta(" + Paths.get(this.folder.getText()).resolve(this.periodo.getText() + "_ST1.dta").toString() + 
	            				") pec1(" + Paths.get(this.folder.getText()).resolve(ST1).resolve(PEC1).toString() + ")");
	            		out.close();            		
	            		String[] exec = {this.stata.getText(), p.toString()};
	            		Runtime.getRuntime().exec(exec);
	
	                }
        		} else { new Alert(AlertType.ERROR,INDIQUE_PERIODO).showAndWait();}
        	} else { new Alert(AlertType.ERROR,TRABAJO_NO_EXISTE).showAndWait();}
        } catch(Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }    	    	
    }
    
    @FXML
    public void pbStata(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Seleccionar ejecutable de Stata");
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
    
    public Path GetPathCurso() {
    	Path p = Paths.get(this.onedrive.getText());
    	return p.resolve(Stata).resolve(CursoStata).resolve(this.year.getText());
    }
    
    public void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                	fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
        	new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }        
    }
}
