package org.example.userserver.config;

public class RegisterStatus {
    public static final int SUCCESS = 0;
    public static final int EMAIL_EXISTS = 1;
    public static final int FAILED = 2;

    public static String getMessage(int status) {
        switch (status) {
            case SUCCESS: return "注册成功";
            case EMAIL_EXISTS: return "邮箱已存在";
            case FAILED: return "注册失败";
            default: return "未知状态";
        }
    }
}