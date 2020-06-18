package com.analist;// Save file as Client.java

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.models.Node;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class AnalistClient
{

    public static void main(String args[]) throws UnknownHostException, IOException
    {
        final Scanner scn = new Scanner(System.in);
        final String gatewayIP = "http://localhost:8080/restserver/gateway";
        Client client = Client.create();
        WebResource webResource = client
                .resource(gatewayIP + "/analist");
        System.out.println("Type 'exit' to exit\nType 'nodes' to view the number of nodes\nType 'stats [N]' to see last N stats\nType 'infos [N]' to see Std Dev and Avg of last N stats");
        String input = scn.nextLine();

        ClientResponse response;
        while(!input.equals("exit")){
            if(input.equals("nodes") || Pattern.matches("^stats [1-9][0-9]?$|^100$",input)|| Pattern.matches("^infos [1-9][0-9]?$|^100$",input)){
                response = webResource.header("type", input)
                        .get(ClientResponse.class);

                if (response.getStatus() != 412 && response.getStatus() != 200) {
                    throw new RuntimeException("Server unreachable or invalid parameters - "
                            + response.getStatus());
                } else if (response.getStatus() == 412) {
                    throw new RuntimeException("Error in receiving datas - "
                            + response.getStatus());
                }else{
                    String output = response.getEntity(String.class);
                    System.out.println(output);
                }
            }
            else{
                System.out.println("Invalid parameters");
            }

            input = scn.nextLine();
        }


    }
}

