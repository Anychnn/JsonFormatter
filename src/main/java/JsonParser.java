/**
 * Created by any on 2017/2/19.
 * Json解析器
 */
public class JsonParser {

    private JsonLexer lexer;
    private JsonToken look;

    private StringBuffer outPut=new StringBuffer();
    private int indent=0;
    private static String indentStr =" ";

    public JsonParser(JsonLexer lexer){
        this.lexer=lexer;
    }

    public void parse() throws ParseException {
        move();
        obj();
        match(JsonToken.Type.Eof);
    }

    public void move() throws ParseException {
        look=lexer.read();
    }

    public StringBuffer getOutPut(){
        return outPut;
    }

    public void generateStr(){
        if(look.getType()== JsonToken.Type.Eof){
            //结束
            return;
        }
        if(look.getType()== JsonToken.Type.Lbb||look.getType()== JsonToken.Type.Lmb){
            outPut.append(look.getValue()+"\n");
            indent++;
            for (int i = 0; i < indent; i++) {
                outPut.append(indentStr);
            }
        }else if(look.getType()== JsonToken.Type.Rbb||look.getType()== JsonToken.Type.Rmb){
            indent--;
            outPut.append("\n");
            for (int i = 0; i < indent; i++) {
                outPut.append(indentStr);
            }
            outPut.append(look.getValue());
        }else if(look.getType()==JsonToken.Type.Comma){
            outPut.append(look.getValue());
            outPut.append("\n");
            for (int i = 0; i < indent; i++) {
                outPut.append(indentStr);
            }
        }else{
            outPut.append(look.getValue());
        }
    }
    public void match(JsonToken.Type type) throws ParseException {
        if(look.getType()==type){
            generateStr();
            look=lexer.read();
        }else{
            throw new ParseException("json格式异常："+type+" required");
        }
    }

    void error() throws ParseException {
        throw new ParseException("json格式异常：");
    }

    void obj() throws ParseException {
        match(JsonToken.Type.Lbb);
        kv();
        while (look.getType()== JsonToken.Type.Comma){
            match(JsonToken.Type.Comma);
            kv();
        }
        match(JsonToken.Type.Rbb);
    }
    void kv() throws ParseException {
        key();
        match(JsonToken.Type.Semi);
        value();
    }
    void key() throws ParseException {
        if(look.getType()== JsonToken.Type.SingleQuote){
            match(JsonToken.Type.SingleQuote);
            string();
            match(JsonToken.Type.SingleQuote);
        }else if(look.getType()== JsonToken.Type.DoubleQuote){
            match(JsonToken.Type.DoubleQuote);
            string();
            match(JsonToken.Type.DoubleQuoteEnd);
        }
    }
    void value() throws ParseException {
        if(look.getType()== JsonToken.Type.Lmb){
            array();
        }else if(look.getType()== JsonToken.Type.Lbb){
            obj();
        }else if(look.getType()== JsonToken.Type.DoubleQuote){
            match(JsonToken.Type.DoubleQuote);
            string();
            match(JsonToken.Type.DoubleQuoteEnd);
        }else if(look.getType()== JsonToken.Type.SingleQuote){
            match(JsonToken.Type.SingleQuote);
            string();
            match(JsonToken.Type.SingleQuote);
        }else if(look.getType()== JsonToken.Type.Number){
            number();
        }else {
            error();
        }
    }
    void array() throws ParseException {
        match(JsonToken.Type.Lmb);
        obj();
        while (look.getType()== JsonToken.Type.Comma){
            match(JsonToken.Type.Comma);
            obj();
        }
        match(JsonToken.Type.Rmb);
    }
    void string() throws ParseException {
        match(JsonToken.Type.Str);
    }

//    void jsons() throws ParseException {
//        json();
//        if(look.getType()== JsonToken.Type.Comma){
//            match(JsonToken.Type.Comma);
//            jsons();
//        }
//    }
//
//    void json() throws ParseException {
//        if(look.getType()!=JsonToken.Type.Eof){
//            match(JsonToken.Type.Lbb);
//            stmts();
//            match(JsonToken.Type.Rbb);
//        }
//    }
//    void stmts() throws ParseException {
//
//        if(look.getType()!= JsonToken.Type.Rbb){
//            stmt();
//            if(look.getType()== JsonToken.Type.Comma){
//                match(JsonToken.Type.Comma);
//                stmts();
//            }
//        }else{
//            // e
//        }
//    }
//    void stmt() throws ParseException {
//        string();
//        match(JsonToken.Type.Semi);
//        if(look.getType()==JsonToken.Type.Lbb){
//            json();
//        }else if(look.getType()==JsonToken.Type.Lmb){
//            arr();
//        }else if(look.getType()==JsonToken.Type.Number){
//            match(JsonToken.Type.Number);
//        }else if(look.getType()==JsonToken.Type.Null){
//            match(JsonToken.Type.Null);
//        }else if(look.getType()==JsonToken.Type.DoubleQuote){
//            string();
//        }
//    }
//
//    void strings() throws ParseException {
//        string();
//        if(look.getType()==JsonToken.Type.Comma){
//            match(JsonToken.Type.Comma);
//            strings();
//        }
//    }
//    void string() throws ParseException {
//        match(JsonToken.Type.DoubleQuote);
//        if(look.getType()== JsonToken.Type.Str){
//            match(JsonToken.Type.Str);
//        }
//        match(JsonToken.Type.DoubleQuoteEnd);
//
//    }

//    void arr() throws ParseException {
//        match(JsonToken.Type.Lmb);
//        if(look.getType()==JsonToken.Type.Lbb){
//            jsons();
//        }else if(look.getType()==JsonToken.Type.DoubleQuote){
//            strings();
//        }else if(look.getType()==JsonToken.Type.Number) {
//            numbers();
//        }
//        match(JsonToken.Type.Rmb);
//    }

    void number() throws ParseException {
        match(JsonToken.Type.Number);
    }


}
