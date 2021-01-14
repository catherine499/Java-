package common.model.entity;


//响应状态枚举
public enum ResponseStatus {
	
    OK, //请求处理成功
    
    SERVER_ERROR, //服务器内部出错 
    
    NOT_FOUND, //请求的资源未找到
    
    BAD_REQUEST //错误的请求对象
}
