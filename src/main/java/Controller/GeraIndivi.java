package Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Aluno;
import model.Carteirinha;
import model.CarteirinhaModel;
import model.Curso;
import model.Matricula;

public class GeraIndivi implements Initializable {

	PDDocument doc;
	private PDPage myPage;
	PDImageXObject pdImageFrente;
	PDImageXObject pdImage2Costa;
	PDImageXObject pdCodBarras;
	PDImageXObject pdFoto;
	private SimpleDateFormat in;
	private SimpleDateFormat out;

	@FXML
	private TableView<CarteirinhaFull> TVConsultAluno = new TableView<CarteirinhaFull>();
	@FXML
	private TableColumn<CarteirinhaFull, String> TCConsultMat = new TableColumn<CarteirinhaFull, String>(
			"numeroMatricula");
	@FXML
	private TableColumn<CarteirinhaFull, String> TCConsultCPF = new TableColumn<CarteirinhaFull, String>("cpfAluno");
	@FXML
	private TableColumn<CarteirinhaFull, String> TCConsultNome = new TableColumn<CarteirinhaFull, String>("nomeAluno");

	@FXML
	private TextField Matricula;

	@FXML
	public void Voltar() throws IOException {
		App.setRoot("principal");
	}

	@FXML
	public void ConsultAlunoMat() throws ParseException {
		TVConsultAluno.getItems().removeAll(TVConsultAluno.getItems());
		TVConsultAluno.getItems().addAll(CarteirinhaFull.findCarteirinhasByMatricula(Matricula.getText()));
	}

	@FXML
	public void GerarIndividu() throws ParseException {
		String cart = TVConsultAluno.getSelectionModel().getSelectedItem().getNumeroMatricula();

		in = new SimpleDateFormat("yyyy-MM-dd");
		out = new SimpleDateFormat("dd/MM/yyyy");

		List<Carteirinha> listIm = CarteirinhaModel.List(cart);
		List<Carteirinha> List4 = new ArrayList<Carteirinha>();
		List4.addAll(listIm);

		try {
			doc = new PDDocument();

			
			
			/*
			 * String currentDir = System.getProperty("user.dir");
			 * 
			 * URL f1 = App.class.getResource("/Untitled-2.png"); URL f3 =
			 * App.class.getResource("/Untitled-3.png");
			 * 
			 * File file = new File(f1.getPath()); String f2 = file.getCanonicalPath();
			 * 
			 * File file2 = new File(f3.getPath()); String f4 = file2.getCanonicalPath();
			 * 
			 */
			String currentDir = System.getProperty("user.dir");
			pdImageFrente = PDImageXObject.createFromFile(currentDir + "/Carteirinhas" + "/Untitled-2.png", doc);
			pdImage2Costa = PDImageXObject.createFromFile(currentDir+ "/Carteirinhas" + "/Untitled-3.png", doc);
						
			Gerar(List4);
			List4.clear();

			Random random = new Random();
			int numero = random.nextInt(100);
			String currentDir1 = System.getProperty("user.dir");
			doc.save(currentDir1 +"/Carteirinhas/"+ numero + ".pdf");
			doc.close();

			Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
			dialogoInfo.setTitle("Operação realizada com sucesso");
			dialogoInfo.setHeaderText("Carteirinha gerada com sucesso.");
			dialogoInfo.showAndWait();

		} catch (IOException e) {
			Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
			dialogoInfo.setTitle("Erro");
			dialogoInfo.setHeaderText("Não foi possivel realizar essa operação.");
			dialogoInfo.showAndWait();
		}
	}

	private void Gerar(List<Carteirinha> List4) throws ParseException, IOException {
		float posicoesCBarras[] = { 605 };
		float posicoesFotos[] = { 615 };
		float posicoesTexto[] = { 683 };
		float posicoesFotoFundo[] = { 600 };
		float posicoesFotoFundo2[] = { 600 };

		myPage = new PDPage();
		doc.addPage(myPage);

		try (PDPageContentStream cont = new PDPageContentStream(doc, myPage)) {

			for (int i = 0; i < List4.size(); i++) {
				Aluno aluno = List4.get(i).getMatricula().getAluno();
				Matricula matricula = List4.get(i).getMatricula();
				Carteirinha carteirinha = List4.get(i).getMatricula().getCarteirinha();
				Curso curso = matricula.getCurso();

				byte[] fotoaluno = aluno.getFoto();
				pdFoto = PDImageXObject.createFromByteArray(doc, fotoaluno, null);
				byte[] codBarras = carteirinha.getCodigoBarras();
				pdCodBarras = PDImageXObject.createFromByteArray(doc, codBarras, null);

				String nome = aluno.getNome();
				String numeroMatricula = matricula.getNumero();
				String nomeCurso = curso.getNome();
				String dataNascimento = out.format(in.parse(aluno.getDataNascimento().toString()));
				String dataValidade = out.format(in.parse(carteirinha.getValidade().toString()));

				cont.drawImage(pdImage2Costa, 20, posicoesFotoFundo[i], 280, 160);
				cont.drawImage(pdImageFrente, 300, posicoesFotoFundo2[i], 280, 160);
				cont.drawImage(pdCodBarras, 100, posicoesCBarras[i], 90, 40);
				cont.drawImage(pdFoto, 305, posicoesFotos[i], 80, 90);

				cont.beginText();
				cont.setFont(PDType1Font.TIMES_ROMAN, 12);
				cont.setLeading(14.5f);
				cont.newLineAtOffset(390, posicoesTexto[i]);

				cont.showText(nome);
				cont.newLine();
				cont.showText("Curso: " + nomeCurso);
				cont.newLine();
				cont.showText("Matrícula: " + numeroMatricula);
				cont.newLine();
				cont.showText("Data de Nascimento: " + dataNascimento);
				cont.newLine();
				cont.showText("Válida até: " + dataValidade);
				cont.endText();
			}
			cont.close();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Chamar a table view Consultar alunos
		TCConsultMat.setCellValueFactory(new PropertyValueFactory<CarteirinhaFull, String>("numeroMatricula"));
		TCConsultCPF.setCellValueFactory(new PropertyValueFactory<CarteirinhaFull, String>("cpfAluno"));
		TCConsultNome.setCellValueFactory(new PropertyValueFactory<CarteirinhaFull, String>("nomeAluno"));
	}
}
