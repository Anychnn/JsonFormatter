/**
 * Created by Administrator on 2017/2/19.
 */
public class JsonToken {
    public enum Type{
        Str,Lmb,Rmb,Lbb,Rbb,Semi,Number,Comma, DoubleQuote, DoubleQuoteEnd,Null, Type, Eof,SingleQuote
    }

    public JsonToken(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    private Type type;
    private String value;

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
