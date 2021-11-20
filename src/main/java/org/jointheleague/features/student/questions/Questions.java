package org.jointheleague.features.student.questions;

import org.javacord.api.event.message.MessageCreateEvent;
import org.jointheleague.features.abstract_classes.Feature;
import org.jointheleague.features.student.questions.QuestionsWrapper;
import org.jointheleague.features.help_embed.plain_old_java_objects.help_embed.HelpEmbed;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;

public class Questions extends Feature {
    public final String COMMAND = "!questionsapi";
    public final String ATTEMPT = "!attempt";

    private WebClient webClient;
    private static final String baseUrl = "https://opentdb.com/api.php";

    public boolean gameStarted = false;
    public int wrongCounter = 0;

    public Questions(String channelName) {
        super(channelName);
        helpEmbed = new HelpEmbed(COMMAND, "type !questionsapi for a question and !attempt to guess. ex: !guess duck . after 3 guesses, use !request answer");

        this.webClient = WebClient
                .builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public void handle(MessageCreateEvent event) {
        String messageContent = event.getMessageContent();
        String[] response;
        String question = "";
        String answer = "";

        if (messageContent.startsWith(COMMAND)) {
            response = getQuestionAndAnswer();
            question = response[0];
            answer = response[1];
            event.getChannel().sendMessage(question);
            gameStarted = true;
        }
        if (gameStarted == true && messageContent.contains(ATTEMPT)) {
            if (messageContent.contains(answer)) {
                event.getChannel().sendMessage("Good job.");
            } else {
                event.getChannel().sendMessage("Wrong.");
                wrongCounter ++;
            }
        }
        if (gameStarted == true && messageContent.contains("!request answer") && wrongCounter >= 3) {
            event.getChannel().sendMessage(answer);
            gameStarted = false;
        }
    }

    public String[] getQuestionAndAnswer() {
        QuestionsWrapper questionData = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("amount", "1")
                        .build())
                .retrieve()
                .bodyToMono(QuestionsWrapper.class)
                .block();
        System.err.println(questionData.getResults().get(0).getQuestion());
        System.err.println(questionData.getResults().get(0).getCorrectAnswer());
        System.err.println(questionData.getResults().get(0).getType());
        /*String[] cutQuestion = questionData.split("question\":\"");
        String[] cutAnswer = cutQuestion[1].split("\",\"incorrect_answers");
        String[] trimmedQA = cutAnswer[0].split("\",\"correct_answer\":\"");

        while(trimmedQA[0].contains("&quot;")) {
            trimmedQA[0] = trimmedQA[0].replace("&quot;","");
        }
        while(trimmedQA[0].contains("&#039;s")) {
            trimmedQA[0] = trimmedQA[0].replace("&#039;s", "");
        }
        for(int i = 0; i < trimmedQA.length; i++) {
            System.err.println(trimmedQA[i]);
        }
       return trimmedQA;*/
        return new String[1];
    }

    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }
}
