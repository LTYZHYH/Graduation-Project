package team.j2e8.findcateserver.infrastructure;


import team.j2e8.findcateserver.infrastructure.expressions.ObjectQueryExpression;
import team.j2e8.findcateserver.infrastructure.expressions.PropertyQueryExpression;
import team.j2e8.findcateserver.infrastructure.expressions.QueryExpression;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;



public class QueryExpressionParser {

    public ObjectQueryExpression parseObjectQuery(String objectQuery) {

        ObjectQueryExpression objectQueryExpression = new ObjectQueryExpression();
        objectQueryExpression.setRawQuery(objectQuery);

        StringBuilder objectName = new StringBuilder();

        int i = 0;
        char[] chars = objectQuery.toCharArray();
        while (i < chars.length) {
            char c = chars[i];
            if (c == '(') {
                // "(" 前的字符为查询对象的名称，对括号中的属性访问表达式进行解析
                objectQueryExpression.setObject(objectName.toString());
                String propertyQuery = objectQuery.substring(i + 1, objectQuery.length() - 1);
                objectQueryExpression.getProperties().addAll((this.parsePropertyExpression(propertyQuery)));
                break;
            } else {
                if (!Character.isWhitespace(c)) {
                    objectName.append(c);
                }
                i++;
            }
        }

        return objectQueryExpression;

    }


    private List<QueryExpression> parsePropertyExpression(String propertyQuery) {

        StringBuilder propertyName = new StringBuilder();
        List<QueryExpression> propertyQueryExpressions = new ArrayList<>();
        char[] chars = propertyQuery.toCharArray();
        int i = 0;
        while (i < chars.length) {
            char c = chars[i];
            if (c == ',') {
                // 简单属性
                PropertyQueryExpression propertyQueryExpression = new PropertyQueryExpression();
                propertyQueryExpression.setProperty(propertyName.toString());
                propertyQueryExpressions.add(propertyQueryExpression);
                propertyName = new StringBuilder();
                i++;
            } else if (c == '(') {
                // 嵌套对象属性
                String subObjectQuery = this.extraSubObjectQuery(i, chars);
                int skipLength = subObjectQuery.length();
                String objectQuery = propertyName.append(subObjectQuery).toString();
                propertyQueryExpressions.add(this.parseObjectQuery(objectQuery));
                propertyName = new StringBuilder();
                // 处理下一个表达式
                i = i + skipLength + 1;

            } else {
                if (!Character.isWhitespace(c)) {
                    propertyName.append(c);
                }
                i++;
            }
        }
        if (propertyName.length() > 0) {
            PropertyQueryExpression propertyQueryExpression = new PropertyQueryExpression();
            propertyQueryExpression.setProperty(propertyName.toString());
            propertyQueryExpressions.add(propertyQueryExpression);
        }
        return propertyQueryExpressions;

    }

    private String extraSubObjectQuery(int startIndex, char[] propertyQueryChars) {

        Stack<Character> leftParentheses = new Stack<>();

        int i = startIndex;

        String subObjectQuery = "";

        while (i < propertyQueryChars.length) {
            char c = propertyQueryChars[i];
            if (c == '(') {
                leftParentheses.push(c);
            } else if (c == ')') {
                leftParentheses.pop();
                if (leftParentheses.size() == 0) {
                    // 堆栈中的左括号全部出栈，此时已查找到完整的表达式
                    subObjectQuery = String.valueOf(propertyQueryChars).substring(startIndex, i + 1);
                    break;
                }
            }
            i++;
        }

        return subObjectQuery;
    }
}
