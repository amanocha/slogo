package Parsing;

import model.command.turtle.movement.Forward;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


public class ParserRunner {

    private ProgramParser parser;
    private String language;

    private final String REGEX = "\\p{Space}|^#.*";
    private final String PATH = "resources/languages/";
    private final String PATH_SYNTAX = "resources/languages/Syntax";
    private final String METHOD = "run";
    private final String PARAM_COUNT = "knownParams";

    File file = new File("data/examples/sample.logo");

    public ParserRunner (String language, ProgramParser parser) {
        this.parser = parser;
        this.language = language;
        parser.setNames(PATH + language);
        parser.setLabels(PATH_SYNTAX);
    }

    private String[][] parseLine (ProgramParser lang, String input) {
        String[] text = input.split(REGEX);
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> symbols = new ArrayList<String>();
        // System.out.println(lang.mySymbolNames);
        for (int i = 0; i < text.length; i++) {
            
            String s = text[i];
            //System.out.println(s);
            if (s.trim().length() > 0) {
                System.out.println(String.format("%s : %s : %s", s, lang.getName(s),
                                                 lang.getLabel(s)));
                if(isNumber(s)){
                    names.add(s);
                }
                
                else if(s.equals("[") || s.equals("]")){
                    names.add(s);
                }
                else names.add(lang.getName(s));
                symbols.add(lang.getLabel(s));
            }
        }
        ArrayList<String> combined = mergeSymbols(names, symbols);
        String[] sol1 = symbols.toArray(new String[symbols.size()]);
        String[] sol2 = names.toArray(new String[names.size()]);
        String[][] sol = {sol1,sol2};
        return sol;
    }

    public String[][] combineAllLines () throws FileNotFoundException {
        Scanner in = new Scanner(file);
        ArrayList<String> nombres = new ArrayList<String>();
        ArrayList<String> types = new ArrayList<String>();
        while (in.hasNextLine()) {
            String line = in.nextLine();
            String[][] lineArray = parseLine(parser, line);
            for (int i = 0; i < lineArray[0].length; i++) {
                nombres.add(lineArray[1][i]);
                types.add(lineArray[0][i]);
            }
        }
        in.close();
       String[] sol1 = nombres.toArray(new String[nombres.size()]);
       String[] sol2 = types.toArray(new String[types.size()]);
       //System.out.println("am here");
       //for(int i = 0; i < sol1.length; i++) System.out.println(sol1[i]);
       ArrayList<String> combined = mergeSymbols(nombres,types);
       String[] sol3 = combined.toArray(new String[combined.size()]);
       String[][] sol = {sol1,sol2,sol3};
       return sol;

    }

    public String[][] markDepth (String[][] input) throws NoSuchMethodException, SecurityException,
                                               ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
        ArrayList<String> userInput = new ArrayList<String>();
        ArrayList<String> vestigialLabels = new ArrayList<String>();
        Stack<Integer> st = new Stack<Integer>();
        for(int i = 0; i < input[0].length; i++){
            String s = input[0][i];
            userInput.add(s);
            vestigialLabels.add(input[1][i]);
            if(isMethod(s)){
                int count = (int)getParameterCount(s);
                st.push(count);
                userInput.add("{");
                vestigialLabels.add("{");
            }
            else{
                if(!st.isEmpty()){
                    int check = st.pop()-1;
                    st.push(check);
                    if(st.peek() == 0){
                        st.pop();
                        userInput.add("}");
                        vestigialLabels.add("}");
                    }
                }
            }
            System.out.println(st);
        }
        for(int i = 0; i < st.size(); i++){
            userInput.add("}");
        }
        String[] sol1 = userInput.toArray(new String[userInput.size()]);
        String[] sol2 = vestigialLabels.toArray(new String[vestigialLabels.size()]);
        String[][] sol = {sol1,sol2};
        return sol;
    } 

    private boolean isMethod (String input) {
        return parser.getLabel(input).equals("Command");
    }

    private ArrayList<String> mergeSymbols (ArrayList<String> a, ArrayList<String> b) {
        ArrayList<String> sol = new ArrayList<String>();
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).equals("NO MATCH")) {
                sol.add(i, b.get(i));
            }
            else {
                sol.add(i, a.get(i));
            }
        }
        return sol;
    }
    

    private boolean isNumber (String input) {
        try {
            double d = Double.parseDouble(input);
        }
        catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    public Object getParameterCount (String method) throws InstantiationException,
                                                    IllegalAccessException,
                                                    IllegalArgumentException,
                                                    InvocationTargetException,
                                                    NoSuchMethodException, SecurityException,
                                                    ClassNotFoundException, NoSuchFieldException{
        try{
            Class a = Class.forName("model.command.turtle.movement."+method);
            Object count = a.getField(PARAM_COUNT);
            return count;
        }
        catch(Exception e){
            return 0;
        }
        
    }
    
    public void toString (String[] a){
        for(int i = 0; i < a.length; i++){
            System.out.println(a[i]);
        }
    }
}