package storage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import applicationLogic.bean.Film;
import applicationLogic.bean.FilmLocal;
import applicationLogic.bean.Recensione;
import applicationLogic.bean.Utente;
import applicationLogic.bean.Voto;

public class RecensioneDAO {
	private static final String SELECT_BY_UTENTE = "SELECT * FROM `recensioni` WHERE `usernameUtente`=?";
	private static final String SELECT_BY_FILM = "SELECT * FROM `recensioni` WHERE `idFilm`=?";
	private static final String SELECT_BY_UTENTE_AND_FILM = "SELECT * FROM `recensioni` WHERE `idFilm`=? AND `usernameUtente`=?";
	private static final String SELECT_BY_ID = "SELECT * FROM `recensioni` WHERE `id_recensione`=?";
	private static final String SELECT_BY_SEGNALAZIONE = "SELECT * FROM `recensioni` WHERE `segnalata`=1";
	private static final String SELECT = "SELECT * FROM `recensioni`";
	private static final String INSERT = "INSERT INTO `recensioni` (`idFilm`, `usernameUtente`, `voto_recensione`, `testo_recensione`, `titolo_recensione`) VALUES (?, ?, ?, ?, ?);";
	private static final String INSERT_SEGNALAZIONE = "UPDATE `recensioni` SET `segnalata`=? WHERE `id_recensione`=?";
	private static final String DELETE = "DELETE FROM `recensioni` WHERE `id_recensione`=?";

	public static Recensione selectById(Recensione r) throws SQLException {
		Connection con = DBConnection.ottieniConnessione();
		PreparedStatement pst = con.prepareStatement(SELECT_BY_ID);
		pst.setInt(1, r.getId());

		ResultSet rs = pst.executeQuery();
		con.commit();

		Recensione momentaneo = null;
		while (rs.next()) {
			momentaneo = new Recensione();

			Film f = new FilmLocal(rs.getInt("idFilm"));
			Utente u = new Utente(rs.getString("usernameUtente"));
			String titolo = rs.getString("titolo_recensione");
			String testo = rs.getString("testo_recensione");
			int voto = rs.getInt("voto_recensione");
			boolean segnalata = rs.getBoolean("segnalata");

			ArrayList<Voto> voti;
			try {
				voti = VotoDAO.selectByIdReview(r);
			} catch (SQLException e) {
				voti = null;
			}

			momentaneo.setId(r.getId());
			momentaneo.setFilm(f);
			momentaneo.setUtente(u);
			momentaneo.setTitolo(titolo);
			momentaneo.setTesto(testo);
			momentaneo.setVoto(voto);
			momentaneo.setVoti(voti);
			momentaneo.setSegnalata(segnalata);
		}
		return momentaneo;
	}

	public static Recensione selectByUtenteFilm(Recensione r) throws SQLException {
		Connection con = DBConnection.ottieniConnessione();
		PreparedStatement pst = con.prepareStatement(SELECT_BY_UTENTE_AND_FILM);
		pst.setInt(1, r.getFilm().getId());
		pst.setString(2, r.getUtente().getUsername());

		ResultSet rs = pst.executeQuery();
		con.commit();

		Recensione momentaneo = null;
		while (rs.next()) {
			momentaneo = new Recensione();

			int id = rs.getInt("id_recensione");
			Film f = new FilmLocal(rs.getInt("idFilm"));
			Utente u = new Utente(rs.getString("usernameUtente"));
			String titolo = rs.getString("titolo_recensione");
			String testo = rs.getString("testo_recensione");
			int voto = rs.getInt("voto_recensione");
			boolean segnalata = rs.getBoolean("segnalata");

			ArrayList<Voto> voti;
			try {
				voti = VotoDAO.selectByIdReview(r);
			} catch (SQLException e) {
				voti = null;
			}

			momentaneo.setId(id);
			momentaneo.setFilm(f);
			momentaneo.setUtente(u);
			momentaneo.setTitolo(titolo);
			momentaneo.setTesto(testo);
			momentaneo.setVoto(voto);
			momentaneo.setVoti(voti);
			momentaneo.setSegnalata(segnalata);
		}
		return momentaneo;
	}

	public static ArrayList<Recensione> selectByUtente(Utente u) throws SQLException {
		Connection con = DBConnection.ottieniConnessione();
		PreparedStatement pst = con.prepareStatement(SELECT_BY_UTENTE);
		pst.setString(1, u.getUsername());

		ResultSet rs = pst.executeQuery();
		con.commit();

		ArrayList<Recensione> listaRec = new ArrayList<Recensione>();

		while (rs.next()) {
			int id = rs.getInt("id_recensione");
			Film f = new FilmLocal(rs.getInt("idFilm"));
			int voto = rs.getInt("voto_recensione");
			String titolo = rs.getString("titolo_recensione");
			String recensione = rs.getString("testo_recensione");
			boolean segnalata = rs.getBoolean("segnalata");

			ArrayList<Voto> voti;
			try {
				voti = VotoDAO.selectByIdReview(new Recensione(id));
			} catch (SQLException e) {
				voti = null;
			}

			Recensione r = new Recensione(id, f, u, voto, titolo, recensione, voti, segnalata);
			listaRec.add(r);
		}

		DBConnection.riaggiungiConnessione(con);
		return listaRec;
	}

