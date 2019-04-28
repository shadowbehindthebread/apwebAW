/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatserveraw;

import java.util.ArrayList;
import javax.servlet.MultipartConfigElement;
import static spark.Spark.*;

public class ChatServer {

    final static ArrayList<String> messages = new ArrayList<>(); //here it is

    public static void main(String[] args) {
        staticFiles.location("static/");
        
        before("*", (req, res) -> {System.out.println("request coming in: " + req.requestMethod() + " : " + req.url());});

        get("/hello", (req, res) -> "Hello World"); //these tell server what to do when they see a request for a specific path
        get("/factorial", (req, res) -> factorial(req));
        post("/login", (req, res) -> login(req));
        get("/getContext", (req, res) -> getContext(req));
        post("/sendMsg", (req, res) -> sendMsg(req)); //to send message
        get("/getNewMsgs", (req, res) -> getNewMsgs(req)); //works with lastRead from a context

    }

    public static String login(spark.Request req) { //recieved a request, will send a response 
        Context ctx = getContext(req);
        ctx.name = req.queryParams("Name"); //query sent by js
        ctx.numRead = 0;
        String result = ctx.name != null ? "ok" : "not nice"; //how would you get a null name??
        System.out.println("beep: " + ctx.name + " is " + result);
        return (result); //return their name if they have one
    }

    public static void verifyLoggedIn(spark.Request req) { //if user is not logged in, tell them to go away
        Context ctx = getContext(req);
        if (ctx.name == null) {
            halt(401, "Go away!");
        }
    }

    public static Context getContext(spark.Request req) { //give valid context for user, and make one if necessary
        Context ctx = req.session().attribute("Context");
        if (ctx == null) { //if this person doesn't exist, make them a context
            ctx = new Context(); //but this is a null context
            req.session().attribute("Context", ctx);
        }
        return ctx;
    }

    public static String factorial(spark.Request req) {
        verifyLoggedIn(req); //make sure logged in first
        String s = req.queryParamOrDefault("Number", "1");
        int fact = Integer.parseInt(s);
        int total = 1;
        for (int i = fact; i < 0; i--) {
            total = total * i;
        }
        String result = String.valueOf(total); // total.toString() didn't work ??
        return result;

    }

    public static String sendMsg(spark.Request req) { //starts with magic code
        verifyLoggedIn(req);
        Context ctx = getContext(req);
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
        req.raw().setAttribute("org.eclipse.jetty.multipartConfig",
                multipartConfigElement);
        
        String message = req.queryParams("Message");
        //String name = ctx.name; //req.queryParams("Name"); //just for printing 
        //System.out.println("MESSAGE (ChatServer.java): " + name + ": " + message);

        synchronized(messages) {
            messages.add(message);
        }
        
        //for (int i = 0; i < messages.size(); i++) {
        //    System.out.println(messages.get(i));
        //}

        return message; //send it back??
    }

    public static String getNewMsgs(spark.Request req) {
        verifyLoggedIn(req);
        Context ctx = getContext(req);
        int lastRead = ctx.numRead;
        //query the server for messages after that point?
        return null;
    }

    //setInterval(getMsgs, 100); - create this timer in login, also no parenthesis after function name
    //Server:
    //create static variable with ArrayList<String> //needs to be static because we're in a static context, routes are not being invoked on an object
    //create methods and then routes for sendMessage and getNewMessages
    //for sendMsg, look up how to handle POST params
    //for getNewMsgs, compile all new array items into big string with new lines in between
    //Client:
    //create function to send a "sendMsg request" (with SendMsg route and post params as explained in OneNote
    //create functioon to retrieve new msgs and append to output, call this function from timer 5-10 times per second - don't create meaningless output

    private static void synchronize(ArrayList<String> messages) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
