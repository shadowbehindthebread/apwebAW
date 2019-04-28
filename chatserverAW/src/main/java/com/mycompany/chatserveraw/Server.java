/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatserveraw;

import java.util.ArrayList;

/**
 *
 * @author audrey
 */
public class Server {
    public static ArrayList<String> messages = new ArrayList(1000); //static messages because I only need one instance
    //length?
    //list of users?

    static void addMessage(String m) { 
        messages.add(m);
    }
    
    
}
