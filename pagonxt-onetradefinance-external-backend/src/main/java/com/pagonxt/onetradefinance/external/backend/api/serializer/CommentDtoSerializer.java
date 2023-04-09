package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.pagonxt.onetradefinance.external.backend.api.model.CommentDto;
import com.pagonxt.onetradefinance.integrations.model.Comment;
import org.springframework.stereotype.Component;

/**
 * This class has methods to convert DTO's into entities and viceversa, for comments
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Component
public class CommentDtoSerializer {

    /**
     * This method convert a comment object to commentDto object
     * @param comment comment object
     * @return commentDto object
     */
    public CommentDto toDto(Comment comment) {
        if(comment == null) {
            return null;
        }
        CommentDto commentDto = new CommentDto();
        commentDto.setUserDisplayedName(comment.getUserDisplayedName());
        commentDto.setTaskName(comment.getTaskName());
        commentDto.setTimestamp(comment.getTimestamp());
        commentDto.setContent(comment.getContent());
        return commentDto;
    }

    /**
     * This method converts a commentDto object to comment object
     * @param commentDto commentDto object
     * @return comment object
     */
    public Comment toModel(CommentDto commentDto) {
        if(commentDto == null) {
            return new Comment();
        }
        Comment comment = new Comment();
        comment.setUserDisplayedName(commentDto.getUserDisplayedName());
        comment.setTaskName(commentDto.getTaskName());
        comment.setTimestamp(commentDto.getTimestamp());
        comment.setContent(commentDto.getContent());
        return comment;
    }
}
