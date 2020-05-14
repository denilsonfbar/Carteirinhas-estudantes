package database;

/**
*
* @author Carliete
*/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConexaoBd {

	public static final String DRIVE = "com.mysql.cj.jdbc.Driver";
	public static final String URL = "jdbc:mysql://localhost/carteirinhas_db";
	public static final String USUÁRIO = "cart";
	public static final String SENHA = "123";

	public static Connection getConnection() {
		try {
			Class.forName(DRIVE);
			DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
			return DriverManager.getConnection(URL, USUÁRIO, SENHA);
		} catch (ClassNotFoundException | SQLException ErroSQL) {
			throw new RuntimeException("Erro 1 " + ErroSQL);
		}
	}

	public static void closeConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException ErroSQL) {
			throw new RuntimeException("Erro 2 " + ErroSQL);
		}
	}

	public static void closeConnection(Connection con, PreparedStatement pstm) {
		closeConnection(con);
		try {
			if (pstm != null) {
				pstm.close();
			}
		} catch (SQLException ErroSQL) {
			throw new RuntimeException("Erro 3 " + ErroSQL);
		}
	}

	public static void closeConnection(Connection con, PreparedStatement pstm, ResultSet rs) {
		closeConnection(con, pstm);
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException ErroSQL) {
			throw new RuntimeException("Erro " + ErroSQL);
		}
	}

}
