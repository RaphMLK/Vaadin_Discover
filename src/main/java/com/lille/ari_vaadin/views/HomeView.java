package com.lille.ari_vaadin.views;

import java.util.List;

import com.lille.ari_vaadin.services.CocktailService;
import com.vaadin.flow.component.charts.model.HorizontalAlign;
import com.vaadin.flow.component.charts.model.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lille.ari_vaadin.services.GreetService;
import com.lille.ari_vaadin.models.*;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

@Route("")
@PWA(name = "Vaadin Application", shortName = "Vaadin App", description = "This is an example Vaadin application.", enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class HomeView extends VerticalLayout{

	public static List<Question> questions;

	private CocktailService cocktailService;


	@Autowired
	public HomeView( GreetService service, CocktailService cocktailService) {
		this.cocktailService = cocktailService;

		questions = service.get10Questions().getResults();

		Text title = new Text("Bienvenue ! ");

		HorizontalLayout menu = new HorizontalLayout();

		Button buttonQuizz = new Button("Quizz");
		buttonQuizz.addClickListener(e -> buttonQuizz.getUI().ifPresent(ui -> ui.navigate("quizz")));
		menu.addAndExpand(buttonQuizz);

		Button buttonCocktail = new Button("Random coktail");
		buttonCocktail.addClickListener(e -> buttonCocktail.getUI().ifPresent(ui -> ui.navigate("cocktail")));
		menu.addAndExpand(buttonCocktail);

		add(title, menu);
		setAlignItems(Alignment.CENTER);


	}

}
