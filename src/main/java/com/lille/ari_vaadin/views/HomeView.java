package com.lille.ari_vaadin.views;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lille.ari_vaadin.models.Question;
import com.lille.ari_vaadin.services.GreetService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

@Route
@PWA(name = "Vaadin Application", shortName = "Vaadin App", description = "This is an example Vaadin application.", enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class HomeView extends VerticalLayout {

	public static List<Question> questions;

	public HomeView(@Autowired GreetService service) {
		questions = service.get10Questions().getResults();
		Text title = new Text("Welcome to Quizz");
		IntegerField nb = new IntegerField();
		Button button1 = new Button("Start");
		button1.addClickListener(e -> button1.getUI().ifPresent(ui -> ui.navigate("quizz")));
		add(imageBanner(), title, button1);
		setAlignItems(Alignment.CENTER);
	}

	public HorizontalLayout imageBanner() {
		HorizontalLayout images = new HorizontalLayout();
		Image univLogo = new Image(
				"https://upload.wikimedia.org/wikipedia/commons/thumb/0/02/Universit%C3%A9_de_Lille_logo.svg/1200px-Universit%C3%A9_de_Lille_logo.svg.png",
				"Université de Lille");
		univLogo.setWidth("300px");
		Image miageLogo = new Image(
				"https://lh3.googleusercontent.com/proxy/cBVv6waxc3HSUjOTvxPyAfD0EEswltj_OVw7ECFWUGHZfQ0ueqXU0QTAiv7pyvUw8MYU9khDrx7xWkj4hrU5mrPZ4VRlrgrTAgY2OPA3f_HZ8h7uUR8__i81BijYv987uP6O",
				"MIAGE");
		miageLogo.setWidth("300px");
		miageLogo.setHeight("70px");
		images.add(univLogo, miageLogo);
		images.setVerticalComponentAlignment(Alignment.CENTER, miageLogo);
		return images;
	}

}
