package applicationLogic.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import applicationLogic.exception.DatiNonValidi;
import applicationLogic.exception.DatiOccupati;
import applicationLogic.managers.RegistrazioneManager;
import applicationLogic.models.Utente;

/**
 * Implementazione del controllo della registrazione lato server
 * 
 * @author Luca
 * @since 1.0
 */
@WebServlet("/checkregistrazione")
public class CheckRegistrazioneController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utente u = new Utente();
		u.setUsername(request.getParameter("username").trim());
		u.setPassword(request.getParameter("password").trim());
		u.setNome(request.getParameter("nome").trim());
		u.setCognome(request.getParameter("cognome").trim());
		u.setEmail(request.getParameter("email").trim());
		u.setRuolo("utente");

		try {
			u = RegistrazioneManager.aggiungiUtente(u);
			response.sendRedirect(request.getContextPath());
			/* TODO questo deve ridirezionare verso l'area utente */

		} catch (DatiNonValidi e) {
			request.getSession().setAttribute("errore", "registrazione fallita (dati non validi e/o campi vuoti)");
			response.sendRedirect(request.getContextPath() + "/registrazione");

		} catch (DatiOccupati e) {
			request.getSession().setAttribute("errore", "registrazione fallita (dati occupati) -> " + e.getCampo());
			response.sendRedirect(request.getContextPath() + "/registrazione");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}