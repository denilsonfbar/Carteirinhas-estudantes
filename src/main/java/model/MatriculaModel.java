package model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import database.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MatriculaModel {

	// Salva uma matricula no banco de dados
	public static void save(Matricula matricula) {
		EntityManager em = DbConnection.getEntityManager();
		EntityTransaction et = em.getTransaction();
		try {
			et.begin();
			em.persist(matricula);
			et.commit();
		} catch (Exception e) {
			et.rollback();
		} finally {
			em.close();
		}
	}
	
	public static ObservableList<Matricula> ListMatriAll() {
		ObservableList<Matricula> AllListMat = FXCollections.observableArrayList();
		EntityManager em = DbConnection.getEntityManager();
		Query q = em.createQuery("FROM Matricula")
		;
		List<Matricula> matriculaList = q.getResultList();
		for (Matricula mat : matriculaList) {
			AllListMat.add(mat);
		}
		return AllListMat;
	}
	
	// Retorna uma lista dos Alunos cadastrados de acordo com a matricula
	public static ObservableList<Matricula> FindAlunoMat(String matPart) {
		ObservableList<Matricula> obsAlunoMatricula = FXCollections.observableArrayList();
		EntityManager em = DbConnection.getEntityManager();
		Query q = em.createQuery("FROM Matricula WHERE numero LIKE :matPart").
				setParameter("matPart", "%" + matPart + "%");
		List<Matricula> matList = q.getResultList();
		for (Matricula mat : matList) {
			obsAlunoMatricula.add(mat);
		}
		return obsAlunoMatricula;		
	}
	
	// Retorna uma lista dos Alunos cadastrados 
	public static ObservableList<Matricula> FindAluno(String matPart) {
		ObservableList<Matricula> obsAlunoMatricula = FXCollections.observableArrayList();
		EntityManager em = DbConnection.getEntityManager();
		Query q = em.createQuery("FROM Matricula WHERE numero =:matPart").
				setParameter("matPart", matPart );
		List<Matricula> matList = q.getResultList();
		for (Matricula mat : matList) {
			System.out.println(mat.getNumero());
			obsAlunoMatricula.add(mat);
		}
		return obsAlunoMatricula;		
	}
	
	public static void deleteMatricula(Matricula del) {
		EntityManager em = DbConnection.getEntityManager();
		em.getTransaction().begin();
		Matricula matricula = em.find(Matricula.class, del.getNumero());
		em.remove(matricula);
		em.getTransaction().commit();

	}
	
	public static void UpdateMat(Matricula update) {
		EntityManager em = DbConnection.getEntityManager();
		em.getTransaction().begin();
		Matricula mat = em.find(Matricula.class, update.getNumero());
		mat.setCurso(update.getCurso());
		em.getTransaction().commit();
	}
	

}
