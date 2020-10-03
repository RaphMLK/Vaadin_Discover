package com.lille.ari_vaadin.views;

import java.util.Collections;
import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lille.ari_vaadin.models.Question;
import com.lille.ari_vaadin.services.QuestionService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * Quizz page
 */
@Route(value = "quizz")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

	private VerticalLayout mainLayout;
	private int index_question = 0;
	private boolean finishQuestion = false;
	private int goodAnswers = 0;

	/**
	 * Choose a Vaadin view if the questions are been generated or no Build the
	 * initial UI state for the user accessing the application.
	 *
	 * @param service The message service. Automatically injected Spring managed
	 *                bean.
	 */
	public MainView(@Autowired QuestionService service) {
		System.out.println(HomeView.questions);
		if (HomeView.questions == null) {
			goToHomePage();
		} else {
			loadPage();
		}
	}

	/**
	 * Construct a view if the questions are'nt been generated
	 */
	public void goToHomePage() {
		Text title = new Text("Go to Home Page to generate questions");
		Button button1 = new Button("Start");
		button1.addClickListener(e -> button1.getUI().ifPresent(ui -> ui.navigate("")));
		add(title, button1);
		setAlignItems(Alignment.CENTER);
	}

	/**
	 * Construct a view if the questions are been generated
	 */
	public void loadPage() {
		// display score if all question are been answered
		// display question and potential answer if questions are not answered yet
		if (index_question == HomeView.questions.size()) {
			displayScore();
		} else {
			mainLayout = new VerticalLayout();
			Text question = new Text(
					StringEscapeUtils.unescapeHtml4(HomeView.questions.get(index_question).getQuestion()));

			mainLayout.add(question, responses(HomeView.questions.get(index_question)));
			mainLayout.setAlignItems(Alignment.CENTER);

			// Use custom CSS classes to apply styling. This is defined in
			// shared-styles.css.
			addClassName("centered-content");
			setSizeFull();

			add(mainLayout);
		}
	}

	/**
	 * Generate vertical layout with answer buttons
	 * 
	 * @param question the current question
	 * @return vertical layout contains only buttons
	 */
	public VerticalLayout responses(Question question) {
		VerticalLayout buttons = new VerticalLayout();
		// 2 types of questions : true/false or MCQ (QCM)
		// 2 buttons for true and false or 4 buttons for MCQ
		// You are free to display the buttons are you want
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
			button1.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
			Button button2 = new Button(StringEscapeUtils.unescapeHtml4(allResponses.get(1)),
					e -> answer(allResponses.get(1), question));
			button2.setClassName("buttonResponse");
			button2.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
			firstLayout.add(button1, button2);

			HorizontalLayout secondLayout = new HorizontalLayout();
			Button button3 = new Button(StringEscapeUtils.unescapeHtml4(allResponses.get(2)),
					e -> answer(allResponses.get(2), question));
			button3.setClassName("buttonResponse");
			button3.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
			Button button4 = new Button(StringEscapeUtils.unescapeHtml4(allResponses.get(3)),
					e -> answer(allResponses.get(3), question));
			button4.setClassName("buttonResponse");
			button4.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
			secondLayout.add(button3, button4);

			buttons.add(firstLayout, secondLayout);
			buttons.setAlignItems(Alignment.CENTER);
		}
		return buttons;
	}

	/**
	 * Behavior when you click on answer button : If the question is already
	 * answered : do nothing / Set class name for general view : if good answer the
	 * class name is "goodAnswer", "badAnswer" otherwise / Increment the total of
	 * good answers / Make a notification with the good answer if the answer is
	 * incorrect / Call nextQuestion()
	 * 
	 * @param answer          the answer chosen by the user
	 * @param currentQuestion the current question
	 */
	public void answer(String answer, Question currentQuestion) {
		if (finishQuestion)
			return;
		boolean goodAnswer = answer.equals(currentQuestion.getCorrect_answer());
		addClassName(goodAnswer ? "goodAnswer" : "badAnswer");
		if (goodAnswer) {
			this.goodAnswers++;
		} else {
			Notification notification = new Notification("The good answer is " + currentQuestion.getCorrect_answer(),
					5000);
			notification.open();
		}
		nextQuestion();
		finishQuestion = true;
	}

	/**
	 * Add a button to pass to the next question. The button handle the pass to the
	 * next question
	 */
	public void nextQuestion() {
		Button button = new Button("Next question", e -> {
			removeClassNames("goodAnswer", "badAnswer");
			this.index_question++;
			finishQuestion = false;
			removeAll();
			loadPage();
		});
		button.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		mainLayout.add(button);
		add(mainLayout);
	}

	/**
	 * Display the score
	 */
	public void displayScore() {
		Text score = new Text("Your score is : " + goodAnswers + "/" + HomeView.questions.size());
		Button goHome = new Button("Go to Home page");
		goHome.addClickListener(e -> goHome.getUI().ifPresent(ui -> ui.navigate("")));
		add(score, goHome);
		setAlignItems(Alignment.CENTER);
	}

}
