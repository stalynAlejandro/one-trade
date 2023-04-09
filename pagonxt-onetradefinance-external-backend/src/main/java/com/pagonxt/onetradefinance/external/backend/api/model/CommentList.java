package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class create an object with a list of comments(DTO)
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.model.CommentDto
 * @since jdk-11.0.13
 */
public class CommentList extends ArrayList<CommentDto> {

    /**
     * Class constructor
     * @param comments list of comments(dto)
     */
    public CommentList(List<CommentDto> comments) {
        super(comments);
    }
}
