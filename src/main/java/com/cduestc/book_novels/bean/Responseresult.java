package com.cduestc.book_novels.bean;

import lombok.Data;

@Data
public class Responseresult {

        // 响应业务状态
        private Integer status;
        // 响应中的数据
        private Object data;

        // 响应消息
        private String msg;

        public Responseresult(Integer status, String msg) {
                this.status = status;
                this.msg = msg;
        }

        public Responseresult() {
        }
}
