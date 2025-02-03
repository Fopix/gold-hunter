package com.gold_hunter.gold_hunter.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class MailSender {
    private final JavaMailSender mailSender;

    public MailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String username;

    public void send(String emailTo, String subject, String heading, String orderUrl, String message, String receipt) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage, "utf-8");

        try {
            mailMessage.setText(mailText("https://goldhunter.fun", heading, orderUrl, message, receipt), true);
            mailMessage.setFrom(username, "Gold Hunter");
            mailMessage.setTo(emailTo);
            mailMessage.setSubject(subject);
        } catch (MessagingException | UnsupportedEncodingException ignored) { }

        mailSender.send(mimeMessage);
    }

    private String mailText(String domain, String heading, String orderUrl, String message, String receipt) {

        if (!receipt.equals("")) {
            message = "<img src=\"https://lknpd.nalog.ru/api/v1/receipt/344107768050/" + receipt + "/print\" width=\"330\" height=\"619\" alt=\"Чек\">\n";
        }

        return "<div link=\"#006ecd\" alink=\"#006ecd\" vlink=\"#006ecd\" text=\"#555555\" style=\"font-family:Helvetica,Arial,sans-serif;font-size:14px;color:#555555\">\n" +
                "    <table style=\"width:538px;background-color:#fef9f5\" align=\"center\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "        <tbody>\n" +
                "        <tr>\n" +
                "             <img src=\"" + domain + "/img/favicon/header.png\" width=\"538\" height=\"65\" alt=\"Gold Hunter\" class=\"CToWUd\">\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td bgcolor=\"#fef9f5\">\n" +
                "                <table width=\"470\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"padding-left:5px;padding-right:5px;padding-bottom:10px\">\n" +
                "                    <tbody>\n" +
                "                    <tr bgcolor=\"#fef9f5\">\n" +
                "                        <td style=\"padding-top:30px;padding-bottom:20px\">\n" +
                "\t\t\t\t\t        <span style=\"font-size:24px;color:#222222;font-family:Arial,Helvetica,sans-serif;font-weight:bold\">\n" +
                "                                " + heading + "\n" +
                "                            </span>\n" +
                "                            <br>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <tr bgcolor=\"#ffffff\">\n" +
                "                        <td style=\"padding:20px;font-size:14px;line-height:18px;color:#555555;border-radius:10px;border: 1px solid #b3b3b3;font-family:Arial,Helvetica,sans-serif\">\n" +
                "                            <table width=\"420\">\n" +
                "                                <tbody>\n" +
                "                                <tr>\n" +
                "                                    <td width=\"420\">\n" +
                "                                        <span>" + message + "</span>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td width=\"420\" style=\"padding-top:10px\">\n" +
                "                                        <a style=\"color:#006ecd\" href=\"" + domain + "/orders/" + orderUrl + "\" target=\"_blank\">Страница вашего заказа</a>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                </tbody>\n" +
                "                            </table>\n" +
                "                        </td>\n" +
                "                        <tr><td style=\"padding-bottom:25px;\"></td></tr>\n" +
                "                    </tr>\n" +
                "                    </tbody>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td bgcolor=\"#fef9f5\" style=\"border-top:1px solid #b3b3b3;line-height:10px\">\n" +
                "                <table width=\"460\" height=\"65\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                    <tbody>\n" +
                "                    <tr valign=\"center\">\n" +
                "                        <td width=\"60\">\n" +
                "                            <a href=\"" + domain + "\" target=\"_blank\">\n" +
                "                                <img src=\"" + domain + "/img/favicon/apple-touch-icon.png\" alt=\"Gold Hunter\" width=\"40\" height=\"40\" hspace=\"0\" vspace=\"0\" border=\"0\" align=\"top\" class=\"CToWUd\">\n" +
                "                            </a>\n" +
                "                        </td>\n" +
                "\n" +
                "                        <td width=\"350\" valign=\"center\">\n" +
                "                            <span style=\"color:#555555;font-size:9px;font-family:Verdana,Arial,Helvetica,sans-serif\">Магазин, в котором есть все необходимое для того, чтобы ваша игра стала комфортной и результативной. Мы, как никто другой, знаем, чего хочет игрок и как ему это получить максимально безопасно и выгодно.</span>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    </tbody>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        </tbody>\n" +
                "    </table>\n" +
                "</div>";
    }
}
