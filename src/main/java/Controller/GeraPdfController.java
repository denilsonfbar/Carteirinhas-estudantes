package Controller;

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
import javafx.scene.control.Label;
import model.Aluno;
import model.Carteirinha;
import model.CarteirinhaModel;
import model.Curso;
import model.Matricula;

public class GeraPdfController implements Initializable {

	@FXML
	private Label NaoImpress;

	private PDDocument doc;

	private PDPage myPage;

	private SimpleDateFormat in;
	private SimpleDateFormat out;
	PDImageXObject pdImageFrente;
	PDImageXObject pdImage2Costa;
	PDImageXObject pdCodBarras;
	PDImageXObject pdFoto;

	@FXML
	public void GerarPDF() throws ParseException, IOException {

		try {
			in = new SimpleDateFormat("yyyy-MM-dd");
			out = new SimpleDateFormat("dd/MM/yyyy");
			List<Carteirinha> listIm = CarteirinhaModel.ListImpress();
			int tamanho = listIm.size();
			List<Carteirinha> List4 = new ArrayList<Carteirinha>();
			if (tamanho == 0) {
				Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
				dialogoInfo.setTitle("Erro");
				dialogoInfo.setHeaderText("Não existem carteirinhas para impressão.");
				dialogoInfo.showAndWait();
			} else {

				try {
					doc = new PDDocument();

					String currentDir = System.getProperty("user.dir");
					pdImageFrente = PDImageXObject.createFromFile(currentDir + "/Carteirinhas" + "/Untitled-2.png", doc);
					pdImage2Costa = PDImageXObject.createFromFile(currentDir+ "/Carteirinhas" + "/Untitled-3.png", doc);
					for (int i = 0; i < tamanho; i++) {
						List4.add(listIm.get(i));
						if ((i + 1) % 4 == 0 && i != 0 || i == (tamanho - 1)) {
							Gerar(List4);
							List4.clear();
						}
					}
					Random random = new Random();
					int numero = random.nextInt(100);
					String currentDir1 = System.getProperty("user.dir");
					doc.save(currentDir1 + "/Carteirinhas/" + numero + ".pdf");
					doc.close();
				} catch (IOException e) {
					Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
					dialogoInfo.setTitle("Erro");
					dialogoInfo.setHeaderText("Não foi possivel realizar essa operação.");
					dialogoInfo.showAndWait();
				}

				Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
				dialogoInfo.setTitle("Operação realizada com sucesso");
				dialogoInfo.setHeaderText("Carteirinha gerada com sucesso");
				dialogoInfo.showAndWait();
				CartNaoImpress();

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
			dialogoInfo.setTitle("Erro de dados!");
			dialogoInfo.setHeaderText("Não foi possivel realizar essa operação, favor rever os dados dos alunos!");
			dialogoInfo.showAndWait();

		}
	}

	@FXML
	private void Voltar() throws IOException {
		App.setRoot("principal");
	}

	@FXML
	private void CartNaoImpress() {
		NaoImpress.setText(String.valueOf(CarteirinhaModel.ListImpress().size()));
	}

	private void Gerar(List<Carteirinha> List4) throws ParseException, IOException {
		float posicoesCBarras[] = { 605, 425, 225, 25 };
		float posicoesFotos[] = { 615, 435, 225, 25 };
		float posicoesTexto[] = { 683, 500, 300, 100 };
		float posicoesFotoFundo[] = { 600, 420, 220, 20 };
		float posicoesFotoFundo2[] = { 600, 420, 220, 20 };

		myPage = new PDPage();

		doc.addPage(myPage);

		try (PDPageContentStream cont = new PDPageContentStream(doc, myPage)) {

			for (int i = 0; i < List4.size(); i++) {
				Aluno aluno = List4.get(i).getMatricula().getAluno();
				Matricula matricula = List4.get(i).getMatricula();
				Carteirinha carteirinha = List4.get(i).getMatricula().getCarteirinha();
				Curso curso = matricula.getCurso();
				carteirinha.setStsImpress(true);
				CarteirinhaModel.update(carteirinha);

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
				cont.showText("Curso:" + nomeCurso);
				cont.newLine();
				cont.showText("Matrícula:" + numeroMatricula);
				cont.newLine();
				cont.showText("Data de Nascimento:" + dataNascimento);
				cont.newLine();
				cont.showText("Válida até:" + dataValidade);
				cont.endText();
			}
			cont.close();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		CartNaoImpress();
	}
}
