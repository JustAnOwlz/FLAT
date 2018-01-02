package applicationLogic.controllers;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import applicationLogic.managers.RicercaManager;
import applicationLogic.models.Film;

@WebServlet("/ricerca")
public class RicercaController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String query = request.getParameter("query");
		
		ArrayList<Film> films = RicercaManager.ricercaFilms(query);
		
		JSONArray array = new JSONArray(films);
		JSONObject obj = new JSONObject().put("results", array);
		
		response.setContentType("text/json");
		response.getWriter().write(obj.toString());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
