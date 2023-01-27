package com.cooperation.record.domain.vo;

import com.cooperation.record.common.enums.ApiInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 前后端交互的信息包
 * serializable接口的作用：
 * 1、存储对象在存储介质中，以便在下次使用的时候，可以很快捷的重建一个副本；
 * 2、便于数据传输，尤其是在远程调用的时候。
 * @author cyl
 * @date 2021/10/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiMsg<T> implements Serializable {
   /** 结果码 */
   private int code;

   /** 返回信息内容 */
   private String message;

   /** 不需要返回查询对象时为空 */
   private T data;

   public ApiMsg(ApiInfo apiInfo){
       this.code = apiInfo.getCode();
       this.message = apiInfo.getMessage();
   }

   public ApiMsg(String message) {
       this.message = message;
   }
}
