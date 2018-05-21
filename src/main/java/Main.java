/**
 * Created by Anyang on 2018/5/17.
 */
public class Main {
    public static void main(String[] args) throws ParseException {
        String json ="{'employees': [{'firs\\'tNam e': 'Bill', 'age': 12}, {'firstName': 'George', 'lastName': 'Bush'}]}";
        JsonFormatter formatter=new JsonFormatter();
        System.out.println(formatter.formate(json));
    }
}
