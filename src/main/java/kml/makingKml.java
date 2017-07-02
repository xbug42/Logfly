/*
 * Copyright Gil THOMAS
 * Ce fichier fait partie intégrante du projet Logfly
 * Pour tous les détails sur la licence du projet Logfly
 * Consulter le fichier LICENSE distribué avec le code source
 */
package kml;

import org.xnap.commons.i18n.I18n;
import trackgps.traceGPS;

/**
 *
 * @author gil
 */
public class makingKml {
    
    private I18n i18n;
    
    private static final String RC = "\n";    
    private boolean kmlOK;
    private int errorCode;
    private String kmlString;
    
    private boolean GraphAltiBaro;
    private int kmlReduc;
    private boolean useReduc;
    private boolean withModel;
    private boolean traceSimple;
    private boolean colorByVario;
    private boolean colorBySpeed;
    private boolean colorByAlti;
    private boolean drawThermal;
    private boolean drawScore;
    private boolean replay;
    private boolean export;
    private String exportPath;
    private boolean mail;
    private boolean runGE;
    private int camDessus;
    private int camIncli;
    private int camRecul;
    private int camStep;
    private int camTimer;

    public boolean isKmlOK() {
        return kmlOK;
    }

    public int getErrorCode() {
        return errorCode;
    }
        
    public String getKmlString() {
        return kmlString;
    }

    public int getKmlReduc() {
        return kmlReduc;
    }

    public boolean isGraphAltiBaro() {
        return GraphAltiBaro;
    }

    public void setGraphAltiBaro(boolean GraphAltiBaro) {
        this.GraphAltiBaro = GraphAltiBaro;
    }
        

    public boolean isWithModel() {
        return withModel;
    }            

    public boolean isUseReduc() {
        return useReduc;
    }

    public void setUseReduc(boolean useReduc) {
        this.useReduc = useReduc;
    }

    public boolean isColorByVario() {
        return colorByVario;
    }

    public void setColorByVario(boolean colorByVario) {
        this.colorByVario = colorByVario;
    }

    public boolean isColorBySpeed() {
        return colorBySpeed;
    }

    public void setColorBySpeed(boolean colorBySpeed) {
        this.colorBySpeed = colorBySpeed;
    }

    public boolean isColorByAlti() {
        return colorByAlti;
    }

    public void setColorByAlti(boolean colorByAlti) {
        this.colorByAlti = colorByAlti;
    }

    public int getCamDessus() {
        return camDessus;
    }

    public void setCamDessus(int camDessus) {
        this.camDessus = camDessus;
    }

    public int getCamIncli() {
        return camIncli;
    }

    public void setCamIncli(int camIncli) {
        this.camIncli = camIncli;
    }

    public int getCamRecul() {
        return camRecul;
    }

    public void setCamRecul(int camRecul) {
        this.camRecul = camRecul;
    }

    public int getCamStep() {
        return camStep;
    }

    public void setCamStep(int camStep) {
        this.camStep = camStep;
    }

    public int getCamTimer() {
        return camTimer;
    }

    public void setCamTimer(int camTimer) {
        this.camTimer = camTimer;
    }

    public boolean isDrawThermal() {
        return drawThermal;
    }

    public void setDrawThermal(boolean drawThermal) {
        this.drawThermal = drawThermal;
    }

    public boolean isDrawScore() {
        return drawScore;
    }

    public void setDrawScore(boolean drawScore) {
        this.drawScore = drawScore;
    }

    public boolean isReplay() {
        return replay;
    }

    public void setReplay(boolean replay) {
        this.replay = replay;
    }

    public boolean isTraceSimple() {
        return traceSimple;
    }

    public void setTraceSimple(boolean traceSimple) {
        this.traceSimple = traceSimple;
    }

    public boolean isRunGE() {
        return runGE;
    }

    public void setRunGE(boolean runGE) {
        this.runGE = runGE;
    }

    public boolean isExport() {
        return export;
    }

    public void setExport(boolean export) {
        this.export = export;
    }

    public boolean isMail() {
        return mail;
    }

    public void setMail(boolean mail) {
        this.mail = mail;
    }

    public String getExportPath() {
        return exportPath;
    }

