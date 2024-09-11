package com.example.loginscreen;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.loginscreen.model.User;
import com.example.loginscreen.repository.LoginScreenRepository;
import com.example.loginscreen.service.AuthenticationService;
import com.example.loginscreen.service.LoginScreenService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@SpringBootApplication
public class LoginScreenApplication {

    private final AuthenticationService authenticationService;
    private final LoginScreenService loginScreenService;
    private final LoginScreenRepository loginScreenRepository;

    //Main Page
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Main Page");
        System.out.println("************************");

        // Prompt the user to login or register
        System.out.print("1) Login \n");
        System.out.print("2) Register \n");
        System.out.print("9) Debug \n");
        String  choice = scanner.nextLine();
        if (choice.equals("1")) {
            login();
        } else if (choice.equals("2")) {
            register();
        } else if(choice.equals("9")){
            debug();
        } else {
            System.out.println("Please select 1 or 2.");
            run();
        }

        // Close the scanner
        scanner.close();
    }

    //Login Page
    public void login(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Login System");
        System.out.println("***************************");

        // Prompt the user for their username and password
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        // Use the AuthenticationService to authenticate the user
        if (authenticationService.authenticateUser(username, password)) {
            System.out.println("\u001B[32mLogin successful!\u001B[0m");
            mainPage(username);
        } else {
            System.out.println("\u001B[31mLogin failed. Invalid username or password.\u001B[0m");
            run();
        }
        // Close the scanner
        scanner.close();
    }

    //Registery Form
    public void register(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Registration System");
        System.out.println("**********************************");

        // Prompt the user for their username and password
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        while (username.equals("")){
        System.out.print("Please don't leave the username empty: ");
        username = scanner.nextLine();
        }

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        while (password.equals("")){
        System.out.print("Please don't leave the password empty: ");
        password = scanner.nextLine();
        }

        //Post it in the database
        User createUser = new User();
        createUser.setName(username);
        createUser.setPassword(password);
        createUser.setEncryptedPasswordCustom(password);
        loginScreenService.createUser(createUser);

        mainPage(username);
        // Close the scanner
        scanner.close();
    }

    //Main Page Logged in
    public void mainPage(String loggedUser){
        Scanner scanner = new Scanner(System.in);
        User currentUser = loginScreenRepository.findByName(loggedUser).orElse(null);

        System.out.println("Logged in as " + currentUser.getName());
        System.out.println("*****************");

        System.out.print("1) Change Password \n");
        System.out.print("2) Delete your account \n");
        String  choice = scanner.nextLine();
        if (choice.equals("1")) {
            System.out.print("New password: ");
            String newPassword = scanner.nextLine();
            currentUser.setPassword(newPassword);
            loginScreenRepository.save(currentUser);
            mainPage(loggedUser);
        } else if (choice.equals("2")) {
            System.out.print("Are you sure you want to delete your account? (Type 'yes' to confirm.) \n");
            String  deleteChoice = scanner.nextLine();
            if(deleteChoice.equals("yes")){
                loginScreenService.deleteUser(currentUser.getId());
                run();
            } else {
                mainPage(loggedUser);
            }
        } else {
            System.out.println("Please select 1 or 2.");
            mainPage(loggedUser);
        }
        // Close the scanner
        scanner.close();
    }

    public void debug() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Debug");
        System.out.println("*****");
        // Options
        System.out.print("1) Encrypt Password \n");
        String debugChoice = scanner.nextLine();
        if (debugChoice.equals("1")){
            User dummyUser = new User();
            System.out.print("Password to be encrypted: ");
            String passwordToEncrypt = scanner.nextLine();
            String encryptedPasswordDebug = dummyUser.hashPassword(passwordToEncrypt);

            //encrypt the given password and print it out.
            System.out.print("Encrypted password: \u001B[32m" + encryptedPasswordDebug + "\u001B[0m\n");
            // System.out.println("\u001B[31mRed Text\u001B[0m"); // Red text
            // System.out.println("\u001B[32mGreen Text\u001B[0m"); // Green text
            // System.out.println("\u001B[34mBlue Text\u001B[0m"); // Blue text
            // System.out.println("\u001B[30mBlack Text\u001B[0m"); // Black text
            // System.out.println("\u001B[33mYellow Text\u001B[0m"); // Yellow text
            // System.out.println("\u001B[35mMagenta Text\u001B[0m"); // Magenta (Purple) text
            // System.out.println("\u001B[36mCyan Text\u001B[0m"); // Cyan (Light Blue) text
            // System.out.println("\u001B[37mWhite Text\u001B[0m"); // White text
            debug();

        } else {
            debug();
        }
        // Close the scanner
        scanner.close();
    }

    public static void main(String[] args) {
		SpringApplication app = new SpringApplication(LoginScreenApplication.class);
        ConfigurableApplicationContext context = app.run(args);
        LoginScreenApplication application = context.getBean(LoginScreenApplication.class);
        // Call the run method to execute your application logic
        application.run();

        // Close the application context when you're done
        context.close();
	}
}
