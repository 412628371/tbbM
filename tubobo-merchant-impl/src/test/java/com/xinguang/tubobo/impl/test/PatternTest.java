package com.xinguang.tubobo.impl.test;

/**
 * Created by Administrator on 2017/4/20.
 */
public class PatternTest {
    //验证金额
    public static boolean isNumber(String str)
    {
        java.util.regex.Pattern pattern=java.util.regex.Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后一位的数字的正则表达式
        java.util.regex.Matcher match=pattern.matcher(str);
        if(match.matches()==false)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static void main(String[] args) {
        System.out.print(isNumber("0.001"));
    }
}
