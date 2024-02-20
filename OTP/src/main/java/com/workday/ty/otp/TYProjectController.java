package com.workday.ty.otp;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


/**
 * Run http://localhost:8081/login to start
 */
@Controller
public class TYProjectController {

    /**
     * When isSixDigitCodeInUse is set to True verifyCode page will show.
     */
    private final boolean isSixDigitCodeInUse = false;

    private TYTeamDetails teamDetails = new TYTeamDetails();

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    /**
     * First Page of Web Application - login page - Asks for Username and Password
     */
    @PostMapping("/login")
    public String postLogin(@ModelAttribute(name = "loginForm") TYTeamDetails login, Model model) {
        teamDetails = TYProjectFileUtils.checkUsernamePassword(login.getUsername(), login.getPassword());
        if (teamDetails == null) {
            model.addAttribute("error", "Incorrect Username & Password");
            return "login";
        } else {
            if (isSixDigitCodeInUse) {
                // Generate OTPC
                String code = TYProjectEmailUtils.generateOneTimeCodeAndMessage();
                // Save code to file with correct usename and email for verification later
                TYProjectFileUtils.addSixDigitCodeToFile(teamDetails.getUsername(), teamDetails.getEmail(), code);
                // Send email with code
                new TYProjectEmailUtils().sendEmail(teamDetails.getUsername(), teamDetails.getEmail(), code);
                return "verifycode";
            } else {
                return "tooldesign";
            }
        }
    }

    /**
     * Second Page of Web Application - Verify Code - Enter your 6-digit code here
     */
    @PostMapping("/verifycode")
    public String postVerifyCode(@ModelAttribute(name = "code") String code, Model model) {

        if (code != null && code.length() != 6) {
            model.addAttribute("error", "Code entered is not six digits!");
        }
        boolean result = TYProjectFileUtils.validateCode(teamDetails.getUsername(), teamDetails.getEmail(), code);
        if (!result) {
            model.addAttribute("error", "Incorrect code");
            return "verifycode";
        } else {
            return "tooldesign";
        }
    }

    /**
     * Third page of Web Application - Design Page - this is where you will add HTML
     */
    @GetMapping("/tooldesign")
    public String getToolDesign() {
        return "tooldesign";
    }

    @RequestMapping(value = "examples/{name}", method = RequestMethod.GET)
    public String getExample(@PathVariable("name") String name) {
        return "examples/" + name;
    }

    @RequestMapping(value = "examples/poster", method = RequestMethod.GET)
    public String getPoster(@RequestParam String name,
                            @RequestParam String age,
                            @RequestParam String address,
                            @RequestParam String skills, Model model) throws IOException {

        URL url = new URL("https://api.openai.com/v1/images/generations");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Authorization", "Bearer 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");

        httpConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        writer.write("{\n  \"prompt\": \"cartoon zombie hunter\",\n  \"n\": 1,\n  \"size\": \"512x512\"\n}");
        writer.flush();
        writer.close();
        httpConn.getOutputStream().close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        System.out.println(response);

        JSONObject obj = new JSONObject(response);

        String image =  obj.getJSONArray("data").getJSONObject(0).getString("url");

        model.addAttribute("image", image);
        model.addAttribute("name", name);
        model.addAttribute("age", age);
        model.addAttribute("address", address);
        model.addAttribute("skills", skills);

        return "examples/poster";
    }
}