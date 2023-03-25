package com.cduestc.book_novels.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 用于接收前端请求参数
 *
 * @author coffee
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FictionVo {
    /**
     * 页数
     */
    private Integer curr;
    /**
     * 每页数
     */
    private Integer limit;
    /**
     * 总数
     */
    private Integer count;
    /**
     * 小说类型
     */
    private Integer type;
    /**
     * 小说字数
     */
    private Integer number;
    /**
     * 小说写作进度
     */
    private Integer state;

    /**
     * 小说更新时间段
     */
    private Integer time;

    /**
     * 小说名称
     */
    private String fictionName;

    /**
     * 首页展示列表模式
     */
    private Integer viewType;
    /**
     * 小说id
     */
    private String fictionId;

    /**
     * 获取小说分类
     *
     * @param type 分类
     * @return 分类中文名称
     */
    public static String getTypeName(Integer type) {
        String name;
        switch (type) {
            case 1:
                name = "玄幻魔法";
                break;
            case 2:
                name = "武侠修真";
                break;
            case 3:
                name = "都市言情";
                break;
            case 4:
                name = "历史军事";
                break;
            case 5:
                name = "侦探推理";
                break;
            case 6:
                name = "网游动漫";
                break;
            case 7:
                name = "科幻灵异";
                break;
            case 8:
                name = "完本";
                break;
            case 9:
                name = "文学典籍";
                break;
            default:
                name = null;
                break;
        }
        return name;
    }

    /**
     * 获取小说写作进度
     *
     * @param index 进度
     * @return 写作进度中文名称
     */
    public static String getState(Integer index) {
        String state;
        switch (index) {
            case 1:
                state = "连载中";
                break;
            case 2:
                state = "完结";
                break;
            default:
                state = null;
                break;
        }
        return state;
    }
    private  String newType;
    private  String  newState;
}
