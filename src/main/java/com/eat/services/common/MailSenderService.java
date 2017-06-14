package com.eat.services.common;

import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.AppUserToken;
import com.sparkpost.Client;
import com.sparkpost.exception.SparkPostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class MailSenderService {

    @Autowired
    private Client client;

    public SimpleMailMessage getMail(AppUserToken appUserToken) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String subject = null;
        String text = null;
        String confirmationUrl;
        switch (appUserToken.getTokenType()) {
            case REGISTRATION_TOKEN:
                subject = "Eat! Registration confirmation";
                confirmationUrl = "/api/registration/confirm?token=" + appUserToken.getToken();
                text = "<p>Please confirm your email by clicking on the : "
                        .concat("<a href=\"http://localhost:8080")
                        .concat(confirmationUrl)
                        .concat("\">link</a></p>");
                break;
            case RESET_PASSWORD_TOKEN:
                subject = "Eat! Password changing confirmation";
                confirmationUrl = "/api/new-password?token=" + appUserToken.getToken();
                text = "Please enter new password on the page: " + "http://localhost:8080" + confirmationUrl;
                break;
        }
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        return mailMessage;
    }

    public void sendMail(AppUser appUser, AppUserToken token) {
        SimpleMailMessage mail = getMail(token);
        String email = appUser.getEmail();
        try {
            client.sendMessage(client.getFromEmail(), email, mail.getSubject(), null, mail.getText());
            log.info("Send mail to " + email);
        } catch (SparkPostException e) {
//            TODO throw exception
            e.printStackTrace();
        }
    }

    /*public void smart_mail_delivering_method_with_templates() {
        TransmissionWithRecipientArray transmission = new TransmissionWithRecipientArray();
        List<RecipientAttributes> recipientAttributesList = new ArrayList<>();
        RecipientAttributes recipientAttributes = new RecipientAttributes();
        recipientAttributes.setAddress(new AddressAttributes("maystrovyy@gmail.com"));
        recipientAttributes.setReturnPath("noreply@thronefx.com");
        recipientAttributesList.add(recipientAttributes);
        transmission.setRecipientArray(recipientAttributesList);
        TemplateContentAttributes contentAttributes = new TemplateContentAttributes();
        contentAttributes.setFrom(new AddressAttributes("noreply@thronefx.com"));
        contentAttributes.setSubject("Testing");
        contentAttributes.setText("Hello world!");
        transmission.setContentAttributes(contentAttributes);
        try {
            RestConnection restConnection = new RestConnection(client, null);
            Response response = ResourceTransmissions.create(restConnection, 0, transmission);
        } catch (SparkPostException e) {
            e.printStackTrace();
        }

    }*/

}
