package Controller;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Aluno;
import model.AlunoModel;
import model.Carteirinha;
import model.CarteirinhaModel;
import model.Matricula;
import model.MatriculaModel;

public class ConsultAluno implements javafx.fxml.Initializable {

	@FXML
	private TextField ConsultMatricula;
	@FXML
	private TextField ConsultCPF;
	@FXML
	private TextField ConsultNome;

	public static String edit;
	public static String edit2;

	@FXML
	private TableView<CarteirinhaFull> TVConsultAluno = new TableView<CarteirinhaFull>();
	@FXML
	private TableColumn<CarteirinhaFull, String> TCConsultMat = new TableColumn<CarteirinhaFull, String>(
			"numeroMatricula");
	@FXML
	private TableColumn<CarteirinhaFull, String> TCConsultCPF = new TableColumn<CarteirinhaFull, String>("cpfAluno");
	@FXML
	private TableColumn<CarteirinhaFull, String> TCConsultNome = new TableColumn<CarteirinhaFull, String>("nomeAluno");

	// Botão de Consultar aluno por Matricula
	@FXML
	public void ConsultAlunoMat() throws ParseException {

		TVConsultAluno.getItems().removeAll(TVConsultAluno.getItems());
		try {
			ObservableList<CarteirinhaFull> ListAluno = (CarteirinhaFull
					.findCarteirinhasByMatricula(ConsultMatricula.getText()));
			if (ListAluno.isEmpty()) {
				Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
				dialogoInfo.setTitle("Aluno não encontrado");
				dialogoInfo.setHeaderText("Aluno não encontrado na base de dados");
				dialogoInfo.showAndWait();
			} else {
				TVConsultAluno.getItems().addAll(ListAluno);
			}
		} catch (Exception e) {
			Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
			dialogoInfo.setTitle("Erro");
			dialogoInfo.setHeaderText("Não foi possivel realizar essa operação");
			dialogoInfo.showAndWait();
		}
	}

	// Botão de Consultar aluno por CPF
	@FXML
	public void ConsultAlunoCPF() throws ParseException {
		TVConsultAluno.getItems().removeAll(TVConsultAluno.getItems());
		try {
			ObservableList<CarteirinhaFull> ListAluno = CarteirinhaFull
					.findCarteirinhasByCpfAluno(ConsultCPF.getText());

			if (ListAluno.isEmpty()) {
				Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
				dialogoInfo.setTitle("Aluno não encontrado");
				dialogoInfo.setHeaderText("Aluno não encontrado na base de dados");
				dialogoInfo.showAndWait();
			} else {
				TVConsultAluno.getItems().addAll(ListAluno);
			}

		} catch (Exception e) {
			Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
			dialogoInfo.setTitle("Erro");
			dialogoInfo.setHeaderText("Não foi possivel realizar essa operação");
			dialogoInfo.showAndWait();
		}
	}

	// Botão de Consultar aluno por NOME
	@FXML
	public void ConsultAlunoName() throws ParseException {
		TVConsultAluno.getItems().removeAll(TVConsultAluno.getItems());
		try {
			ObservableList<CarteirinhaFull> ListAluno = CarteirinhaFull
					.findCarteirinhasByNameAluno(ConsultNome.getText());

			if (ListAluno.isEmpty()) {
				Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
				dialogoInfo.setTitle("Aluno não encontrado");
				dialogoInfo.setHeaderText("Aluno não encontrado na base de dados");
				dialogoInfo.showAndWait();
			} else {
				TVConsultAluno.getItems().addAll(ListAluno);
			}

		} catch (Exception e) {
			Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
			dialogoInfo.setTitle("Erro");
			dialogoInfo.setHeaderText("Não foi possivel realizar essa operação");
			dialogoInfo.showAndWait();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Chamar a table view Consultar alunos
		TCConsultMat.setCellValueFactory(new PropertyValueFactory<CarteirinhaFull, String>("numeroMatricula"));
		TCConsultCPF.setCellValueFactory(new PropertyValueFactory<CarteirinhaFull, String>("cpfAluno"));
		TCConsultNome.setCellValueFactory(new PropertyValueFactory<CarteirinhaFull, String>("nomeAluno"));
	}

	@FXML
	public void DeletarAluno() {
		try {

			String del = TVConsultAluno.getSelectionModel().getSelectedItem().getCpfAluno();
			String del2 = TVConsultAluno.getSelectionModel().getSelectedItem().getNumeroMatricula();

			Carteirinha carteirinha = new Carteirinha(del2);
			Matricula matricula = new Matricula(del2);
			Aluno aluno = new Aluno(del);

			CarteirinhaModel.deleteCarteirinha(carteirinha);
			MatriculaModel.deleteMatricula(matricula);
			AlunoModel.deleteAluno(aluno);

			TVConsultAluno.getItems().removeAll(TVConsultAluno.getItems());

			Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
			dialogoInfo.setTitle("Operação realizada com sucesso");
			dialogoInfo.setHeaderText("Informações excluidas com sucesso");
			dialogoInfo.showAndWait();

		} catch (Exception e) {
			Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
			dialogoInfo.setTitle("Erro");
			dialogoInfo.setHeaderText("Não foi possivel realizar essa operação");
			dialogoInfo.showAndWait();
		}
	}

	@FXML
	private void Voltar() throws IOException {
		App.setRoot("principal");
	}

	@FXML
	private void EditarAluno() throws IOException {
		try {
			edit = TVConsultAluno.getSelectionModel().getSelectedItem().getCpfAluno();
			edit2 = TVConsultAluno.getSelectionModel().getSelectedItem().getNumeroMatricula();
			System.out.println(edit);
			System.out.println(edit2);
			App.setRoot("EditarAluno");
		} catch (Exception e) {
			Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
			dialogoInfo.setTitle("Erro");
			dialogoInfo.setHeaderText("Selecione um aluno.");
			dialogoInfo.showAndWait();
		}
	}
}
