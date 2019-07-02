//package com.zilean.queue.domain.response;
//
//import com.zilean.queue.domain.ZileanJob;
//import com.zilean.queue.exception.ZileanExceptionEnum;
//
///**
// * 描述:
// *
// * @author xjh
// * created on 2019-07-01 21:01
// */
//public class ZileanResponseUtil {
//    public static ZileanResponse success() {
//        return new ZileanResponse<>();
//    }
//
//    public static ZileanResponse success(ZileanJob data) {
//        return new ZileanResponse<>(0, "", data);
//    }
//
//    public static ZileanResponse error(int code, String msg) {
//        return new ZileanResponse<>(code, msg);
//    }
//
//    public static ZileanResponse error(ZileanExceptionEnum zileanExceptionEnum) {
//        return new ZileanResponse<>(zileanExceptionEnum);
//    }
//
//}
