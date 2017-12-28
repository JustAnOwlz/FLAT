package applicationLogic.managers;

import java.sql.SQLException;

import applicationLogic.exception.DatiNonValidi;
import applicationLogic.exception.DatiOccupati;
import applicationLogic.models.Utente;
import storage.database.UtenteDAO;

/**
 * Gestisce tutto ci� che riguarda il collegamento fra l'applicazione ed il
 * database per la registrazione
 * 
 * @author Luca
 * @since 1.0
 */
public class RegistrazioneManager {

	private RegistrazioneManager() {/* Costruttore vuoto e privato poich� non istanziabile */}

	/**
	 * Gesisce la registrazione di un utente
	 * 
	 * @param utente
	 *            da inserire nel database
	 * @return l'utente inserito nel database
	 * @throws DatiNonValidi
	 *             nel caso i dati inseriti non siano validi (campi nulli etc)
	 * @throws DatiOccupati
	 *             nel caso i dati inseriti siano gi� occupati nel database
	 */
	public static Utente aggiungiUtente(Utente utente) throws DatiNonValidi, DatiOccupati {

		if (utente.getUsername().equals("")
				|| utente.getPassword().equals("")
				|| utente.getNome().equals("")
				|| utente.getCognome().equals("")
				|| utente.getEmail().equals("")) {

			throw new DatiNonValidi();

		} else {
			try {
				return UtenteDAO.inserisciUtente(utente);
			} catch (SQLException e) {
				throw new DatiOccupati(e.getMessage());
			}
		}
	}
}