	public static ArrayList<Recensione> selectByFilm(Film f) throws SQLException {
		Connection con = DBConnection.ottieniConnessione();
		PreparedStatement pst = con.prepareStatement(SELECT_BY_FILM);
		pst.setInt(1, f.getId());

		ResultSet rs = pst.executeQuery();
		con.commit();

		ArrayList<Recensione> listaRec = new ArrayList<Recensione>();

		while (rs.next()) {
			int id = rs.getInt("id_recensione");
			Utente u = new Utente(rs.getString("usernameUtente"));
			int voto = rs.getInt("voto_recensione");
			String titolo = rs.getString("titolo_recensione");
			String recensione = rs.getString("testo_recensione");
			boolean segnalata = rs.getBoolean("segnalata");

			ArrayList<Voto> voti;
			try {
				voti = VotoDAO.selectByIdReview(new Recensione(id));
			} catch (SQLException e) {
				voti = null;
			}

			Recensione r = new Recensione(id, f, u, voto, titolo, recensione, voti, segnalata);
			listaRec.add(r);
		}

		DBConnection.riaggiungiConnessione(con);
		return listaRec;
	}

	public static Recensione insert(Recensione r) throws SQLException {
		Connection con = DBConnection.ottieniConnessione();
		PreparedStatement pst = con.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
		pst.setInt(1, r.getFilm().getId());
		pst.setString(2, r.getUtente().getUsername());
		pst.setInt(3, r.getVoto());
		pst.setString(4, r.getTesto());
		pst.setString(5, r.getTitolo());

		pst.executeUpdate();
		ResultSet rs = pst.getGeneratedKeys();

		con.commit();

		int id = -1;
		if (rs.next()) {
			id = rs.getInt(1);
		}
		DBConnection.riaggiungiConnessione(con);

		if (id != -1) {
			r.setId(id);
		}
		return r;
	}

	public static void delete(Recensione r) throws SQLException {
		Connection con = DBConnection.ottieniConnessione();
		PreparedStatement pst = con.prepareStatement(DELETE);
		pst.setInt(1, r.getId());

		pst.executeUpdate();
		con.commit();

		DBConnection.riaggiungiConnessione(con);
	}

	public static void updateSegnalazione(Recensione r) throws SQLException {
		Connection con = DBConnection.ottieniConnessione();
		PreparedStatement pst = con.prepareStatement(INSERT_SEGNALAZIONE);
		pst.setBoolean(1, r.isSegnalata());
		pst.setInt(2, r.getId());

		pst.executeUpdate();
		con.commit();

		DBConnection.riaggiungiConnessione(con);
	}

	public static ArrayList<Recensione> selectBySegnalate() throws SQLException {
		Connection con = DBConnection.ottieniConnessione();
		PreparedStatement pst = con.prepareStatement(SELECT_BY_SEGNALAZIONE);

		ResultSet rs = pst.executeQuery();
		con.commit();

		ArrayList<Recensione> listaRec = new ArrayList<Recensione>();

		while (rs.next()) {
			int id = rs.getInt("id_recensione");
			Utente u = new Utente(rs.getString("usernameUtente"));
			Film f = new FilmLocal(rs.getInt("idFilm"));
			int voto = rs.getInt("voto_recensione");
			String titolo = rs.getString("titolo_recensione");
			String recensione = rs.getString("testo_recensione");
			boolean segnalata = rs.getBoolean("segnalata");

			ArrayList<Voto> voti;
			try {
				voti = VotoDAO.selectByIdReview(new Recensione(id));
			} catch (SQLException e) {
				voti = null;
			}

			Recensione r = new Recensione(id, f, u, voto, titolo, recensione, voti, segnalata);
			listaRec.add(r);
		}

		DBConnection.riaggiungiConnessione(con);
		return listaRec;
	}

	public static ArrayList<Recensione> select() throws SQLException {
		Connection con = DBConnection.ottieniConnessione();
		PreparedStatement pst = con.prepareStatement(SELECT);

		ResultSet rs = pst.executeQuery();
		con.commit();

		ArrayList<Recensione> listaRec = new ArrayList<Recensione>();

		while (rs.next()) {
			int id = rs.getInt("id_recensione");
			Utente u = new Utente(rs.getString("usernameUtente"));
			Film f = new FilmLocal(rs.getInt("idFilm"));
			int voto = rs.getInt("voto_recensione");
			String titolo = rs.getString("titolo_recensione");
			String recensione = rs.getString("testo_recensione");
			boolean segnalata = rs.getBoolean("segnalata");

			ArrayList<Voto> voti;
			try {
				voti = VotoDAO.selectByIdReview(new Recensione(id));
			} catch (SQLException e) {
				voti = null;
			}

			Recensione r = new Recensione(id, f, u, voto, titolo, recensione, voti, segnalata);
			listaRec.add(r);
		}

		DBConnection.riaggiungiConnessione(con);
		return listaRec;
	}
}
