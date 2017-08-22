/* 
 * Copyright Gil THOMAS
 * This file forms an integral part of Logfly project
 * See the LICENSE file distributed with source code
 * for details of Logfly licence project
 */
package controller;

import database.dbAdd;
import database.dbSearch;
import dialogues.dialogbox;
import java.io.File;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.Import;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import settings.configProg;
import trackgps.traceGPS;

/**
 *
 * @author gil
 */
public class ImportViewController {
    
    @FXML
    private TableView<Import> tableImp;
    @FXML
    private TableColumn<Import, Boolean> checkCol;
    @FXML
    private TableColumn<Import, String> dateCol;
    @FXML
    private TableColumn<Import, String> heureCol;
    @FXML
    private TableColumn<Import, String> nomFichierCol;
    @FXML
    private TableColumn<Import, String> nomPiloteCol; 
    @FXML
    private Button btnSelect;
    @FXML
    private Button btnDecocher;
    @FXML
    private Button btnMaj;
    @FXML
    private Button btnNettoyage;
    // Localization
    private I18n i18n; 
    
    // Settings
    configProg myConfig;
     
    // Track list
    ArrayList<String> trackPathList = new ArrayList<>(); 
    
    private ObservableList <Import> dataImport; 
     
    private Stage dialogStage;
    
    @FXML
    private HBox buttonBar;
    
    @FXML
    private HBox hbTable;
    
    private RootLayoutController rootController;

    /**
     * recovered settings
     * @param mainConfig 
     */
    public void setMyConfig(configProg mainConfig) {
        this.myConfig = mainConfig;
        i18n = I18nFactory.getI18n("","lang/Messages",ImportViewController.class.getClass().getClassLoader(),myConfig.getLocale(),0);
        winTraduction();
    }
        
    
    @FXML
    private void initialize() {                
        dataImport = FXCollections.observableArrayList();       
               
        dateCol.setCellValueFactory(new PropertyValueFactory<Import, String>("date"));
        heureCol.setCellValueFactory(new PropertyValueFactory<Import, String>("heure"));
        nomFichierCol.setCellValueFactory(new PropertyValueFactory<Import, String>("fileName"));
        nomPiloteCol.setCellValueFactory(new PropertyValueFactory<Import, String>("pilotName"));   
        checkCol.setCellValueFactory(new PropertyValueFactory<Import,Boolean>("checked"));
        checkCol.setCellFactory( CheckBoxTableCell.forTableColumn( checkCol ) );       
    }
       
    /**
     * Select import folder
     * @throws Exception 
     */
    @FXML
    private void selectImpFolder() throws Exception {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(dialogStage);
        if(selectedDirectory != null){
            long tempsDebut = System.currentTimeMillis();
            listTracksFiles(selectedDirectory);
            long tempsFin = System.currentTimeMillis();
            float seconds = (tempsFin - tempsDebut) / 1000F;
            System.out.println("Nombre de traces : "+trackPathList.size());
            System.out.println("Opération effectuée en: "+ Float.toString(seconds) + " secondes.");
            if (trackPathList.size() > 0) {
                InitialiseTableData();
                
            }            
        }
        
    }
    
