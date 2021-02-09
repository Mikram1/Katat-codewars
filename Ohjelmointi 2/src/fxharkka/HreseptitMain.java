package fxharkka;
	
import fi.jyu.mit.fxgui.Dialogs;
import harkka.Reseptit;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

/**
 * Pääohjelma Helpot reseptit-ohjelman ajamiseen.
 * @author Mipetoit
 * 16.2.2016
 */
public class HreseptitMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader ldr = new FXMLLoader(getClass().getResource("hreseptitGUI.fxml"));
			final Pane root = ldr.load();
			final HreseptitController reseptitCtrl = (HreseptitController)ldr.getController();
			final Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("Helpot reseptit");
			primaryStage.setOnCloseRequest((event) -> {
				boolean vastaus = Dialogs.showQuestionDialog("Lopetus", "Lopetetaanko?", "Kyllä", "Ei");
				if(vastaus) {
					//reseptitCtrl.tallenna();
					return;
				}
				event.consume();
			});
			
			Reseptit hreseptit = new Reseptit();
			reseptitCtrl.setReseptit(hreseptit);
			reseptitCtrl.lueTiedostosta();
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
