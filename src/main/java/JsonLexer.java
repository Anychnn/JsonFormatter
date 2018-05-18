/**
 * Created by any on 2017/2/19.
 * Lexer
 */
public class JsonLexer {

    private enum State {
        Normal, StartStr, EndStr, SingleQuoteStr, SingleQuoteEndStr
    }

    private String source;
    private int index = 0;
    private char peek;
    private State state;

    public JsonLexer(String source) {
        this.source = source + '\0';
        state = State.Normal;
        move();
    }

    public JsonToken read() throws ParseException {
        if (state == State.Normal) {
            while (peek == ' ' || peek == '\n') {
                move();
            }
            switch (peek) {
                case '{':
                    move();
                    return new JsonToken(JsonToken.Type.Lbb, "{");
                case '}':
                    move();
                    return new JsonToken(JsonToken.Type.Rbb, "}");
                case '[':
                    move();
                    return new JsonToken(JsonToken.Type.Lmb, "[");
                case ']':
                    move();
                    return new JsonToken(JsonToken.Type.Rmb, "]");
                case '"':
                    move();
                    state = State.StartStr;
                    return new JsonToken(JsonToken.Type.DoubleQuote, "\"");
                case ':':
                    move();
                    return new JsonToken(JsonToken.Type.Semi, ":");
                case ',':
                    move();
                    return new JsonToken(JsonToken.Type.Comma, ",");
                case '\'':
                    move();
                    state = State.SingleQuoteStr;
                    return new JsonToken(JsonToken.Type.SingleQuote, "\'");
            }

            if (Character.isLetter(peek)) {
                StringBuffer sb = new StringBuffer();
                while (Character.isLetter(peek)) {
                    sb.append(peek);
                    move();
                }
                String res = sb.toString();
                if (res.equals("null")) {
                    return new JsonToken(JsonToken.Type.Null, res);
                } else {
                    error();
                }
            } else if (Character.isDigit(peek)) {
                StringBuffer sb = new StringBuffer();
                while (Character.isDigit(peek)) {
                    sb.append(peek);
                    move();
                }
                return new JsonToken(JsonToken.Type.Number, sb.toString());
            }

        } else if (state == State.StartStr) {
            StringBuffer sb = new StringBuffer();
            char tmp = peek;
            while (true) {
                if(peek=='\\'){
                    move();
                    sb.append(peek);
                    if(peek=='\"'){
                        sb.append(peek);
                        move();
                    }
                }
                if(peek=='"'){
                    break;
                }
                if (peek == '"' && tmp != '\\') break;
                sb.append(peek);
                move();
                tmp = peek;
            }
            state = State.EndStr;
            return new JsonToken(JsonToken.Type.Str, sb.toString());
        } else if (state == State.SingleQuoteStr) {
            StringBuffer sb = new StringBuffer();
            while (true) {
                if(peek=='\\'){
                    move();
                    sb.append('\\');
                    if(peek=='\''){
                        sb.append('\'');
                        move();
                    }
                }
                if (peek == '\'') {
                    break;
                }
                sb.append(peek);
                move();
            }
            state = State.SingleQuoteEndStr;
            return new JsonToken(JsonToken.Type.Str, sb.toString());
        } else if (state == State.EndStr) {
            if (peek == '"') {
                move();
                state = State.Normal;
                return new JsonToken(JsonToken.Type.DoubleQuoteEnd, "\"");
            } else {
                error();
            }

        } else if (state == State.SingleQuoteEndStr) {
            if (peek == '\'') {
                move();
                state = State.Normal;
                return new JsonToken(JsonToken.Type.SingleQuote, "\'");
            } else {
                error();
            }
        }
        if (peek == '\0') {
            return new JsonToken(JsonToken.Type.Eof, "eof");
        }
//        if(index==source.length()){
//            return new JsonToken(JsonToken.Type.Eof,"eof");
//        }
        error();
        return null;
    }

    private void error() throws ParseException {
        throw new ParseException("json格式异常");
    }

    private void move() {
        peek = source.charAt(index++);
    }
}
