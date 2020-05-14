package model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import database.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CarteirinhaModel {

	// Salva uma carteirinha no banco de dados
	public static void save(Carteirinha carteirinha) {
		EntityManager em = DbConnection.getEntityManager();
		EntityTransaction et = em.getTransaction();
		try {
			et.begin();
			em.persist(carteirinha);
			et.commit();
		} catch (Exception e) {
			et.rollback();
		} finally {
			em.close();
		}
	}

	public static void update(Carteirinha carteirinha) {
		EntityManager em = DbConnection.getEntityManager();
		EntityTransaction et = em.getTransaction();
		try {
			et.begin();
			em.merge(carteirinha);
			et.commit();
		} catch (Exception e) {
			et.rollback();
		}
	}

	public static ObservableList<Carteirinha> ListImpress() {
		Boolean sts = false;
		ObservableList<Carteirinha> allImpress = FXCollections.observableArrayList();
		EntityManager em = DbConnection.getEntityManager();
		Query q = em.createQuery("From Carteirinha where StsImpress = :sts").setParameter("sts", sts);
		List<Carteirinha> listIm = q.getResultList();
		for (Carteirinha cart : listIm) {
			allImpress.add(cart);
		}
		em.close();
		return allImpress;
	}

	public static ObservableList<Carteirinha> List(String mat) {
		ObservableList<Carteirinha> all = FXCollections.observableArrayList();
		EntityManager em = DbConnection.getEntityManager();
		Query q = em.createQuery("FROM Carteirinha where numero_matricula = :mat").setParameter("mat", mat);
		List<Carteirinha> list = q.getResultList();
		for (Carteirinha cart : list) {
			all.add(cart);
		}
		em.close();
		return all;
	}

	public static void deleteCarteirinha(Carteirinha del) {
		EntityManager em = DbConnection.getEntityManager();
		em.getTransaction().begin();
		Carteirinha carteirinha = em.find(Carteirinha.class, del.getNumeroMatricula());
		em.remove(carteirinha);
		em.getTransaction().commit();

	}
	
	public static void UpdateCart(Carteirinha update) {
		EntityManager em = DbConnection.getEntityManager();
		em.getTransaction().begin();
		Carteirinha carteirinha = em.find(Carteirinha.class, update.getNumeroMatricula());
		carteirinha.setExpedicao(update.getExpedicao());
		carteirinha.setCodBarras(update.getCodigoBarras());
		carteirinha.setValidade(update.getValidade());
		carteirinha.setMatricula(update.getMatricula());
		em.getTransaction().commit();
	}
}
