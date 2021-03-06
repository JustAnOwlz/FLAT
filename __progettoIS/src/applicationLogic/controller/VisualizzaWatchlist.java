package applicationLogic.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import applicationLogic.bean.FilmLocal;
import applicationLogic.bean.Utente;
import applicationLogic.model.WatchlistManager;

/**
 * Servlet per visualizzare la watchlist
 * 
 * @author Luca
 *
 */
@WebServlet("/watchlist")
public class VisualizzaWatchlist extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Utente u = (Utente) request.getSession().getAttribute("utente");
		ArrayList<FilmLocal> watchlist = WatchlistManager.getWatchlist(u);

		request.setAttribute("watchlist", watchlist);
		request.getRequestDispatcher("watchlist_view.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
