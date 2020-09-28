package com.lille.ari_vaadin;

import java.util.Collections;
import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lille.ari_vaadin.models.Question;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and use @Route
 * annotation to announce it in a URL as a Spring managed bean. Use the @PWA
 * annotation make the application installable on phones, tablets and some
 * desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every browser
 * tab/window.
 */
@Route
@PWA(name = "Vaadin Application", shortName = "Vaadin App", description = "This is an example Vaadin application.", enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

	private int index_question = 0;
	private List<Question> questions;
	private String backgroundStyle = null;

	/**
	 * Construct a new Vaadin view.
	 * <p>
	 * Build the initial UI state for the user accessing the application.
	 *
	 * @param service The message service. Automatically injected Spring managed
	 *                bean.
	 */
	public MainView(@Autowired GreetService service) {
		this.questions = service.get10Questions().getResults();
		VerticalLayout mainLayout = new VerticalLayout();
		// Use TextField for standard text input
		Text question = new Text(StringEscapeUtils.unescapeHtml4(questions.get(index_question).getQuestion()));

		mainLayout.add(question, responses(questions.get(index_question)));
		mainLayout.setAlignItems(Alignment.CENTER);

		// Use custom CSS classes to apply styling. This is defined in
		// shared-styles.css.
		addClassName("centered-content");
		setSizeFull();

		add(mainLayout);
	}

	public VerticalLayout responses(Question question) {
		VerticalLayout buttons = new VerticalLayout();
		if (question.getType().equals("boolean")) {
			HorizontalLayout horizontalLayout = new HorizontalLayout();
			Button trueButton = new Button("True", e -> answer("True", question));
			trueButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
			Button falseButton = new Button("False", e -> answer("False", question));
			falseButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
			horizontalLayout.add(trueButton, falseButton);
			buttons.add(horizontalLayout);
			buttons.setAlignItems(Alignment.CENTER);
		} else {
			List<String> allResponses = question.getIncorrect_answers();
			allResponses.add(question.getCorrect_answer());
			Collections.shuffle(allResponses);

			HorizontalLayout firstLayout = new HorizontalLayout();
			Button button1 = new Button(StringEscapeUtils.unescapeHtml4(allResponses.get(0)),
					e -> answer(allResponses.get(0), question));
			button1.setClassName("buttonResponse");
			Button button2 = new Button(StringEscapeUtils.unescapeHtml4(allResponses.get(1)),
					e -> answer(allResponses.get(1), question));
			button2.setClassName("buttonResponse");
			firstLayout.add(button1, button2);

			HorizontalLayout secondLayout = new HorizontalLayout();
			Button button3 = new Button(StringEscapeUtils.unescapeHtml4(allResponses.get(2)),
					e -> answer(allResponses.get(2), question));
			button3.setClassName("buttonResponse");
			Button button4 = new Button(StringEscapeUtils.unescapeHtml4(allResponses.get(3)),
					e -> answer(allResponses.get(3), question));
			button4.setClassName("buttonResponse");
			secondLayout.add(button3, button4);

			buttons.add(firstLayout, secondLayout);
			buttons.setAlignItems(Alignment.CENTER);
		}
		return buttons;
	}

	public void answer(String answer, Question currentQuestion) {
		System.out.println(answer.equals(currentQuestion.getCorrect_answer()));
		addClassName(answer.equals(currentQuestion.getCorrect_answer()) ? "goodAnswer" : "badAnswer");
	}

}