    public void setExportPath(String exportPath) {
        this.exportPath = exportPath;
    }
    
    
    
    
           
    
    public makingKml()  {
        kmlOK = false;
    }
                
    public makingKml(traceGPS traceKml)
    {
//        kmlOK = false;
//        // Réduction de la trace
//        int intervalle = (int) traceKml.getDuree_Vol()/traceKml.Tb_Good_Points.size();
//        if (intervalle < 2)
//            kmlReduc = 5;
//        else if (intervalle < 3)
//            kmlReduc = 4;
//        else if (intervalle < 4)
//            kmlReduc = 3;
//        else if (intervalle < 5)
//            kmlReduc = 2;
//        else
//            kmlReduc = 1;
//        
//        // Paramètres appliqués par défaut en attendant le lancement par fenêtre
//        GraphAltiBaro = false;
//        useReduc = true;
//        withModel = false;
//        colorByVario = false;
//        colorBySpeed = false;
//        colorByAlti = false;
//        drawThermal = false;
//        replay = false;
//        drawScore = true;
//        
//        camDessus = 50;    
//        camTimer = 5;
//        camRecul = 150;
//        camIncli = 80;
//        camStep = 20;
//        replay = false;
//        
//        
//        
//        genKml(traceKml);
        
    }
    
    public void genKml(traceGPS traceKml)  {
         StringBuilder sbKml = new StringBuilder();
        
        try {
            // Réduction de la trace
            if (useReduc) {
            int intervalle = (int) traceKml.getDuree_Vol()/traceKml.Tb_Good_Points.size();
            if (intervalle < 2)
                kmlReduc = 5;
            else if (intervalle < 3)
                kmlReduc = 4;
            else if (intervalle < 4)
                kmlReduc = 3;
            else if (intervalle < 5)
                kmlReduc = 2;
            else
                kmlReduc = 1;
            } else {
                kmlReduc = 1;
            }
        
            errorCode = 0;
            headersKml genHeader = new headersKml(traceKml,this);
            if (genHeader.isHeaderOK())  {
                sbKml.append(genHeader.getKmlString());
                sbKml.append(stylesKml.getStyle());
                trackSimple genTrack = new trackSimple(traceKml, this);
                if (genTrack.isTrackOK()) {
                    sbKml.append(genTrack.getKmlString());                    
                    if (colorByVario || colorBySpeed || colorByAlti)  {
                        trackColor genColored = new trackColor(traceKml,this);
                        sbKml.append(genColored.genDebFolder());
                        if (colorByVario) {
                            if (genColored.genColorVario()) 
                                sbKml.append(genColored.getKmlString());
                            else
                                errorCode = 1014;
                        }
                        if (colorByAlti) {
                            if (genColored.genColorAlti()) 
                                sbKml.append(genColored.getKmlString());  
                            else
                                errorCode = 1016;
                        }
                        if (colorBySpeed) {
                            if (genColored.genColorSpeed()) 
                                sbKml.append(genColored.getKmlString()); 
                            else
                                errorCode = 1018;
                        }
                        sbKml.append(genColored.genFinFolder());
                    }
                    if (drawScore) {
                        
                        trackScore genScore = new trackScore(traceKml,this);
                        if (genScore.genScore())
                            sbKml.append(genScore.getKmlString());
                        else
                            errorCode = 1020;
                    }
                    if (drawThermal) {
                        trackThermals genThermal = new trackThermals(traceKml,this);
                        if (genThermal.genThermals())
                            sbKml.append(genThermal.getKmlString());
                        else
                            errorCode = 1022;
                    }
                    if (replay) {
                        trackReplay genReplay = new trackReplay(traceKml,this);
                        if (genReplay.genKmlReplay())
                            sbKml.append(genReplay.getKmlString());
                        else
                            errorCode = 1024;
                    }
                    // Instructions de clôture du kml  in xLogfly -> kml_Z_Header_Fin
                    sbKml.append("</Document>").append("\n");
                    sbKml.append("</kml>").append("\n");
  
                    kmlOK = true;
                    kmlString = sbKml.toString();
                } else {
                    errorCode = 1012;
                }
            }            
        } catch (Exception e) {
            kmlOK = false;
        } 
        
    }
    
}
