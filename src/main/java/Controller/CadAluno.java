package Controller;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import library.MaskedTextField;
import model.Aluno;
import model.AlunoModel;
import model.Carteirinha;
import model.CarteirinhaModel;
import model.Curso;
import model.CursoModel;
import model.Matricula;
import model.MatriculaModel;

public class CadAluno implements Initializable {

	@FXML
	private ComboBox<Curso> CBCurso = new ComboBox<Curso>();
	// Salvar dados do aluno
	@FXML
	private TextField nome;
	@FXML
	private TextField Matricula;
	@FXML
	private MaskedTextField Cpf;
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

	public void updateCBCurso() {
		CBCurso.getItems().removeAll(CBCurso.getItems());
		ObservableList<Curso> listaCursos = CursoModel.getCursos();
		CBCurso.getItems().addAll(listaCursos);
	}

	// Botão de salvar aluno e gerar a carteirinha
	public void SalvarAluno() throws ParseException, FileNotFoundException, IOException {
		if (Cpf.getText().isEmpty() || Matricula.getText().isEmpty() || nome.getText().isEmpty()
				|| CBCurso.getValue().getCodigo().isEmpty()) {
			ErroDadosInseridos();
		} else {

			if (!MatriculaModel.FindAluno(Matricula.getText()).isEmpty()) {
				ErroMatriculaCadastrada();
			} else {

				try {
					// Salvando o aluno

					byte[] fotoAluno = WebcamController.bao.toByteArray();

					Aluno aluno = new Aluno();
					aluno.setCpf(Cpf.getText());
					aluno.setNome(nome.getText());
					aluno.setDataNascimento(formatDate(dataNascimento));
					aluno.setFoto(fotoAluno);

					// Criando a matricula
					Matricula matricula = new Matricula();
					matricula.setNumero(Matricula.getText());
					matricula.setAluno(aluno);
					matricula.setCurso(CBCurso.getValue());

					// Gerando o codigo de barras para carteirinha
					byte[] fotoCodBarras = CodBarras.geraCodigoBarras(Matricula.getText());

					// Salvando a carteirinha
					Carteirinha carteirinha = new Carteirinha();
					carteirinha.setMatricula(matricula);
					carteirinha.setExpedicao(formatDate(expedicao));
					carteirinha.setValidade(formatDate(validadeCarteirinha));
					carteirinha.setStsImpress(false);
					carteirinha.setCodBarras(fotoCodBarras);

					AlunoModel.save(aluno);
					CarteirinhaModel.save(carteirinha);

					Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
					dialogoInfo.setTitle("Operação realizada com sucesso");
					dialogoInfo.setHeaderText("Aluno Salvo");
					dialogoInfo.showAndWait();

					limparTela();

				} catch (Exception e) {
					ErroSalvar();
				}
			}

		}
	}

	// Formatação de data
	public Date formatDate(DatePicker date) {
		LocalDateTime time = date.getValue().atStartOfDay();
		return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
	}

	@FXML
	private void Voltar() throws IOException {
		App.setRoot("principal");
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

	@FXML
	public void FotoAluno() throws IOException {
		byte[] foto = WebcamController.bao.toByteArray();
		Image img = new Image(new ByteArrayInputStream(foto));
		fotoAlunoImage.setImage(img);
	}
//Erro de insersão de dados
	public void ErroDadosInseridos() {
		Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
		dialogoInfo.setTitle("Erro");
		dialogoInfo.setHeaderText("Favor rever os dados inseridos");
		dialogoInfo.showAndWait();
	}
	
//Erro de Salvamento
		public void ErroSalvar() {
			Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
			dialogoInfo.setTitle("Não foi possível salvar as informações.");
			dialogoInfo.setHeaderText("Não foi possível salvar o aluno, favor rever os dados inseridos!");
			dialogoInfo.showAndWait();
		}
//Erro de matricula
	public void ErroMatriculaCadastrada() {
		Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
		dialogoInfo.setTitle("Erro");
		dialogoInfo.setHeaderText("Matricula já cadastrada!");
		dialogoInfo.showAndWait();
	}

	public void limparTela() {
		nome.clear();
		Cpf.clear();
		Matricula.clear();
		validadeCarteirinha.setValue(null);
		expedicao.setValue(null);
		dataNascimento.setValue(null);
		fotoAlunoImage.setImage(null);

	}
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		updateCBCurso();
		
		Matricula.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		        	Matricula.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		    }
		});

	}
}
