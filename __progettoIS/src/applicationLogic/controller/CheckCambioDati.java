package applicationLogic.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import applicationLogic.bean.Utente;
import applicationLogic.exception.DatiNonValidi;
import applicationLogic.model.GestioneAccountManager;

@WebServlet("/controllocambiodati")
public class CheckCambioDati extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utente uNew = new Utente();
		Utente uAtt = (Utente) request.getSession().getAttribute("utente");
		
		uNew.setPassword(request.getParameter("password").trim());
		uNew.setNome(request.getParameter("nome").trim());
		uNew.setCognome(request.getParameter("cognome").trim());
		uNew.setEmail(request.getParameter("email").trim());
		
		try {
			uNew = GestioneAccountManager.aggiornaUtente(uNew, uAtt);
			request.getSession().setAttribute("utente", uNew);
			response.sendRedirect(request.getContextPath() + "/utente?id=" + uNew.getUsername());

		} catch (DatiNonValidi e) {
			request.getSession().setAttribute("errore", "cambio dati fallito (dati occupati)");
			response.sendRedirect(request.getContextPath() + "/cambiodati");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
