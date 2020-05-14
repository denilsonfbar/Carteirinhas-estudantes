package Controller;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Aluno;
import model.AlunoModel;
import model.Carteirinha;
import model.CarteirinhaModel;
import model.Curso;
import model.CursoModel;
import model.Matricula;

public class EditAluno implements Initializable {

	@FXML
	private TextField nome;
	@FXML
	private TextField Matricula;
	@FXML
	private TextField Cpf;
	@FXML
	private TextField Encontrar;
	@FXML
	private DatePicker validadeCarteirinha;
	@FXML
	private DatePicker expedicao;
	@FXML
	private DatePicker dataNascimento;
	@FXML
	public ImageView fotoAlunoImage;

	byte[] fotoAluno;

	@FXML
	private ComboBox<Curso> CBCurso = new ComboBox<Curso>();

	public void updateCBCurso() {
		CBCurso.getItems().removeAll(CBCurso.getItems());
		ObservableList<Curso> listaCursos = CursoModel.getCursos();
		CBCurso.getItems().addAll(listaCursos);
	}

	@FXML
	private void Voltar() throws IOException {
		App.setRoot("ConsultaAluno");
	}

	@FXML
	private void Foto() throws IOException {
		if (Matricula.getText().isEmpty()) {
			Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
			dialogoInfo.setTitle("Erro");
			dialogoInfo.setHeaderText("Não é possivel adicionar foto sem a matrícula.");
			dialogoInfo.showAndWait();
		} else {
			FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/primary.fxml"));
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();

			WebcamController web = (WebcamController) fxmlLoader.getController();
			web.matricula(Matricula.getText());
		}

	}

	public void limparTela() {
		nome.setText("");
		Matricula.setText("");
		Cpf.setText("");
		validadeCarteirinha.setValue(null);
		expedicao.setValue(null);
		dataNascimento.setValue(null);
		fotoAlunoImage.setImage(null);

	}
	@FXML
	public void SalvarAluno() throws ParseException, FileNotFoundException, IOException {
		if (Cpf.getText().isEmpty() || Matricula.getText().isEmpty() || nome.getText().isEmpty()
				|| CBCurso.getValue().getCodigo().isEmpty()) {
			Erro();

		} else {
			try {
				System.out.println("Antes de salvar o aluno");
				// Salvando o aluno

				byte[] fotoAluno = WebcamController.bao.toByteArray();

				Aluno aluno = new Aluno(ConsultAluno.edit);
				aluno.setNome(nome.getText());
				aluno.setDataNascimento(formatDate(dataNascimento));
				aluno.setFoto(fotoAluno);

				// Criando a matricula
				Matricula matricula = new Matricula(ConsultAluno.edit2);
				matricula.setAluno(aluno);
				matricula.setCurso(CBCurso.getValue());

				// Gerando o codigo de barras para carteirinha
				byte[] fotoCodBarras = CodBarras.geraCodigoBarras(Matricula.getText());

				// Salvando a carteirinha
				Carteirinha carteirinha = new Carteirinha(ConsultAluno.edit2);
				carteirinha.setExpedicao(formatDate(expedicao));
				carteirinha.setValidade(formatDate(validadeCarteirinha));
				carteirinha.setStsImpress(false);
				carteirinha.setCodBarras(fotoCodBarras);

				AlunoModel.Update(aluno);
				CarteirinhaModel.UpdateCart(carteirinha);

				Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
				dialogoInfo.setTitle("Operação realizada com sucesso");
				dialogoInfo.setHeaderText("Aluno Salvo");
				dialogoInfo.showAndWait();

			} catch (Exception e) {
				Erro();
			}
		}
	}

	public void Erro() {
		Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
		dialogoInfo.setTitle("Erro");
		dialogoInfo.setHeaderText("Favor rever os dados inseridos");
		dialogoInfo.showAndWait();
	}

	// Formatação de data
	public Date formatDate(DatePicker date) {
		LocalDateTime time = date.getValue().atStartOfDay();
		return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
	}

	@FXML
	public void FotoAluno() throws IOException {
		byte[] foto = WebcamController.bao.toByteArray();
		Image img = new Image(new ByteArrayInputStream(foto));
		fotoAlunoImage.setImage(img);
	}

	public void CarregarInfosAluno() {

		Matricula.setText(ConsultAluno.edit2);
		Cpf.setText(ConsultAluno.edit);

		List<Carteirinha> listIm = CarteirinhaModel.List(ConsultAluno.edit2);
		List<Carteirinha> List4 = new ArrayList<Carteirinha>();
		List4.addAll(listIm);

		for (int i = 0; i < List4.size(); i++) {
			Aluno aluno = List4.get(i).getMatricula().getAluno();
			Matricula matricula = List4.get(i).getMatricula();
			Carteirinha carteirinha = List4.get(i).getMatricula().getCarteirinha();
			

			nome.setText(aluno.getNome());
			
			dataNascimento.setValue(dateToLocalDate(aluno.getDataNascimento()));
			validadeCarteirinha.setValue(dateToLocalDate(carteirinha.getValidade()));
			expedicao.setValue(dateToLocalDate(carteirinha.getExpedicao()));
			
			CBCurso.setValue(matricula.getCurso());
			byte[] fotoaluno = aluno.getFoto();
			Image image = new Image(new ByteArrayInputStream(fotoaluno));
			fotoAlunoImage.setImage(image);


		}
	}

	private LocalDate dateToLocalDate(Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		LocalDate localDate = localDateTime.toLocalDate();
		return localDate;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		updateCBCurso();
		CarregarInfosAluno();
	}

}
