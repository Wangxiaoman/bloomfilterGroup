package com.paradigm.constants;


public enum CommonStatus{
    // text字段为枚举描述
    UNKNOWN(-1, ""),
    
    COMMON_INVALID(0,"无效、未校验、未上线"),
    COMMON_VALID(1,"有效、已校验、已上线"),

    SUCCESS(200,"成功"),
    PARAM_ERROR(400,"参数异常,请检查"),
    SERVER_ERROR(500,"服务器内部错误");
    
    private int    value;
    private String text; 

    private static final KV<Integer, CommonStatus> LOOKUP = new KV<Integer, CommonStatus>();

    static {
        for (CommonStatus status : CommonStatus.values()) {
            LOOKUP.put(status.getValue(), status);
        }
        LOOKUP.putDefault(UNKNOWN);
    }

    private CommonStatus(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return this.value;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    
    public static CommonStatus of(Integer value) {
        return LOOKUP.get(value);
    }

}
