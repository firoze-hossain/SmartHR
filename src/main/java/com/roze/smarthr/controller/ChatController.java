
package com.roze.smarthr.controller;

import com.roze.smarthr.dto.*;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.repository.UserRepository;
import com.roze.smarthr.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final UserRepository userRepository;

    @PostMapping("/conversations")
    public ResponseEntity<BaseResponse<ConversationResponse>> createConversation(
            @Valid @RequestBody ConversationRequest request,
            @AuthenticationPrincipal User user) {
        ConversationResponse response = chatService.createConversation(request, user);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Conversation created successfully",
                response
        ));
    }

    @GetMapping("/conversations")
    public ResponseEntity<BaseResponse<List<ConversationResponse>>> getUserConversations(
            @AuthenticationPrincipal User user) {
        List<ConversationResponse> responses = chatService.getUserConversations(user);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Conversations retrieved successfully",
                responses
        ));
    }

    @GetMapping("/conversations/{conversationId}")
    public ResponseEntity<BaseResponse<ConversationResponse>> getConversation(
            @PathVariable Long conversationId,
            @AuthenticationPrincipal User user) {
        ConversationResponse response = chatService.getConversation(conversationId, user);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Conversation retrieved successfully",
                response
        ));
    }

    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<BaseResponse<List<MessageResponse>>> getConversationMessages(
            @PathVariable Long conversationId,
            @AuthenticationPrincipal User user) {
        List<MessageResponse> responses = chatService.getConversationMessages(conversationId, user);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Messages retrieved successfully",
                responses
        ));
    }

    @PutMapping("/messages/{messageId}/status")
    public ResponseEntity<BaseResponse<Void>> updateMessageStatus(
            @PathVariable Long messageId,
            @Valid @RequestBody MessageStatusUpdateRequest request,
            @AuthenticationPrincipal User user) {
        chatService.updateMessageStatus(messageId, request, user);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Message status updated successfully",
                null
        ));
    }

    @MessageMapping("/send-message")
    public void handleMessage(@Payload MessageRequest request, Principal principal) {
        try {
            User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
            chatService.sendMessage(request, user);
        } catch (Exception e) {
            // Log error and handle appropriately
            System.err.println("Error handling message: " + e.getMessage());
            throw e;
        }
    }

}