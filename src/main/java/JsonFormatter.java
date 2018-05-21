/**
 * Created by any on 2017/2/19.
 * json字符串的格式化工具
 * 暂时不支持key为 不带 "" 的
 * 暂时不支持 字符串中带转译字符 "\""的
 *
 */
public class JsonFormatter {

    public String formate(String json) throws ParseException {
        //通过对json进行解析
        //解析为词法解析 转为为一个个Token 最后获取对应的语义

        JsonLexer lexer=new JsonLexer(json);
        JsonParser parser=new JsonParser(lexer);
        parser.parse();
        return parser.getOutPut().toString();
    }
}
