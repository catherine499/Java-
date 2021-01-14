package common.model.entity;

//响应数据的类型枚举
public enum ResponseType {
    
    TEXT, //文本内容
    
    TOSENDFILE, //准备发送文件
    
    AGREERECEIVEFILE, //同意接收文件
    
    REFUSERECEIVEFILE, //拒绝接收文件
    
    RECEIVEFILE, //发送文件
    
    LOGIN, //用户登录
    
    LOGOUT, //用户退出
    
    CHAT, //聊天
    
    OTHER, //其他
    
    BOARD, //服务器广播
    
    REMOVE //服务器剔除用户
}
