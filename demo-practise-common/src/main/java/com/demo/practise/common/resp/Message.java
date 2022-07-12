package com.demo.practise.common.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "返回信息实体类")
public class Message<T> {

	public static Message<String> success=new Message<>();
	

    @ApiModelProperty(value = "返回状态码")
    private Integer code = 200;
    @ApiModelProperty(value = "返回信息")
    private String message = "success";
    
    private String messageDetail = "";
    
    @ApiModelProperty(value = "返回的数据，是一个泛型，无具体类型，根据实际判断")
    private T data = null;

    public Message(T t){
        this.data = t;
    }
    public Message(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public Message(Integer code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public static Message<String> getSuccess(){
        return success; 
    }
    
    public Message(Integer code, String message, String messageDetail){
        this.code = code;
        this.message = message;
        this.messageDetail=messageDetail;
    }
}