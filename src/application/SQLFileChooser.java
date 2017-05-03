package application;
 
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
 
public final class SQLFileChooser extends Application {
 
    private Desktop desktop = Desktop.getDesktop();
 
    @Override
    public void start(final Stage stage) {
        stage.setTitle("PHP Class from SQL Generator");
 
        final FileChooser fileChooser = new FileChooser();
        final Label lbl = new Label("Choose an SQL file to generate PHP code");
        lbl.setFont(new Font("Arial", 20));
        final Button openButton = new Button("Open a File...");
       
        openButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    configureFileChooser(fileChooser);
                    File file = fileChooser.showOpenDialog(stage);
                    if (file != null) {
                        try {
                        	openFile(file);
                        } catch (UnknownSQLStatementException e1) {
                        	System.out.println(e1.toString());
                        }
                    }
                }

				private void configureFileChooser(FileChooser fileChooser) {
					// TODO Auto-generated method stub
					
				}
            });
 
 
        final GridPane inputGridPane = new GridPane();
 
        GridPane.setConstraints(openButton, 5, 5);

        inputGridPane.setHgap(6);
        inputGridPane.setVgap(6);
        inputGridPane.getChildren().addAll(openButton, lbl);
        
        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));
 
        stage.setScene(new Scene(rootGroup));
        stage.show();
    }
 
    public static void main(String[] args) {
        Application.launch(args);
    }
    
    private void openFile(File file) throws UnknownSQLStatementException {
        try {
        	if (file.exists()){
        		System.out.println("Opening: " + file.getPath());
        		BufferedReader r = new BufferedReader(new FileReader(file.getPath()));
	        	String s = "", line = null;
	        	while ((line = r.readLine()) != null) {
	        	    s += line;
	        	}
	        	//System.out.print(s);
	        	StringTokenizer st = new StringTokenizer(s);
	        	String firstWord = st.nextToken();
	        	if (!firstWord.equals("CREATE") || !st.nextToken().equals("TABLE")){
	        		throw new UnknownSQLStatementException();
	        	}
	        	
	        	// save the name of the class
	        	String tableClassName = st.nextToken().toLowerCase();
	        	
	        	// get the parameters for the class
	        	List<ClassParameter> params = new ArrayList<ClassParameter>();
	        	
	        	String parametersStr = s.substring(s.indexOf("(") + 1, s.indexOf(");"));
//	        	System.out.println("Parameters string: " + parametersStr);
	        	String[] parametersArr = parametersStr.split(",");
	        	
	        	for (int i = 0; i < parametersArr.length; i++){
	        		String pStr = parametersArr[i].toString();
//	        		System.out.println(pStr);

	        		StringTokenizer ptk = new StringTokenizer(pStr);
	        		String pName = ptk.nextToken();
	        		String typeAndLength = ptk.nextToken();
	        		
//	        		System.out.println("Found parameter: " + pName + "[" + typeAndLength + "]");

	        		ClassParameter cp = new ClassParameter();
	        		cp.setName(pName.toLowerCase());
	        		cp.setType(typeAndLength);

	        		int paramLength = 0;
	        		
	        		try{
	        			paramLength = Integer.parseInt(typeAndLength.substring(typeAndLength.indexOf("(") + 1, typeAndLength.indexOf(")")));
		        		String[] typeLenArr = typeAndLength.split("\\(");
//		        		System.out.println("Setting :" + typeLenArr[0]);
		        		cp.setType(typeLenArr[0]);

	        		} catch (NumberFormatException e){
	        			System.out.println(e.toString());
	        		} catch (StringIndexOutOfBoundsException se){
	        			System.out.println("Parameter " + pName + " does not have length.");
	        		} catch (PatternSyntaxException pse){
	        			System.out.println("Parameter " + pName + " does not have a proper pattern () for length.");
	        		}
	        		
	                if (paramLength > 0){
	                	cp.setLength(paramLength);
	                }
	   
	        		params.add(cp);
	        	}
	        
	        	GeneratedClass gc = new GeneratedClass(tableClassName, params);
	        	String fileContents = gc.export();
	        	
	        	BufferedWriter bw = null;
	    		FileWriter fw = null;
	    		String filename = file.getParentFile() + "/" + tableClassName +"Class.php";
	    		try {
	    			System.out.println("* Generating php output...");
	    			System.out.println("* Writing file "+ filename);

	    			fw = new FileWriter(filename);
	    			bw = new BufferedWriter(fw);
	    			bw.write(fileContents);
	    			System.out.println("~ Done");

	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		} finally {
	    			try {
	    				if (bw != null)
	    					bw.close();
	    				if (fw != null)
	    					fw.close();
	    			} catch (IOException ex) {
	    				ex.printStackTrace();
	    			}
	    		}
	       	}else{
        		System.out.print("file not found");
        	}
            // desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                SQLFileChooser.class.getName()).log(
                    Level.SEVERE, null, ex
                );
        }
    }
    
}