    /**
     * Recursive track search in selected folder and sub folders
     * @param dir
     * @throws Exception 
     */
    private void listTracksFiles(File dir) throws Exception {        
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            // put in your filter here
            if (fileName.endsWith(".igc") || fileName.endsWith(".IGC")) {
            //if (fileName.endsWith(".igc") || fileName.endsWith(".IGC") || fileName.endsWith(".gpx") || fileName.endsWith(".GPX")) {                                    
                if (files[i].isFile()) {
                    trackPathList.add(files[i].getPath());                   
                }
            }
            if (files[i].isDirectory()) {
                listTracksFiles(files[i]);
            }
        }
    }
    
    /**
     * each IGC or GPX file is decoded
     * Flight parameters pushed in the table
     * if a flight not exists in  the logbook, the line is checked
     */
    private void InitialiseTableData() {
        
        traceGPS myTrace;
        
        dataImport.clear();
        tableImp.getItems().clear();
        // Loop on arraylist of track files with path 
        for (String sTracePath : trackPathList) {
            File fMyTrace = new File(sTracePath);
            if(fMyTrace.exists() && fMyTrace.isFile()) {           
                myTrace = new traceGPS(fMyTrace,false, myConfig);
                if (myTrace.isDecodage()) { 
                    Import imp = new Import();
                    dbSearch rechDeco = new dbSearch(myConfig); 
                    // For debugging
                    boolean resDeco = rechDeco.searchVolByDeco(myTrace.getDT_Deco(),myTrace.getLatDeco(),myTrace.getLongDeco());
                    imp.setChecked(!resDeco);
                    imp.setDate(myTrace.getDate_Vol_SQL());
                    imp.setHeure(myTrace.getDate_Vol_SQL());
                    imp.setFileName(fMyTrace.getName());
                    imp.setPilotName(myTrace.getsPilote());
                    imp.setFilePath(sTracePath); 
                    dataImport.add(imp);
                    // Pattern must be applied in decoding
                    System.out.println("Carnet : "+resDeco+" Decodage : "+fMyTrace.getName()+" "+myTrace.isDecodage()+"  "+myTrace.getsPilote());
                    // Arrêt boulot faire un sout des paramètres attendus pour la tableview
                    // Si c'est bon on poussera dans l'ObservableList
                
                    // Decoding date checking
                    //System.out.println("Decodage : "+myTrace.isDecodage()+"  "+myTrace.getDT_Deco().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }
            
            }
        }        
        System.out.println("DataImp size : "+dataImport.size());
        tableImp.setItems(dataImport); 
        if (tableImp.getItems().size() > 0) {
            buttonBar.setVisible(true);
            hbTable.setVisible(true);            
        }
        
        
        // Just in case...
        // https://examples.javacodegeeks.com/desktop-java/javafx/tableview/javafx-tableview-example/
        
        //List list = new ArrayList();
        //    list.add(new Book("The Thief", "Fuminori Nakamura"));
        //ObservableList data = FXCollections.observableList(list);

       // return data;
    }
    
    /**
     * Uncheck all flights
     */
    @FXML
    private void unCheckList() {
        ObservableList<Import> data = tableImp.getItems();        
        // Comptage du nombre de vols
        for (Import nbItem : data){
            if (nbItem.getChecked())  {               
                nbItem.setChecked(Boolean.FALSE);
            }
        }
    }
    
    /**
     * Checked flights are inserted in the logbook
     */
    public void insertionCarnet()  {
        ObservableList<Import> data = tableImp.getItems();
        int nbVols = 0;
        
        // Comptage du nombre de vols
        for (Import nbItem : data){
            if (nbItem.getChecked())  {               
                nbVols++;
            }
        }
        dialogbox dConfirm = new dialogbox();
        StringBuilder sbMsg = new StringBuilder();
        sbMsg.append(String.valueOf(nbVols)).append(" ").append(i18n.tr("vols à insérer")).append(" ?");
        if (dConfirm.YesNo("", sbMsg.toString()))   {       
            for (Import item : data){
                if (item.getChecked())  {     
                    File fMyTrace = new File(item.getFilePath());
                    if(fMyTrace.exists() && fMyTrace.isFile()) {           
                        traceGPS myTrace = new traceGPS(fMyTrace,true, myConfig);
                        if (myTrace.isDecodage()) { 
                            dbAdd myDbAdd = new dbAdd(myConfig);
                            myDbAdd.addVolCarnet(myTrace);
                        }
                    }
                }
            }
            rootController.changeCarnetView();
        }                                                                  
    }               
    
    /** 
     * check the boolean value of each item to determine checkbox state
     */
    public void getValues(){        
        ObservableList<Import> data = tableImp.getItems();

        for (Import item : data){            
            System.out.println("Valeur table : "+item.getChecked());
        }
    }

    /**
     * set the bridge with RootLayoutController  
     * @param rootlayout 
     */
    public void setRootBridge(RootLayoutController rootlayout) {
        this.rootController = rootlayout; 
        
    }
    
    /**
    * Translate labels of the window
    */
    private void winTraduction() {
        checkCol.setText(i18n.tr("Carnet"));
        dateCol.setText(i18n.tr("Date"));
        heureCol.setText(i18n.tr("Heure"));
        nomFichierCol.setText(i18n.tr("Nom Fichier"));
        nomPiloteCol.setText(i18n.tr("Nom pilote"));
        btnSelect.setText(i18n.tr("Sélectionner un dossier"));
        btnDecocher.setText(i18n.tr("Décocher"));
        btnMaj.setText(i18n.tr("Mise à jour Carnet"));
        btnNettoyage.setText(i18n.tr("Nettoyage dossier"));        
    }
    
}
