package org.chat.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "videos_accessed")
public class VideoAccessed {
    @DatabaseField
    private int video_id;
    @DatabaseField
    private int visit_id;


    /**
     * Default Constructor needed by ormlite
     */
    public VideoAccessed() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param video_id
     * @param visit_id
     * 
     */
    public VideoAccessed(int video_id, int visit_id) {
    	this.video_id = video_id;
    	this.visit_id = visit_id;
    }
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public VideoAccessed(VideoAccessed existingVideosAccessedModel) {
        this.video_id = existingVideosAccessedModel.video_id;
        this.visit_id = existingVideosAccessedModel.visit_id;
    }

	public int getVideoId() {
		return video_id;
	}
	
	public void setVideoId(int video_id) {
		this.video_id = video_id;
	}
	
	public int getVisitId() {
		return visit_id;
	}
	
	public void setVisitId(int visit_id) {
		this.visit_id = visit_id;
	}
}