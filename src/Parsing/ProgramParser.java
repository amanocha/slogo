package Parsing;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
public class ProgramParser {
    // "types" and the regular expression patterns that recognize those types
    // note, it is a list because order matters (some patterns may be more generic)
    
    private List<Entry<String, Pattern>> names;
    private List<Entry<String, Pattern>> labels;
    
    public ProgramParser () {
        names = new ArrayList<>();
    }
    // adds the given resource file to this language's recognized types
    public List<Entry<String, Pattern>> addPatterns (String syntax) {
        List<Entry<String, Pattern>> data = new ArrayList<>();
        ResourceBundle resources = ResourceBundle.getBundle(syntax);
        Enumeration<String> iter = resources.getKeys();
        while (iter.hasMoreElements()) {
            String key = iter.nextElement();
            String regex = resources.getString(key);
            data.add(new SimpleEntry<>(key,
                           // THIS IS THE IMPORTANT LINE
                           Pattern.compile(regex, Pattern.CASE_INSENSITIVE)));
        }
        return data;
    }
    // returns the language's type associated with the given text if one exists 
    public String getName (String text) {
        final String ERROR = "NO MATCH";
        for (Entry<String, Pattern> e : names) {
            if (match(text, e.getValue())) {
                return e.getKey();
            }
        }
        return ERROR;
    }
    
    public String getLabel (String text) {
        final String ERROR = "NO MATCH";
        for (Entry<String, Pattern> e : labels) {
            if (match(text, e.getValue())) {
                return e.getKey();
            }
        }
        return ERROR;
    }
    // returns true if the given text matches the given regular expression pattern
    private boolean match (String text, Pattern regex) {
        // THIS IS THE KEY LINE
        return regex.matcher(text).matches();
    }
       
    public void setNames(String path){
        this.names = addPatterns(path);
    }
    public void setLabels(String path){
        this.labels = addPatterns(path);
    }
    
    public List<Entry<String, Pattern>> getNames(){
        return names;
    }
    public List<Entry<String, Pattern>> getLabels(){
        return labels;
    }
    
}