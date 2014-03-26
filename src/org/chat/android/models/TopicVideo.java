package org.chat.android.models;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "topic_video")				// bridge table to relate videos to their video pages
public class TopicVideo {
	@DatabaseField(id = true)
	private int id;
	@DatabaseField
	private int page_video1_id;
    @DatabaseField
    private int video_id;
    @DatabaseField
	private Date created_at;
	@DatabaseField
	private Date modified_at;
    
    /**
     * Default Constructor needed by ormlite
     */
    public TopicVideo() {
    }

    
    /**
     * Constructor that instantiates the private member variable(s)
     * @param id
     * @param page_video1_id
     * @param video_id
     * @param created_at
     * @param modified_at
     */
    public TopicVideo(int id, int page_video1_id, int video_id, Date created_at, Date modified_at) {
    	this.page_video1_id = page_video1_id;
        this.video_id = video_id;
        this.created_at = created_at;
    	this.modified_at = modified_at;
    }
    
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
//    public TopicVideo(TopicVideo existingTopicVideoModel) {
//        this.page_video1_id = existingTopicVideoModel.page_video1_id;
//        this.video_id = existingTopicVideoModel.video_id;
//    }
    
    public void setPageVideo1Id(int page_video1_id) {
		this.page_video1_id = page_video1_id;
	}
    
	public int getPageVideo1Id() {
		return page_video1_id;
	}
	
	public void setVideoId(int video_id) {
		this.video_id = video_id;
	}
	
	public int getVideoId() {
		return video_id;
	}
	
}