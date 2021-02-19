package edu.nju;

import org.apache.commons.cli.*;

public class CommandLineUtil {
    private static CommandLine commandLine;
    private static CommandLineParser parser = new DefaultParser();
    private static Options options = new Options();
    private boolean sideEffect;
    public static final String WRONG_MESSAGE = "Invalid input.";

    /**
     * you can define options here
     * or you can create a func such as [static void defineOptions()] and call it before parse input
     */
    static {
        options.addOption(new Option("h","help",false,"print all helps"));
        options.addOption(new Option("p","print",true,"print arg"));
        options.addOption(new Option("s",false,"change side effect"));

    }

    public void main(String[] args){
        parseInput(args);
        handleOptions();
    }

    /**
     * Print the usage of all options
     */
    private static void printHelpMessage() {
        System.out.println("help");
    }

    /**
     * Parse the input and handle exception
     * @param args origin args form input
     */
    public void parseInput(String[] args) {
        try{
            commandLine = parser.parse(options,args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * You can handle options here or create your own func
     */
    public void handleOptions() {
        if(commandLine == null){
            System.out.println(WRONG_MESSAGE);
            return;
        }
        String[] allargs = commandLine.getArgs();
        int flag = 0;
        if(!commandLine.hasOption("h") && commandLine.hasOption("p")){
            for(int i = 0;i < allargs.length; ++i){
                if(allargs[i].equals("arg0")){
                    flag = 1;
                    break;
                }
            }
            if(flag == 0){
                System.out.println(WRONG_MESSAGE);
                return;
            }
        }
        if(commandLine.hasOption("h")){
            printHelpMessage();
            return;
        }
        if(commandLine.hasOption("s")){
            sideEffect = true;
        }
        if(commandLine.hasOption("p")){
            printArg(commandLine.getOptionValue("p"));
        }
    }

    public void printArg(String arg){
        System.out.println(arg);
    }

    public boolean getSideEffectFlag(){
        return sideEffect;
    }

}
