package model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Carliete
 */

@Entity
@Table(name = "tb_curso")
public class Curso {

	@Id
	@Column(length = 15)
	private String codigo;
	
	@OneToMany(mappedBy = "curso")
	private List<Matricula> matriculas;
	
	@Column(length = 255, nullable = false)
	private String nome;

	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public List<Matricula> getMatriculas(){
		return matriculas;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return codigo + " - " + nome;
	}
	
	public Curso(String codigo, String nome) {
		super();
		this.codigo = codigo;
		this.nome = nome;
	}
	public Curso() {
		super();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
		Curso other = (Curso) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
	
	
	public Curso(String codigo) {
		super();
		this.codigo = codigo;
	}
	
	
}
