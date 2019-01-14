package com.jzkl.Bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PubBean implements Serializable {

    public PubBean() {

    }

    List<PubBean> lists = new ArrayList<>();
    List listSs = new ArrayList();

    private String id;
    private String name;
    private String icon;
    private String title;
    private String content;
    private String img_url;
    private String updateTime;
    private String createTime;
    private String detailUrl;
    private String mobile;

    private String logoUrl;
    private String headimgUrl;
    private String realnameStr;
    private String type;
    private String price;
    private String rule;
    private String descript;
    private String address;


    private String bankName;
    private String cardnoStr;
    private String acctTypeStr;

    /*我的交易*/
    private String orderid;
    private String amount;
    private String statusStr;
    private String creditNoStr;
    private String settfee;//手续费
    private String rate;//利息
    private String income;//实际收到
    private String subject;//失败
    /*信用卡  速度快*/
    private String creditTag;//实际收到
    private String goUrl;//实际收到
    private String levelApp;//实际收到

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRealnameStr() {
        return realnameStr;
    }

    public void setRealnameStr(String realnameStr) {
        this.realnameStr = realnameStr;
    }

    public String getHeadimgUrl() {
        return headimgUrl;
    }

    public void setHeadimgUrl(String headimgUrl) {
        this.headimgUrl = headimgUrl;
    }

    public String getLevelApp() {
        return levelApp;
    }

    public void setLevelApp(String levelApp) {
        this.levelApp = levelApp;
    }

    public String getGoUrl() {
        return goUrl;
    }

    public void setGoUrl(String goUrl) {
        this.goUrl = goUrl;
    }

    public String getCreditTag() {
        return creditTag;
    }

    public void setCreditTag(String creditTag) {
        this.creditTag = creditTag;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getCreditNoStr() {
        return creditNoStr;
    }

    public void setCreditNoStr(String creditNoStr) {
        this.creditNoStr = creditNoStr;
    }

    public String getSettfee() {
        return settfee;
    }

    public void setSettfee(String settfee) {
        this.settfee = settfee;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardnoStr() {
        return cardnoStr;
    }

    public void setCardnoStr(String cardnoStr) {
        this.cardnoStr = cardnoStr;
    }

    public String getAcctTypeStr() {
        return acctTypeStr;
    }

    public void setAcctTypeStr(String acctTypeStr) {
        this.acctTypeStr = acctTypeStr;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PubBean> getLists() {
        return lists;
    }

    public void setLists(List<PubBean> lists) {
        this.lists = lists;
    }

    public List getListSs() {
        return listSs;
    }

    public void setListSs(List listSs) {
        this.listSs = listSs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public PubBean(JSONObject json) {

        try {
            //成功有约 首页数据
            this.id = json.optString("id");
            this.name = json.optString("name");
            this.icon = json.optString("icon");
            this.title = json.optString("title");
            this.content = json.optString("content");
            this.img_url = json.optString("img_url");
            this.updateTime = json.optString("updateTime");
            this.createTime = json.optString("createTime");
            this.detailUrl = json.optString("detailUrl");
            this.mobile = json.optString("mobile");

            this.logoUrl = json.optString("logoUrl");
            this.headimgUrl = json.optString("headimgUrl");
            this.realnameStr = json.optString("realnameStr");
            this.type = json.optString("type");
            this.price = json.optString("price");
            this.rule = json.optString("rule");
            this.descript = json.optString("descript");
            this.address = json.optString("address");

            this.bankName = json.optString("bankName");
            this.cardnoStr = json.optString("cardnoStr");
            this.acctTypeStr = json.optString("acctTypeStr");
            /*我的交易*/
            this.orderid = json.optString("orderid");
            this.statusStr = json.optString("statusStr");
            this.amount = json.optString("amount");
            this.settfee = json.optString("settfee");
            this.creditNoStr = json.optString("creditNoStr");
            this.rate = json.optString("rate");
            this.income = json.optString("income");
            this.subject = json.optString("subject");
            this.creditTag = json.optString("tag");
            this.goUrl = json.optString("goUrl");
            this.levelApp = json.optString("levelApp");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PubBean getJsonObj(JSONObject json, String objName) {
        String jName = json.optString(objName);
        PubBean bean = null;
        if (null != jName && !"".equals(jName)) {
            JSONObject jsonObj = json.optJSONObject(objName);
            bean = new PubBean(jsonObj);
        } else {
            bean = new PubBean();
        }
        return bean;
    }

    public static List<PubBean> getJsonArr(JSONObject json, String arrName) {
        List<PubBean> list = new ArrayList<PubBean>();
        String jName = json.optString(arrName);
        if (null != jName && !"".equals(jName)) {
            JSONArray array = json.optJSONArray(arrName);
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.optJSONObject(i);
                    if (item != null) {
                        list.add(new PubBean(item));
                    }
                }
            }
        }
        return list;
    }

}