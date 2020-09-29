package com.lille.ari_vaadin.views;

import java.util.List;

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

@Route
@PWA(name = "Vaadin Application", shortName = "Vaadin App", description = "This is an example Vaadin application.", enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class HomeView extends VerticalLayout{
	
	public static List<Question> questions;
	
	public HomeView(@Autowired GreetService service) {
		questions = service.get10Questions().getResults();
		Text title = new Text("Welcome to Quizz");
		IntegerField nb = new IntegerField();
		Button button1 = new Button("Start");
		button1.addClickListener(e -> button1.getUI().ifPresent(ui -> ui.navigate("quizz")));
		add(title, button1);
		setAlignItems(Alignment.CENTER);
	}

}
