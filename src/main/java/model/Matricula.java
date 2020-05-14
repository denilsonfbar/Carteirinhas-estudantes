package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Carliete
 */

@Entity
@Table(name = "tb_matricula")
public class Matricula {

	@Id
	@Column(length = 15)
	private String numero;

	@ManyToOne(optional = false)
	private Aluno aluno;

	@ManyToOne(optional = false)
	private Curso curso;
	
	@OneToOne(mappedBy = "matricula")
	private Carteirinha carteirinha;

	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Aluno getAluno() {
		return aluno;
	}
	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Curso getCurso() {
		return curso;
	}
	public void setCurso(Curso curso) {
		this.curso = curso;
	}

    public Carteirinha getCarteirinha() {
    	return carteirinha;
    }
    public void setCarteirinha(Carteirinha carteirinha) {
    	this.carteirinha = carteirinha;
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matricula other = (Matricula) obj;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}
	public Matricula(String numero) {
		super();
		this.numero = numero;
	}
	public Matricula() {
		super();
	}
	
	
}
