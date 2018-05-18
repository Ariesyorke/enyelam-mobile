package com.nyelam.android.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

import com.nyelam.android.R;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

public class TermsAndConditionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);

        initToolbar();

        String agreementText = "<ol type=\"1\">\n" +
                " <br/><b><li>CONDITIONS OF USE</li></b>\n" +
                " <br/> e-nyelam is offered to you, the user, conditioned on your acceptance of the terms, conditions and notices contained or incorporated by reference herein and such additional terms and conditions, agreements, and notices that may apply to any page or section of the Site.\n\n" +
                " <br/><br/> <b><li>OVERVIEW</li></b>\n" +
                " <br/> Your use of this Site constitutes your agreement to all terms, conditions and notices. Please read them carefully. By using this Site, you agree to these Terms and Conditions, as well as any other terms, guidelines or rules that are applicable to any portion of this Site, without limitation or qualification. If you do not agree to these Terms and Conditions, you must exit the Site immediately and discontinue any use of information or products from this Site.\n\n" +
                " <br/><br/> <b><li>MODIFICATION OF THE SITE AND THESE TERMS & CONDITIONS</li></b>\n" +
                " <br/> e-nyelam reserves the right to change, modify, alter, update or discontinue the terms, conditions, and notices under which this Site is offered and the links, content, information, prices and any other materials offered via this Site at any time and from time to time without notice or further obligation to you except as may be provided therein. We have the right to adjust prices from time to time. If for some reason there may have been a price mistake, e-nyelam has the right to refuse the order. By your continued use of the Site following such modifications, alterations, or updates you agree to be bound by such modifications, alterations, or updates.\n\n" +
                " <br/><br/> <b><li>COPYRIGHTS</li></b>\n" +
                " <br/> This Site is owned and operated by e-nyelam. Unless otherwise specified, all materials\n\n" +
                "\n" +
                "on this Site, trademarks, service marks, logos are the property of e-nyelam and are protected by the copyright laws of Indonesia and, throughout the world by the applicable copyright laws. No materials published by e-nyelam on this Site, in whole or in part, may be copied, reproduced, modified, republished, uploaded, posted, transmitted, or distributed in any form or by any means without prior written permission from e-nyelam.\n\n" +
                " <br/><br/> <b><li>SIGN UP</li></b>\n" +
                " <br/> You need to sign up this Site to order the services by insert your username and password. You will get benefits such as newsletters, updates, and special offers by signing up. You will be asked to provide accurate and current information on all registration forms on this Site. You are solely responsible for maintaining the confidentiality of any username and password that you choose or is chosen by your web administrator on your behalf, to access this Site as well as any activity that occur under your username/password. You will not misuse or share your username or password, misrepresent your identity or your affiliation with an entity, impersonate any person or entity, or misstate the origin of any Materials you are exposed to through this Site.\n\n" +
                " <br/><br/> <b><li>ELECTRONIC COMMUNICATIONS</li></b>\n" +
                " <br/> You agree that e-nyelam may send electronic mails to you for the purpose of advising you of changes or additions to this Site, about any of e-nyelam’s services, or for such other purpose(s) as we deem appropriate. If you wish to unsubscribe from our newsletters, please click on \"Newsletters\" in your account page and unsubscribe.\n\n" +
                " <br/><br/> <b><li>SERVICES DESCRIPTIONS</li></b>\n" +
                " <br/> We always try our best to display the information and colors of the products that appear on the Site as accurately as possible. However, we cannot guarantee that your monitor's display of any color will be accurate as the actual colors you see depends on your monitor quality.\n\n" +
                " <br/><br/> <b><li>PRIVACY POLICY</li></b>\n" +
                " <br/> Your information is safe with us. e-nyelam understands that privacy concerns are extremely important to our customers. You can rest assured that any information you submit to us will not be misused, abused or sold to any other parties. We only use your personal information to complete your order.\n\n" +
                " <br/><br/> <b><li>INDEMNITY</li></b>\n" +
                " <br/> You agree to indemnify, defend and hold e-nyelam harmless from and against any and all third party claims, liabilities, damages, losses or expenses (including reasonable attorney's fees and costs) arising out of, based on or in connection with your access and/or use of this Site.\n\n" +
                " <br/><br/> <b><li>DISCLAIMER</li></b>\n" +
                " <br/> e-nyelam assumes no responsibility for accuracy, correctness, timeliness, or content of the Materials provided on this Site. You should not assume that the Materials on this Site are continuously updated or otherwise contain current information. e-nyelam is not responsible for supplying content or materials from the Site that have expired or have been removed.\n\n" +
                " <br/><br/> <b><li>APPLICABLE LAWS</li></b>\n" +
                " <br/> These Terms and Conditions are governed by the law in force in Indonesia.\n\n" +
                " <br/><br/> <b><li>QUESTIONS AND FEEDBACK</li></b>\n" +
                " <br/> We welcome your questions, comments, and concerns about privacy or any of the information\n" +
                "\n" +
                "collected from you or about you. Please send us any and all feedback pertaining to privacy, or any other issue.\n" +
                "\n" +
                "Legal Notice\n\n" +
                " <br/><br/> <b><li>CANCELLATION</li></b>\n" +
                "<ol>"+
                "<li>When making a reservation through the e-Nyelam Apps, you agree to all terms and conditions provided by diving centers, including their cancellation policies. E-Nyelam is not responsible to any infringement caused by customer’s special requests to the diving centres. Therefore, you need to read all terms and condition carefully.</li>\n\n" +
                "<li>In terms of customer’s cancellation, E-Nyelam has authority to hold and/or charge your payment as cancellation fees.</li>\n\n" +
                "</ol>"+
                " <br/><br/> <b><li>ADDITIONAL COSTS AND REFUND</li></b>\n" +
                "<ol>"+
                "<li>All quotations are subject to the terms and conditions stated herein.</li>\n\n" +
                "<li>All published prices are subject to change without prior notice, depending on the amount of reservation and/or quota in the diving centers.</li>\n" +
                "<li>All published prices include tax and additional fees; however, in a certain condition, there might be additional cost required by diving centers such as guide tips, personal costs, and/or other services. Therefore, the customer is responsible to verify the total costs and other regulation before making the payment.</li>\n" +
                "<li>Cost of additional services, including the use of credit card or transfer fees, are charged to the customers. E-Nyelam will notify the customers via phone call or email if there is the lack of payment.</li>\n\n" +
                "<li>If the customer cancels the booking, all the payment is non-refundable.</li>\n\n" +
                "<li>E-Nyelam will refund the payment to the customers if cancellation is caused by force majeur. However, we will deduct it due to service fees. If you have a question, need help, or need to report a problem, please call our customer service or you may send email to:info@e-nyelam.com</li>\n\n" +
                "<li>We do not take any responsibility and we are not liable for any transaction caused by, but not limited to: the lack of payment.</li>\n\n" +
                "<li>We will annul the transaction caused by payment delays.</li>\n\n" +
                "</ol>"+
                "\n" +
                "</ol><br/><br/><center><b>e-nyelam is a brand by PT Wimiku Indo Digital</b></center>\n" +
                " <br/><center>Copyright 2018 All Rights Reserved.</center>\n";

        HtmlTextView htmlTextView = (HtmlTextView) findViewById(R.id.term_textView);

        // loads html from string and displays http://www.example.com/cat_pic.png from the Internet
        htmlTextView.setHtml(agreementText,
                new HtmlHttpImageGetter(htmlTextView));


        WebView webView = (WebView) findViewById(R.id.webView);
        webView.loadData(agreementText, "text/html", "UTF-8");


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
        toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }



}
