package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tb_carteirinha")
public class Carteirinha {

	@Id
	private String numero_matricula;

	@JoinColumn(name = "numero_matricula")
	@OneToOne
	@MapsId
	private Matricula matricula;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date expedicao;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date validade;

	@Lob
	@Column(name = "codigo_barras")
	private byte[] codigoBarras;

	@Column(name = "status_impressao", nullable = false, columnDefinition = "boolean default false")
	private Boolean StsImpress;

	public Boolean getStsImpress() {
		return StsImpress;
	}

	public void setStsImpress(Boolean stsImpress) {
		StsImpress = stsImpress;
	}

	public String getNumeroMatricula() {
		return numero_matricula;
	}

	public void setNumeroMatricula(String numeroMatricula) {
		this.numero_matricula = numeroMatricula;
	}

	public Matricula getMatricula() {
		return matricula;
	}

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}

	public Date getExpedicao() {
		return expedicao;
	}

	public void setExpedicao(Date expedicao) {
		this.expedicao = expedicao;
	}

	public void setExpedicao(String expedicao) throws ParseException {
		this.expedicao = new SimpleDateFormat("dd-MM-yyyy").parse(expedicao);
	}

	public Date getValidade() {
		return validade;
	}

	public void setValidade(Date validade) {
		this.validade = validade;
	}

	public void setValidade(String validade) throws ParseException {
		this.validade = new SimpleDateFormat("dd-MM-yyyy").parse(validade);
	}

	public byte[] getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodBarras(byte[] codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numero_matricula == null) ? 0 : numero_matricula.hashCode());
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
		Carteirinha other = (Carteirinha) obj;
		if (numero_matricula == null) {
			if (other.numero_matricula != null)
				return false;
		} else if (!numero_matricula.equals(other.numero_matricula))
			return false;
		return true;
	}

	public Carteirinha(String numero_matricula) {
		super();
		this.numero_matricula = numero_matricula;
	}

	public Carteirinha() {
		super();
	}

}
