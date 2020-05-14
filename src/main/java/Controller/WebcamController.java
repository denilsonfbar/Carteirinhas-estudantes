package Controller;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDiscoveryEvent;
import com.github.sarxos.webcam.WebcamDiscoveryListener;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.util.Initializable;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class WebcamController
		implements Initializable, Runnable, WebcamListener, UncaughtExceptionHandler, WebcamDiscoveryListener {

	private Webcam webcam = null;

	private BufferedImage grabbedImage;
	private ObjectProperty<Image> imgobjweb = new SimpleObjectProperty<Image>();

	private boolean stopCamera = false;

	@FXML
	AnchorPane anchor;

	@FXML
	private Label mat;
	@FXML
	private ImageView imagemweb;
	@FXML
	private ImageView imagemarquivo;

	Image mainiamge;

	public static Image img;
	public byte[] foto;

	public static ByteArrayOutputStream bao = new ByteArrayOutputStream();

	private static final long serialVersionUID = 1L;

	@Override
	public void webcamFound(WebcamDiscoveryEvent event) {
	}

	@Override
	public void webcamGone(WebcamDiscoveryEvent event) {
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
	}

	@Override
	public void webcamOpen(WebcamEvent we) {
		System.out.println("webcam open");
	}

	@Override
	public void webcamClosed(WebcamEvent we) {
		System.out.println("webcam close");
		webcam.close();
	}

	@Override
	public void webcamDisposed(WebcamEvent we) {
		System.out.println("webcam disposed");
	}

	@Override
	public void webcamImageObtained(WebcamEvent we) {
		System.out.println("new Photograph");
	}

	@FXML
	public void TirarFoto() {
		try {
			ImageIO.write(grabbedImage, "jpg", bao);
			foto = bao.toByteArray();
			img = new Image(new ByteArrayInputStream(foto));
			imagemarquivo.setImage(img); 
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void confirmar() throws IOException {
		stopCamera = true;
		bao.close();
		webcam.close();

		Stage stage = (Stage) anchor.getScene().getWindow();
		stage.close();
	}

	protected void startWebCamStream() {

		stopCamera = false;

		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				while (!stopCamera) {
					try {
						if ((grabbedImage = webcam.getImage()) != null) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									mainiamge = SwingFXUtils.toFXImage(grabbedImage, null);
									imgobjweb.set(mainiamge);
								}
							});
							grabbedImage.flush();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				return null;
			}

		};
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
		imagemweb.imageProperty().bind(imgobjweb);
	}

	@Override
	public void run() {
	}
	@Override
	public void initialize() {
		bao.reset();
		webcam = Webcam.getDefault();
		webcam.setViewSize(new Dimension(640, 480));
		webcam.open();
		System.out.println("Inicializando a webcam...");
		startWebCamStream();
	}
	public void matricula(String nome) {
		this.mat.setText(nome);
	}

	@Override
	public void teardown() {

	}
}