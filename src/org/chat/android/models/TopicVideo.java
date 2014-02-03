package org.chat.android.models;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "topic_video")
public class TopicVideo {
	@DatabaseField
	private int page_video1_id;
    @DatabaseField
    private int video_id;
    
    /**
     * Default Constructor needed by ormlite
     */
    public TopicVideo() {
    }

    
    /**
     * Constructor that instantiates the private member variable(s)
     * @param page_video1_id
     * @param video_id
     */
    public TopicVideo(int page_video1_id, int video_id) {
    	this.page_video1_id = page_video1_id;
        this.video_id = video_id;
    }
    
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public TopicVideo(TopicVideo existingTopicVideoModel) {
        this.page_video1_id = existingTopicVideoModel.page_video1_id;
        this.video_id = existingTopicVideoModel.video_id;
    }
    
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