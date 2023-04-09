package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.CommentDto;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.Comment;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.core.io.ClassPathResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@UnitTest
class CommentDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private
    CommentDtoSerializer serializer;

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        Comment comment = mapper.readValue(new ClassPathResource("data/model/comment.json").getFile(), Comment.class);
        CommentDto expectedCommentDto = mapper.readValue(new ClassPathResource("data/dto/comment-dto.json").getFile(), CommentDto.class);
        // When
        CommentDto result = serializer.toDto(comment);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedCommentDto);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given
        // When
        CommentDto result = serializer.toDto(null);
        // Then
        assertNull(result);
    }

    @Test
    void toModel_ok_returnsValidModel() throws Exception {
        // Given
        Comment expectedComment = mapper.readValue(new ClassPathResource("data/model/comment.json").getFile(), Comment.class);
        CommentDto commentDto = mapper.readValue(new ClassPathResource("data/dto/comment-dto.json").getFile(), CommentDto.class);
        // When
        Comment result = serializer.toModel(commentDto);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedComment);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toModel_whenNull_thenReturnsNewComment() {
        // Given
        Comment expectedResult = new Comment();
        // When
        Comment result = serializer.toModel(null);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }
}
