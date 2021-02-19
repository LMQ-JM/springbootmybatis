package com.example.posting.entity;

import java.util.List;

/**
 * @author Administrator
 */
public class Posting {
	//发帖表id
	 int id;

	String name;//帖子名称

	int user_id;//发帖人ID
	String cover;//封面
	int likes;//点赞数量
	String images;
	int collect_num; //收藏数量
	int commentary_num; //帖子评论数量
	String videos;//视频
	String description;//详细介绍
	int comment_id;//评论表id
	String	lable_id;//标签表id组
	String create_at;//创建时间
	String end_at;//最后操作时间
	String is_delete ;//是否删除
	int type;//图片OR视频
	String address;//发帖地址
	//（0公开，1让谁看，2不让谁看）
	int status;
	//是否置顶(0否，1是)
	int top;
	
	//user关联查询start
	String dname;
	String avatar;
	//user关联查询end
	
	int is_zan;//是否点赞;
	int is_collect;//是否收藏	
	int is_follow;//是否关注
	
	int browseNumber; //帖子浏览数量
	
	int ExceptionalNumber;//帖子打赏数量
	
	int official_recommendation;
	
	int is_admin;
	
	int activity_id;
	
	int is_open_shop_article;
	
	List<Images> imagesUrl;//图片组
	
	List<String> labels;//标签组
	
	
	
	
	
	public int getExceptionalNumber() {
		return ExceptionalNumber;
	}
	public void setExceptionalNumber(int exceptionalNumber) {
		ExceptionalNumber = exceptionalNumber;
	}
	public int getBrowseNumber() {
		return browseNumber;
	}
	public void setBrowseNumber(int browseNumber) {
		this.browseNumber = browseNumber;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getCommentary_num() {
		return commentary_num;
	}
	public void setCommentary_num(int commentary_num) {
		this.commentary_num = commentary_num;
	}
	public int getIs_open_shop_article() {
		return is_open_shop_article;
	}
	public void setIs_open_shop_article(int is_open_shop_article) {
		this.is_open_shop_article = is_open_shop_article;
	}
	public int getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(int activity_id) {
		this.activity_id = activity_id;
	}
	public int getIs_admin() {
		return is_admin;
	}
	public void setIs_admin(int is_admin) {
		this.is_admin = is_admin;
	}
	public int getCollect_num() {
		return collect_num;
	}
	public void setCollect_num(int collect_num) {
		this.collect_num = collect_num;
	}
	public int getOfficial_recommendation() {
		return official_recommendation;
	}
	public void setOfficial_recommendation(int official_recommendation) {
		this.official_recommendation = official_recommendation;
	}
	public int getIs_follow() {
		return is_follow;
	}
	public void setIs_follow(int is_follow) {
		this.is_follow = is_follow;
	}
	public int getIs_zan() {
		return is_zan;
	}
	public void setIs_zan(int is_zan) {
		this.is_zan = is_zan;
	}
	public List<String> getLabels() {
		return labels;
	}
	public void setLabels(List<String> labels) {
		this.labels = labels;
	}
	public int getId() {
		return id;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}

	
	public String getVideos() {
		return videos;
	}
	public void setVideos(String videos) {
		this.videos = videos;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
	public String getLable_id() {
		return lable_id;
	}
	public void setLable_id(String lable_id) {
		this.lable_id = lable_id;
	}
	public String getCreate_at() {
		return create_at;
	}
	public void setCreate_at(String create_at) {
		this.create_at = create_at;
	}
	public String getEnd_at() {
		return end_at;
	}
	public void setEnd_at(String end_at) {
		this.end_at = end_at;
	}
	public String getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
	public List<Images> getImagesUrl() {
		return imagesUrl;
	}
	public void setImagesUrl(List<Images> imagesUrl) {
		this.imagesUrl = imagesUrl;
	}
	public int getIs_collect() {
		return is_collect;
	}
	public void setIs_collect(int is_collect) {
		this.is_collect = is_collect;
	}
	@Override
	public String toString() {
		return "Posting [id=" + id + ", name=" + name + ", user_id=" + user_id + ", cover=" + cover + ", likes=" + likes
				+ ", images=" + images + ", videos=" + videos + ", description=" + description + ", comment_id="
				+ comment_id + ", lable_id=" + lable_id + ", create_at=" + create_at + ", end_at=" + end_at
				+ ", is_delete=" + is_delete + ", type=" + type + ", address=" + address + ", dname=" + dname
				+ ", avatar=" + avatar + ", is_zan=" + is_zan + ", is_collect=" + is_collect + ", is_follow="
				+ is_follow + ", imagesUrl=" + imagesUrl + ", labels=" + labels + "]";
	}
	

}
