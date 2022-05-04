package org.jointheleague;

import java.util.Random;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.jointheleague.features.abstract_classes.Feature;
import org.jointheleague.features.help_embed.plain_old_java_objects.help_embed.HelpEmbed;

public class SendEmbed extends Feature {

    public final String COMMAND = "!embed";
    public int step = 0;
    public String title;
    public String body;

    public SendEmbed(String channelName) {
        super(channelName);
        helpEmbed = new HelpEmbed(COMMAND, "Allows you to send your own embeded message");
    }

    @Override
    public void handle(MessageCreateEvent event) {
    	String messageContent = event.getMessageContent();
    	
        if (messageContent.equals(COMMAND)&&step==0) {
            event.getChannel().sendMessage("What is the title of the embed?");
            step++;
            System.out.println(step);
        }
        else if(step==1&&!messageContent.equals("What is the title of the embed?")&&!messageContent.equals("What is the message of the embed?")){
        	title = messageContent;
            event.getChannel().sendMessage("What is the message of the embed?");
            step++;
            System.out.println(step);
        }
        else if(step==2&&!messageContent.equals("What is the title of the embed?")&&!messageContent.equals("What is the message of the embed?")) {
        	body = messageContent;
        	step = 0;
        	EmbedBuilder eb = new EmbedBuilder();
        	eb.setTitle(title);
        	eb.setDescription(body);
        	event.getChannel().sendMessage(eb);
            System.out.println(step);
        }
    }

}
